package com.openhunme.sdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cfjn.javacf.util.HunmeApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class APIUtil {
		public static final  String PREFS_DEVICE_ID ="pref_device_id";

	public static boolean isWifi(Context context) {  
	    ConnectivityManager connectivityManager = (ConnectivityManager) context  
	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
	    if (activeNetInfo != null 
	            && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
	        return true;
	    }  
	    return false;
	}
	public static String getPackageName(Context context) {
		String name = "";
        try {
            name = context.getApplicationInfo().packageName;
        } catch (Exception e) {
        }
        return name;
	}
	public static String getAppVersionName(Context context) {
		String name = "";
		PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
        	packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            name = packInfo.versionName;
        } catch (Exception e) {
        }
        return name;
	}
	public static int getAppVersionCode(Context context) {
		int code = 0;
		PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
        	packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            code = packInfo.versionCode;
        } catch (Exception e) {
        }
        return code;
	}

	public static boolean hasEnoughModelSpace(final long size) {
		boolean ret = false;
		try {
			final File path = Environment.getDataDirectory();
			final StatFs stat = new StatFs(path.getPath());
			final long blockSize;
			final long availableBlocks;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
				blockSize = stat.getBlockSizeLong();
				availableBlocks = stat.getAvailableBlocksLong();
			} else {
				blockSize = stat.getBlockSize();
				availableBlocks = stat.getAvailableBlocks();
			}
			if (availableBlocks * blockSize >= size)
				ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean hasEnoughAppSpace(final long size) {
		boolean ret = false;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				final File path = Environment.getExternalStorageDirectory();
				final StatFs stat = new StatFs(path.getPath());
				final long blockSize;
				final long availableBlocks;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
					blockSize = stat.getBlockSizeLong();
					availableBlocks = stat.getAvailableBlocksLong();
				} else {
					blockSize = stat.getBlockSize();
					availableBlocks = stat.getAvailableBlocks();
				}
				if (availableBlocks * blockSize >= size)
					ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 *   获取包中的渠道号
	 * @param context
	 * @param channelKey  一般传入 tfchannel
	 * @return
	 */
	public static String getChannelId(Context context, String channelKey) {
		String channelId = "xcmkh"; //默认的渠道号
         //从apk包中获取
			ApplicationInfo appinfo = context.getApplicationInfo();
			String sourceDir = appinfo.sourceDir;
			//注意这里：默认放在meta-inf/里， 所以需要再拼接一下
			String key = "META-INF/" + channelKey;
			String ret = "";
			ZipFile zipfile = null;
			try {
				zipfile = new ZipFile(sourceDir);
				Enumeration<?> entries = zipfile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = ((ZipEntry) entries.nextElement());
					String entryName = entry.getName();
					if (entryName.startsWith(key)) {
						ret = entryName;
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (zipfile != null) {
					try {
						zipfile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			String[] split = ret.split("_");
			if (split != null && split.length >= 2) {
				channelId = ret.substring(split[0].length() + 1);
			}
		return channelId;
	}

	/**
	 * 获取设备的唯一id
	 * @return
	 */
	public static String getDeviceUUId(Context context) {
		String deviceUUId = "";
		try {
			deviceUUId = SharedPreferencesUtil.getDeviceUUIDByKey(
					PREFS_DEVICE_ID, "");
			if (!StringHelper.isEmpty(deviceUUId)) {
				// 使用原来已存储的uu id
				return deviceUUId;
			} else {
				// 使用imei
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(context.TELEPHONY_SERVICE);
				deviceUUId = tm.getDeviceId();
				if (!StringHelper.isEmpty(deviceUUId)) {
					SharedPreferencesUtil.setDeviceUUIDByKey(PREFS_DEVICE_ID,
							deviceUUId);
					return deviceUUId;
				}

				// 使用 cup 序列号
				deviceUUId = getCpuSerial();
				if (!"0000000000000000".equalsIgnoreCase(deviceUUId)) {
					// cup 序列号 是有效的
					SharedPreferencesUtil.setDeviceUUIDByKey(PREFS_DEVICE_ID,
							deviceUUId);
					return deviceUUId;
				}

				// 使用 Android ID
				deviceUUId = Secure.getString(HunmeApplication.getInstance()
						.getContentResolver(), Secure.ANDROID_ID);
				if (!"9774d56d682e549c".equals(deviceUUId)) {
					// 安卓id 号 是有效的
					SharedPreferencesUtil.setDeviceUUIDByKey(PREFS_DEVICE_ID,
							deviceUUId);
					return deviceUUId;
				}
				// 使用一个随机的UUID
				deviceUUId = UUID.randomUUID().toString();
				SharedPreferencesUtil.setDeviceUUIDByKey(PREFS_DEVICE_ID,
						deviceUUId);
				return deviceUUId;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "00000000";
		}
	}

	/**
	 *   获取cpu的序列号
	 * @return cpu serial
	 */
	public static String getCpuSerial() {
		String cpuSerial = "";
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null && str.startsWith("Serial")) {
					cpuSerial = str.substring(str.indexOf(":")+1, str.length()).trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			Log.i("my tag", "获取IP信息错误");
		}
		return cpuSerial;
	}
}
