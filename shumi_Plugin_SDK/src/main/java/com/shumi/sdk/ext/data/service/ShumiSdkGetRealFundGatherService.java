package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeRealFundGatherBean;

@ShumiSdkOpenApiDataRequestTag(uri = "/trade_foundation.getrealfundgather", bean = ShumiSdkTradeRealFundGatherBean.class, isArrayBean = true)
public class ShumiSdkGetRealFundGatherService extends
		ShumiSdkOpenApiDataService {
	public ShumiSdkGetRealFundGatherService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}

	public ShumiSdkTradeRealFundGatherBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeRealFundGatherBean.class);
	}
}
