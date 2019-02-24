package util

func IsLogn(userToken string)  bool{
	//获取leveldb的实例
	dbModel := GetDb("./db")
	dbToken := GetDbByKey("uuid",dbModel)
	CloserDb(dbModel)
	if string(dbToken) == userToken{
		return true
	}else{
		return false
	}
}