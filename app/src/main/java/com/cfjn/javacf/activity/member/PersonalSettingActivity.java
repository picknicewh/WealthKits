package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.base.UserInfo;
import com.openhunme.cordova.activity.HMDroidGap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 个人设置
 *  版本说明：代码规范整改
 *  附加注释：显示投资风格/姓名/身份证/手机号码
 *            退出登录情况用户信息
 *  主要接口：暂无
 */
public class PersonalSettingActivity extends Activity implements View.OnClickListener {
    /**
     * 投资风格
     */
    @Bind(R.id.tv_evaStyle)
    TextView tvEvaStyle;
    /**
     * 用户名字
     */
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    /**
     * 身份证号码
     */
    @Bind(R.id.tv_userIdNumber)
    TextView tvUserIdNumber;
    /**
     * 电话号码
     */
    @Bind(R.id.tv_PhoneNumber)
    TextView tvPhoneNumber;

    private UserInfo userInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalsetting);
        ButterKnife.bind(this);
        userInfo = UserInfo.getInstance(this);
        initializeView();
    }

    private void initializeView() {
        String idNumber = userInfo.getIdNumber();
        if (userInfo.getIdNumber().length() == 18) {
            tvUserIdNumber.setText(idNumber.substring(0, 1) + "*****************" + idNumber.substring(idNumber.length() - 1, idNumber.length()));
        } else {
            tvUserIdNumber.setText(idNumber);
        }
        tvUserName.setText(userInfo.getUserName());
    }

    /**
     * onResume获取投资风格和手机号码
     */
    @Override
    protected void onResume() {
        super.onResume();
        tvEvaStyle.setText(userInfo.getStyleEva());
        String phoneNumber = userInfo.getLoginName();
        tvPhoneNumber.setText(phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11));
    }


    @OnClick({R.id.ib_back, R.id.ll_phone, R.id.b_outLogin, R.id.ll_evaStyle})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_evaStyle:
                intent = new Intent(this, StyleEvaluationActivity.class);
                break;
            case R.id.ll_phone:
                intent = new Intent(this, UpdatephoneActivity.class);
                break;
            case R.id.ib_back:
                finish();
                break;
            case R.id.b_outLogin:
                showPopupWindow();
                break;
        }

        if (null != intent) {
            startActivity(intent);
        }
    }

    /**
     * 退出提示框
     */
    private void showPopupWindow() {
        View coupons_view = LayoutInflater.from(this).inflate(R.layout.pop_exit, null);
        final AlertDialog coupons_s = new AlertDialog.Builder(this).create();
        coupons_s.setCanceledOnTouchOutside(true);
        coupons_s.show();
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params =
                coupons_s.getWindow().getAttributes();
//        params.height = (int) (display.getHeight());
        params.width = (int) (display.getWidth() * 0.8);
        coupons_s.getWindow().setAttributes(params);
        coupons_s.getWindow().setBackgroundDrawableResource(R.drawable.fillet_pop);
        coupons_s.getWindow().setContentView(coupons_view);
        Button pop_notrigst = (Button) coupons_view.findViewById(R.id.pop_notrigst);
        Button pop_mastrigst = (Button) coupons_view.findViewById(R.id.pop_mastrigst);

        pop_mastrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //清除个人所有记录
                userInfo.clean();
                HMDroidGap.synCookies(PersonalSettingActivity.this);
                Intent intent = new Intent(PersonalSettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        pop_notrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupons_s.cancel();
            }
        });
    }


}
