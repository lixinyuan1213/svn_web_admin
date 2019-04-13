package com.sljm.svnadmin.model;

public class SvnProject {
	//主键
	private long id;
	//项目名称（中文）
	private String name;
	//svn上的项目名称，对应文件夹名称
	private String file_name;
	//创建者
	private long creator;
	//项目说明
	private String intro;
	//创建时间
	private String ctime;
	//修改时间
	private String mtime;
	//项目对应的所有用户
	private String users;
	//项目对应的用户权限
	private String auth;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public long getCreator() {
		return creator;
	}
	public void setCreator(long creator) {
		this.creator = creator;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getMtime() {
		return mtime;
	}
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	@Override
	public String toString() {
		return "SvnProject [id=" + id + ", name=" + name + ", file_name=" + file_name + ", creator=" + creator
				+ ", intro=" + intro + ", ctime=" + ctime + ", mtime=" + mtime + ", users=" + users + ", auth=" + auth
				+ "]";
	}
}
