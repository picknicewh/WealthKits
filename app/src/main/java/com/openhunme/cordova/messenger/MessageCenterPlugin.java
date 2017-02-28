package com.openhunme.cordova.messenger;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.openhunme.sdk.utils.APIUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageCenterPlugin extends CordovaPlugin {

	private CallbackContext callbackContext;
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
		boolean retValue = true;
		if (action.equals("handleMessage")) {
			this.handleMessage(args);
		}
		else if (action.equals("handleMessageObject")) {
			this.handleMessageObject(args);
		}else {
			retValue = false;
		}
		
		return retValue;
	}

	private void handleMessageObject(JSONArray args) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jObject = args.getJSONObject(0);
		String message = jObject.getString("msg");
		Log.d("handleMessageObject", message);
	}

	// internal implementation 
	private void handleMessage(JSONArray args) throws JSONException {
		JSONObject jObject = args.getJSONObject(0);
		
		String message = jObject.getString("msg");
		if("close".equals(message)){
			this.cordova.getActivity().finish();
			this.callbackContext.success();
		} else if ("shellVersion".equals(message)){
			this.shellVersion();
		} else if ("deviceExt".equals(message)){
			this.deviceExt(args);
		}
	}

	public static String filterString(String source) {
		StringBuilder buf = null;
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if ((codePoint >= 'a') && (codePoint <= 'z')) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} else if ((codePoint >= 'A') && (codePoint <= 'Z')) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} else if ((codePoint >= '0') && (codePoint <= '9')) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} else {
			}
		}
		if (buf == null) {
			return source;
		} else {
			if (buf.length() == len) {
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}
	private void deviceExt(JSONArray args) {
		// TODO Auto-generated method stub
        JSONObject r = new JSONObject();
        try {
        		String uuid =  APIUtil.getDeviceUUId(this.cordova.getActivity());
        		r.put("uuid", uuid);
        		r.put("platform", "Android");
        		r.put("version", android.os.Build.VERSION.RELEASE);
			r.put("mac", this.getMacAddress(this.cordova.getActivity()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        callbackContext.success(r);
	}
	// internal implementation 
	private void shellVersion() {
		JSONObject version = new JSONObject();
		try {
			version.put("platform", "Android");
			version.put("version",  APIUtil.getAppVersionName(this.cordova.getActivity()));
			version.put("buildNo", APIUtil.getAppVersionCode(this.cordova.getActivity()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.callbackContext.success(version);
	}
	public static String getMacAddress(Context context) {
	    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	    String macAddress = wifiInfo == null ? null : wifiInfo.getMacAddress();

	    if (macAddress == null) {
	        macAddress = "03:00:00:00:00:00";
	    }
	    return macAddress;
	}
	public static String getStationInfo(Activity activity)
	{
		String uuid = APIUtil.getDeviceUUId(activity);
		String version = APIUtil.getAppVersionName(activity);
        String op_station = "[ip][" + getMacAddress(activity) + 
        		"][xcm-" + version + "][" + "Android" + "-" +
        		android.os.Build.VERSION.RELEASE + 
        		"][mobile_tel]["+ uuid +"]";
        return op_station;
	}
}