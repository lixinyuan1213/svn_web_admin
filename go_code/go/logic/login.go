package logic

import (
	"config"
	"encoding/json"
	"github.com/satori/go.uuid"
	"net/http"
	"util"
)
func Login(w http.ResponseWriter, r *http.Request)  {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Content-Type", "application/json")
	config := config.GetConfig()
	userName := r.PostFormValue("userName")
	passWord := r.PostFormValue("passWord")
	if (userName==config.AdminName)&&(passWord==config.Password){
		//产生唯一id
		u1 := uuid.Must(uuid.NewV4())
		u1Str := u1.String()
		//获取leveldb的实例
		dbModel := util.GetDb("./db")
		util.SetDbByKey("uuid",u1Str,dbModel)
		util.CloserDb(dbModel)
		req := util.Resp(u1,"登录成功",200)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
	}else{
		req := util.Resp("","登录失败",400)
		rsq,_ := json.Marshal(req)
		w.Write(rsq)
	}
}