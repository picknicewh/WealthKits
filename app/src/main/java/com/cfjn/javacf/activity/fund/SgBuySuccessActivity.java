package com.cfjn.javacf.activity.fund;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.SGBuyOkVo;
import com.cfjn.javacf.util.DateUtil;
import com.cfjn.javacf.util.G;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 作者： zll
 * 时间： 2016-5-31
 * 名称： 松果交易成功
 * 版本说明：代码规范整改
 * 附加注释： 显示松果交易成功 收益状态 和资产到账状态
 * 主要接口： 暂无
 */
public class SgBuySuccessActivity extends Activity implements View.OnClickListener {
    //-----------布局变量--------------
    /**
     * 转入或转出
     */
    @Bind(R.id.tv_title)
    TextView tvTitle;
    /**
     * 转入金额
     */
    @Bind(R.id.tv_buy_money)
    TextView tvBuyMoney;
    /**
     * 购买或者赎回基金名称
     */
    @Bind(R.id.tv_fund_name)
    TextView tvFundName;
    /**
     * 收益状态图标
     */
    @Bind(R.id.iv_profit_type)
    ImageView ivProfitType;
    /**
     * 收益状态
     */
    @Bind(R.id.tv_profit_state)
    TextView tvProfitState;
    /**
     * 买入计算收益日期
     */
    @Bind(R.id.tv_profit_value)
    TextView tvProfitValue;
    /**
     * 到账状态
     */
    @Bind(R.id.tv_arrival_type)
    TextView tvArrivalType;
    /**
     * 收益到账
     */
    @Bind(R.id.tv_arrival_value)
    TextView tvArrivalValue;
    /**
     * 松果到账页面
     */
    @Bind(R.id.ll_date)
    LinearLayout llDate;
    /**
     * 松果撤单页面
     */
    @Bind(R.id.ll_revoke)
    LinearLayout llRevoke;

    //-------------逻辑变量-------------------
    private String fundName;
    private String fundCode;
    /**
     * 买入或买出的金额
     */
    private String money;
    private String cardNumber;
    private String cardName;
    /**
     * 判断是买入还是买出
     */
    private int index;
    private SGBuyOkVo.SgTransaction sgBuyOkVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinealbuy_finish);
        ButterKnife.bind(this);
        sgBuyOkVo = new SGBuyOkVo.SgTransaction();
        setContextValue();
    }

    public void setContextValue() {
        index = getIntent().getIntExtra("index", 0);
        fundName = getIntent().getStringExtra("fundname");
        fundCode = getIntent().getStringExtra("fundcode");
        money = getIntent().getStringExtra("money");
        cardNumber = getIntent().getStringExtra("cardnumber");
        cardName = getIntent().getStringExtra("cardname");
        sgBuyOkVo = (SGBuyOkVo.SgTransaction) getIntent().getSerializableExtra("sg");
        if (index == 0) {
            if (fundCode.equals("999999999")) {
                tvTitle.setText("现金宝");
                tvBuyMoney.setText("成功转入" + Double.parseDouble(money) / 100 + "元");
                tvFundName.setText("现金宝");
            } else {
                tvTitle.setText("申购成功");
                tvBuyMoney.setText("成功申购" + Double.parseDouble(money) / 100 + "元");
                tvFundName.setText(fundName);
            }
            ivProfitType.setImageResource(R.drawable.ic_account_day);
            tvProfitState.setText("开始计算收益日期");
            tvProfitValue.setText(sgBuyOkVo.getExpectedToConfirm().substring(5, 10) + " " + DateUtil.getWeekOfDate(sgBuyOkVo.getExpectedToConfirm()));
            tvArrivalValue.setText(sgBuyOkVo.getReturnToAccount().substring(5, 10) + " " + DateUtil.getWeekOfDate(sgBuyOkVo.getReturnToAccount()));
        }else if(index==-1){
            llDate.setVisibility(View.GONE);
            llRevoke.setVisibility(View.VISIBLE);
            tvTitle.setText("撤单成功");
        }else {
            if (fundCode.equals("999999999")) {
                tvTitle.setText("现金宝");
                tvBuyMoney.setText("成功转出" + Double.parseDouble(money) / 100 + "元");
                tvFundName.setText("现金宝");
                tvProfitState.setText("到账银行");
                ivProfitType.setImageResource(R.drawable.ic_dz_bank);
                tvProfitValue.setText(cardName + "(" + cardNumber.substring(cardNumber.length() - 4, cardNumber.length()) + ")");
                tvArrivalValue.setText("预计两小时内到账");
                tvArrivalType.setText("收益到账");
            } else {
                tvTitle.setText("赎回成功");
                tvBuyMoney.setText("成功赎回" + money + "份");
                tvFundName.setText(fundName);
                tvProfitState.setText("到账");
                ivProfitType.setImageResource(R.drawable.ic_account_time);
                tvProfitValue.setText("现金宝");
//                tvArrivalValue.setText("预计两小时内到账");
                tvArrivalType.setText("到账时间");
                tvArrivalValue.setText(sgBuyOkVo.getExpectedToConfirm().substring(5, 10) + " " + DateUtil.getWeekOfDate(sgBuyOkVo.getExpectedToConfirm()));
            }
        }
    }

    @OnClick({R.id.ib_back, R.id.b_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.b_confirm:
                G.ISPURCHASE = true;
                finish();
                break;
        }
        G.ISPURCHASE = true;
    }
}
