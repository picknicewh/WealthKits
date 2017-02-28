package com.cfjn.javacf.modle;

import java.util.List;

public class UserClassifyAccount {
	private int classifyCode;
	private double classifyMoney;
	private List<UserAccountBook> userAccountBookList;
	public int getClassifyCode() {
		return classifyCode;
	}

	public void setClassifyCode(int classifyCode) {
		this.classifyCode = classifyCode;
	}

	public double getClassifyMoney() {
		return classifyMoney;
	}

	public void setClassifyMoney(double classifyMoney) {
		this.classifyMoney = classifyMoney;
	}

	public List<UserAccountBook> getUserAccountBookList() {
		return userAccountBookList;
	}

	public void setUserAccountBookList(List<UserAccountBook> userAccountBookList) {
		this.userAccountBookList = userAccountBookList;
	}
}
