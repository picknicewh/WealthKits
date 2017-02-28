package com.cfjn.javacf.activity.fund;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.widget.PinealHelpPopup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 松果撤单
 *  版本说明：代码规范整改
 *  附加注释：松果撤单调用SGPopup类传对应参数进行撤单
 *  主要接口：暂无
 */
public class SgRevokeActivity extends Activity implements View.OnClickListener {
    //-----------布局变量--------------
    /**
     * 基金名称
     */
    @Bind(R.id.tv_fund_name)
    TextView tvFundName;
    /**
     * 业务类型
     */
    @Bind(R.id.tv_business_type)
    TextView tvBusinessType;
    /**
     * 份额/金额
     */
    @Bind(R.id.tv_purchase_name)
    TextView tvPurchaseName;
    /**
     * 份额/金额  值
     */
    @Bind(R.id.tv_purchase_money)
    TextView tvPurchaseMoney;
    /**
     * 银行卡
     */
    @Bind(R.id.tv_bank_number)
    TextView tvBankNumber;
    /**
     * 温馨提示
     */
    @Bind(R.id.tv_prompt)
    TextView tvPrompt;

    //-------------逻辑变量-------------------
    /**
     * 交易账号
     */
    private String tradeAccount;

    /**
     * 交易流水号
     */
    private String applySerial;

    /**
     * 赎回撤单或申购撤单
     */
    private String index;

    /**
     * 基金名称
     */
    private String fundName;

    /**
     * 金额
     */
    private double amount;
    /**
     * 份额
     */
    private double shares;

    /**
     * 银行卡名称
     */
    private String bankName;
    /**
     * 银行卡号
     */
    private String bankAccount;

    private String fundCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_revoke);
        ButterKnife.bind(this);
        setContentValue();
    }

    private void setContentValue() {
        tradeAccount = getIntent().getStringExtra("tradeAccount");
        applySerial = getIntent().getStringExtra("applySerial");
        index = getIntent().getStringExtra("index");
        fundName = getIntent().getStringExtra("fundName");
        amount = getIntent().getDoubleExtra("amount", 0.0);
        shares = getIntent().getDoubleExtra("shares", 0.0);
        bankName = getIntent().getStringExtra("bankName");
        bankAccount = getIntent().getStringExtra("bankAccount");
        fundCode=getIntent().getStringExtra("fundCode");
        tvFundName.setText(fundName);

        if (index.equals("022")) {
            tvBusinessType.setText("申购");
            tvPurchaseName.setText("金额");
            tvPurchaseMoney.setText(String.valueOf(amount));
            tvPrompt.setVisibility(View.VISIBLE);
            if (bankName == null || bankName.equals("") || bankName.equals("null")) {
                tvBankNumber.setText("现金宝");
            } else {
                tvBankNumber.setText(bankName + "(" + bankAccount.substring(bankAccount.length() - 4, bankAccount.length()) + ")");
            }
        } else {
            tvBusinessType.setText("赎回");
            tvPurchaseName.setText("份额");
            tvPurchaseMoney.setText(String.valueOf(shares));
            tvBankNumber.setText("现金宝");
            tvPrompt.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.ib_back, R.id.b_revoke})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                //返回
                finish();
                break;
            case R.id.b_revoke:
                //撤单
                PinealHelpPopup popupWindow = new PinealHelpPopup(SgRevokeActivity.this, 3, "", applySerial, tradeAccount, fundCode, "", 0, "", "");
                popupWindow.showAtLocation(SgRevokeActivity.this.findViewById(R.id.rl_revoke), Gravity.BOTTOM, 0, 0);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;
        }
    }

}
