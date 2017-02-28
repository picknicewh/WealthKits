package com.openhunme.cordova.utility;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HttpUtil extends HttpUtils {
	private static HttpUtil httpUtil;

	public static HttpUtil getInstance() {
		if (httpUtil == null) {
			httpUtil = new HttpUtil();
			httpUtil.configCurrentHttpCacheExpiry(100000);
			SSLSocketFactory sf = null;
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				sf = new SSLSocketFactoryEx(trustStore);
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
			registry.register(new Scheme("https", sf, 8443));
			httpUtil.configSSLSocketFactory(sf);
		}
		return httpUtil;
	}

	/**
	 * Post请求服务器
	 * 
	 * @param uri
	 *            请求的URI
	 * @param requestParamsMap
	 *            请求参数
	 * @param jsonCatcher
	 *            JSON捕手
	 */
	public static void sendGet(String uri, Map<String, String> requestParamsMap, JsonCatcher jsonCatcher) {
		if (uri == null) {
			return;
		}
		final RequestParams requestParams = new RequestParams();
		requestParams.addHeader("Accept-Encoding", "gzip,deflate");
		requestParams.addHeader("Authorization", "simple_auth_v1");
		// 处理请求参数
		if (requestParamsMap != null) {
			Set<Entry<String, String>> set = requestParamsMap.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				requestParams.addQueryStringParameter(entry.getKey(), entry.getValue());
			}
		}
		connect(HttpMethod.GET, uri, requestParams, jsonCatcher, 0);
	}

	/**
	 * Post请求服务器
	 * 
	 * @param uri
	 *            请求的URI
	 * @param requestParamsMap
	 *            请求参数
	 * @param jsonCatcher
	 *            JSON捕手
	 */
	public static void sendPost(String uri, Map<String, String> requestParamsMap, JsonCatcher jsonCatcher) {
		if (uri == null) {
			return;
		}
		final RequestParams requestParams = new RequestParams();
		requestParams.addHeader("Accept-Encoding", "gzip,deflate");
		requestParams.addHeader("Authorization", "simple_auth_v1");
		// 处理请求参数
		if (requestParamsMap != null) {
			Set<Entry<String, String>> set = requestParamsMap.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				requestParams.addQueryStringParameter(entry.getKey(), entry.getValue());
			}
		}
		connect(HttpMethod.POST, uri, requestParams, jsonCatcher, 0);
	}

	/**
	 * 递归请求备服务器
	 */
	private static void connect(final HttpMethod method, final String url, final RequestParams requestParams, final JsonCatcher jsonCatcher, final int location) {
//			G.log(url);
			HttpUtil.getInstance().send(method, url, requestParams, new RequestCallBack<String>() {
				@Override
				public void onStart() {
				}

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					if (jsonCatcher != null) {
						jsonCatcher.onCatched(url, responseInfo.result);
					}
//					G.log(responseInfo.result);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					String errorMsg = "连接服务器异常...";
					if (jsonCatcher != null) {
						jsonCatcher.onCatched("ERROR", errorMsg);
					}
//					G.log(errorMsg);
				}
			});
		}
}
