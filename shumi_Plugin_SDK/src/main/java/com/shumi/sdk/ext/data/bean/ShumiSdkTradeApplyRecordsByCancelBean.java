package com.shumi.sdk.ext.data.bean;

import java.util.List;

import com.google.myjson.annotations.SerializedName;

/**
 * 交易申请记录
 * 
 * @author John
 * 
 */
@SuppressWarnings("serial")
public class ShumiSdkTradeApplyRecordsByCancelBean extends ShumiSdkTradeBaseBean {
	public static class Item extends ShumiSdkTradeApplyRecordItemBean {
		
	}

	/**
	 * 具体记录
	 */
	@SerializedName("datatable")
	public List<Item> Items;
	
	public List<Item> getItems() {
		return Items;
	}

}
