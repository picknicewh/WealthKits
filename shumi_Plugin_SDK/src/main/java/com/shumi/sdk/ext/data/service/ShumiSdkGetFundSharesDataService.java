package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeFundSharesBean;

/**
 * 查询持仓请求 返回DataObject: List&ltShumiSdkTradeFundSharesBean&gt
 * 返回所有基金持仓
 * 
 * @author John
 *
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_foundation.getfundshares", bean = ShumiSdkTradeFundSharesBean.class, isArrayBean = true)
public class ShumiSdkGetFundSharesDataService extends
		ShumiSdkOpenApiDataService {

	public ShumiSdkGetFundSharesDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}

	public ShumiSdkTradeFundSharesBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeFundSharesBean.class);
	}
}
