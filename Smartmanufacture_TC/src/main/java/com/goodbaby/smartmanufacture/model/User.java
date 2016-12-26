package com.goodbaby.smartmanufacture.model;

public class User {
	/**
	 * 手机号
	 */
	private String tel;
	/**
	 * 密码.MD5加密
	 */
	private String password;
	/**
	 * 应用名称
	 */
	private String appname;
	/**
	 * ERP账号
	 */
	private String userid;
	/**
	 * 状态
	 */
	private String isvalid;
	/**
	 * 是否可用
	 */
	private String remark;
	/**
	 * 登录时间
	 */
	private String loginTime;
	/**
	 * 自动登录flag
	 */
	private boolean autoLogin;
	/**
	 * 记住密码flag
	 */
	private boolean rememberPsw;

	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getIsvalid() {
		return isvalid;
	}
	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public boolean isAutoLogin() {
		return autoLogin;
	}
	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}
	public boolean isRememberPsw() {
		return rememberPsw;
	}
	public void setRememberPsw(boolean rememberPsw) {
		this.rememberPsw = rememberPsw;
	}





}
