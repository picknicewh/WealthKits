package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeRealHoldBean;

@ShumiSdkOpenApiDataRequestTag(uri = "/trade_foundation.getrealhold", bean = ShumiSdkTradeRealHoldBean.class, isArrayBean = false)
public class ShumiSdkGetRealHoldService extends
		ShumiSdkOpenApiDataService {
	public ShumiSdkGetRealHoldService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}

	public ShumiSdkTradeRealHoldBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeRealHoldBean.class);
	}
}
