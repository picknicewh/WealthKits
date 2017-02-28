package com.shumi.sdk.ext.data.bean;

import java.util.List;

import com.google.myjson.annotations.SerializedName;

@SuppressWarnings("serial")
public class ShumiSdkTradeBindedBankCardBean extends ShumiSdkTradeBaseBean {
	public static class Item extends ShumiSdkTradeBaseBean {

		/**
		 * 绑定的完整银行卡号
		 */
		@SerializedName("No")
		public String No;

		/**
		 * 交易帐号
		 */
		@SerializedName("TradeAccount")
		public String TradeAccount;

		/**
		 * 交易子帐号
		 */
		@SerializedName("SubTradeAccount")
		public String SubTradeAccount;

		/**
		 * 银行卡是否已验证
		 */
		@SerializedName("IsVaild")
		public Boolean IsVaild;

		/**
		 * 银行卡未验证时，每日额度限制，超过交易额度需要进行验卡<br>
		 * 已验证卡每日没有交易额度限制
		 * 
		 * @deprecated 已过时，此字段已经无意义
		 */
		@Deprecated
		@SerializedName("Balance")
		public Double Balance;

		/**
		 * 银行卡状态代码
		 */
		@SerializedName("Status")
		public String Status;

		/**
		 * 银行卡状态（中文）
		 */
		@SerializedName("StatusToCN")
		public String StatusToCN;

		/**
		 * 是否被冻结
		 */
		@SerializedName("IsFreeze")
		public Boolean IsFreeze;

		/**
		 * 银行编号
		 */
		@SerializedName("BankSerial")
		public String BankSerial;

		/**
		 * 银行名称
		 */
		@SerializedName("BankName")
		public String BankName;

		/**
		 * 绑卡渠道
		 */
		@SerializedName("CapitalMode")
		public String CapitalMode;

		/**
		 * 绑卡方式
		 */
		@SerializedName("BindWay")
		public Integer BindWay;

		/**
		 * 是否支持自动付款
		 */
		@SerializedName("SupportAutoPay")
		public Boolean SupportAutoPay;

		/**
		 * 银行卡费率折扣
		 */
		@SerializedName("DiscountRate")
		public Double DiscountRate;

		/**
		 * 限制描述<br>
		 * 如：单笔50万元，日累计50万元
		 */
		@SerializedName("LimitDescribe")
		public String LimitDescribe;

		/**
		 * 内容描述<br>
		 * 如：必须开通网上银行(U盾或动态口令)
		 */
		@SerializedName("ContentDescribe")
		public String ContentDescribe;
	}

	@SerializedName("datatable")
	public List<Item> mBindedBankCard;
}
