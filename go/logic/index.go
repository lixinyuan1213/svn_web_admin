package logic

import (
	"config"
	"encoding/json"
	"github.com/json-iterator/go"
	"io/ioutil"
	"net/http"
	"strings"
	"util"
)
type SvnInfo struct {
	//项目名称
	FileName string `json:"file_name"`
	//项目中文名称
	Name string `json:"name"`
	//项目备注
	Remark string `json:"remark"`
	//创建时间
	Ctime string `json:"create_time"`
	//最后更新时间
	Mtime string `json:"update_time"`
}
func Index(w http.ResponseWriter, r *http.Request)  {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json")
	userToken := r.PostFormValue("userToken")
	if util.IsLogn(userToken)==false {
		req := util.Resp("","请确认已经登录",430)
		var json = jsoniter.ConfigCompatibleWithStandardLibrary
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	config := config.GetConfig()
	//获取系统中svn目录下的所有项目名称（文件夹名称）
	files := GetDirFilesName(config.SvnPath,"AllFiles")
	//svn中所有项目的信息数组
	var svnData []SvnInfo
	//获取leveldb的实例
	dbModel := util.GetDb("./db")
	for _, f := range files {
		FileName := f
		db := util.GetDbByKey(FileName,dbModel)
		//项目实体
		svnInfoModel := SvnInfo{}
		if string(db) != ""{
			var json = jsoniter.ConfigCompatibleWithStandardLibrary
			json.Unmarshal(db,&svnInfoModel)
		}else{
			svnInfoModel.FileName = FileName
			var json = jsoniter.ConfigCompatibleWithStandardLibrary
			jsonSSS,_ := json.Marshal(svnInfoModel)
			util.SetDbByKey(FileName,string(jsonSSS),dbModel)
		}
		svnData = append(svnData,svnInfoModel)
	}
	//关闭leveldb
	util.CloserDb(dbModel)
	req := util.Resp(svnData,"获取成功",200)
	rsq,_ := json.Marshal(req)
	w.Write(rsq)
}
/**
** 扫描某路径下的文件，并保存到本地数据库中
 */
func GetDirFilesName(path string,key string)  []string{
	//获取leveldb的实例
	dbModel := util.GetDb("./db")
	dbFilesNames := util.GetDbByKey(key,dbModel)
	dbFilesNamesStr := string(dbFilesNames)
	if dbFilesNamesStr!=""{
		fileNames := strings.Split(dbFilesNamesStr,",")
		util.CloserDb(dbModel)
		return fileNames
	}else{
		files, _ := ioutil.ReadDir(path)
		var fileNames []string
		for _, f := range files {
			fileNames = append(fileNames,f.Name())
		}
		fileNamesStr := strings.Join(fileNames,",")
		util.SetDbByKey(key,fileNamesStr,dbModel)
		util.CloserDb(dbModel)
		return fileNames
	}
}
/**
** 更新某路径下的文件名称到特定的键下面（保存文件名到leveldb里面）
 */
func UpdateDirFilesNameToDb(path string,key string){
	files, _ := ioutil.ReadDir(path)
	var fileNames []string
	for _, f := range files {
		fileNames = append(fileNames,f.Name())
	}
	fileNamesStr := strings.Join(fileNames,",")
	//获取leveldb的实例
	dbModel := util.GetDb("./db")
	util.SetDbByKey(key,fileNamesStr,dbModel)
	util.CloserDb(dbModel)
}