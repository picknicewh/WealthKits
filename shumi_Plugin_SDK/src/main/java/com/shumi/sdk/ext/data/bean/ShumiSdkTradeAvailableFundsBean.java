package com.shumi.sdk.ext.data.bean;

import java.util.List;

import com.google.myjson.annotations.SerializedName;
import com.shumi.sdk.ext.util.ShumiSdkFundTradingDictionary;

/**
 * 可申购基金列表
 * 
 * @author John
 */
@SuppressWarnings("serial")
public class ShumiSdkTradeAvailableFundsBean extends ShumiSdkTradeBaseBean {
	public static class Item extends ShumiSdkTradeBaseBean {
		/**
		 * 基金代码
		 */
		@SerializedName("FundCode")
		public String FundCode;

		/**
		 * 基金名称
		 */
		@SerializedName("FundName")
		public String FundName;
		
		/**
		 * 份额类别<br>
		 * 参见字典{@link ShumiSdkFundTradingDictionary.Dictionary#ShareType}<br>
		 * A:前端申购 B:后端申购
		 */
		@SerializedName("ShareType")
		public String ShareType;
		
		/**
		 * 基金状态<br>
		 * 参见字典{@link ShumiSdkFundTradingDictionary.Dictionary#FundState}<br>
		 */
		@SerializedName("FundState")
		public String FundState;
		
		/**
		 * 基金类型<br>
		 * TA下发的基金类型，非行情数据的基金类型(如：股票型、混合型)<br>
		 * 参见字典{@link ShumiSdkFundTradingDictionary.Dictionary#FundType}
		 */
		@SerializedName("FundType")
		public String FundType;
		
		/**
		 * 是否可申购
		 */
		@SerializedName("DeclareState")
		public Boolean DeclareState;
		
		/**
		 * 是否可赎回
		 */
		@SerializedName("WithdrawState")
		public Boolean WithdrawState;
		
		/**
		 * 是否可普通定投
		 */
		@SerializedName("ValuagrState")
		public Boolean ValuagrState;
		
		/**
		 * 是否可认购
		 */
		@SerializedName("SubscribeState")
		public Boolean SubscribeState;
		
		/**
		 * 是否可趋势定投
		 */
		@SerializedName("TrendState")
		public Boolean TrendState;
		
		/**
		 * 基金风险等级 参见字典
		 * {@link ShumiSdkFundTradingDictionary.Dictionary#FundRiskLevel}
		 */
		@SerializedName("RiskLevel")
		public Integer RiskLevel;
		
		/**
		 * 申购最高限额
		 */
		@SerializedName("PurchaseLimitMax")
		public Double PurchaseLimitMax;
		
		/**
		 * 申购起购额
		 */
		@SerializedName("PurchaseLimitMin")
		public Double PurchaseLimitMin;
		
		/**
		 * 赎回限额
		 */
		@SerializedName("RedeemLimitMax")
		public Double RedeemLimitMax;
		
		/**
		 * 最小赎回份额
		 */
		@SerializedName("RedeemLimitMin")
		public Double RedeemLimitMin;
		/**
		 * 认购最高限额
		 */
		@SerializedName("SubscribeLimitMax")
		public Double SubscribeLimitMax;
		
		/**
		 * 认购起购额
		 */
		@SerializedName("SubscribeLimitMin")
		public Double SubscribeLimitMin;
		
		/**
		 * 快速取现最高限额
		 */
		@SerializedName("QuickcashLimitMax")
		private Double QuickcashLimitMax;
		
		/**
		 * 快速取现最低限额
		 */
		@SerializedName("QuickcashLimitMin")
		public Double QuickcashLimitMin;

		/**
		 * 定投最小份额
		 */
		@SerializedName("RationLimitMax")
		public Double RationLimitMax;

		/**
		 * 定投最大份额
		 */
		@SerializedName("RationLimitMin")
		public Double RationLimitMin;
		
		/**
		 * 转换最高限额
		 */
		@SerializedName("TransformLimitMax")
		public Double TransformLimitMax;
		
		/**
		 * 转换最低限额
		 */
		@SerializedName("TransformLimitMin")
		public Double TransformLimitMin;
		
		/**
		 * 最低保留份额
		 */
		@SerializedName("MinShares")
		public Double MinShares;
	}
	
	@SerializedName("datatable")
	public List<Item> mAvailableFund;
}
