package com.cfjn.javacf.shumi;

import android.content.Context;
import android.util.Log;

import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.activity.assets.AssertDetailActivity;
import com.cfjn.javacf.activity.assets.TransactionRecordDetailActivity;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.UserDataManager;
import com.cfjn.javacf.base.UserSyntion;
import com.cfjn.javacf.modle.StyleCodeVo;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.util.ThreadPool;
import com.google.gson.Gson;
import com.shumi.sdk.IShumiSdkDataBridge;
import com.shumi.sdk.data.eventargs.ShumiSdkAddBankCardEventArgs;
import com.shumi.sdk.data.eventargs.ShumiSdkAuthorizedEventArgs;
import com.shumi.sdk.data.eventargs.ShumiSdkBuyFundEventArgs;
import com.shumi.sdk.data.eventargs.ShumiSdkCancelOrderEventArgs;
import com.shumi.sdk.data.eventargs.ShumiSdkChangeMobileEventArgs;
import com.shumi.sdk.data.eventargs.ShumiSdkCreateOrderEventArgs;
import com.shumi.sdk.data.eventargs.ShumiSdkEventArgs;
import com.shumi.sdk.data.eventargs.ShumiSdkRedeemFundEventArgs;
import com.shumi.sdk.data.param.ShumiSdkConsumerUserInfo;

import java.util.HashMap;
import java.util.Map;


public class MyShumiSdkDataBridge implements IShumiSdkDataBridge, OKHttpListener {
	private static UserInfo us;
//	private static Handler mHandler;
	private String LogTag = "MyShumiSdkDataBridge";
	private Context context;
	public static boolean chedan=false;
	public static MyShumiSdkDataBridge instance;

	public static MyShumiSdkDataBridge getInstance(Context context) {
		if (instance == null) {
			instance = new MyShumiSdkDataBridge();
//			mHandler= new Handler();
		}
		instance.context = context;
		us=new UserInfo(context);
		return instance;
	}

//	public void showToast(final String message) {
//		mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}

	public void init(Context context) {
		this.context = context;
	}

	@Override
	public String getConsumerKey() {
		return "SM_SDK_CFJN";
	}

	@Override
	public String getConsumerSecret() {
		return "728B3BFE476141F8902C1816A40C9978";
	}

	@Override
	public String getAccessToken() {
		return G.user.TOKEN_KEY;
	}

	@Override
	public String getAccessTokenSecret() {
		return G.user.TOKEN_SECRET;
	}

	@Override
	public ShumiSdkConsumerUserInfo getConsumerUserInfo() {
		ShumiSdkConsumerUserInfo user = new ShumiSdkConsumerUserInfo();
		user.setRealName(G.user.REAL_NAME);
		user.setIdNumber(G.user.USER_CARD_ID);
		user.setEmailAddr(G.user.EMAIL);
		String phone = G.user.MOBILE;
		if (phone.equals("")) {
			phone = G.user.USER_ID;
		}
		user.setPhoneNum(phone);
		return user;
	}

	@Override
	public void onSdkEvtAuthorized(ShumiSdkAuthorizedEventArgs eventArgs) {
		G.user.TOKEN_KEY = eventArgs.getAccessToken();
		G.user.TOKEN_SECRET = eventArgs.getAccessTokenSecret();
		G.user.MOBILE = eventArgs.getMobile();
		G.user.REAL_NAME = eventArgs.getRealName();
		G.user.EMAIL = eventArgs.getEmailAddr();
		G.user.BANK_CARD = eventArgs.getBankCard();
		G.user.USER_CARD_ID = eventArgs.getIdNumber();
		saveUserBaseInfo();
		us.setSmToken(G.user.TOKEN_KEY);
		us.setSmTokenSecret(G.user.TOKEN_SECRET);
		us.initUserData();
		UserDataManager manager = UserDataManager.getInstance(context);
		manager.saveUserInfo(G.user.TOKEN_KEY, G.user.TOKEN_SECRET, G.user.REAL_NAME, G.user.USER_CARD_ID,G.user.MOBILE);
	}

	@Override
	public void onSdkEvtBuySuccessed(ShumiSdkBuyFundEventArgs eventArgs) {
		Log.d(LogTag, "onSdkEvtBuySuccessed =>" + eventArgs.toString());
		G.showToast(context,"申购成功");
		AssertDetailActivity.buy=true;
//		G.showToast(context,"申购");
		//用户每次申购需要需要向服务端传一次信息
		new UserSyntion(context,false,false);
	}

	@Override
	public void onSdkEvtRedeemSuccessed(ShumiSdkRedeemFundEventArgs eventArgs) {
		Log.d(LogTag, "onSdkEvtRedeemSuccessed =>" + eventArgs.toString());
		G.showToast(context,"赎回成功");
		AssertDetailActivity.buy=true;
//		G.showToast(context, "赎回");
		//用户每次申购需要需要向服务端传一次信息
		new UserSyntion(context,false,false);
	}

