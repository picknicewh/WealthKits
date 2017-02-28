package com.shumi.sdk.ext.diagnostics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.widget.Toast;

/**
 * 崩溃处理
 * 
 * @author John
 * 
 */
public class ShumiSdkCrashReportHandler implements UncaughtExceptionHandler {
	private static ShumiSdkCrashReportHandler mInstance;
	// 交易插件2.0版包名
	private static final String PACKAGE_NAME_SHUMI_SDK = "com.shumi.sdk";
	// 交易插件1.0版包名
	private static final String PACKAGE_NAME_FUND123_SDK = "com.fund123.sdk";

	/**
	 * 获得singleton
	 * @param ctx
	 * @return
	 */
	public static ShumiSdkCrashReportHandler getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new ShumiSdkCrashReportHandler();
			mInstance.mContext = ctx.getApplicationContext();
		}
		return mInstance;
	}
	
	private Context mContext;

	/**
	 * 未处理异常
	 * 
	 * @param thread
	 *            异常发生的thread
	 * @param tr
	 *            未处理异常
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable tr) {
		StackTraceElement element = tr.getStackTrace()[0];
		// 错误的堆栈信息:writer.toString()
		StringWriter writer = new StringWriter();
		
		// 日志打印
		tr.printStackTrace(new PrintWriter(writer));
		
		// TODO 异常处理，这里只是简单的记录，如果是sdk的异常，则可以选择发至数米崩溃报告服务器去
		// 数米sdk的包名均以"com.shumi"开头
		if (element.getClassName().startsWith(PACKAGE_NAME_SHUMI_SDK)
				|| element.getClassName().startsWith(PACKAGE_NAME_FUND123_SDK)) {
			String msg = String.format("数米sdk中的%s:%d中%s.%s方法发生崩溃了",
					element.getFileName(), element.getLineNumber(),
					element.getClassName(), element.getMethodName());
			Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
		}
		tr.printStackTrace();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
