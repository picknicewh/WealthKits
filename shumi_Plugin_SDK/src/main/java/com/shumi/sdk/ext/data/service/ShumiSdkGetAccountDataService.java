package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeAccountBean;

/**
 * 获得数米帐号信息<br>
 * 请求成功返回{@link ShumiSdkTradeAccountBean}
 * 
 * @author John
 * 
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_account.getaccount", bean = ShumiSdkTradeAccountBean.class)
public class ShumiSdkGetAccountDataService extends ShumiSdkOpenApiDataService {


	public ShumiSdkGetAccountDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}
	
	public ShumiSdkTradeAccountBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeAccountBean.class);
	}
}
