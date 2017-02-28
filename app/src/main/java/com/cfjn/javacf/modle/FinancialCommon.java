package com.cfjn.javacf.modle;
/**
 * 
 * 记账明细每日实体类
 *
 */
public class FinancialCommon implements Comparable<FinancialCommon>{
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
	 * 几号
	 */
	private String weekName;
	/**
	 * 周几
	 */
	private String dayName;

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

	public double getSurplus() {
		return surplus;
	}

	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	@Override
	public int compareTo(FinancialCommon another) {
		// TODO Auto-generated method stub
		int day = Integer.parseInt(dayName);
		int anotherDay = Integer.parseInt(another.getDayName());
		return day>anotherDay?-1:1;
	}
}
