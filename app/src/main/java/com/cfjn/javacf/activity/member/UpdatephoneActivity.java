package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
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
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 修改手机号码
 *  版本说明：代码规范整改
 *  附加注释：修改用户手机号码
 *  主要接口：1.获取验证码
 *            2.提交用户新手机号
 */
public class UpdatephoneActivity extends Activity implements OKHttpListener {
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
     * 完成
     */
    @Bind(R.id.b_confirm)
    Button bConfirm;

    private UserInfo userInfo;
    private String phoneNumber;
    private MyCount myCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_num);
        ButterKnife.bind(this);
        userInfo = new UserInfo(this);
        etPhoneNumber.requestFocus();
        myCount = new MyCount(60000, 1000);

        bMessageCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = etPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    G.showToast(UpdatephoneActivity.this, "手机号码不能为空");
                    return;
                }
                if (!FormValidation.isMobileNO(phoneNumber)) {
                    G.showToast(UpdatephoneActivity.this, "请输入正确的手机号码");
                    return;
                }
                Map<String,String>map = new HashMap<>();
                map.put("loginName", phoneNumber);
                map.put("token", userInfo.getToken());
                map.put("secretKey", userInfo.getSecretKey());
                OkHttpUtil.sendPost(ApiUri.UPDATEPHONE, map, UpdatephoneActivity.this);

            }
        });
    }

    /**
     * 修改手机号码
     * @param loginName 手机号
     * @param sign  密钥
     * @param createTime 修改时间
     */
    private void getUpdatePhone(final String loginName, String sign, String createTime) {
        String verification = etMessageCode.getText().toString().trim();
        if(G.isEmteny(verification)||verification.length()<6){
            G.showToast(this,"验证码输入不符合规范");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("token", userInfo.getToken());
        map.put("loginName", loginName);
        map.put("secretKey", userInfo.getSecretKey());
        map.put("sign", sign);
        map.put("createTime", createTime);
        map.put("verification", verification);
        OkHttpUtil.sendPost(ApiUri.SETUPDATEPHONE, map, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.SETUPDATEPHONE)) {
                    UserManger userManger = gson.fromJson(result, UserManger.class);
                    if (userManger == null) {
                        return;
                    }
                    if (!G.isEmteny(userManger.getDetails()) && !G.isEmteny(userManger.getSign())) {
                        userInfo.setLoginName(phoneNumber);
                        userInfo.setToken(userManger.getDetails());
                        userInfo.setSecretKey(userManger.getSign());
                        G.showToast(UpdatephoneActivity.this, "手机号码修改成功");
                    }
                    finish();
                } else if (uri.equals(ApiUri.UPDATEPHONE)) {
                    final UserManger userManger = gson.fromJson(result, UserManger.class);
                    myCount.start();
                    G.showToast(UpdatephoneActivity.this, userManger.getResultDesc());
                    if (!TextUtils.isEmpty(userManger.getResultCode()) && userManger.getResultCode().equals("0")) {
                        etMessageCode.setFocusable(true);
                        etMessageCode.requestFocus();
                        bConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getUpdatePhone(phoneNumber, userManger.getSign(), userManger.getCreateTime());
                            }
                        });
                    } else {
                        myCount.cancel();
                    }
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.SETUPDATEPHONE)) {
                    G.showToast(UpdatephoneActivity.this, "由于网络问题信息提交失败，请稍后重试！");
                } else if (uri.equals(ApiUri.UPDATEPHONE)) {
                    myCount.cancel();
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }

    /* 定义一个倒计时的内部类 */
    private final class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            setMessageCodeEnable();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bMessageCode.setBackgroundResource(R.drawable.rounded_rectangle_gray);
            bMessageCode.setTextColor(Color.BLACK);
            bMessageCode.setEnabled(false);
            bMessageCode.setText("重新获取(" + millisUntilFinished / 1000 + ")秒");
        }
    }

    private void setMessageCodeEnable() {
        bMessageCode.setBackgroundResource(R.drawable.s_rounded_rectangle_red);
        bMessageCode.setTextColor(Color.WHITE);
        bMessageCode.setEnabled(true);
        bMessageCode.setText("获取验证码");
    }
}
