package config

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
)

type Config struct {
	//服务端口
	Port string `json:"port"`
	//项目根路径
	Path string `json:"path"`
	//svn根路径
	SvnPath string `json:"svn_root_path"`
	//管理员登录账号
	AdminName string `json:"admin"`
	//管理员登录密码
	Password string `json:"password"`
	//删除svn项目时的密码
	DelPwd string `json:"del_pwd"`
}
func GetConfig()  Config{
	b, err := ioutil.ReadFile("config.json")
	if err != nil {
		fmt.Print(err)
	}
	config := Config{}
	json.Unmarshal(b,&config)
	return config
}