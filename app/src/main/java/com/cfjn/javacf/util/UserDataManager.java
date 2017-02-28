package com.cfjn.javacf.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 作者： zll
 * 时间： 2016-6-29
 * 名称： 用户数据管理
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public final class UserDataManager {
	private static UserDataManager mInstance;
	private static SharedPreferences preferences;
	private static final String TAG = "DataManager";

	public static UserDataManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new UserDataManager();
		}
		preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		return mInstance;
	}

	/**
	 * 保存登陆时返回的用户信息到本地
	 */
	public void saveUserInfo(String userId, String userPwd, String userToken, String tokenKey, String tokenSecret, String userCardId) {
		Editor edit = preferences.edit();
		edit.putString("userId", userId);
		if(userPwd != null){
			edit.putString("userPwd", userPwd);
		}
		edit.putString("userToken", userToken);
		edit.putString("tokenKey", tokenKey);
		edit.putString("tokenSecret", tokenSecret);
		edit.putString("userCardId", userCardId);

		edit.commit();
	}
	
	/**
	 * 保存登陆时返回的用户信息到本地
	 */
//

	/**	public void saveUserInfo(String userId, String userToken, String tokenKey, String tokenSecret, String userCardId) {
	 ////		Editor edit = ferences.edit();
	 ////		edit.putString("userId", userId);
	 ////		edit.putString("userToken", userToken);
	 ////		edit.putString("tokenKey", tokenKey);
	 ////		edit.putString("tokenSecret", tokenSecret);
	 ////		edit.putString("userCardId", userCardId);
	 ////		edit.commit();
	 //		saveUserInfo(userId, null, userToken, tokenKey, tokenSecret, userCardId);
	 //	}
	 * 保存身份验证时返回的用户信息到本地
	 */

	public void saveUserInfo(String tokenKey, String tokenSecret, String userRealName, String userCardId,String mobile) {
		Editor edit = preferences.edit();
		edit.putString("tokenKey", tokenKey);
		edit.putString("tokenSecret", tokenSecret);
		edit.putString("userRealName", userRealName);
		edit.putString("userCardId", userCardId);
		edit.putString("mobile",mobile);
		edit.commit();
	}
	/**
	 * 初始化用户数据
	 */
	public void initUserData() {
		G.user.set(getUserId(), getUserPwd(), getUserToken(), getTokenKey(), getTokenSecret(), getUserCardId());
	}

	public String getUserId() {
		return preferences.getString("userId", "");
	}

	public String getUserPwd() {
		return preferences.getString("userPwd", "");
	}

	public String getUserToken() {
		return preferences.getString("userToken", "");
	}

	public String getTokenKey() {
		return preferences.getString("tokenKey", "");
	}

	public String getTokenSecret() {
		return preferences.getString("tokenSecret", "");
	}

	public String getUserRealName() {
		return preferences.getString("userRealName", "");
	}

	public String getUserCardId() {
		return preferences.getString("userCardId", "");
	}

	/**
	 * 删掉存储在本地的用户信息
	 */
	public void removeUserInfo() {
		Editor edit = preferences.edit();
		edit.clear();
		edit.commit();
		G.user.reset();
	}
}