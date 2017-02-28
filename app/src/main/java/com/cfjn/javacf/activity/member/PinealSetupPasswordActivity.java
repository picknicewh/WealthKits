package com.cfjn.javacf.activity.member;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.StyleCodeVo;
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
 * 松果修改手机号码
 * Created by Administrator on 2016/1/19.
 */
public class PinealSetupPasswordActivity extends Activity implements View.OnClickListener, OKHttpListener {
    /**
     * 返回
     */
    @Bind(R.id.ib_back)
    ImageButton ibBack;
    /**
     * 拨打电话
     */
    @Bind(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    /**
     * 提示
     */
    @Bind(R.id.tv_prompt)
    TextView tvPrompt;
    /**
     * 手机号
     */
    @Bind(R.id.et_password)
    EditText etPassword;
    /**
     * 提示
     */
    @Bind(R.id.ib_details)
    ImageButton ibDetails;
    /**
     * 验证码
     */
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    /**
     * 获取验证码
     */
    @Bind(R.id.b_getCode)
    Button bGetCode;
    /**
     * 验证码界面
     */
    @Bind(R.id.ll_input_date)
    LinearLayout llInputDate;
    /**
     * 修改密码
     */
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    /**
     * 确认密码
     */
    @Bind(R.id.et_new_password_again)
    EditText etNewPasswordAgain;
    /**
     * 修改交易密码的界面
     */
    @Bind(R.id.ll_updateWord)
    LinearLayout llUpdateWord;
    /**
     * 确认
     */
    @Bind(R.id.b_next)
    Button bNext;
    /**
     * 弹窗提示
     */
    Dialog myDialog;
    private String loginName;
    private String IDcard;
    private String ImagerCord;
    private MyCount mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_change_password);
        ButterKnife.bind(this);
        IDcard = getIntent().getStringExtra("IdCard");
        bNext.setClickable(false);
    }

    /**
     * 弹窗提示
     */
    public void MyDialog(int type) {
        LinearLayout   dialogView = (LinearLayout) LayoutInflater.from(PinealSetupPasswordActivity.this).inflate(R.layout.sg_password_title, null);
        myDialog = new AlertDialog.Builder(PinealSetupPasswordActivity.this).create();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        myDialog.getWindow().setContentView(dialogView);
        TextView sg_password_title_phone = (TextView) dialogView.findViewById(R.id.sg_password_title_phone);
        TextView sg_password_title = (TextView) dialogView.findViewById(R.id.sg_password_title);
        Button sg_password_title_ok = (Button) dialogView.findViewById(R.id.sg_password_title_ok);
        if (type == 0) {
            sg_password_title_phone.setVisibility(View.VISIBLE);
            sg_password_title.setText(R.string.songguo_password_title);
            sg_password_title_ok.setText("知道了");
        } else {
            sg_password_title_phone.setVisibility(View.GONE);
            sg_password_title.setText(R.string.songguo_password_error_title);
            sg_password_title_ok.setText("确定");
        }
        sg_password_title_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
    }

    /**
     * 修改密码
     */
    private void ModifyPoss(String password) {
        Map<String, String> map = new HashMap<>();
        map.put("token", new UserInfo(this).getToken());
        map.put("secretKey", new UserInfo(this).getSecretKey());
        map.put("loginName", loginName);
        map.put("idNumber", IDcard);
        map.put("password", password);
        map.put("identifyingCode", ImagerCord);
        OkHttpUtil.sendPost(ApiUri.SONGGUO_MODIFY_PASS, map, this);
       /* OkHttpManager.postFill(G.uri.SONGGUO_MODIFY_PASS, new OkHttpManager.ResultCallback<StyleCodeVo>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(StyleCodeVo response) {
                if(response.getResultCode().equals("0")){
                    G.showToast(PinealSetupPasswordActivity.this, "密码修改成功");
                    finish();
                }else{
                    bNext.setText("下一步");
                    tvPrompt.setText("修改交易密码需短信验证");
                    llInputDate.setVisibility(View.VISIBLE);
                    llUpdateWord.setVisibility(View.GONE);
                    G.showToast(PinealSetupPasswordActivity.this,response.getResultDesc());
                }
            }

        },map);*/
    }

    /**
     * 获取短信验证码
     *
     * @param loginName
     */
    private void getImagerCode(String loginName) {
        Map<String, String> map = new HashMap<>();
        map.put("secretKey", new UserInfo(this).getSecretKey());
        map.put("token", new UserInfo(this).getToken());
        map.put("loginName", loginName);
        map.put("purpose", "03");
        mc = new MyCount(60000, 1000);
        mc.start();
        OkHttpUtil.sendPost(ApiUri.SONGGUO_SMS, map, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.SONGGUO_MODIFY_PASS)) {
                    StyleCodeVo styleCodeVo = gson.fromJson(result, StyleCodeVo.class);
                    if (styleCodeVo.getResultCode().equals("0")) {
                        G.showToast(PinealSetupPasswordActivity.this, "密码修改成功");
                        finish();
                    } else {
                        bNext.setText("下一步");
                        tvPrompt.setText("修改交易密码需短信验证");
                        llInputDate.setVisibility(View.VISIBLE);
                        llUpdateWord.setVisibility(View.GONE);
                        G.showToast(PinealSetupPasswordActivity.this, styleCodeVo.getResultDesc());
                    }
                } else if (uri.equals(ApiUri.SONGGUO_SMS)) {
                    StyleCodeVo styleCodeVo = gson.fromJson(result, StyleCodeVo.class);
                    if (styleCodeVo.getResultCode() != null && !styleCodeVo.getResultCode().equals("0")) {
                        MyDialog(-1);
                    } else {
                        G.showToast(PinealSetupPasswordActivity.this, styleCodeVo.getResultDesc());
                    }
                    mc.onFinish();
                    mc.cancel();
                    bNext.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.SONGGUO_MODIFY_PASS)) {

                } else if (uri.equals(ApiUri.SONGGUO_SMS)) {
                    G.showToast(PinealSetupPasswordActivity.this, "与服务器连接失败，请检查网络！");
                    mc.onFinish();
                    mc.cancel();
                }
            }
        });
    }

    @OnClick({R.id.ib_back, R.id.tv_phoneNumber, R.id.ib_details, R.id.b_getCode, R.id.b_next})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ib_back:
                if (bNext.getText().toString().equals("下一步")) {
                    finish();
                } else {
                    bNext.setText("下一步");
                    tvPrompt.setText("修改交易密码需短信验证");
                    llInputDate.setVisibility(View.VISIBLE);
                    llUpdateWord.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_phoneNumber:
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("ic_tel:" + tvPhoneNumber.getText()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                break;
            case R.id.ib_details:
                MyDialog(0);
                break;
            case R.id.b_getCode:
                loginName = etPassword.getText().toString().trim();
                if (G.isEmteny(loginName)) {
                    G.showToast(PinealSetupPasswordActivity.this, "手机号码不能为空");
                    return;
                }
                if (!FormValidation.isMobileNO(loginName)) {
                    G.showToast(this, "请输入正确的手机号码");
                    return;
                }
                getImagerCode(loginName);
                break;
            case R.id.b_next:
                if (bNext.getText().toString().equals("下一步")) {
                    ImagerCord = etVerifyCode.getText().toString().trim();
                    if (G.isEmteny(ImagerCord)) {
                        G.showToast(PinealSetupPasswordActivity.this, "请输入验证码");
                        return;
                    }
                    etVerifyCode.setText("");
                    etPassword.setText("");
                    bNext.setText("确认修改");
                    tvPrompt.setText("请重新设置交易密码");
                    llInputDate.setVisibility(View.GONE);
                    llUpdateWord.setVisibility(View.VISIBLE);
                } else {
                    String password = etNewPassword.getText().toString().trim();
                    String password_ = etNewPasswordAgain.getText().toString().trim();
                    if (password.length() < 6) {
                        G.showToast(PinealSetupPasswordActivity.this, "密码长度不能小于6位");
                        return;
                    }
                    if (!password.equals(password_)) {
                        G.showToast(PinealSetupPasswordActivity.this, "两次输入的密码不符");
                        return;
                    }
                    etNewPassword.setText("");
                    etNewPasswordAgain.setText("");
                    ModifyPoss(password_);
                }
                break;
        }
    }

    /* 定义一个倒计时的内部类 */
    private final class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            bGetCode.setBackgroundResource(R.drawable.s_rounded_rectangle_red);
            bGetCode.setTextColor(Color.WHITE);
            bGetCode.setEnabled(true);
            bGetCode.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bGetCode.setBackgroundResource(R.drawable.rounded_rectangle_gray);
            bGetCode.setTextColor(Color.BLACK);
            bGetCode.setEnabled(false);
            bGetCode.setText("重新获取(" + millisUntilFinished / 1000 + ")秒");
        }
    }
}
