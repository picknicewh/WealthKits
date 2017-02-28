package com.shumi.sdk;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.hxcr.chinapay.util.Utils;
import com.shumi.sdk.env.ShumiSdkEnv;
import com.shumi.sdk.ext.logging.ShumiSdkAndroidLogHandler;
import com.shumi.sdk.utils.ShumiSdkAndroidUtil;

/**
 * 数米SDK初始化工具
 * @author John
 *
 */
public class ShumiSdkInitializer {
	
	/**
	 * 初始化交易插件
	 * @param context
	 */
	public static void init(Context context) {
		// 初始化数米交易插件
		ShumiSdkEnv.init(context);
		
		// 初始化默认日志回调
		ShumiSdkAndroidLogHandler.init(false);
		
		// 初始化ChinaPaySDK
		try {
			Utils.setPackageName(ShumiSdkAndroidUtil.getPackageName(context));
		} catch (NameNotFoundException e) {
			Utils.setPackageName("com.shumi.sdk");
		}
	}
}
