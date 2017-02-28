/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.openhunme.cordova.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;

import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.util.G;
import com.chinapay.authplugin.util.CPGlobaInfo;
import com.chinapay.authplugin.util.Utils;
import com.openhunme.cordova.chinapay.ChinaPayHelper;
import com.openhunme.cordova.chinapay.ChinaPayPlugin;
import com.openhunme.cordova.messenger.MessageCenterPlugin;
import com.openhunme.cordova.utility.AppUtil;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaPlugin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HMDroidGap extends CordovaActivity  {
    private static String loadUrlStr = null;
    public static final String TAG = "HMDroidGap";
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        CookieManager.setAcceptFileSchemeCookies(true);
        final Intent intent = getIntent();
        loadUrlStr = intent.getStringExtra("loadUrl");
        if (loadUrlStr == null || loadUrlStr.equals("")) {
            loadUrlStr = Config.getStartUrl();
            if (loadUrlStr == null || loadUrlStr.equals("")) {
                loadUrlStr = "file:///android_asset/www/index.html";
            } else {
                String station = MessageCenterPlugin.getStationInfo(this);
                String channel = AppUtil.getChannel(this);
                Log.d("channel_id", channel);
                loadUrlStr = loadUrlStr + "?close=1&from=2&recommendInfos=" + channel + "&tf_op_station=" + station;
            }

        } else {
            if(loadUrlStr.equals("phone_hunme")) {
               loadUrlStr = getURL()+"&token"+UserInfo.getInstance(HMDroidGap.this).getToken()+"&key"+UserInfo.getInstance(HMDroidGap.this).getSecretKey();
//                loadUrlStr="file:///android_asset/www/phone/view/index_android.html";
                G.log(loadUrlStr+"-----------------------------");
            }else{
                setResult(-1, intent);
                super.loadUrl(loadUrlStr);

            }
        }

        setResult(RESULT_OK, intent);
        super.loadUrl(loadUrlStr);

        // this.setBooleanProperty("keepRunning", false);
//        String station = MessageCenterPlugin.getStationInfo(this);
//        String channel = "xcmkh";
//        loadUrlStr = "file:///android_asset/www/sdk/view/index_android.html#/open-prepare";
//        loadUrlStr = loadUrlStr + "?recommendInfos=" + channel + "&tf_op_station=" + station + "&open_pay_kind=1&open_return_url=&open_exit_url=";
//        Log.e("loadUrl", loadUrlStr);
//        setResult(RESULT_OK, intent);
//        super.loadUrl(loadUrlStr);
    }


    private String  getURL() {
        //        String url="file:///android_asset/www/phone_hunme/preLoading.shtml?";
        String url= MainActivity.TFurl+"/www/phone_hunme/preLoading.shtml?";
        String deviceId;
        StringBuffer loadUrlStr = new StringBuffer(url);
        try {
            loadUrlStr.append("locateProvince=" + URLEncoder.encode(G.PROVINCE, "UTF-8"));
            loadUrlStr.append("&locateCity=" + URLEncoder.encode(G.CITY, "UTF-8"));
            loadUrlStr.append("&locateArea=" + URLEncoder.encode(G.DISTRICT, "UTF-8"));
        } catch (Exception e) {
            loadUrlStr.append("locateProvince=");
            loadUrlStr.append("&locateCity=");
            loadUrlStr.append("&locateArea=");
        }
        //用户手机号
        String mobile = UserInfo.getInstance(this).getLoginName();
        G.log(mobile+"---------------rr-");
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            //设备号
             deviceId = tm.getDeviceId();
        }catch (Exception e){
             deviceId="888888";
        }
        //渠道号
        String channelNo = "7_3";
        String recommendInfos = "88001";
        //购买凭证（用户手机号或者设备号）
        String ticket = mobile;
        if (mobile == null || mobile.equals("")) {
            ticket = deviceId;
        }
        loadUrlStr.append("&mobile=" + mobile);
        loadUrlStr.append("&channelNo=" + channelNo);
        loadUrlStr.append("&recommendInfos=" + recommendInfos);
        loadUrlStr.append("&ticket=" + ticket);
        return  loadUrlStr.toString();
    }

    @Override
    protected void createViews() {
        // TODO Auto-generated method stub
//		super.createViews();
        //Why are we setting a constant as the ID? This should be investigated
        appView.getView().setId(100);
        appView.getView().setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        if (preferences.contains("BackgroundColor")) {
//            int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
            // Background of activity:
//            appView.getView().setBackgroundColor(backgroundColor);
//            appView.getView().setBackgroundResource(R.layout.welcome_page);
        }

        appView.getView().requestFocusFromTouch();

        setContentView(appView.getView());
    }


    /**
     * 新增方法，替代Cordova老版本调用方式，本Activity的方法和银联插件强耦合
     * 本方法重新修改了CordovaInterface的实现，使它支持cordoa环境下银联插件的特殊调用机制
     * 这个银联插件版本并不像其它cordova插件一样规范调用startActivityForResult，
     * 通过OnActivityResult返回结果，而是通过onResume返回结果
     */
    @Override
    protected CordovaInterfaceImpl makeCordovaInterface() {
        return new CordovaInterfaceImpl(this) {
            @Override
            public void startActivityForResult(CordovaPlugin command,
                                               Intent intent, int requestCode) {
                setActivityResultCallback(command);
                try {
                    if (requestCode == ChinaPayPlugin.REQUEST_CHINAPAY_AUTH)
                        activity.startActivity(intent);
                    else
                        activity.startActivityForResult(intent, requestCode);
                } catch (RuntimeException e) { // E.g.: ActivityNotFoundException
                    activityResultCallback = null;
                    throw e;
                }
            }
            @Override
            public Object onMessage(String id, Object data) {
                // Plumb this to CordovaActivity.onMessage for backwards compatibility
                return HMDroidGap.this.onMessage(id, data);
            }
        };
    }

    // 当Activity重新恢复时自行发起调用onResume方法
    @Override
    protected void onResume() {
        super.onResume();
        // 返回结果的格式为：
        // 1.失败：<respCode>返回码</respCode><respDesc>失败原因</respDesc>
        // 2.成功：<respCode>0000</respCode>
        // 应答码详细如下表.
        if (Utils.getPayResult() != null && !Utils.getPayResult().equals("")) {
            // 根据返回码做出相应处理
            int ret = Activity.RESULT_OK;
            Bundle extras = new Bundle();
            if (Utils.getPayResult().indexOf("0000") > -1) {
                // 认证成功，返回卡信息及用户信息
                // Log.i("AuthSDK", "姓名：" + Utils.getCerName() + "\n身份证类型：" +
                // Utils.getCerType() + "\n身份证号：" + Utils.getCerNo() + "\n卡号：" +
                // Utils.getCardNo() + "\n手机号：" + Utils.getMobile());
                extras.putString("cerName", Utils.getCerName());
                extras.putString("cerType", Utils.getCerType());
                extras.putString("cerNo", Utils.getCerNo());
                extras.putString("cardNo", Utils.getCardNo());
                extras.putString("mobile", Utils.getMobile());
                extras.putString("respSign", Utils.getRespSign());
            } else {
                // 认证失败
                ret = ChinaPayPlugin.REQUEST_CHINAPAY_ERROR;
                String raw_result = Utils.getPayResult();
                String code = ChinaPayHelper.substring(raw_result, "<respCode>", "<");
                String message = ChinaPayHelper.substring(raw_result, "<respDesc>", "<");
                extras.putString("code", code);
                extras.putString("msg", message);
            }

			/* Cordova版本升级，该代码块已经删除，为此增加新代码块
			if (this.activityResultCallback != null) {
				Intent intent = new Intent();
				intent.putExtras(extras);
				this.cordovaInterface.onActivityResult(requestCode, resultCode, intent)
				this.activityResultCallback.onActivityResult(ChinaPayPlugin.REQUEST_CHINAPAY_AUTH, ret, intent);
			}
			*/
            //新增代码块，支持新版本的Cordorva
            Intent intent = new Intent();
            intent.putExtras(extras);
            this.cordovaInterface.onActivityResult(ChinaPayPlugin.REQUEST_CHINAPAY_AUTH, ret, intent);
        }
        CPGlobaInfo.init(); // 清空返回结果
    }

    /**
     * 同步一下cookie
     */
    public static void synCookies(Activity context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        if (loadUrlStr == null) {
            Config.init(context);
            loadUrlStr = Config.getStartUrl();
        }
        cookieManager.setCookie(loadUrlStr, "");// 指定要修改的cookies
//        cookieManager.setCookie("file:///android_asset/www/tap/","");
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 修改 Report an error to the host application. These errors are
     * unrecoverable (i.e. the main resource is unavailable). The errorCode
     * parameter corresponds to one of the ERROR_* constants.
     *
     * @param errorCode
     *            The error code corresponding to an ERROR_* value.
     * @param description
     *            A String describing the error.
     * @param failingUrl
     *            The url that ic_failed to load.
     */
    @Override
    public void onReceivedError(final int errorCode, final String description, final String failingUrl) {
        String errorUrl;
        try {
            errorUrl = Config.getErrorUrl() + "?url=" + URLEncoder.encode(failingUrl, "UTF-8") + "&osVer=" + android.os.Build.VERSION.RELEASE;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            errorUrl = Config.getErrorUrl();
        }
//      Cordova版本升级，该代码块已经删除，为此增加新代码块
//		this.setStringProperty("errorUrl", errorUrl);
//		this.setBooleanProperty("clearHistory", false);
        this.preferences.set("errorUrl", errorUrl);
        this.preferences.set("clearHistory", false);
        Log.d("onReceivedError", "code:" + errorCode + ", description:" + description + ", errorUrl=" + errorUrl);
        super.onReceivedError(errorCode, description, failingUrl);
    }

}
