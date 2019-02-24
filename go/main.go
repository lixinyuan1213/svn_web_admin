package main

import (
	"config"
	"logic"
	"net/http"
)

func main() {
	config := config.GetConfig()
	//首页
	http.HandleFunc("/", logic.Index)
	//登录
	http.HandleFunc("/login", logic.Login)
	//编辑
	http.HandleFunc("/edit", logic.Edit)
	//新增svn
	http.HandleFunc("/add", logic.Add)
	//删除svn
	http.HandleFunc("/del", logic.Del)
	http.ListenAndServe(":"+config.Port, nil)

}