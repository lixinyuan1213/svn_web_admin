package com.sljm.svnadmin.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class ResultMsg {
	private int code;
	private String msg;
	private Object data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public static ResultMsg success(String msg,Object data) 
	{
		ResultMsg r = new ResultMsg();
		r.setCode(200);
		r.setMsg(msg);
		r.setData(data);
		return r;
	}
	public static ResultMsg success(String msg) 
	{
		ResultMsg r = new ResultMsg();
		r.setCode(200);
		r.setMsg(msg);
		return r;
	}
	public static ResultMsg error(String msg) 
	{
		ResultMsg r = new ResultMsg();
		r.setCode(-1);
		r.setMsg(msg);
		return r;
	}
	public static String pageErrorMsg(HttpServletRequest request,String msg) 
	{
		request.setAttribute("errorinfo",msg);
		return "error";
	}
}
