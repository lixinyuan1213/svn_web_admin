package com.sljm.svnadmin.model;

import java.util.Date;

public class Logs {
	private Long id;
	private Long userId;
	private Date ctime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	@Override
	public String toString() {
		return "Logs [id=" + id + ", userId=" + userId + ", ctime=" + ctime + "]";
	}
}
