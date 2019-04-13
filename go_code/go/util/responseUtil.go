package util

type RespModel struct {
	Code int32 `json:"code"`
	Msg string `json:"msg"`
	Data interface{} `json:"data"`
}
func Resp(args interface{},msg string,code int32)  RespModel{
	reqN := RespModel{}
	reqN.Code = code
	reqN.Msg = msg
	reqN.Data = args
	return reqN
}