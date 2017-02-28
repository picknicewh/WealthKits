package com.cfjn.javacf.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.lidroid.xutils.HttpUtils;

public class PackageUtils extends HttpUtils {
	/**
	 * 获取软件版本号，对应AndroidManifest.xml下android:versionCode
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo("com.cfjn.javacf", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取软件版本号，对应AndroidManifest.xml下android:versionCode
	 */
	public static String getVersionName(Context context) {
		String versionName = null;
		try {
			versionName = context.getPackageManager().getPackageInfo("com.cfjn.javacf", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

}
