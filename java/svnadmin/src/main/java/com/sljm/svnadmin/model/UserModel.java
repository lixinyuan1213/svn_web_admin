package com.sljm.svnadmin.model;

public class UserModel {
	//主键
	private long id;
	//登录名
    private String name;
    //用户角色（1超级管理员2普通用户）
    private short role;
    //所属部门，对应部门表id
    private String department;
    //状态（1可用2不可用）
    private short status;
    //备注说明
    private String remark;
    //密码
	private String password;
	//创建时间
    private String ctime;
    //修改时间
    private String mtime;
    //真实姓名
    private String real_name;
    
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
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
	public short getRole() {
		return role;
	}
	public void setRole(short role) {
		this.role = role;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	  public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
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
		@Override
		public String toString() {
			return "UserModel [id=" + id + ", name=" + name + ", role=" + role + ", department=" + department
					+ ", status=" + status + ", remark=" + remark + ", password=" + password + ", ctime=" + ctime
					+ ", mtime=" + mtime + "]";
		}
		public UserModel(String name, String password) {
			super();
			this.name = name;
			this.password = password;
		}
		public UserModel() {
			super();
		}
}
