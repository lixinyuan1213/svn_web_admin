package logic

import (
	"config"
	"encoding/json"
	"io/ioutil"
	"net/http"
	"strings"
	"time"
	"util"
)

type SvnContent struct {
	BaseInfo SvnInfo `json:"base_info"`
	AuthzInfo string `json:"auth_info"`
	Passwd string `json:"passwd_info"`
}
var authzContent []byte
var passwdContent []byte
func Edit(w http.ResponseWriter, r *http.Request)  {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json")
	config := config.GetConfig()
	if r.Method == "GET"{
		parmsD := r.URL.Query()
		id := parmsD["id"][0]
		if id == ""{
			req := util.Resp(id,"参数错误",400)
			rsq,_ := json.Marshal(req)
			w.Write(rsq)
			return
		}
		userToken := parmsD["userToken"][0]
		if util.IsLogn(userToken)==false {
			req := util.Resp("","请确认已经登录",430)
			rsq,_ := json.Marshal(req)
			w.Write(rsq)
			return
		}
		svnInfo := GetSvnDb(id)
		authS,passS := getSvnConfig(&config,id)
		svnInfos := SvnContent{}
		svnInfos.BaseInfo = svnInfo
		svnInfos.AuthzInfo = string(authS)
		svnInfos.Passwd = string(passS)
		req := util.Resp(svnInfos,"获取成功",200)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
	}else if r.Method == "POST"{
		//登录标识
		userToken := r.PostFormValue("userToken")
		if util.IsLogn(userToken)==false {
			req := util.Resp("","请确认已经登录",430)
			rsq,_ := json.Marshal(req)
			w.Write(rsq)
			return
		}
		pname := r.PostFormValue("pname")
		pname = strings.Replace(pname, " ", "", -1)
		pname = strings.Replace(pname, "\n", "", -1)
		if pname == ""{
			req := util.Resp("","参数错误",400)
			rsq,_ := json.Marshal(req)
			w.Write(rsq)
			return
		}
		cname := r.PostFormValue("cname")
		cname = strings.Replace(cname, " ", "", -1)
		cname = strings.Replace(cname, "\n", "", -1)
		remark := r.PostFormValue("remark")
		//获取leveldb的实例
		dbModel := util.GetDb("./db")
		svnInfoModel := SvnInfo{}
		//根据键（项目名称）从数据库中取出数据，映射成结构体
		dbContent := util.GetDbByKey(pname,dbModel)
		dbContentStr := string(dbContent)
		var dbCreateTime string
		//确定创建时间
		if dbContentStr != ""{
			json.Unmarshal(dbContent,&svnInfoModel)
			dbCreateTime = svnInfoModel.Ctime
		}else{
			dbCreateTime = ""
		}
		//更新结构体数据
		svnInfoModel.FileName = pname
		svnInfoModel.Name = cname
		svnInfoModel.Remark = remark
		svnInfoModel.Mtime = time.Now().Format("2006-01-02 15:04:05")
		svnInfoModel.Ctime = dbCreateTime
		dbJson,_ := json.Marshal(svnInfoModel)
		util.SetDbByKey(pname,string(dbJson),dbModel)
		util.CloserDb(dbModel)
		devId := r.PostFormValue("devId")
		authId := r.PostFormValue("authId")
		saveFlag := SaveSvnConfig(&config,pname,authId,devId)
		if saveFlag == false {
			req := util.Resp("","配置文件错误",400)
			rsq,_ := json.Marshal(req)
			w.Write(rsq)
			return
		}
		req := util.Resp("","操作成功",200)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}else{
		req := util.Resp("","请求方法错误",430)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
	}
}
/*
* 返回svn两个重要的配置文件的内容（用户和权限）
* @param config 自定义的配置文件
* @param id     需要读取的项目名称
 */
func getSvnConfig(config *config.Config,id string)  (authzContent []byte,passwdContent []byte){
	svnConfigPath := config.SvnPath+id+"/conf/"
	files, _ := ioutil.ReadDir(svnConfigPath)
	for _, f := range files {
		if f.Name() == "authz"{
			authzContent,_ = ioutil.ReadFile(svnConfigPath+"authz")
		}
		if f.Name() == "passwd"{
			passwdContent,_ = ioutil.ReadFile(svnConfigPath+"passwd")
		}
	}
	return authzContent,passwdContent
}
func GetSvnDb(id string)  SvnInfo{
	//获取leveldb的实例
	dbModel := util.GetDb("./db")
	db := util.GetDbByKey(id,dbModel)
	util.CloserDb(dbModel)
	svnInfoModel := SvnInfo{}
	if string(db) != ""{
		json.Unmarshal(db,&svnInfoModel)
	}
	return svnInfoModel
}
func SaveSvnConfig(config *config.Config,id string,authz string,passwd string)  bool{
	svnConfigPath := config.SvnPath+id+"/conf/"
	error := ioutil.WriteFile(svnConfigPath+"authz", []byte(authz), 0644)
	if error == nil{
	}
	error2 := ioutil.WriteFile(svnConfigPath+"passwd", []byte(passwd), 0644)
	if error2 == nil{
	}
	return true
}