package com.shumi.sdk.ext.logging;

import android.util.Log;

import com.shumi.sdk.logging.ShumiSdkLogContent;
import com.shumi.sdk.logging.ShumiSdkLogHandler;
import com.shumi.sdk.logging.ShumiSdkLogger;

/**
 * 日志回调函数<br>
 * 默认使用android.util.log函数，如要自定义日志输出 则需要继承ShumiSdkLogHandler并实现log方法
 * 并使用ShumiSdkLogger.getInstance().addObserver注册回调 可以在
 * 
 * @author John
 * 
 */
public class ShumiSdkAndroidLogHandler extends ShumiSdkLogHandler {
	/**
	 * 初始化LogHandler
	 * 
	 * @param debug
	 */
	public static void init(boolean debug) {
		if (mInstance == null) {
			mInstance = new ShumiSdkAndroidLogHandler();
		}
		ShumiSdkLogger logger = ShumiSdkLogger.getInstance();
		logger.deleteObserver(mInstance);
		logger.addObserver(mInstance);
		mInstance.setLogPriority(debug ? Log.DEBUG : Log.INFO);
	}

	private static ShumiSdkAndroidLogHandler mInstance;

	@Override
	public void log(ShumiSdkLogContent logContent) {
		Log.println(logContent.getPriority(), logContent.getTag(),
				logContent.getMessageEx());
	}
}
