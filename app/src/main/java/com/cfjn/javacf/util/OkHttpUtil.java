package com.cfjn.javacf.util;

import android.support.annotation.Nullable;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.lzy.okhttputils.request.PostRequest;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 作    者： ZLL
 * 时    间： 2016/7/5
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class OkHttpUtil {
    private static OkHttpUtils httpUtils;
    public static String errMsg="您的网络好像出现了一些问题...";
    public static PostRequest postRequest;
    private static OkHttpUtils getInstance() {
        if(null==httpUtils){
            httpUtils =OkHttpUtils.getInstance()
                    .setCertificates("") // 自签名https的证书，可变参数，可以设置多个
                    .debug("OkHttpUtils")                                              //是否打开调试
                    .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        }
        return httpUtils;
    }

    /**
     *  没有缓存的请求
     * @param uri 请求地址
     * @param requestParamsMap 请求参数
     * @param okHttpListener 请求回调对象
     */
    public static void sendPost(final String uri, Map<String, String> requestParamsMap, final OKHttpListener okHttpListener) {
        if(null==uri||okHttpListener==null) {
            G.log("参数或者访问地址为空");
            return;
        }
        postRequest= OkHttpUtil.getInstance()
                               .post(ServerConfigManager.SERVER_IP+uri);
        doInternet(uri,requestParamsMap,okHttpListener);
    }

    /**
     * 带缓存的请求
     * @param uri 请求地址
     * @param requestParamsMap  请求参数
     * @param okHttpListener  请求回调对象
     * @param cacheMode   缓存模式
     * @param cacheKey  缓存名
     */
    public static void sendPost(final String uri, Map<String, String> requestParamsMap,
                                final OKHttpListener okHttpListener, int cacheMode, String cacheKey) {
        if(null==uri||okHttpListener==null) {
            G.log("参数或者访问地址为空");
            return;
        }
//        DEFAULT: 按照HTTP协议的默认缓存规则，例如有304响应头时缓存
//        REQUEST_FAILED_READ_CACHE：先请求网络，如果请求网络失败，则读取缓存，如果读取缓存失败，本次请求失败。
//                                   该缓存模式的使用，会根据实际情况，导致onResponse,onError,onAfter三个方法调用不只一次，
//                                   具体请在三个方法返回的参数中进行判断。
//        IF_NONE_CACHE_REQUEST：如果缓存不存在才请求网络，否则使用缓存。
//        FIRST_CACHE_THEN_REQUEST：先使用缓存，不管是否存在，仍然请求网络，如果网络顺利，会导致onResponse方法执行两次，第一次isFromCache为true，第二次isFromCache为false。
//                                 使用时根据实际情况，对onResponse,onError,onAfter三个方法进行具体判断。
        CacheMode mode;
        switch (cacheMode){
            case 1:
                mode=CacheMode.DEFAULT;
                break;
            case 2:
                mode=CacheMode.REQUEST_FAILED_READ_CACHE;
                break;
            case 3:
                mode=CacheMode.IF_NONE_CACHE_REQUEST;
                break;
            default:
                mode=CacheMode.FIRST_CACHE_THEN_REQUEST;
                break;
        }

        postRequest=OkHttpUtil.getInstance()
                .post(ServerConfigManager.SERVER_IP+uri)
                .cacheMode(mode)
                .cacheKey(cacheKey);
        doInternet(uri,requestParamsMap,okHttpListener);
    }

    private static void doInternet(final String uri, Map<String, String> requestParamsMap,
                            final OKHttpListener okHttpListener){
        HttpParams params=new HttpParams();
        if (requestParamsMap != null) {
            Set<Map.Entry<String, String>> set = requestParamsMap.entrySet();
            Iterator<Map.Entry<String, String>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                params.put(entry.getKey(), entry.getValue());
            }
        }

        postRequest.params(params)
                .execute(new AbsCallback<String>() {
                    @Override
                    public String parseNetworkResponse(Response response) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        okHttpListener.onSuccess(uri,s);
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        if(null==e)
                            okHttpListener.onError(uri,response.networkResponse().toString());
                         else
                            okHttpListener.onError(uri,e.toString());
                    }
                });

    }

}
