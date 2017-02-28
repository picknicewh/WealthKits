package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeFundSharesBean;

/**
 * 查询持仓请求 返回DataObject: List&ltShumiSdkTradeFundSharesBean&gt
 * 返回货币基金持仓
 * 
 * @author John
 *
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_foundation.getfundsharesbymonetary", bean = ShumiSdkTradeFundSharesBean.class, isArrayBean = true)
public class ShumiSdkGetFundSharesMonetaryDataService extends
		ShumiSdkOpenApiDataService {

	public ShumiSdkGetFundSharesMonetaryDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}

	public ShumiSdkTradeFundSharesBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeFundSharesBean.class);
	}
}
