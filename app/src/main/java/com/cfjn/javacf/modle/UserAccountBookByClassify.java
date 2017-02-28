package com.cfjn.javacf.modle;
/**
 *记账明细日实体类
 */
public class UserAccountBookByClassify {

	private int code;

	private String name;

	private double totalExpend;

	private double totalIncome;

	private int type;

	private int  image;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotalExpend() {
		return totalExpend;
	}

	public void setTotalExpend(double totalExpend) {
		this.totalExpend = totalExpend;
	}

	public double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

}
