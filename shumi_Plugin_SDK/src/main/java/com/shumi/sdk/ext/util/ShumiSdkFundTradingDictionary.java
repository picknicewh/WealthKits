package com.shumi.sdk.ext.util;

import android.content.Context;

import com.fund123.sdk.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * 数米OpenApi字典帮助类<br>
 * 由于某些业务代码不会改动
 * 所以将这些业务类型的字典数据缓存至res/raw/shumi_sdk_dict文件
 * 如果ETrading有所改动，则需要更新json文件
 * @author John
 */
public final class ShumiSdkFundTradingDictionary {
	/**
	 * 数米openapi字典项
	 * @author John
	 *
	 */
	public enum Dictionary {
		/** 份额类别  */
		ShareType,
		/** 分红方式 */
		BonusType,
		/**基金状态  */
		FundState,
		/** 基金类型 */
		FundType,
		/** 基金风险 */
		FundRiskLevel,
		/** 扣款状态 */
		PayResult,
		/** 全量的业务名称 */
		BusinFlag,
		/** 交易确认标志 */
		ConfirmState,
		/** 资金方式 */
		CapitalMode,
	}	
	
	/**
	 * 字典类型
	 */
	private EnumMap<Dictionary, HashMap<String, String>> mDictEnumMap;
	/**
	 * Singleton对象
	 */
	private static ShumiSdkFundTradingDictionary mInstance;
	private Context mContext;

	/**
	 * 获得该类的singleton实例
	 * @return sinleton实例
	 */
	public static ShumiSdkFundTradingDictionary getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new ShumiSdkFundTradingDictionary(context);
		}
		return mInstance;
	}
	
	/**
	 * 查询字典
	 */
	public<T> String lookup(Dictionary dict, T key) {
		String val = "";
		if (key != null && mDictEnumMap.containsKey(dict)) {
			HashMap<String, String> map = mDictEnumMap.get(dict);
			if (map.containsKey(key.toString())) {
				val = map.get(key.toString());
			}
		}
		return val;
	}
	
	private ShumiSdkFundTradingDictionary(Context context) {
		mContext = context;
		mDictEnumMap = new EnumMap<Dictionary, HashMap<String,String>>(Dictionary.class);
		reload();
	}
	
	public void reload() {
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					mContext.getResources().openRawResource(R.raw.shumi_sdk_dict)));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				sb.append(s);
			}
			JSONObject jsonObj = new JSONObject(sb.toString());
			mDictEnumMap.clear();
			// 只获取枚举的字典项
			for (Dictionary d : Dictionary.values()) {
				HashMap<String, String> dictionMap = new HashMap<String, String>();
				JSONArray jsonArray = jsonObj.getJSONArray(d.toString());
				if (jsonArray != null) {
					for (int i = 0; i != jsonArray.length(); ++i) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						String key = jo.getString("key");

						String value = jo.getString("value");
						dictionMap.put(key, value);
					}
				}
				if (dictionMap.size() != 0) {
					mDictEnumMap.put(d, dictionMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
