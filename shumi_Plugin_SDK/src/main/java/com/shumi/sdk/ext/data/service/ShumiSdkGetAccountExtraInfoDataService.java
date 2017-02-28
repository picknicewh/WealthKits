package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeAccountExtraInfoBean;

/**
 * 获得数米帐号附加信息<br>
 * 请求成功返回{@link ShumiSdkTradeAccountExtraInfoBean}
 * 
 * @author John
 * 
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_account.getaccountextrainfo", bean = ShumiSdkTradeAccountExtraInfoBean.class)
public class ShumiSdkGetAccountExtraInfoDataService extends
		ShumiSdkOpenApiDataService {

	public ShumiSdkGetAccountExtraInfoDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}
	
	public ShumiSdkTradeAccountExtraInfoBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeAccountExtraInfoBean.class);
	}
}
