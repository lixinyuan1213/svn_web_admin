package config

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
)

type Config struct {
	Port string `json:"port"`
	Path string `json:"path"`
	SvnPath string `json:"svn_root_path"`
	AdminName string `json:"admin"`
	Password string `json:"password"`
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