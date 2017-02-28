package com.cfjn.javacf.modle;

public class Segment {
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 编码
	 */
	private int classifyCode;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 关键码
	 */
	private int keywordCode;
	/**
	 * 钱
	 */
	private double money;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getClassifyCode() {
		return classifyCode;
	}

	public void setClassifyCode(int classifyCode) {
		this.classifyCode = classifyCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getKeywordCode() {
		return keywordCode;
	}

	public void setKeywordCode(int keywordCode) {
		this.keywordCode = keywordCode;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
}