	@Override
	public void onSdkEvtCancelOrderSuccessed(ShumiSdkCancelOrderEventArgs eventArgs) {
		Log.d(LogTag, "onSdkEvtCancelOrderSuccessed =>" + eventArgs.toString());
		G.showToast(context,"撤单成功");
		chedan=true;
		new TransactionRecordDetailActivity().finish();
		new UserSyntion(context,false,false);
	}

	@Override
	public void onSdkEvtAddBankCardSuccessed(ShumiSdkAddBankCardEventArgs eventArgs) {
		Log.d(LogTag, "onSdkEvtAddBankCardSuccessed =>" + eventArgs.toString());
		G.showToast(context,"添加银行卡成功");
		new ShumiBankCard(context);
	}


	@Override
	public void onSdkEvtNotHandled(ShumiSdkEventArgs eventArgs) {
		Log.d(LogTag, "onSdkEvtNotHandled =>" + eventArgs.toString());
		G.showToast(context,"未处理事件");
	}
	/**
	 * 保存用户信息到服务器
	 */
	public void saveUserBaseInfo() {
		Map<String, String> requestParamsMap = new HashMap<String, String>();
		requestParamsMap.put("token", us.getToken());
		requestParamsMap.put("secretKey",us.getSecretKey());
		requestParamsMap.put("realName", G.user.REAL_NAME);
		requestParamsMap.put("idNumber", G.user.USER_CARD_ID);
		requestParamsMap.put("smToken",G.user.TOKEN_KEY);
		requestParamsMap.put("smTokenSecret", G.user.TOKEN_SECRET);
		requestParamsMap.put("mobile", G.user.MOBILE);
		requestParamsMap.put("source", "0");
		G.log(G.user.REAL_NAME + "---rrr----" + G.user.USER_CARD_ID + "----rr----" + G.user.MOBILE + "------rrr--" + G.user.TOKEN_KEY + "-------rrr---" + G.user.TOKEN_SECRET);
		OkHttpUtil.sendPost(ApiUri.SAVEUSERINFO, requestParamsMap, this);
	}

	@Override
	public void onSdkEvtChangeMobileSuccessed(ShumiSdkChangeMobileEventArgs arg0) {
		G.showToast(context,"修改手机号码成功");
		Map<String, String> requestParamsMap = new HashMap<String, String>();
		requestParamsMap.put("token", us.getToken());
		requestParamsMap.put("secretKey", us.getSecretKey());
		requestParamsMap.put("mobile",arg0.getMobile());
		requestParamsMap.put("source", "0");
		OkHttpUtil.sendPost(ApiUri.UPDATEFUNDMODEILE, requestParamsMap, this);
	}

//	private  Handler handler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (msg.what==0x01){
//				Bundle bundle =  msg.getChartManger();
//				String resultCode =bundle.getString("resultCode");
//				String  object = bundle.getString("object");
//				String sign = bundle.getString("sign");
//				String resultDesc = bundle.getString("resultDesc");
//				if (resultCode.equals("0")) {
//					us.setSmToken(object);
//					us.setSecretKey(sign);
//					new UserSyntion(context, false, true);
//					showToast("已经绑定用户：" + G.user.REAL_NAME);
//				} else if (resultCode.equals("9")) {
//					G.showToast(context, resultDesc);
//				}
//			}else if (msg.what==0x11){
//				Toast.makeText(context,"提交失败",Toast.LENGTH_SHORT).show();
//			}
//		}
//	};
	@Override
	public void onSdkEvtCreateOrderSuccessed(ShumiSdkCreateOrderEventArgs arg0) {
		G.showToast(context,"订单号：" + arg0.getApplySerial());
	}

	@Override
	public void onSuccess(String uri, final String result) {
		if (uri.equals(ApiUri.SAVEUSERINFO)){
			ThreadPool.fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					Gson gson = new Gson();
					StyleCodeVo styleCodeVo = gson.fromJson(result,StyleCodeVo.class);
					String resultCode =styleCodeVo.getResultCode();
					String  object = styleCodeVo.getObject();
					String sign = styleCodeVo.getSign();
					String resultDesc = styleCodeVo.getResultDesc();
					if (resultCode.equals("0")) {
						us.setSmToken(object);
						us.setSecretKey(sign);
						new UserSyntion(context, false, true);
						G.showToast(context,"已经绑定用户：" + G.user.REAL_NAME);
					} else if (resultCode.equals("9")) {
						G.showToast(context, resultDesc);
					}
				}
			});
		}
	}

	@Override
	public void onError(String uri, String error) {
		if (uri.equals(ApiUri.SAVEUSERINFO)){
			G.log("数米保存用户信息失败");
		}else if(uri.equals(ApiUri.UPDATEFUNDMODEILE)){
			G.log("向服务端提交手机号码失败");
		}
	}
}
