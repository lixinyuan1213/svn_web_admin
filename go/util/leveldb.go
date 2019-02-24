package util

import "github.com/syndtr/goleveldb/leveldb"

func GetDb(path string)  *leveldb.DB{
	//创建并打开数据库
	//db, err := leveldb.OpenFile("./db", nil)
	db, err := leveldb.OpenFile(path, nil)
	if err != nil {
		panic(err)
		return nil
	}
	return db
}
func CloserDb(db *leveldb.DB) {
	defer db.Close()
}
func GetDbByKey(key string,db *leveldb.DB)  []byte{
	data, _ := db.Get([]byte(key), nil)
	return data
}
func SetDbByKey(key string,value string,db *leveldb.DB) bool{
	error := db.Put([]byte(key), []byte(value),nil)
	if error!=nil{
		return false
	}else{
		return true
	}
}
func DelDbByKey(key string,db *leveldb.DB) bool{
	error := db.Delete([]byte(key),nil)
	CloserDb(db)
	if error!=nil{
		return false
	}else{
		return true
	}
}
