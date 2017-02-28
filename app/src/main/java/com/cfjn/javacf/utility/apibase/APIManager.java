package com.cfjn.javacf.utility.apibase;

import android.content.Context;
import android.util.Log;

import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.openhunme.cordova.utility.HttpUtil;
import com.openhunme.cordova.utility.JsonCatcher;
import com.openhunme.sdk.update.ModelUpdateManager;

import java.util.HashMap;
import java.util.Map;

//应用启动后，首先调用checkAppVersion，然后调用checkInitOk，checkModelVersion，checkVerifyOk，checkVerifyFileOk
public class APIManager {
	private static APIManager INSTANCE;
	private static ModelUpdateManager updateManager = ModelUpdateManager.getInstance();

	public static APIManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new APIManager();
		return INSTANCE;
	}

	//checkAppVersion(this, "http://10.57.7.82:8102/wx-service/validation", cb)
	//http://192.168.57.57/wx-service/validation/soft_version/v1.0/conduit_no/123source_id/123/source/android
	//http://192.168.57.57/wx/validation/soft_version/v1.0/conduit_no/xcmkh/source_id/123/source/android
	public void checkAppVersion(Context context, String baseUrl, OKHttpListener okHttpListener) {
		//baseUrl = "https://slave.openhunme.com/wealthKits/auxiliary/resourcePackDownload/";
		String version = APIUtil.getAppVersionName(context);
		String conduitNo = APIUtil.getChannelId(context, "tfchannel");
		String sourceId = APIUtil.getDeviceUUId(context);
		Map<String,String> params  = new HashMap<>();
		params.put("soft_version","v"+version);
		params.put("conduit_no",conduitNo);
		params.put("source_id",sourceId);
		params.put("source","android");
		params.put("dataFormat", "1");
		OkHttpUtil.sendPost(baseUrl, params, okHttpListener);
	//	String url = baseUrl + "?soft_version=v" + version + "&conduit_no=" + conduitNo + "&source_id=" + sourceId + "&source=android&dataFormat=1";
		//Log.i("checkAppVersion", url);
		//HttpUtil.sendGet(url, null, cb);
	}


	//checkModelVersion(this, "http://10.57.7.82:8102/wx-service/validation", "kh", cb)
	/**
	 *检查版本，并下载相应的资源包
	 */
	public void checkModelVersion(Context context, String baseUrl, String model,OKHttpListener okHttpListener) {
		String version = APIUtil.getAppVersionName(context);
		String sourceId = APIUtil.getDeviceUUId(context);
        updateManager.initBaseDir(context, "/tap/");
        String bundleVersion = updateManager.getModelCurrentVersion(model);
		Map<String,String> params  = new HashMap<>();
		params.put("soft_version","v"+version);
		params.put("bundle_model",model);
		params.put("bundle_version",bundleVersion);
		params.put("source_id", sourceId);
		params.put("source","android");
		params.put("dataFormat", "1");
		OkHttpUtil.sendPost(baseUrl, params, okHttpListener);
		//String url = baseUrl + "?soft_version=v" + version + "&bundle_model=" + model + "&bundle_version=" + bundleVersion + "&source_id=" + sourceId + "&source=android&dataFormat=1";
       // Log.i("checkModelVersion", baseUrl);
		Log.i("TAG","version:"+version+"model:"+model+"bundleVersion:"+bundleVersion+"sourceId:"+sourceId);
	//	HttpUtil.sendGet(url, null, cb);
	}

	//checkInitOk(this)
	public int checkInitOk(Context context) {		
        updateManager.initBaseDir(context, "/tap/");
        int ret = updateManager.doCheckInitOk(context);
        switch (ret) {
        case ModelUpdateManager.INIT_OK:
        	Log.i("checkInitOk", "OK");
        	break;
        case ModelUpdateManager.INIT_ALREADY:
        	Log.i("checkInitOk", "ALREADY");
        	break;
        case ModelUpdateManager.INIT_FAIL:
        	Log.i("checkInitOk", "FAIL");
        	break;
        default:
        	break;
        }
        return ret;
	}

	//checkVerifyOk(this)
	public int checkVerifyOk(Context context, String tapMd5) {
        updateManager.initBaseDir(context, "/tap/");
        int ret = updateManager.doVerify(context, "tap", tapMd5);
        switch (ret) {
        case ModelUpdateManager.VERIFY_OK:
        	Log.i("checkVerifyOk", "OK");
        	break;
        case ModelUpdateManager.VERIFY_MANIFEST_FAIL:
        	Log.i("checkVerifyOk", "MANIFEST FAIL");
        	break;
        case ModelUpdateManager.VERIFY_FILE_FAIL:
        	Log.i("checkVerifyOk", "FILE FAIL");
        	break;
        case ModelUpdateManager.VERIFY_FAIL:
        	Log.i("checkVerifyOk", "FAIL");
        	break;
        default:
        	break;
        }
        return ret;
	}

	//checkVerifyFileOk(this)
	public int checkVerifyFileOk(Context context, String path) {
		updateManager.initBaseDir(context, "/tap/");
		int ret = updateManager.doVerifyFile(context, "tap", path);
		switch (ret) {
			case ModelUpdateManager.VERIFY_OK:
				Log.i("checkVerifyFileOk", "OK");
				break;
			case ModelUpdateManager.VERIFY_MANIFEST_FAIL:
				Log.i("checkVerifyFileOk", "MANIFEST FAIL");
				break;
			case ModelUpdateManager.VERIFY_FILE_FAIL:
				Log.i("checkVerifyFileOk", "FILE FAIL");
				break;
			case ModelUpdateManager.VERIFY_FAIL:
				Log.i("checkVerifyFileOk", "FAIL");
				break;
			default:
				break;
		}
		return ret;
	}
}
