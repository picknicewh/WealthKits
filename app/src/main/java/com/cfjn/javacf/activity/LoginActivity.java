package com.cfjn.javacf.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cfjn.javacf.JPush.JPushUtil;
import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.base.UserSyntion;
import com.cfjn.javacf.modle.UserManger;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.FormValidation;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者：zll
 *  时间：2016-6-2
 *  名称：登录注册
 *  版本说明：代码规范整改
 *  附加注释：登录注册
 *  主要接口：1.登录
 *            2.获取验证码
 *
 *
 *  修改部分：加入极光推送的发送RegistrationID与AppUid对应数据的接口到服务器
 *
 */
public class LoginActivity extends Activity implements View.OnClickListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 手机号码
     */
    @Bind(R.id.et_phone_number)
    EditText etPhoneNumber;
    /**
     * 验证码
     */
    @Bind(R.id.et_message_code)
    EditText etMessageCode;
    /**
     * 获取验证码
     */
    @Bind(R.id.b_message_code)
    Button bMessageCode;
    /**
     * 确定
     */
    @Bind(R.id.b_next)
    Button bNext;
    /**
     * 是否同意条款
     */
    @Bind(R.id.cb_user_agreement)
    CheckBox cbUserAgreement;
    /**
     * 用户协议
     */
    private AlertDialog myDialog;

    //-------------逻辑变量-------------------
    /**
     * 计时器
     */
    private MyCount myCount;
    /**
     * 锁屏传过来的值
     */
    private int lockType;
    /**
     * 是否登录成功
     * 登录后同步数据
     */
    public static boolean isOK;
    private UserInfo userInfo;
    private String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();

    }

    private void initializeMode(){
        userInfo = UserInfo.getInstance(this);
        myCount = new MyCount(60000, 1000);
        lockType = getIntent().getIntExtra("value", 0);//用户锁屏页面错误过多跳转登录页面，或者强制登录跳转
    }
    private void initializeView() {
        etPhoneNumber.clearFocus();
        etPhoneNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    bMessageCode.setTextColor(Color.WHITE);
                    bMessageCode.setEnabled(true);
                    bMessageCode.setBackgroundResource(R.drawable.s_rounded_rectangle_red);
                } else {
                    bMessageCode.setTextColor(Color.parseColor("#ffffff"));
                    bMessageCode.setEnabled(false);
                    bMessageCode.setBackgroundResource(R.drawable.rounded_rectangle_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        etMessageCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    bNext.setTextColor(Color.WHITE);
                    bNext.setEnabled(true);
                    bNext.setBackgroundResource(R.drawable.s_rounded_rectangle_red);
                } else {
                    bNext.setEnabled(false);
                    bNext.setBackgroundResource(R.drawable.personal_out_login);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.ib_back,R.id.tv_user_agreement,R.id.b_message_code, R.id.b_next})
    public void onClick(View v) {
        loginName = etPhoneNumber.getText().toString();
        switch (v.getId()) {
            case R.id.ib_back:
                //返回
                if (lockType == -1) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.tv_user_agreement:
                //用户登录协议
                myDialog = new AlertDialog.Builder(LoginActivity.this).create();
                myDialog.show();
                myDialog.getWindow().setContentView(R.layout.activity_customer);
                myDialog.getWindow().findViewById(R.id.b_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                break;
            case R.id.b_next:
                //登录
                String messageCode=etMessageCode.getText().toString();
                if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(messageCode)) {
                    G.showToast(this, "手机号码或者验证码不能为空");
                    return;
                }
                if (!cbUserAgreement.isChecked()) {
                    G.showToast(this, "请您选中用户协议");
                    return;
                }
                getGoLogin(messageCode);
                break;
            case R.id.b_message_code:
                //获取验证码
                if (TextUtils.isEmpty(loginName)) {
                    G.showToast(this, "手机号码不能为空");
                    return;
                }
                if (!FormValidation.isMobileNO(loginName)) {
                    G.showToast(this, "请输入正确的手机号码");
                    return;
                }
                if (!cbUserAgreement.isChecked()) {
                    G.showToast(this, "请您选中用户协议");
                    return;
                }
                etMessageCode.setFocusable(true);
                etMessageCode.requestFocus();
                myCount.start();
                getMessageCode();
                break;
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                String values = (String) msg.obj;
                G.showToast(LoginActivity.this, values);
            } else if (msg.what == 0x12) {
                G.showToast(LoginActivity.this, "由于网络原因，验证码获取失败，请重试");
            } else if (msg.what == 0x13) {
                G.showToast(LoginActivity.this, "由于网络原因，登录失败，请重试");
            }
        }
    };

    /**
     * 前往登录
     */
    private void getGoLogin(String messageCode){
        Map<String, String> map = new HashMap<>();
        map.put("loginName", etPhoneNumber.getText().toString());
        map.put("verification", messageCode);
        map.put("appId", "1");
        OkHttpUtil.sendPost(ApiUri.LOGINCHECK, map, this);
    }

    /**
     * 获取验证码
     */
    private void getMessageCode(){
        Map<String, String> map = new HashMap<>();
        map.put("loginName", loginName);
        OkHttpUtil.sendPost(ApiUri.MOBILECODE, map, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.MOBILECODE)) {
                    UserManger userManger = gson.fromJson(result, UserManger.class);
                    if (null == userManger) {
                        return;
                    } else if (!userManger.getResultCode().equals("0")) {
                        myCount.onFinish();
                        myCount.cancel();
                    }
                    Message msg = handler.obtainMessage();
                    msg.obj = userManger.getResultDesc();
                    msg.what = 0x11;
                    handler.sendMessage(msg);
                } else if (uri.equals(ApiUri.LOGINCHECK)) {
                    UserManger userManger = gson.fromJson(result, UserManger.class);
                    {
                        Message msg = handler.obtainMessage();
                        msg.obj = userManger.getResultDesc();
                        msg.what = 0x11;
                        handler.sendMessage(msg);
                        if (!TextUtils.isEmpty(userManger.getResultCode()) && userManger.getResultCode().equals("0")) {

                            Log.i("TAGH",JPushUtil.getRegid(getApplicationContext()));

                            userInfo.clean();
                            userInfo.setLoginName(loginName);
                            userInfo.setToken(userManger.getDetails());
                            userInfo.setSecretKey(userManger.getSign());
                            new UserSyntion(LoginActivity.this, false, true);
                            if (lockType != 0) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                finish();
                            }
                            isOK = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onError(final String uri, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.MOBILECODE)) {
                    myCount.onFinish();
                    myCount.cancel();
                    handler.sendEmptyMessage(0x12);
                } else if (uri.equals(ApiUri.LOGINCHECK)) {
                    handler.sendEmptyMessage(0x13);
                }
            }
        });
    }


    /* 定义一个倒计时的内部类 */
    private final class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            getMessageCodeEnable();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            etPhoneNumber.setEnabled(false);
            bMessageCode.setBackgroundResource(R.drawable.rounded_rectangle_gray);
            bMessageCode.setTextColor(Color.BLACK);
            bMessageCode.setEnabled(false);
            bMessageCode.setText("重新获取(" + millisUntilFinished / 1000 + ")秒");
        }
    }

    private void getMessageCodeEnable() {
        etPhoneNumber.setEnabled(true);
        bMessageCode.setBackgroundResource(R.drawable.s_rounded_rectangle_red);
        bMessageCode.setTextColor(Color.WHITE);
        bMessageCode.setEnabled(true);
        bMessageCode.setText("获取验证码");
    }

}
