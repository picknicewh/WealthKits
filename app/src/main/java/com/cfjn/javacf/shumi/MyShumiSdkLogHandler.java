package com.cfjn.javacf.shumi;

import android.util.Log;

import com.shumi.sdk.logging.ShumiSdkLogContent;
import com.shumi.sdk.logging.ShumiSdkLogHandler;

/**
 * 日志句柄
 * 
 * @author Shurrik
 * 
 */
public class MyShumiSdkLogHandler extends ShumiSdkLogHandler {
	public static MyShumiSdkLogHandler Instance = new MyShumiSdkLogHandler();
	// 默认的日志等级
	private volatile int logPriority = Log.DEBUG;

	public int getLogPriority() {
		return logPriority;
	}

	public void setLogPriority(int logPriority) {
		this.logPriority = logPriority;
	}

	@Override
	public void log(ShumiSdkLogContent logContent) {
		// TODO 请根据实际情况进行处理
		if (logContent.getPriority() >= logPriority) {
			Log.println(logContent.getPriority(), logContent.getTag(), logContent.getMessageEx());
		}
	}
}
