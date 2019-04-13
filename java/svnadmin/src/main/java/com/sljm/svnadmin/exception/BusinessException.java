package com.sljm.svnadmin.exception;

public class BusinessException extends RuntimeException{
private static final long serialVersionUID = 1L;

	private int code; // 错误码
	public BusinessException() {
	}
	public BusinessException(String msg,int code) {
		super(msg);
		this.code = code;
	}
	public BusinessException(String msg) {
		super(msg);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
