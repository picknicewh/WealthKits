package com.cfjn.javacf.utility.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cfjn.javacf.util.HunmeApplication;

import java.util.Set;

/*
 * 文件名 
 * 		SharedPreferencesUtil.java
 * 包含类名列表
 * 
 * 版本信息，版本号
 * 		v11.0.0
 * 创建日期。
 * 		2014.06.09
 * 附加注释
 * 		单例SharedPreferences的引用，需在程序启动的时候初始化
 */
public class SharedPreferencesUtil {
	private static SharedPreferences mSharedPreferences;
	private static Editor editor;
	private static SharedPreferencesUtil preferencesInstance;

	public static String TF_PREFERENCE_NAME = "tfsharedpreference";

	public static SharedPreferencesUtil getInstance() {
		if( preferencesInstance == null) {
			preferencesInstance = new SharedPreferencesUtil();
		}
		
		if (mSharedPreferences == null) {
			mSharedPreferences = HunmeApplication.getInstance()
					.getSharedPreferences(TF_PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
		if (editor == null) {
			editor = mSharedPreferences.edit();
		}
		return preferencesInstance;
	}

	public static boolean contains(String key) {
		getInstance();
		return mSharedPreferences.contains(key);
	}

	public static int getIntValueByKey(String key, int defValue) {
		getInstance();
		return mSharedPreferences.getInt(key, defValue);
	}

	public static String getStringValueByKey(String key, String defValue) {
		getInstance();
		return mSharedPreferences.getString(key, defValue);
	}

	public static float getfloatValueByKey(String key, Float defValue) {
		getInstance();
		return mSharedPreferences.getFloat(key, defValue);
	}

	public static boolean getBooleanValueByKey(String key, Boolean defValue) {
		getInstance();
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public static long getLongValueByKey(String key, long defValue) {
		getInstance();
		return mSharedPreferences.getLong(key, defValue);
	}

	public static void setBooleanValueByKey(String key, boolean value) {
		getInstance();
		editor.putBoolean(key, value).commit();
	}

	public static void setStringValueByKey(String key, String value) {
		getInstance();
		editor.putString(key, value).commit();
	}

	public static void setIntValueByKey(String key, int value) {
		getInstance();
		editor.putInt(key, value).commit();
	}

	public static void setFloatValueByKey(String key, Float value) {
		getInstance();
		editor.putFloat(key, value).commit();
	}

	public static void setLongValueByKey(String key, Long value) {
		getInstance();
		editor.putLong(key, value).commit();
	}
	
	public static void setStringSetByKey(String key,Set<String> set){
		getInstance();
		editor.putStringSet(key, set).commit();
	}
	
	public static String getDeviceUUIDByKey(String key, String defValue) {
		getInstance();
		return mSharedPreferences.getString(key, defValue);
	}
	
	public static void setDeviceUUIDByKey(String key, String Value){
		getInstance();
		editor.putString(key, Value).commit();
	}
	
	
}
