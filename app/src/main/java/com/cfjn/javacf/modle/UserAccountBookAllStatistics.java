package com.cfjn.javacf.modle;

import java.util.List;

/**
 *记账明细月份实体类
 */
public class UserAccountBookAllStatistics {
	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 总收入
	 */
	private double totalIncome;
	/**
	 * 总支出
	 */
	private double totalExpend;
	/**
	 * 总结余
	 */
	private double surplus;
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 每日数据
	 */
	private List<FinancialCommon> userAccountBookMonthList;

	public double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public double getTotalExpend() {
		return totalExpend;
	}

	public void setTotalExpend(double totalExpend) {
		this.totalExpend = totalExpend;
	}

	public List<FinancialCommon> getUserAccountBookMonthList() {
		return userAccountBookMonthList;
	}

	public void setUserAccountBookMonthList(List<FinancialCommon> userAccountBookMonthList) {
		this.userAccountBookMonthList = userAccountBookMonthList;
	}

	public double getSurplus() {
		return surplus;
	}

	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
