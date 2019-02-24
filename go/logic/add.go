package logic

import (
	"config"
	"encoding/json"
	"io/ioutil"
	"net/http"
	"os/exec"
	"time"
	"util"
)

func Add(w http.ResponseWriter, r *http.Request)  {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json")
	config := config.GetConfig()
	pname := r.PostFormValue("pname")
	if pname == ""{
		req := util.Resp("","项目名称不能为空",400)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	cname := r.PostFormValue("cname")
	if cname == ""{
		req := util.Resp("","项目名称（中文）不能为空",400)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	userToken := r.PostFormValue("userToken")
	if util.IsLogn(userToken)==false {
		req := util.Resp("","请确认已经登录",430)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	files, _ := ioutil.ReadDir(config.SvnPath)
	for _, f := range files {
		if f.Name()==pname{
			req := util.Resp("","项目名称已经存在",400)
			rsq,_ := json.Marshal(req)
			w.Write(rsq)
			return
		}
	}
	remark := r.PostFormValue("remark")
	//linux下创建svn项目命令
	command := "/usr/bin/svnadmin create "+config.SvnPath+pname
	cmd := exec.Command("/bin/bash", "-c", command)
	_,err := cmd.Output()
	if err != nil {
		req := util.Resp("","svn创建失败",400)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	//创建配置文件
	svnserveStr := "[general]\nanon-access = none\nauth-access = write\npassword-db = passwd\nauthz-db = authz\n[sasl]\n"
	ioutil.WriteFile(config.SvnPath+pname+"/conf/svnserve.conf", []byte(svnserveStr), 0644)
	//当前时间
	nowTime := time.Now().Format("2006-01-02 15:04:05")
	svnInfoModel := SvnInfo{}
	svnInfoModel.FileName = pname
	svnInfoModel.Name = cname
	svnInfoModel.Remark = remark
	svnInfoModel.Mtime = nowTime
	svnInfoModel.Ctime = nowTime
	dbJson,_ := json.Marshal(svnInfoModel)
	//获取leveldb的实例
	dbModel := util.GetDb("./db")
	util.SetDbByKey(pname,string(dbJson),dbModel)
	util.CloserDb(dbModel)
	req := util.Resp("","创建成功,请及时配置开发人员账号密码及其权限",200)
	rsq,_ := json.Marshal(req)
	//更新文件名称的leveldb
	go UpdateDirFilesNameToDb(config.SvnPath,"AllFiles")
	w.Write(rsq)
}