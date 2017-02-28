package com.shumi.sdk.ext.data.bean;

import com.google.myjson.annotations.SerializedName;

/**
 * 错误代码bean
 * @author John
 *
 */
@SuppressWarnings("serial")
public class ShumiSdkCodeMessageBean extends ShumiSdkTradeBaseBean {
	@SerializedName("Code")
	public String Code = "0";

	@SerializedName("Message")
	public String Message = "";
}
