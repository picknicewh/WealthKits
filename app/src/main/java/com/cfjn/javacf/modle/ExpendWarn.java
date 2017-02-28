package com.cfjn.javacf.modle;

public class ExpendWarn {


	/**
	 * 账号
	 */
	private String loginName;
	/**
	 * 支出预算
	 */
	private double expendBudget;
	/**
	 * 警告之
	 */
	private double warnValue;
	/**
	 * 是否可用
	 */
	private String enable;
	
	/**
	 * 类型代号
	 */
	private int classifyCode;

	private String week;

	private String unit;
	
	/**
	 * 时间
	 */
	private String createDate;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public double getExpendBudget() {
		return expendBudget;
	}

	public void setExpendBudget(double expendBudget) {
		this.expendBudget = expendBudget;
	}

	public double getWarnValue() {
		return warnValue;
	}

	public void setWarnValue(double warnValue) {
		this.warnValue = warnValue;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public int getClassifyCode() {
		return classifyCode;
	}

	public void setClassifyCode(int classifyCode) {
		this.classifyCode = classifyCode;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
