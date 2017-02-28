package com.cfjn.javacf.modle;

import java.util.List;

public class UserBudgetStatistics {
	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 总预算
	 */
	private double totalBudget;
	/**
	 * 结余
	 */
	private double surplus;

	private List<UserBudgetClassify> userBudgetClassifyVos;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public double getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(double totalBudget) {
		this.totalBudget = totalBudget;
	}

	public double getSurplus() {
		return surplus;
	}

	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}

	public List<UserBudgetClassify> getUserBudgetClassifyVos() {
		return userBudgetClassifyVos;
	}

	public void setUserBudgetClassifyVos(List<UserBudgetClassify> userBudgetClassifyVos) {
		this.userBudgetClassifyVos = userBudgetClassifyVos;
	}

}
