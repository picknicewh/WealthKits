package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeFundSharesBean;

/**
 * 可分红基金列表
 * @author John
 *
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_foundation.getfunddividlist", bean = ShumiSdkTradeFundSharesBean.class, isArrayBean = true)
public class ShumiSdkGetFundDividendListDataService  extends ShumiSdkOpenApiDataService {
	
	public ShumiSdkGetFundDividendListDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}

	public ShumiSdkTradeFundSharesBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeFundSharesBean.class);
	}
}
