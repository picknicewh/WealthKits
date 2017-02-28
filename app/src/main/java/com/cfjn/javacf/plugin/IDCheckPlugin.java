package com.cfjn.javacf.plugin;

import android.content.Intent;
import android.os.Bundle;

import com.cfjn.javacf.activity.CaptureActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * IDCheckPlugin
 *
 * Created by 罗成毅 on 2015/12/22.
 */
public class IDCheckPlugin extends CordovaPlugin{

    public static CordovaWebView staWebview = null;
    public static boolean captrue_isruning = false;
    /** ID值，用于锁定发出请求的对象通道ID **/
    private String activeCallBackContextID="0";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("popScanner")){
            if (!captrue_isruning){
                captrue_isruning = true;
                try {
                    activeCallBackContextID = callbackContext.getCallbackId();
                    staWebview = webView;
                    Intent im = new Intent(cordova.getActivity(),CaptureActivity.class);
                    Bundle bl = new Bundle();
                    bl.putString("activeCallBackContextID",activeCallBackContextID);
                    im.putExtras(bl);
                    cordova.getActivity().startActivity(im);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }
}
