package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeAvailableFundsBean;

/**
 * 获得可申购、定投基金列表Demo<br>
 * TIPS：继承自{@link ShumiSdkOpenApiDataService}<br>
 * 成功返回dataObject为List&ltShumiSdkTradeAvailableFundBean&gt
 * @author John
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_common.getavailablefunds", userLevel = false, bean = ShumiSdkTradeAvailableFundsBean.class, isArrayBean = true)
public class ShumiSdkGetAvaiableFundsDataService extends ShumiSdkOpenApiDataService {

	public ShumiSdkGetAvaiableFundsDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}
	
	public ShumiSdkTradeAvailableFundsBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeAvailableFundsBean.class);
	}
}
