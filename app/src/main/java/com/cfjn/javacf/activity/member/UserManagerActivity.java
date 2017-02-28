package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.shumi.MyShumiSdkTradingHelper;
import com.cfjn.javacf.modle.UserInformationVo;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.GetTFMagerUrl;
import com.openhunme.cordova.activity.HMDroidGap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-3
 *  名称： 账户管理
 *  版本说明：代码规范整改
 *  附加注释：显示用户在该柜台账户信息
 *  主要接口：暂无
 */
public class UserManagerActivity extends Activity {
    /**
     * 柜台名称
     */
    @Bind(R.id.tv_account_name)
    TextView tvAccountName;
    /**
     * 实名认证
     */
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    /**
     * 身份证号
     */
    @Bind(R.id.tv_userIdNumber)
    TextView tvUserIdNumber;
    /**
     * 手机号码
     */
    @Bind(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;

    private Intent getValue;
    private String idNumber;
    private String mangerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        getValue= getIntent();
        UserInformationVo information = (UserInformationVo) getValue.getExtras().getSerializable("userInformation");
        tvAccountName.setText(getValue.getStringExtra("name"));
        tvUserName.setText(information.getName());
        idNumber = information.getIdNumber();
        mangerName=getValue.getStringExtra("name");
        //设置身份证号
        if (G.isEmteny(idNumber)) {
            tvUserIdNumber.setText("");
        } else {
            tvUserIdNumber.setText(idNumber.substring(0, 1) + "****************" + idNumber.substring(idNumber.length() - 1, idNumber.length()));
        }
        //设置手机号码
        if (G.isEmteny(information.getLoginName())) {
            tvPhoneNumber.setText("");
        } else {
            if (information.getLoginName().length() == 12) {
                tvPhoneNumber.setText(information.getLoginName().substring(0, 3) + "****" + information.getLoginName().substring(7, 11));
            }
        }

    }

    @OnClick({R.id.ib_back, R.id.ll_phoneNumber, R.id.ll_trading})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ll_phoneNumber:
                if (mangerName.indexOf("数米") != -1) {
                    MyShumiSdkTradingHelper.doChangeMobile(UserManagerActivity.this);
                } else if (mangerName.indexOf("天风") != -1) {
                    Intent intent = new Intent(UserManagerActivity.this, HMDroidGap.class);
                    intent.putExtra("loadUrl", GetTFMagerUrl.TFMagerUrl(2, UserManagerActivity.this));
                    startActivity(intent);
                } else if (mangerName.indexOf("松果") != -1) {
                    G.showToast(UserManagerActivity.this, "敬请期待！");
                }
                break;
            case R.id.ll_trading:
                if (mangerName.indexOf("数米") != -1) {
                    //修改交易密码
                    MyShumiSdkTradingHelper.doChangeTradePassword(UserManagerActivity.this);
                    // 重置交易密码
//                MyShumiSdkTradingHelper.doForgetTradePassword(CF1700PwdCenterActivity.this);
                } else if (mangerName.indexOf("松果") != -1) {
                    Intent intent=new Intent(UserManagerActivity.this,PinealSetupPasswordActivity.class);
                    intent.putExtra("IdCard",idNumber);
                    startActivity(intent);
//                    G.showToast(UserManagerActivity.this, "敬请期待！");
                } else if (mangerName.indexOf("天风") != -1) {
                    Intent intent = new Intent(UserManagerActivity.this, HMDroidGap.class);
                    intent.putExtra("loadUrl", GetTFMagerUrl.TFMagerUrl(1, UserManagerActivity.this));
                    startActivity(intent);
                }
                break;
        }
    }
}
