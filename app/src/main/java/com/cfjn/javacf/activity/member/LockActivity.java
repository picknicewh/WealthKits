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
import com.cfjn.javacf.activity.LoginActivity;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.util.UserDataManager;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LockPatternView;

import java.util.List;


public class LockActivity extends Activity implements LockPatternView.OnPatternListener, View.OnClickListener {

	public static final String LOCK = "lock";
	public static final String LOCK_KEY = "lock_key";

	private TextView tv_tips;
	private LockPatternView lockPatternView;
	/**
	 * 保存的手势密码
	 */
	private List<LockPatternView.Cell> lockPattern;

	/**
	 * 剩余输入次数
	 */
	private int inputCount;

	/**
	 * 是否是重置密码
	 */
	private boolean isReset;

	/**
	 * 是否开启手势密码
	 */
	private boolean isClear;
	/**
	 * 抖动动画
	 */
	private TranslateAnimation translateAnimation;
	/**
	 * 取消
	 */
	private TextView tv_goto_bank;
	/**
	 * 忘记密码
	 */
	private TextView tv_forget_password;
	private MyHandler handler;
	private UserInfo us;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);
		lockPatternView= (LockPatternView) findViewById(R.id.lock_pattern);
		tv_tips= (TextView) findViewById(R.id.tv_tips);
		tv_goto_bank= (TextView) findViewById(R.id.tv_goto_bank);
		tv_forget_password= (TextView) findViewById(R.id.tv_forget_password);
		tv_forget_password.setOnClickListener(this);
		us=UserInfo.getInstance(this);

		UserDataManager manager = UserDataManager.getInstance(this);
		Intent intent = getIntent();
		isReset = intent.getBooleanExtra("isReset", false);
		isClear = intent.getBooleanExtra("isClear", false);

		SharedPreferences preferences = getSharedPreferences(LockActivity.LOCK, Context.MODE_PRIVATE);
		String patternString = preferences.getString(LOCK_KEY + manager.getUserId(), null);
		if (patternString != null) {
			lockPattern = LockPatternView.stringToPattern(patternString);
		}
		lockPatternView.setOnPatternListener(this);
		inputCount = 5;
		
		//左右抖动10
		translateAnimation = new TranslateAnimation(-10, 10, 0, 0);  
		translateAnimation.setDuration(50);  
		translateAnimation.setRepeatCount(Animation.INFINITE);  
		translateAnimation.setRepeatMode(Animation.REVERSE);  
		
		handler = new MyHandler();
		tv_goto_bank.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	/**
	 * 清除本地手势密码
	 */
	public void clear() {
		SharedPreferences preferences = getSharedPreferences(LockActivity.LOCK, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
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
		// 解锁操作
		if (pattern.equals(lockPattern)) {
			// 解锁成功
			Intent intent = null;
			if (isReset) {
				// 去设置新手势密码
				intent = new Intent(LockActivity.this, LockSetupActivity.class);
				intent.putExtra("isReset", isReset);
			} else if (isClear) {
				// 清除手势密码
				G.showToast(this.getApplicationContext(), "验证成功，手势密码已关闭！");
				clear();
			} else {
				// 去主页
				intent = new Intent(LockActivity.this, MainActivity.class);
			}
			if (intent != null) {
				startActivity(intent);
			}
			finish();
		} else {
			lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
			if (inputCount > 1) {
				tv_tips.setText("手势密码不正确(还有" + --inputCount + "次机会)");
				tv_tips.setTextColor(getResources().getColor(R.color.red));
				tv_tips.setAnimation(translateAnimation);  
				translateAnimation.start();
				handler.sendEmptyMessageDelayed(0, 300);
			} else {
				inputCount=-1;
				lockPatternView.disableInput();
				G.showToast(this.getApplicationContext(), "抱歉错误过多，请重新登录！");
				onClick(tv_forget_password);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.tv_forget_password){
			Intent intent = new Intent(LockActivity.this, LoginActivity.class);
			intent.putExtra("value",inputCount);
			startActivity(intent);
			if(inputCount==-1){
				clear();
				us.clean();
				finish();
			}
		}
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
