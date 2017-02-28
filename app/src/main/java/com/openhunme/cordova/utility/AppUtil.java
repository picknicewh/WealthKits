package com.openhunme.cordova.utility;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppUtil {
	public static String getChannel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return "" + appInfo.metaData.getInt("channel_id");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }
}
