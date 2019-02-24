package logic

import (
	"config"
	"encoding/json"
	"net/http"
	"os"
	"util"
)

func Del(w http.ResponseWriter, r *http.Request)  {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json")
	config := config.GetConfig()
	id := r.PostFormValue("id")
	if id == ""{
		req := util.Resp("","待删除的svn项目名称不能为空",400)
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
	pass := r.PostFormValue("pass")
	if pass == ""{
		req := util.Resp("","请上传删除密码",400)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	if pass!=config.DelPwd{
		req := util.Resp("","删除密码不正确",400)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	error := os.RemoveAll(config.SvnPath+id)
	if error != nil{
		req := util.Resp("","删除失败",400)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
		return
	}
	//获取leveldb的实例
	dbModel := util.GetDb("./db")
	util.DelDbByKey(id,dbModel)
	util.CloserDb(dbModel)
	req := util.Resp("","删除成功",200)
	rsq,_ := json.Marshal(req)
	//更新文件名称的leveldb
	go UpdateDirFilesNameToDb(config.SvnPath,"AllFiles")
	w.Write(rsq)
}