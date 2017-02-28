package com.shumi.sdk.ext.data.bean;

import com.google.myjson.annotations.SerializedName;

/**
 * 数米帐户额外信息
 * @author John
 *
 */
@SuppressWarnings("serial")
public class ShumiSdkTradeAccountExtraInfoBean extends ShumiSdkTradeBaseBean {
	/**
	 * 申请份额
	 */
	@SerializedName("ApplyShare")
	public Double ApplyShare;
	
	/**
	 * 申请份额:现金宝
	 */
	@SerializedName("ApplyShareOfCash")
	public Double ApplyShareOfCash;
	
	/**
	 * 申请金额
	 */
	@SerializedName("ApplySum")
	public Double ApplySum;
	
	/**
	 * 申请金额:现金宝
	 */
	@SerializedName("ApplySumOfCash")
	public Double ApplySumOfCash;
	
	/**
	 * 总申请
	 */
	@SerializedName("ApplyTotal")
	public Integer ApplyTotal;
	
	/**
	 * 总确认失败数
	 */
	@SerializedName("ConfirmFailTotal")
	public Integer ConfirmFailTotal;
	
	/**
	 * 总确认成功数
	 */
	@SerializedName("ConfirmTotal")
	public Integer ConfirmTotal;
	
	/**
	 * 总分红数量
	 */
	@SerializedName("DividendTotal")
	public Integer DividendTotal;
}
