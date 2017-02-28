package com.shumi.sdk.ext.data.bean;

import java.util.List;

import com.google.myjson.annotations.SerializedName;

@SuppressWarnings("serial")
public class ShumiSdkTradeRealFundGatherBean extends ShumiSdkTradeBaseBean{

	public static class Item extends ShumiSdkTradeBaseBean {

		// 基金代码
		@SerializedName("FundCode")
		public String fundCode;
		// 交易基金代码
		@SerializedName("TradeFundCode")
		public String tradeFundCode;
		// 基金简称
		@SerializedName("FundSimpleName")
		public String fundSimpleName;
		// 基金简称
		@SerializedName("FundFourWordName")
		public String fourLengthFundName;
		// 基金类型
		@SerializedName("FundType")
		public Integer fundType;
		// 今日市值
		@SerializedName("TodayHoldCityValue")
		public Double todayHoldCityValue;
		// 份额
		@SerializedName("TotalShare")
		public Double totalShare;
		// 持仓本金
		@SerializedName("HoldCost")
		public Double holdCost;
		// 今日收益
		@SerializedName("TodayHoldIncome")
		public Double todayHoldIncome;
		// 今日收益率
		@SerializedName("TodayHoldIncomeRate")
		public Double todayHoldIncomeRate;
		// 持仓收益
		@SerializedName("HoldTotalIncome")
		public Double holdTotalIncome;
		// 持仓收益率
		@SerializedName("HoldTotalIncomeRate")
		public Double holdTotalIncomeRate;
		// 累计收益
		@SerializedName("TotalIncome")
		public Double totalIncome;
		// 未付收益
		@SerializedName("UnpayIncome")
		public Double unPaidIncome;
		// 剩余分红
		@SerializedName("RemainBonus")
		public Double remainBonus;
		// 净值
		@SerializedName("NetValue")
		public Double netValue;
		// 净值增长率(七日年化)
		@SerializedName("NetValuePercent")
		public Double netValuePercent;
		// 净值更新日期
		@SerializedName("NetValueDay")
		public String netValueDay;
		// 是否有分红
		@SerializedName("IfHasBonus")
		public Boolean ifHasBonus;
		// 是否有未确认的份额
		@SerializedName("IfHasUnConfirm")
		public Boolean ifHasUnConfirm;
		// 是否有拆分折算
		@SerializedName("IfSharesSplit")
		public Boolean ifSharesSplit;
		// 收益更新日期
		@SerializedName("DealDate")
		public String dealDate;
	}

	@SerializedName("datatable")
	public List<Item> mFundGathers;

	public List<Item> getItems() {
		return mFundGathers;
	}
}
