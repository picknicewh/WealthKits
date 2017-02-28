package com.shumi.sdk.ext.data.bean;

import com.google.myjson.annotations.SerializedName;

/**
 * 返回数米网帐户信息(未开户用户此信息不全)
 * 
 * @author John
 * 
 */
@SuppressWarnings("serial")
public class ShumiSdkTradeAccountBean extends ShumiSdkTradeBaseBean {
	/**
	 * 真实姓名
	 */
	@SerializedName("RealName")
	public String RealName;

	/**
	 * 是否使用临时密码
	 */
	@SerializedName("TempTradePassword")
	public Boolean TempTradePassword;

	/**
	 * 用户名
	 */
	@SerializedName("UserName")
	public String UserName;

	/**
	 * 状态
	 */
	@SerializedName("Status")
	public Integer Status;

	/**
	 * 证件号码
	 */
	@SerializedName("CertificateNumber")
	public String CertificateNumber;

	/**
	 * 证件类型
	 */
	@SerializedName("CertificateType")
	public Integer CertificateType;

	/**
	 * Email
	 */
	@SerializedName("Email")
	public String Email;

	/**
	 * 手机号
	 */
	@SerializedName("Mobile")
	public String Mobile;

	/**
	 * 风险等级
	 */
	@SerializedName("RiskAbility")
	public String RiskAbility;

	@SerializedName("ReckoningSendType")
	public String ReckoningSendType;

	@SerializedName("ReckoningMailType")
	public String ReckoningMailType;

	/**
	 * 风险等级是否过期
	 */
	@SerializedName("RiskExpired")
	public String RiskExpired;
}