package com.cfjn.javacf.activity.assets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.fund.SgRevokeActivity;
import com.cfjn.javacf.modle.TransactionRecordsVo;
import com.cfjn.javacf.widget.DateFormat;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.PurchaseTotal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-1
 *  名称： 交易记录详情
 *  版本说明：代码规范整改
 *  附加注释：待确认订单有撤单功能
 *  上个页面传过来的List<TransactionRecordsVo>对象是整个页面的数据来源
 *  主要接口：暂无
 */
public class TransactionRecordDetailActivity extends Activity implements View.OnClickListener {
    //-----------布局变量--------------
    /**
     * 交易类型图
     */
    @Bind(R.id.iv_trade_logo)
    ImageView ivTradeLogo;
    /**
     * 交易类型
     */
    @Bind(R.id.tv_trade_value)
    TextView tvTradeValue;
    /**
     * 基金公司logo
     */
    @Bind(R.id.iv_details_logo)
    ImageView ivDetailsLogo;
    /**
     * 基金公司名称
     */
    @Bind(R.id.tv_details_name)
    TextView tvDetailsName;
    /**
     * 基金名称
     */
    @Bind(R.id.tv_fund_name)
    TextView tvFundName;
    /**
     * 交易金额
     */
    @Bind(R.id.tv_trading_amount)
    TextView tvTradingAmount;
    /**
     * 单位
     */
    @Bind(R.id.tv_nuit)
    TextView tvNuit;
    /**
     * 基金编码
     */
    @Bind(R.id.tv_fund_code)
    TextView tvFundCode;
    /**
     * 申请时间的值
     */
    @Bind(R.id.tv_apply_time)
    TextView tvApplyTime;
    /**
     * 确认时间容器
     */
    @Bind(R.id.ll_apply)
    LinearLayout llApply;
    /**
     * 确认时间或预计确认时间
     */
    @Bind(R.id.tv_confirm_type)
    TextView tvConfirmType;
    /**
     * 确认时间或预计确认时间的值
     */
    @Bind(R.id.tv_confirm_time)
    TextView tvConfirmTime;
    /**
     * 确认时间或预计时间的linear
     */
    @Bind(R.id.ll_confirm)
    LinearLayout llConfirm;
    /**
     * 份额
     */
    @Bind(R.id.tv_share)
    TextView tvShare;
    /**
     * 成交净值
     */
    @Bind(R.id.tv_profit)
    TextView tvProfit;
    /**
     * 成交净值容器
     */
    @Bind(R.id.ll_profit)
    LinearLayout llProfit;
    /**
     * 确认状态
     */
    @Bind(R.id.ll_confirm_affairs)
    LinearLayout llConfirmAffairs;
    /**
     * 确认状态-文字
     */
    @Bind(R.id.tv_state)
    TextView tvState;
    /**
     * 确认状态linear
     */
    @Bind(R.id.ll_confirm_state)
    LinearLayout llConfirmState;
    /**
     * 银行logo
     */
    @Bind(R.id.iv_bank_logo)
    ImageView ivBankLogo;
    /**
     * 银行名称
     */
    @Bind(R.id.tv_bank_name)
    TextView tvBankName;
    /**
     * 银行卡号
     */
    @Bind(R.id.tv_bank_number)
    TextView tvBankNumber;
    /**
     * 手续费
     */
    @Bind(R.id.tv_poundage)
    TextView tvPoundage;
    /**
     * 手续费linear
     */
    @Bind(R.id.ll_poundage)
    LinearLayout llPoundage;
    /**
     * 撤单
     */
    @Bind(R.id.b_out_order)
    Button bOutOrder;

