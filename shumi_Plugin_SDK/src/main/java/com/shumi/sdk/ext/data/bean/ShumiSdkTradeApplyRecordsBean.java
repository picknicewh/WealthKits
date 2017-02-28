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
public class ShumiSdkTradeApplyRecordsBean extends ShumiSdkTradeBaseBean {
	public static class Item extends ShumiSdkTradeApplyRecordItemBean {
		
	}

	/**
	 * 总记录条数
	 */
	@SerializedName("Total")
	public Integer Total;

	/**
	 * 具体记录
	 */
	@SerializedName("Items")
	public List<Item> Items;
	
	public List<Item> getItems() {
		return Items;
	}

}
