package com.cfjn.javacf.shumi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cfjn.javacf.R;
import com.shumi.sdk.fragment.ShumiSdkFundTradingFragment;

public class MyShumiSdkTradingActivity extends FragmentActivity {
	/**
	 * 交易Fragment 如果使用内部类(Inner Class)，请保证此类为public static，并且含无参构造
	 * 否则被GC回收后再切入前台会发生InstantiationException
	 * 
	 * @author John
	 * 
	 */
	public static class FundTradingFragment extends ShumiSdkFundTradingFragment {
		/**
		 * 执行退出Sdk的操作 可以finish所在Activity，或者使用FragmentManager将其replace掉
		 */
		@Override
		public void doQuitSdk() {
			getActivity().finish();
		}

		/**
		 * 自定义Loading界面 第三方可以换入自己的Logo
		 */
		@Override
		protected View getLoadingView() {
			return getActivity().getLayoutInflater().inflate(R.layout.shumi_sdk_bg, null);
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_trading);
		// 设置Fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.container, createShumiSdkFundTradingFragment()).commit();
	}

	/**
	 * 创建数米SdkTradingFragment<br>
	 * 需要注意Fragment使用方式，否则detach之后，getActivity()之后返回null
	 * 
	 * @return
	 */
	protected ShumiSdkFundTradingFragment createShumiSdkFundTradingFragment() {
		// TODO 定义一个合适的ShumiSdkFundTradingFragment，重写方法
		ShumiSdkFundTradingFragment tradingFragment = new FundTradingFragment();

		/**
		 * TODO 这里可以传入实现IShumiSdkDataBridge的接口(推荐)
		 * 或者传入实现IFund123OauthInterface的接口
		 */
		tradingFragment.setDataBridge(MyShumiSdkDataBridge.getInstance(this));
//		tradingFragment.setDataBridge(Fund123DataBridge.getInstance(this));

		// TODO 加入页面切换的动画
		Animation pageEnterAnim = AnimationUtils.loadAnimation(this, R.anim.page_left_in);
		Animation pageLeaveAnim = AnimationUtils.loadAnimation(this, R.anim.page_left_out);
		tradingFragment.setPageAnimation(pageEnterAnim, pageLeaveAnim);

		// 写入参数
		tradingFragment.setArguments(getIntent().getExtras());
		return tradingFragment;
	}

	/**
	 * 重写按下返回键时间的操作
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
		if (fragment instanceof ShumiSdkFundTradingFragment) {
			/**
			 * ShumiSdkFundTradingFragment 中实现了一个KeyEvent
			 */
			ShumiSdkFundTradingFragment fundTradingFragment = (ShumiSdkFundTradingFragment) fragment;
			// 调用ShumiSdkFundTradingFragment的onKeyDown方法，
			// 如果返回TRUE则不处理，返回FALSE继续处理
			if (fundTradingFragment.onKeyDown(keyCode, event)) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
