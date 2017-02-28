package com.cfjn.javacf.shumi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.shumi.sdk.ShumiSdkConstant;
import com.shumi.sdk.ShumiSdkFundTradingFunction;
import com.shumi.sdk.business.ShumiSdkFundTradingHelper;
import com.shumi.sdk.data.param.trade.general.ShumiSdkCancelOrderParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkCardValidationParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkModifyDividendParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkPurchaseFundParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkRedeemParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkUnbindBankCardParam;

/**
 * 交易帮助类
 * MyShumiSdkTradingActivity中包含ShumiSdkFundTradingFragment
 * @author John
 *
 */
public class MyShumiSdkTradingHelper {
	/**
	 * 基金申购、认购
	 * @param context 当前的Activity
	 * @param param 认申购参数
	 */
	public static void doPurchase(Context context, ShumiSdkPurchaseFundParam param) {
		Bundle b = new Bundle();
		try {
			// 初始化交易的bundle对象
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.Purchase, param);
			// 将包含交易数据的bundle传入Intent中
			// 
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			// 启动startActivity
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 普通赎回
	 * @param context 当前的Activity
	 * @param param 认申购参数
	 */
	public static void doNormalRedeem(Context context, ShumiSdkRedeemParam param) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b, ShumiSdkFundTradingFunction.NormalRedeem, param);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * T+0赎回
	 * @param
	 * @param param 认申购参数
	 */
	public static void doQuickRedeem(Context context, ShumiSdkRedeemParam param) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.QuickRedeem, param);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();

		}
	}

	/**
	 * 授权绑定
	 */
	public static void doAuthentication(Context context) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.Authentication, null);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 分红修改
	 */
	public static void doModifyDividend(Context context, ShumiSdkModifyDividendParam param) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.ModifyDividend, param);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 交易撤单
	 */
	public static void doCancelOrder(Context context, ShumiSdkCancelOrderParam param) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.CancelOrder, param);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改密码
	 */
	public static void doChangeTradePassword(Context context) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b, ShumiSdkFundTradingFunction.ChangeTradePassword, null);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 忘记交易密码
	 */
	public static void doForgetTradePassword(Context context) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.ForgetTradePassword, null);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 解绑银行卡
	 */
	public static void doUnbindBankCard(Context context, ShumiSdkUnbindBankCardParam param) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.UnbindBankCard, param);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证银行卡,根据不同渠道来调用不用的解绑方式
	 * @param param 解绑参数
	 * @param bindWay 绑定方式 0:网页 1:汇付 2:易宝
	 * @param capitalMode 资金方式,预留字段
	 */
	public static void doVerifyBankCard(Context context, ShumiSdkCardValidationParam param, int bindWay, String capitalMode) {
		Bundle b = new Bundle();
		try {
			// 汇付天下渠道绑卡
			if (bindWay == ShumiSdkConstant.BIND_WAY_CHINAPNR) {
				ShumiSdkFundTradingHelper.setupTradingBundle(b,
						ShumiSdkFundTradingFunction.VerifyBankCardChinaPnr,
						param);
				// 易宝渠道绑卡
			} else if (bindWay == ShumiSdkConstant.BIND_WAY_YEEPAY) {
				ShumiSdkFundTradingHelper.setupTradingBundle(b,
						ShumiSdkFundTradingFunction.VerifyBankCardYeepay,
						param);
			}			
			Intent intent = new Intent(context,
					MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}

	/**
	 * 管理银行卡
	 */
	public static void doManagementBankCards(Context context) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.BankCardManagement, null);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加银行卡
	 */
	public static void doAddBankCard(Context context) {
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.AddBankCard, null);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}

	/**
	 * 修改手机号码
	 */
	public static void doChangeMobile(Context context){
		Bundle b = new Bundle();
		try {
			ShumiSdkFundTradingHelper.setupTradingBundle(b,
					ShumiSdkFundTradingFunction.ChangeMobile, null);
			Intent intent = new Intent(context, MyShumiSdkTradingActivity.class);
			intent.putExtras(b);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO 做好异常处理
			e.printStackTrace();
		}
	}

}
