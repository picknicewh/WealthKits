package com.openhunme.cordova.chinapay;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chinapay.authplugin.activity.Initialize;
import com.chinapay.authplugin.util.CPGlobaInfo;
import com.chinapay.authplugin.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ChinaPayPlugin extends CordovaPlugin {

	private CallbackContext callbackContext;
	private String environment = "TEST";
	private String productionClientId = null;
	private String sandboxClientId = null;
	private Activity activity = null;
	private boolean serverStarted = false;
	private String packageNames;
	
	public static final int REQUEST_CHINAPAY_AUTH = 991;
	public static final int REQUEST_CHINAPAY_ERROR = 4;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
		this.activity = this.cordova.getActivity();
		boolean retValue = true;
		if (action.equals("version")) {
			this.version();
		} else if (action.equals("init")) {
			this.init(args);
		} else if (action.equals("auth")) {
			this.auth(args);
		} else {
			retValue = false;
		}
		
		return retValue;
	}
	// internal implementation 
	private void version() {
		JSONObject version = new JSONObject();
		try {
			version.put("version", "1.0");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.callbackContext.success(version);
	}
	
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO Auto-generated method stub
		super.initialize(cordova, webView);
	    // 当前版本的包名  
	    packageNames = cordova.getActivity().getPackageName();  
	}

	private void init(JSONArray args) throws JSONException {
		JSONObject jObject = args.getJSONObject(0);
		this.productionClientId = jObject.getString("ChinaPayEnvironmentProduction");
		this.sandboxClientId = jObject.getString("ChinaPayEnvironmentSandbox");
		this.callbackContext.success();
	}
	
	//升级银联插件到2.0
	private void auth(JSONArray args) throws JSONException {
		if (args.length() != 1) {
			this.callbackContext
					.error("renderPaymentUI payment object must be provided");
			return;
		}
		StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		// get payment ic_details
		JSONObject paymentObject = args.getJSONObject(0);

		//xml 为返回的订单信息 拼接而成的xml String对象
		String cpToken = paymentObject.getString("cpToken");
		xml.append("<CpPay application=\"LunchPay.Req\">").
				append("<env>").append(paymentObject.getString("environment").toUpperCase()).append("</env>").
				append("<appSysId>").append(paymentObject.getString("appSysId")).append("</appSysId>");
		if (cpToken != null && !"".equals(cpToken))
			xml.append("<cpToken>").append(cpToken).append("</cpToken>");
		xml.append("<sign>").append(paymentObject.getString("sign")).append("</sign>").
				append("</CpPay>");

		// 初始化手机POS环境
		Utils.setPackageName(packageNames);//MY_PKG是你项目的包名
		// 设置Intent指向Initialize.class
		Intent intent = new Intent(this.cordova.getActivity(), Initialize.class);
		//传入移动认证类型
		intent.putExtra (CPGlobaInfo.XML_TAG, xml.toString());

		// 使用intent跳转至移动认证插件
		this.cordova.startActivityForResult(this, intent, REQUEST_CHINAPAY_AUTH);
	}

	//升级银联插件到2.0
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (REQUEST_CHINAPAY_AUTH == requestCode) {
			Bundle extras = null;
			if (intent!=null){
				extras = intent.getExtras();
			}
			if (resultCode == Activity.RESULT_OK) {
				if (extras != null) {
					JSONObject json = new JSONObject();
					try {
						json.put("code", "0000");
						json.put("msg", "实名认证成功");
						json.put("cerType", extras.getString("cerType"));
						json.put("cerName", extras.getString("cerName"));
						json.put("cerNo", extras.getString("cerNo"));
						json.put("cardNo", extras.getString("cardNo"));
						json.put("cardMobile", extras.getString("mobile"));
						json.put("respSign", extras.getString("respSign"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.callbackContext.success(json);
				} else {
					this.callbackContext
							.error("auth was ok but no confirmation");
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				this.callbackContext.error("auth cancelled");
			} else if (resultCode == REQUEST_CHINAPAY_ERROR) {
				JSONObject error = new JSONObject();
				try {
					error.put("code", extras.getString("code"));
					error.put("msg", extras.getString("msg"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.callbackContext.error(error);
			} else {
				this.callbackContext.error(resultCode);
			}
		}
	}
}