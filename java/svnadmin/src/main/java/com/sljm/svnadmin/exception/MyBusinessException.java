package com.sljm.svnadmin.exception;

public class MyBusinessException extends RuntimeException{
private static final long serialVersionUID = 1L;

	private int code; // 错误码
	public MyBusinessException() {
	}
	public MyBusinessException(String msg,int code) {
		super(msg);
		this.code = code;
	}
	public MyBusinessException(String msg) {
		super(msg);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
