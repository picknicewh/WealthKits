package com.cfjn.javacf.util;

/**
 * HTTP 监听器
 * @author Shurrik
 * 2016年4月28日17:30:14
 */
public interface OKHttpListener  {
	void onSuccess(String uri, String result);
	void onError(String uri, String error);
}
