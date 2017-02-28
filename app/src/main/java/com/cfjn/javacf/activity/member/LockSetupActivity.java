package com.cfjn.javacf.activity.member;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LockPatternView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class LockSetupActivity extends Activity implements LockPatternView.OnPatternListener {
	public static final String HAS_LOCK = "has_lock";

	@ViewInject(R.id.tv_tips)
	private TextView tv_tips;

	@ViewInject(R.id.lock_pattern)
	private LockPatternView lockPatternView;

	/**
	 * 手势密码本地存储对象
	 */
	private SharedPreferences preferences;

	/**
	 * 当前绘制的手势密码
	 */
	private List<LockPatternView.Cell> choosePattern = null;

	/**
	 * 是否是重置密码
	 */
	private boolean isReset;

	/**
	 * 抖动动画
	 */
	private TranslateAnimation translateAnimation;

	private int delay = 300;
	
	private MyHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_setup);
		ViewUtils.inject(this);
		
		Intent intent = getIntent();
		isReset = intent.getBooleanExtra("isReset", false);

		preferences = getSharedPreferences(LockActivity.LOCK, Context.MODE_PRIVATE);

		lockPatternView.setOnPatternListener(this);
		lockPatternView.clearPattern();
		lockPatternView.enableInput();

		translateAnimation = new TranslateAnimation(-10, 10, 0, 0);
		translateAnimation.setDuration(50);
		translateAnimation.setRepeatCount(Animation.INFINITE);
		translateAnimation.setRepeatMode(Animation.REVERSE);

		handler = new MyHandler();
		
		if(isReset){
			sendTips("验证成功，绘制新的手势密码！", R.color.white);
		}
	}

	@OnClick({R.id.tv_cancel, R.id.tv_reset})
	public void cancel(View v) {
		if(v == null){
			return;
		}
		switch(v.getId()){
		case R.id.tv_cancel:
			if (!isReset) {
				// 取消
				Editor editor = preferences.edit();
				editor.putBoolean(HAS_LOCK + G.user.USER_ID, false);
				editor.commit();
			}
			finish();
			break;
		case R.id.tv_reset:
			choosePattern = null;
			sendTips("请重新绘制手势密码！", R.color.white);
			break;
		}
		
	}

	@Override
	public void onPatternStart() {
	}

	@Override
	public void onPatternCleared() {
	}

	@Override
	public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
	}

	@Override
	public void onPatternDetected(List<LockPatternView.Cell> pattern) {
		// 设置操作
		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
			sendTips("至少连接4个点，请重试！", R.color.red);
			return;
		}

		if (choosePattern == null) {
			choosePattern = new ArrayList<LockPatternView.Cell>(pattern);
			sendTips("请再次绘制手势密码！", R.color.white);
			return;
		}

		if (choosePattern.equals(pattern)) {
			G.showToast(this.getApplicationContext(), "手势密码设置成功！");
			Editor editor = preferences.edit();
			editor.putString(LockActivity.LOCK_KEY + G.user.USER_ID, LockPatternView.patternToString(choosePattern));
			editor.putBoolean(HAS_LOCK + G.user.USER_ID, true);
			editor.commit();
			lockPatternView.disableInput();
			finish();
		} else {
			sendTips("与首次密码的绘制不一致，请重试！", R.color.red);
		}
	}

	/**
	 * 发送抖动提示
	 */
	private void sendTips(String text, int color) {
		if (color == R.color.red) {
			lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
		}
		tv_tips.setText(text);
		tv_tips.setTextColor(getResources().getColor(color));
		tv_tips.setAnimation(translateAnimation);
		translateAnimation.start();
		handler.sendEmptyMessageDelayed(0, delay);
	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			lockPatternView.clearPattern();
			translateAnimation.cancel();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// disable back key
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
