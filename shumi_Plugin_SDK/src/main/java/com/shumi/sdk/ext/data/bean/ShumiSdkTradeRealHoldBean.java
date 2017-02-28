package com.shumi.sdk.ext.data.bean;

import com.google.myjson.annotations.SerializedName;

@SuppressWarnings("serial")
public class ShumiSdkTradeRealHoldBean extends ShumiSdkTradeBaseBean{

	// 持仓分红
	@SerializedName("HoldBonus")
	public Double holdBonus;
	// 持仓市值
	@SerializedName("HoldCityValue")
	public Double holdCityValue;
	// 持仓收益
	@SerializedName("HoldIncome")
	public Double holdIncome;
	// 持仓收益率
	@SerializedName("HoldIncomeRate")
	public Double holdIncomeRate;
	// 持仓总份额
	@SerializedName("HoldShare")
	public Double holdShare;
	// 今日收益
	@SerializedName("TodayIncome")
	public Double todayIncome;
	// 今日收益率
	@SerializedName("TodayIncomeRate")
	public Double todayIncomeRate;
	// 总收益
	@SerializedName("TotalIncome")
	public Double totalIncome;
	// 净值
	@SerializedName("NetValue")
	public Double netValue;
	// 净值增长率(七日年化)
	@SerializedName("NetValuePercent")
	public Double netValuePercent;
	// 记录条数
	@SerializedName("Total")
	public Integer total;
	// 日期
	@SerializedName("DealDate")
	public String dealDate;
	// 是否有未确认的份额
	@SerializedName("IfHasUnConfirm")
	public Boolean ifHasUnConfirm;
	// 是否有拆分或者折算
	@SerializedName("ifHadBonusOrSplit")
	public Boolean ifHadBonusOrSplit;

}
