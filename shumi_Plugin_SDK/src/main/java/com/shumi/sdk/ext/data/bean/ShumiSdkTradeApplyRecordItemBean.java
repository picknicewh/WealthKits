package com.shumi.sdk.ext.data.bean;

import com.google.myjson.annotations.SerializedName;
import com.shumi.sdk.ext.util.ShumiSdkFundTradingDictionary;

@SuppressWarnings("serial")
public class ShumiSdkTradeApplyRecordItemBean extends ShumiSdkTradeBaseBean {
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
	 * 银行卡号
	 */
	@SerializedName("BankAccount")
	public String BankAccount;

	/**
	 * 交易帐号<br>
	 */
	@SerializedName("TradeAccount")
	public String TradeAccount;
	/**
	 * 份额类别 见字典帮助类，查询
	 * {@link ShumiSdkFundTradingDictionary.Dictionary#ShareType}
	 */
	@SerializedName("ShareType")
	public String ShareType;
	
	/**
	 * 收费方式 中文
	 */
	@SerializedName("ShareTypeToCN")
	public String ShareTypeToCN;
	
	/**
	 * 申请时间<br>
	 * 输出为 C# DateTime.ToString()<br>
	 * java使用DateFormat格式 yyyy-MM-dd'T'HH:mm:ss
	 */
	@SerializedName("ApplyDateTime")
	public String ApplyDateTime;
	
	/**
	 * 确认时间<br>
	 * 输出为 C# DateTime.ToString()<br>
	 * java使用DateFormat格式 yyyy-MM-dd'T'HH:mm:ss
	 */
	@SerializedName("ConfirmDate")
	public String ConfirmDate;
	
	
	/**
	 * 赎回到账时间
	 */
	@SerializedName("RedeemAccountDate")
	public String RedeemAccountDate;
	
	/**
	 * 业务类型 见字典帮助类，查询
	 * {@link ShumiSdkFundTradingDictionary.Dictionary#BusinFlag}
	 */
	@SerializedName("BusinessType")
	public String BusinessType;
	
	/**
	 * 业务类型（中文）
	 */
	@SerializedName("BusinessTypeToCN")
	public String BusinessTypeToCN;
	/**
	 * 申请金额
	 */
	@SerializedName("Amount")
	public Double Amount;
	/**
	 * 申请份额
	 */
	@SerializedName("Shares")
	public Double Shares;
	/**
	 * 确认状态<br>
	 * 见字典帮助类，查询
	 * {@link ShumiSdkFundTradingDictionary.Dictionary#ConfirmState}
	 */
	@SerializedName("Status")
	public Integer Status;
	
	/**
	 * 确认状态 中文
	 */
	@SerializedName("StatusToCN")
	public String StatusToCN;
	
	/**
	 * 申请流水号
	 */
	@SerializedName("ApplySerial")
	public String ApplySerial;
	/**
	 * 是否可撤单
	 */
	@SerializedName("CanCancel")
	public Boolean CanCancel;
	/**
	 * 支付结果 扣款状态<br>
	 * 见字典帮助类，查询{@link ShumiSdkFundTradingDictionary.Dictionary#PayResult}
	 */
	@SerializedName("PayResult")
	public Integer PayResult;
	
	/**
	 * 支付结果 中文
	 */
	@SerializedName("PayStatusToCN")
	public String PayStatusToCN;
	
	/**
	 * 手续费
	 */
	@SerializedName("PoundAge")
	public Double PoundAge;
	
	/**
	 * 大额赎回状态
	 */
	@SerializedName("HugeWithdrawalStatus")
	public String HugeWithdrawalStatus;
	
	/**
	 * 大额赎回状态（中文）
	 */
	@SerializedName("HugeWithdrawalStatusToCN")
	public String HugeWithdrawalStatusToCN;
	
	/**
	 * 定投协议编号
	 */
	@SerializedName("AggreementNo")
	public String AggreementNo;
	
	/**
	 * 均线类型
	 */
	@SerializedName("ExpType")
	public String ExpType;
	
	/**
	 * 是否是现金宝基金
	 */
	@SerializedName("IsCash")
	public Boolean IsCash;
	
	/**
	 * 是否是现金宝赎回转购的
	 */
	@SerializedName("IsCashBuy")
	public Boolean IsCashBuy;
	
	/**
	 * 是否可兑换米币
	 */
	@SerializedName("IsExchange")
	public Boolean IsExchange;
}
