package com.cfjn.javacf.modle;

import java.util.List;

public class UserAccountBookChart {

	private String loginName;

	private double totalIncome;

	private double totalExpend;

	private List<UserAccountBookByClassify> userAccountBookByClassifyVos;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

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

	public List<UserAccountBookByClassify> getUserAccountBookByClassifyVos() {
		return userAccountBookByClassifyVos;
	}

	public void setUserAccountBookByClassifyVos(List<UserAccountBookByClassify> userAccountBookByClassifyVos) {
		this.userAccountBookByClassifyVos = userAccountBookByClassifyVos;
	}

}