    //-------------逻辑变量-------------------
    /**
     * intent传过来的值
     */
    private List<TransactionRecordsVo> clist;
    private DecimalFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);
        ButterKnife.bind(this);
        clist = new ArrayList<>();
        format = new DecimalFormat("#0.00");
        clist.add((TransactionRecordsVo) getIntent().getSerializableExtra("allTransation"));
        setContentValue();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //如果撤单成功则退出该界面 这个数据是上个界面传过来的，必须销毁该界面不然无法数据刷新
        if (G.ISOUTORDEER == true) {
            this.finish();
        }
    }

    private void setContentValue() {
        if ( null==clist || clist.size() <= 0||null==clist.get(0)) {
            G.showToast(TransactionRecordDetailActivity.this, "暂无数据");
            return;
        }

        TransactionRecordsVo recordsVo=clist.get(0);
        //配置撤单按钮
        if (recordsVo.getSource().equals("1")||null==recordsVo.getCanCancel()
                ||1==recordsVo.getCanCancel()||4==recordsVo.getCanCancel()) { //天风撤单键隐藏
            bOutOrder.setVisibility(View.GONE);
        } else {
            bOutOrder.setVisibility(View.VISIBLE);
        }

        //图标配置
        String businessType=recordsVo.getBusinessType();
        if ("022".equals(businessType)) {
            ivTradeLogo.setImageResource(R.drawable.ic_purchase_p);
        } else if ("024".equals(businessType)) {
            ivTradeLogo.setImageResource(R.drawable.ic_redeem);
        } else {
            ivTradeLogo.setImageResource(R.drawable.ic_bonusissue);
        }
        //不属于天风的赎回
        if(!"1".equals(recordsVo.getSource())&&businessType.equals("024")){
            //确认中或者已撤销交易
            if( recordsVo.getStatus() == 9||recordsVo.getStatus() == 4){
                //份
                tvNuit.setText(" 份");
            }else{
                //元
                tvNuit.setText(" 元");
            }
            //强制调增
        }else if(recordsVo.getBusinessType().equals("144")){
            //份
            tvNuit.setText(" 份");
        }else {
            //元
            tvNuit.setText(" 元");
        }
        tvTradingAmount.setText( null==recordsVo.getAmount()?format.format(recordsVo.getShares()):format.format(recordsVo.getAmount()));

        //配置动作
        if (recordsVo.getFundCode().equals("999999999")) {
            if (businessType.equals("022")) {
                tvTradeValue.setText("转入");
            } else if (businessType.equals("024")) {
                tvTradeValue.setText("转出");
            }
        } else {
            //基金属于普通基金
            if ( G.isEmteny(recordsVo.getBusinessTypeToCN())) {
                if (("022").equals(businessType)) {
                    tvTradeValue.setText("申购");
                } else if (("024").equals(businessType)) {
                    tvTradeValue.setText("赎回");
                } else if (("143").equals(businessType)) {
                    tvTradeValue.setText("红利发放");
                } else if (("144").equals(businessType)) {
                    tvTradeValue.setText("强制调增");
                } else if (("093".equals(businessType))) {
                    tvTradeValue.setText("取消定投");
                } else if (("988").equals(businessType)) {
                    tvTradeValue.setText("修改定投");
                } else if (("090").equals(businessType)) {
                    tvTradeValue.setText("添加定投");
                } else if (("098").equals(businessType)) {
                    tvTradeValue.setText("快速赎回");
                } else if (("036").equals(businessType)) {
                    tvTradeValue.setText("基金转换");
                } else if (("124").equals(businessType)) {
                    tvTradeValue.setText("赎回确认");
                } else {
                    tvTradeValue.setText("其他");
                }
            } else {
                tvTradeValue.setText(recordsVo.getBusinessTypeToCN());
            }
        }
        //配置柜台
        if (recordsVo.getSource().equals("0")) {
            ivDetailsLogo.setImageResource(R.drawable.ic_shumi_logo);
            tvDetailsName.setText("数米");
        } else if (recordsVo.getSource().equals("1")) {
            ivDetailsLogo.setImageResource(R.drawable.ic_tf_logo);
            tvDetailsName.setText("天风");
        } else if (recordsVo.getSource().equals("2")||recordsVo.getSource().equals("3")) {
            ivDetailsLogo.setImageResource(R.drawable.ic_sg_logo);
            tvDetailsName.setText("松果");
        }

        //配置基金名称
        if (G.isEmteny(recordsVo.getFundName())) {
            tvFundName.setText("- -");
        } else {
            tvFundName.setText(clist.get(0).getFundName());
        }
        //配置基金代码
        if (G.isEmteny(recordsVo.getFundCode()) || recordsVo.getFundCode().equals("999999999")) {
            tvFundCode.setText("");
        } else {
            tvFundCode.setText(clist.get(0).getFundCode());
        }
        //配置动作时间
        if ( G.isEmteny(recordsVo.getApplyDateTime())) {
            llApply.setVisibility(View.GONE);
        } else {
            llApply.setVisibility(View.VISIBLE);
            String applyDateTime = recordsVo.getApplyDateTime();
            tvApplyTime.setText( DateFormat.dateFormat(applyDateTime));
        }

        //判断是否是申购或赎回
        if (businessType.equals("024") || businessType.equals("022") || businessType.equals("142")) {
            // 是否成功或失败
            if (recordsVo.getPayResult() == 101) {
                llConfirm.setVisibility(View.GONE);
                llPoundage.setVisibility(View.GONE);
                tvState.setText(recordsVo.getPayStatusToCN());
            }

            if (recordsVo.getStatus() == 1) {
                llPoundage.setVisibility(View.VISIBLE);
                tvState.setVisibility(View.GONE);
                llConfirmAffairs.setVisibility(View.VISIBLE);
                tvConfirmType.setText("确认时间");
                //设置确认时间
                if (businessType.equals("024")) {
                    if (G.isEmteny(recordsVo.getRedeemAccountDate())) {
                        tvConfirmTime.setText("- -");
                    } else {
                        tvConfirmTime.setText(DateFormat.dateFormat(recordsVo.getRedeemAccountDate()));
                    }
                } else {
                    if (G.isEmteny(recordsVo.getConfirmDate())) {
                        tvConfirmTime.setText("- -");
                    } else {
                        tvConfirmTime.setText(recordsVo.getConfirmDate());
                    }
                }
                llProfit.setVisibility(View.VISIBLE);
                if (null==recordsVo.getAmount()) {
                    tvShare.setText(String.valueOf(recordsVo.getShares()));
//                  record_details_jingzhi_number.setText(format.format(clist.get(0).getAmount() / clist.get(0).getShares()));
                    llProfit.setVisibility(View.GONE);
                } else {
                    if (null==recordsVo.getShares()) {
                        tvShare.setText(format.format(recordsVo.getAmount()));
                        tvProfit.setText("1.0");
                    } else {
                        tvShare.setText(String.valueOf(recordsVo.getShares()));
                        tvProfit.setText(format.format(recordsVo.getAmount() / recordsVo.getShares()));
                    }
                }

                if (null==recordsVo.getPoundAge()||recordsVo.getPoundAge() == 0) {
                    tvPoundage.setText("免手续费");
                } else {
                    tvPoundage.setText(String.valueOf(clist.get(0).getPoundAge()));
                }

            } else if (recordsVo.getStatus() == 9) {
                if (G.isEmteny(recordsVo.getExpectedToConfirm())) {
                    llConfirm.setVisibility(View.GONE);
                } else {
                    llConfirm.setVisibility(View.VISIBLE);
                    tvConfirmType.setText("预计确认时间");
                    tvConfirmTime.setText(recordsVo.getExpectedToConfirm());
                }
                llPoundage.setVisibility(View.GONE);
                llConfirmState.setVisibility(View.GONE);

            } else if (recordsVo.getStatus() == 0) {
                llConfirmAffairs.setVisibility(View.GONE);
                tvState.setVisibility(View.VISIBLE);
                tvState.setText("确认失败");
                if (G.isEmteny(recordsVo.getConfirmDate())) {
                    llConfirm.setVisibility(View.GONE);
                } else {
                    llConfirm.setVisibility(View.VISIBLE);
                    tvConfirmType.setText("确认时间");
                    tvConfirmTime.setText(DateFormat.dateFormat(recordsVo.getConfirmDate()));
                }
                llPoundage.setVisibility(View.GONE);
//                        state.setText(clist.get(0).getPayStatusToCN());
            } else if (recordsVo.getStatus() == 4) {
                llConfirm.setVisibility(View.GONE);
                llConfirmState.setVisibility(View.VISIBLE);
                llPoundage.setVisibility(View.GONE);
                tvState.setText("交易已撤销");
            } else {
                llConfirm.setVisibility(View.GONE);
                llConfirmState.setVisibility(View.GONE);
                llPoundage.setVisibility(View.GONE);
            }

        } else {
            llConfirm.setVisibility(View.VISIBLE);
            llPoundage.setVisibility(View.VISIBLE);
            tvState.setVisibility(View.GONE);
            llConfirmAffairs.setVisibility(View.VISIBLE);
            tvConfirmType.setText("确认时间");
            if (G.isEmteny(recordsVo.getConfirmDate())) {
                llConfirm.setVisibility(View.GONE);
            } else {
                llConfirm.setVisibility(View.VISIBLE);
                tvConfirmType.setText("确认时间");
                tvConfirmTime.setText(DateFormat.dateFormat(recordsVo.getConfirmDate()));
            }

            if (null==recordsVo.getAmount()) {
                tvShare.setText(String.valueOf(recordsVo.getShares()));
//                    record_details_jingzhi_number.setText(format.format(clist.get(0).getAmount() / clist.get(0).getShares()));
                llProfit.setVisibility(View.GONE);
            } else {
                if (null==recordsVo.getShares() ) {
                    tvShare.setText(format.format(recordsVo.getAmount()));
                    tvProfit.setText("1.0");
                } else {
                    tvShare.setText(String.valueOf(recordsVo.getShares()));
                    tvProfit.setText(format.format(recordsVo.getAmount() /recordsVo.getShares()));
                }
            }
            if (null==recordsVo.getPoundAge()) {
                tvPoundage.setText("免手续费");
            } else {
                if (clist.get(0).getPoundAge() == 0) {
                    tvPoundage.setText("免手续费");
                } else {
                    tvPoundage.setText(String.valueOf(clist.get(0).getPoundAge()));
                }
            }
        }

        if (recordsVo.getBankName() == null || recordsVo.getBankName().equals("")) {
            ivBankLogo.setVisibility(View.GONE);
            tvBankNumber.setVisibility(View.GONE);
            tvBankName.setText("- -");
        } else {
            if (null != recordsVo.getBankName()) {
                if (recordsVo.getBankName().equals("工商银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_gongshang);
                } else if (recordsVo.getBankName().equals("农业银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_nongye);
                } else if (recordsVo.getBankName().equals("建设银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_jianshe);
                } else if (recordsVo.getBankName().equals("交通银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_jiaotong);
                } else if (recordsVo.getBankName().equals("招商银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_zhaoshang);
                } else if (recordsVo.getBankName().equals("光大银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_guangda);
                } else if (recordsVo.getBankName().equals("兴业银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_xingye);
                } else if (recordsVo.getBankName().equals("民生银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_minsheng);
                } else if (recordsVo.getBankName().equals("平安银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_pingan);
                } else if (recordsVo.getBankName().equals("北京银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_beijing);
                } else if (recordsVo.getBankName().equals("广发银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_guangfa);
                } else if (recordsVo.getBankName().equals("浦发银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_pufa);
                } else if (recordsVo.getBankName().equals("上海银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_shanghai);
                } else if (recordsVo.getBankName().equals("邮储银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_youchu);
                } else if (recordsVo.getBankName().equals("中国银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_zhongguo);
                } else if (recordsVo.getBankName().equals("中信银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_zhongxing);
                }
                tvBankName.setText(recordsVo.getBankName());
            }
            tvBankNumber.setText("(" + recordsVo.getBankAccount().substring(recordsVo.getBankAccount().length() - 4, recordsVo.getBankAccount().length()) + ")");
        }

    }

    @OnClick({R.id.ib_back, R.id.b_out_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.b_out_order:
                if (clist.get(0).getCanCancel() == 1) {
                    G.showToast(TransactionRecordDetailActivity.this, "现在已不可撤单");
                } else {
                    if (clist.get(0).getSource().equals("0")) {
                        new PurchaseTotal(this).shuMiCancelOrder(clist.get(0).getApplySerial());
                    } else if (clist.get(0).getSource().equals("1")) {

                    } else if (clist.get(0).getSource().equals("2")) {
                        Intent intent = new Intent(TransactionRecordDetailActivity.this, SgRevokeActivity.class);
                        intent.putExtra("tradeAccount", clist.get(0).getTradeAccount());
                        intent.putExtra("applySerial", clist.get(0).getApplySerial());
                        intent.putExtra("index", clist.get(0).getBusinessType());
                        intent.putExtra("fundName", clist.get(0).getFundName());
                        intent.putExtra("amount", clist.get(0).getAmount());
                        intent.putExtra("shares", clist.get(0).getShares());
                        intent.putExtra("bankName", clist.get(0).getBankName());
                        intent.putExtra("bankAccount", clist.get(0).getBankAccount());
                        intent.putExtra("fundCode",clist.get(0).getFundCode());
                        startActivity(intent);
                        finish();
                    }
                }
                break;
        }
    }
}
