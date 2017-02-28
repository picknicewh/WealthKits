package com.shumi.sdk.ext.data.bean;

import java.util.List;

import com.google.myjson.annotations.SerializedName;
import com.shumi.sdk.ext.util.ShumiSdkFundTradingDictionary;

/**
 * 数米基金持仓
 * 
 * @author John
 * 
 */
@SuppressWarnings("serial")
public class ShumiSdkTradeFundSharesBean extends ShumiSdkTradeBaseBean {
	public static class Item extends ShumiSdkTradeBaseBean {
		/**
		 * 交易帐号
		 */
		@SerializedName("TradeAccount")
		public String TradeAccount;
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
		 * 持有份额<br>
		 * 持有份额 = 可用份额 + 冻结份额
		 */
		@SerializedName("CurrentRemainShare")
		public Double CurrentRemainShare;
		/**
		 * 可用剩余份额
		 */
		@SerializedName("UsableRemainShare")
		public Double UsableRemainShare;
		/**
		 * 冻结份额余额
		 */
		@SerializedName("FreezeRemainShare")
		public Double FreezeRemainShare;
		/**
		 * 分红方式<br>
		 * 参见字典{@link ShumiSdkFundTradingDictionary.Dictionary#BonusType}<br>
		 * "0" : 红利再投资, "1" : 现金红利
		 */
		@SerializedName("MelonMethod")
		public Integer MelonMethod;
		/**
		 * 交易冻结份额
		 */
		@SerializedName("TfreezeRemainShare")
		public Double TfreezeRemainShare;
		/**
		 * <b>到期可用余额</b><br>
		 * 超短期理财产品赎回、赎回转购时的可赎回余额
		 */
		@SerializedName("ExpireShares")
		public Double ExpireShares;

		/**
		 * 未付收益
		 */
		@SerializedName("UnpaidIncome")
		public Double UnpaidIncome;
		/**
		 * 单位净值
		 */
		@SerializedName("PernetValue")
		public Double PernetValue;
		/**
		 * 市值
		 */
		@SerializedName("MarketValue")
		public Double MarketValue;
		/**
		 * 净值日期
		 */
		@SerializedName("NavDate")
		public String NavDate;
		
		/**
		 * 银行卡号 (未四位)
		 */
		@SerializedName("BankAccount")
		public String BankAccount;
		
		/**
		 * 银行名称
		 */
		@SerializedName("BankName")
		public String BankName;
		
		/**
		 * 银行编号
		 */
		@SerializedName("BankSerial")
		public String BankSerial;
		
		/**
		 * 基金类型<br>
		 * TA下发的基金类型，非行情数据的基金类型(如：股票型、混合型)<br>
		 * 参见字典{@link ShumiSdkFundTradingDictionary.Dictionary#FundType}
		 */
		@SerializedName("FundType")
		public String FundType;
		
		/**
		 * 基金类型中文名称
		 */
		@SerializedName("FundTypeToCN")
		public String FundTypeToCN;
		
		/**
		 * 是否支持T+0赎回<br>
		 * <li>true 支持T+0赎回，可以选择调用T+0快速赎回接口或T+2普通赎回接口 <li>false
		 * 不支持T+0赎回，只能选择T+2普通赎回接口
		 */
		@SerializedName("RapidRedeem")
		public Boolean RapidRedeem;
	}
	
	@SerializedName("datatable")
	public List<Item> mFundShare;
	
	public List<Item> getItems() {
		return mFundShare;
	}
}
