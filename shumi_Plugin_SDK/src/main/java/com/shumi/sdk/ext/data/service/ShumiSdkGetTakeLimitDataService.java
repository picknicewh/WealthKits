package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeTakeLimitBean;

/**
 * 获得快速取现交易限制 请求成功返回{@link ShumiSdkTradeTakeLimitBean}
 * 
 * @author John
 * 
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_cash.gettakelimit", bean = ShumiSdkTradeTakeLimitBean.class)
public class ShumiSdkGetTakeLimitDataService extends ShumiSdkOpenApiDataService {


	public ShumiSdkGetTakeLimitDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}
	
	
	public ShumiSdkTradeTakeLimitBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeTakeLimitBean.class);
	}
}
