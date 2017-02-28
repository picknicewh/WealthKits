package com.cfjn.javacf.plugin;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.cfjn.javacf.R;
import com.cfjn.javacf.component.digitalKeyBoard.DigitalKeyBoardUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * 描述：软键盘插件，H5调用此插件可实现密码等特殊有安全要求的的输入
 * </p>
 * 
 * @author: 罗成毅
 * @corporation: 天风
 * @date: 2014年3月18日
 * @version: 1.0
 */

public class SoftKeyBoardPlugin extends CordovaPlugin {
	public static CordovaWebView sWebview = null;

	public static boolean isViewAdded = false;

	private LinearLayout mInputLayout;

	private WindowManager mWinManager;

	private LayoutParams mParams;

	private DigitalKeyBoardUtil mUtil;
	/** 当前密码键盘的值 **/
	private String currentWord;
	/** 密码键盘在敲击一个值后，返回给js当前currentword **/
	private String activeCallBackContextID;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if(action.equals("active")){
			activeCallBackContextID = callbackContext.getCallbackId();
			JSONObject json = args.getJSONObject(0);
			String oldPwd = json.optString("text");
			String maxLength = json.optString("length");
			if(oldPwd.equals("null")){
				oldPwd = "";
			}
			mUtil.setPwd(Integer.valueOf(maxLength), oldPwd);
			if(!isViewAdded){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mWinManager.addView(mInputLayout, mParams);
				isViewAdded = true;
				mUtil.randomKeyword();
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("text", oldPwd);
			PluginResult pr = new PluginResult(PluginResult.Status.OK,jsonObject);
			pr.setKeepCallback(true);
			SoftKeyBoardPlugin.this.webView.sendPluginResult(pr, activeCallBackContextID);
			//关闭系统键盘
//			View v = cordova.getActivity().getCurrentFocus();
//			 inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			return true;
		}
		if(action.equals("ic_close")){
			activeCallBackContextID = "0";
			if(isViewAdded){
				mWinManager.removeView(mInputLayout);
				isViewAdded = false;
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("isok", "true");
			callbackContext.success();
			//启动系统键盘
//			((InputMethodManager) cordova.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
//			InputMethodManager imm = (InputMethodManager)webView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.showSoftInputFromInputMethod(webView.getView().getWindowToken(),0);
			return true;
		}
		return false;
	}

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		try {
			sWebview = webView;
			activeCallBackContextID = "0";
			if (null == mInputLayout) {
				mInputLayout = (LinearLayout) LayoutInflater.from(
						cordova.getActivity()).inflate(
						R.layout.digital_key_board, null);
			}
			if (null == mWinManager) {
				mWinManager = (WindowManager) cordova.getActivity()
						.getSystemService(Context.WINDOW_SERVICE);
			}
			if (null == mParams) {
				mParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						LayoutParams.TYPE_APPLICATION,
						LayoutParams.FLAG_NOT_FOCUSABLE
								| LayoutParams.FLAG_LAYOUT_NO_LIMITS, // 没有边界
						// 半透明效果
						PixelFormat.TRANSLUCENT);
				mParams.gravity = Gravity.BOTTOM;
			}
			//初始化监听
			currentWord = "";
			mUtil = new DigitalKeyBoardUtil(mWinManager, mInputLayout,
					Integer.valueOf(6), "");
//			mUtil.init();
			mUtil.setOnKeyDown(new DigitalKeyBoardUtil.keyboardLinstener() {
				@Override
				public void onKeyDown(String words) {
					currentWord = words;
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("text", currentWord);
						PluginResult pr = new PluginResult(PluginResult.Status.OK,jsonObject);
						pr.setKeepCallback(true);
						SoftKeyBoardPlugin.this.webView.sendPluginResult(pr, activeCallBackContextID);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFinish() {
					isViewAdded = false;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
		super.initialize(cordova, webView);
	}
}
