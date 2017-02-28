package com.shumi.sdk.ext.data.bean;

import com.google.myjson.annotations.SerializedName;

/**
 * <p>
 * <b>快速赎回参数</b>
 * </p>
 * 快速赎回限制条件：<br>
 * <ul>
 * 必须同时满足以下条件，才可以进行快速赎回操作<br>
 * <li>已取现次数(TotalTakeTimes) < 每日取现次数上限(CumulativeLimitTimes)</li>
 * <li>已取现金额(TotalTakeAmount) < 每日取现金额上限(CumulativeLimitAmount)</li>
 * </ul>
 * 
 * @author John
 * 
 */
@SuppressWarnings("serial")
public class ShumiSdkTradeTakeLimitBean extends ShumiSdkTradeBaseBean {
	/**
	 * 每日取现次数上限
	 */
	@SerializedName("CumulativeLimitTimes")
	public Integer CumulativeLimitTimes;

	/**
	 * 每日取现金额上限
	 */
	@SerializedName("CumulativeLimitAmount")
	public Double CumulativeLimitAmount;

	/**
	 * 最低持有份额
	 */
	@SerializedName("MinimumRetained")
	public Double MinimumRetained;

	/**
	 * 单笔最大限额
	 */
	@SerializedName("SingleMaxLimitAmount")
	public Double SingleMaxLimitAmount;

	/**
	 * 单笔最小限额
	 */
	@SerializedName("SingleMinLimitAmount")
	public Double SingleMinLimitAmount;

	/**
	 * 已取现金额
	 */
	@SerializedName("TotalTakeAmount")
	public Double TotalTakeAmount;

	/**
	 * 已取现次数
	 */
	@SerializedName("TotalTakeTimes")
	public Integer TotalTakeTimes;
}
