package com.shumi.sdk.ext.data.service;

import android.content.Context;

import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.annotation.ShumiSdkOpenApiDataRequestTag;
import com.shumi.sdk.data.service.openapi.ShumiSdkOpenApiDataService;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeApplyRecordsByCancelBean;

/**
 * 获得可撤单的交易记录列表 
 * @author John
 * 
 */
@ShumiSdkOpenApiDataRequestTag(uri = "/trade_foundation.getapplyrecordsbycancel", bean = ShumiSdkTradeApplyRecordsByCancelBean.class, isArrayBean = true)
public class ShumiSdkApplyRecordsByCancelDataService extends ShumiSdkOpenApiDataService {

	public ShumiSdkApplyRecordsByCancelDataService(Context context,
			IShumiSdkDataBridge bridge) {
		super(context, bridge);
	}

	public ShumiSdkTradeApplyRecordsByCancelBean getData(Object obj) {
		return cast(obj, ShumiSdkTradeApplyRecordsByCancelBean.class);
	}
}
