package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkDataContentTag;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeAvailableFundBean;

/**
 * 获得可申购、定投基金列表Demo<br>
 * TIPS：继承自{@link ShumiSdkOpenApiDataService}<br>
 * 成功返回dataObject为List&ltShumiSdkTradeAvailableFundBean&gt
 * @author John
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_common.getavailablefund", userLevel = false, bean = ShumiSdkTradeAvailableFundBean.class)
public class ShumiSdkGetAvaiableFundDataService extends ShumiSdkOpenApiDataService {
	
	public static class Param {
		/**
		 * 基金代码
		 */
		@ShumiSdkDataContentTag("fundCode")
		public String FundCode;
	}

	public ShumiSdkGetAvaiableFundDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}
	
	public ShumiSdkTradeAvailableFundBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeAvailableFundBean.class);
	}
}
