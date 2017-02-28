package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeBindedBankCardBean;

@ShumiSdkOpenApiDataRequestTag(uri = "/trade_payment.getbindbankcards", bean = ShumiSdkTradeBindedBankCardBean.class, isArrayBean = true)
public class ShumiSdkGetBindBankCardsDataService extends
		ShumiSdkOpenApiDataService {

	public ShumiSdkGetBindBankCardsDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}
	
	public ShumiSdkTradeBindedBankCardBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeBindedBankCardBean.class);
	}
}
