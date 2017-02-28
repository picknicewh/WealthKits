package com.cfjn.javacf.activity.fund;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.OneBankCardVo;
import com.cfjn.javacf.modle.SGQueryVo;
import com.cfjn.javacf.modle.UserBankcardListVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.widget.CalculatePopup;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.PinealHelpPopup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 *  作者： zll
 *  时间： 2016-6-1
 *  名称： 松果交易页面
 *  版本说明：代码规范整改
 *  附加注释：1.松果申购/赎回都调用这个页面
 *  主要接口：1. 获取银行卡
 *             2.获取现金宝余额
 */
public class SgBuyActivity extends Activity implements View.OnClickListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 转入或转出
     */
    @Bind(R.id.tv_type_name)
    TextView tvTypeName;
    /**
     * 买入或买出
     */
    @Bind(R.id.tv_operate_mode)
    TextView tvOperateMode;
    /**
     * 金额或者份额
     */
    @Bind(R.id.tv_purchase_name)
    TextView tvPurchaseName;
    /**
     * 金额输入框
     */
    @Bind(R.id.ed_money)
    EditText edMoney;
    /**
     * 全部转出
     */
    @Bind(R.id.tv_allout)
    TextView tvAllout;
    /**
     * 支付方式
     */
    @Bind(R.id.tv_payment_type)
    TextView tvPaymentType;
    /**
     * 银行图标
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
     * 支付更多图标
     */
    @Bind(R.id.ib_more)
    ImageButton ibMore;
    /**
     * 点击更换银行卡
     */
    @Bind(R.id.rl_payment_options)
    RelativeLayout rlPaymentOptions;
    /**
     * 风险提醒
     */
    @Bind(R.id.tv_prompt)
    TextView tvPrompt;
    /**
     * 选择按钮
     */
    @Bind(R.id.cb_agreement)
    CheckBox cbAgreement;
    /**
     * 协议
     */
    @Bind(R.id.tv_agreement)
    TextView tvAgreement;
    /**
     * 提示
     */
    @Bind(R.id.tv_agreement_txt)
    TextView tvAgreementTxt;
    /**
     * 协议
     */
    @Bind(R.id.ll_agreement)
    LinearLayout llAgreement;
    /**
     * 确认转入或转出
     */
    @Bind(R.id.b_buy_type)
    Button bBuyType;
    /**
     * 基金购买风险提示
     */
    @Bind(R.id.ll_hint)
    LinearLayout llHint;

    /**
     * 用户协议
     */
    private AlertDialog myDialog;

    //-------------逻辑变量-------------------
    /**
     * 接收页面传过来的值判断展示的是转出界面还是转入界面
     */
    private int index;

    private UserInfo userInfo;
    /**
     * 资产
     */
    private String assets;
    private List<UserBankcardListVo> bankcardListVos;
    /**
     * 输入金额
     */
    private String money;
    /**
     * 基金名称
     */
    private String fundName;

    /**
     * 基金编码
     */
    private String fundCord;

    /**
     * 最低申购金额
     */
    private double purchaseLimitMin;

    /**
     * 银行卡号
     */
    private String cardNumber;

    /**
     * 银行卡类型
     */
    private String cardType;


    private CalculatePopup popupWindow;

    /**
     * 基金类型
     */
    private int fundType;

    /**
     * 份额
     */
    private double unpaidIncome;
    /**
     * 支付方式
     */
    private int payMethod;
    /**
     * 现金宝余额
     */
    private double balance;
    /**
     * 提示框的标题和内容
     */
    private TextView title, agreement;
    /**
     * 银行卡名字
     */
    private String cardName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pineal_buy);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();
        initializeControl();
    }

    private void initializeMode(){
        userInfo = UserInfo.getInstance(this);
        bankcardListVos = new ArrayList<>();
        index = getIntent().getIntExtra("poisition", 0);
        fundType = getIntent().getIntExtra("fundType", 7);
        assets = getIntent().getStringExtra("Assets");
        fundCord = getIntent().getStringExtra("fundCord");
        fundName = getIntent().getStringExtra("fundName");
        purchaseLimitMin = getIntent().getDoubleExtra("purchaseLimitMin", 1.0);
        unpaidIncome = getIntent().getDoubleExtra("unpaidIncome", 1);
    }


    private void initializeView() {
        SpannableString spanBuilde = new SpannableString("同意《风险提示》《货基理财服务协议》《民生加银基金网上销售协议》");
        spanBuilde.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.gray_x0_8)), 2, spanBuilde.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanBuilde.setSpan(new ClickableSpan() {
                               @Override
                               public void updateDrawState(TextPaint ds) {
                                   super.updateDrawState(ds);
                                   ds.setColor(getResources()
                                           .getColor(R.color.red));
                                   ds.setUnderlineText(false);
                               }

                               @Override
                               public void onClick(View widget) {
                                   showHintDialog(2);
                               }

                           }, 2, 8,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanBuilde.setSpan(new ClickableSpan() {
                               @Override
                               public void updateDrawState(TextPaint ds) {
                                   super.updateDrawState(ds);
                                   ds.setColor(getResources()
                                           .getColor(R.color.red));
                                   ds.setUnderlineText(false);
                               }

                               @Override
                               public void onClick(View widget) {
                                   showHintDialog(0);
                               }

                           }, 8, 18,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanBuilde.setSpan(new ClickableSpan() {
                               @Override
                               public void updateDrawState(TextPaint ds) {
                                   super.updateDrawState(ds);
                                   ds.setColor(getResources()
                                           .getColor(R.color.red));
                                   ds.setUnderlineText(false);
                               }

                               @Override
                               public void onClick(View widget) {
                                   showHintDialog(1);
                               }

                           }, 18, spanBuilde.length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(spanBuilde);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        if (index == 0) {
            if (fundCord.equals("999999999")) {
                payMethod = 0;
                tvTypeName.setText("现金宝");
                tvOperateMode.setText("转入现金宝");
                if (purchaseLimitMin < 10000) {
                    edMoney.setHint("最低转入金额￥" + purchaseLimitMin + "元");
                } else {
                    edMoney.setHint("最低转入金额￥" + purchaseLimitMin / 10000 + "万元");
                }
                bBuyType.setText("确认转入");
                ibMore.setVisibility(View.GONE);
                llAgreement.setVisibility(View.VISIBLE);
                tvAgreement.setVisibility(View.VISIBLE);
                tvAgreementTxt.setVisibility(View.GONE);
                llHint.setVisibility(View.GONE);
            } else {
                tvTypeName.setText("申购基金");
                tvOperateMode.setText("申购" + fundName + "(" + fundCord + ")");
                if (purchaseLimitMin < 10000) {
                    edMoney.setHint("最低申购金额￥" + purchaseLimitMin);
                } else {
                    edMoney.setHint("最低申购金额￥" + purchaseLimitMin / 10000 + "万");
                }
                bBuyType.setText("确认申购");
                ibMore.setVisibility(View.VISIBLE);
                llHint.setVisibility(View.VISIBLE);
                if (fundType != 7) {
                    llAgreement.setVisibility(View.VISIBLE);
                } else {
                    llAgreement.setVisibility(View.GONE);
                }
            }
            tvPaymentType.setText("支付方式");
            tvPurchaseName.setText("金额");
            tvAllout.setVisibility(View.GONE);
        } else if (index == 1) {
            if (fundCord.equals("999999999")) {
                tvTypeName.setText("现金宝");
                tvOperateMode.setText("转出现金宝");
                edMoney.setHint("本次最多可转出" + assets + "元");
                tvPaymentType.setText("赎回到银行卡");
                bBuyType.setText("确认转出");
                tvPurchaseName.setText("金额");
                tvPrompt.setVisibility(View.GONE);
                llHint.setVisibility(View.GONE);
            } else {
                tvTypeName.setText("赎回基金");
                tvOperateMode.setText("赎回" + fundName + "(" + fundCord + ")");
                edMoney.setHint("本次最多可赎回" + unpaidIncome + "份");
                tvPaymentType.setText("赎回到现金宝");
                bBuyType.setText("确认赎回");
                tvPurchaseName.setText("份额");
                tvPrompt.setVisibility(View.VISIBLE);
                llHint.setVisibility(View.VISIBLE);
            }
            tvAllout.setVisibility(View.VISIBLE);
            ibMore.setVisibility(View.GONE);
            llAgreement.setVisibility(View.GONE);
        }
    }

    private void initializeControl(){
        getBalance();
        getBankCardManger();
    }
    /**
     * 协议弹窗
     * @param index
     */
    private void showHintDialog(int index) {
        myDialog = new AlertDialog.Builder(SgBuyActivity.this).create();
        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.activity_customer);
        title = (TextView) myDialog.findViewById(R.id.title);
        agreement = (TextView) myDialog.findViewById(R.id.agreement);
        if (index == 0) {
            title.setText(R.string.hjlc);
            agreement.setText(R.string.hjlc_);
        } else if (index == 1) {
            title.setText(R.string.msjj);
            agreement.setText(R.string.msjj_);
        } else if (index == 2) {
            title.setText(R.string.fxts);
            agreement.setText(R.string.fxts_);
        }
        myDialog.getWindow().findViewById(R.id.b_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
    }

    /**
     * 设置支付信息
     * @param type 是否现金宝
     * @param bankcardListVos 银行卡信息对象
     */
    private void setPaymentManger(int type, List<UserBankcardListVo> bankcardListVos) {
        if (bankcardListVos.size() > 0) {
            for (int i = 0; i < bankcardListVos.size(); i++) {
                if (bankcardListVos.get(i).getInUse().equals("1")) {

                    cardNumber = bankcardListVos.get(i).getCardNumber();
                    cardType = bankcardListVos.get(i).getCardType();
                    cardName = bankcardListVos.get(i).getCardName();
                    if (type == 1) {
                        //使用银行卡申购
                        if (fundCord.equals("999999999")) {
                            payMethod = 0;
                        } else {
                            payMethod = 1;
                        }
                        tvBankName.setText(bankcardListVos.get(i).getCardName());
                        tvBankNumber.setText("尾号:" + bankcardListVos.get(i).getCardNumber().substring(bankcardListVos.get(i).getCardNumber().length() - 4, bankcardListVos.get(i).getCardNumber().length()));
                        switch (bankcardListVos.get(i).getCardName()) {
                            case "中国银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_zhongguo);
                                break;
                            case "北京银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_beijing);
                                break;
                            case "工商银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_gongshang);
                                break;
                            case "光大银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_guangda);
                                break;
                            case "广发银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_guangfa);
                                break;
                            case "建设银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_jianshe);
                                break;
                            case "交通银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_jiaotong);
                                break;
                            case "民生银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_minsheng);
                                break;
                            case "农业银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_nongye);
                                break;
                            case "平安银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_pingan);
                                break;
                            case "浦发银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_pufa);
                                break;
                            case "上海银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_shanghai);
                                break;
                            case "兴业银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_xingye);
                                break;
                            case "邮储储蓄银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_youchu);
                                break;
                            case "招商银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_zhaoshang);
                                break;
                            case "中兴银行":
                                ivBankLogo.setImageResource(R.drawable.ic_bank_zhongxing);
                                break;
                        }

                    } else if (type == 0) {
                        //使用现金宝申购
                        tvPrompt.setVisibility(View.GONE);
                        payMethod = 2;
                        tvBankName.setText("现金宝");
                        ivBankLogo.setImageResource(R.drawable.ic_money);
                        tvBankNumber.setText("余额￥" + balance);
                    }
                }
            }
        }
    }

    /**
     * 选择柜台支付
     * @param bankcardListVos 柜台银行卡信息
     */
    private void paymentOptions(final List<UserBankcardListVo> bankcardListVos) {
        rlPaymentOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0 && !fundCord.equals("999999999")) {
                    popupWindow = new CalculatePopup(SgBuyActivity.this, bankcardListVos, balance);
                    popupWindow.showAtLocation(SgBuyActivity.this.findViewById(R.id.rl_sg_buy), Gravity.BOTTOM, 0, 0);
                    popupWindow.setonClick(new CalculatePopup.ICoallBack() {
                        @Override
                        public void onClickButton(int index) {
                            setPaymentManger(index, bankcardListVos);
                        }
                    });
                }
            }
        });
    }

    @OnClick({R.id.ib_back, R.id.tv_allout, R.id.b_buy_type})
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_allout:
                if (fundCord.equals("999999999")) {
                    edMoney.setText(String.valueOf(assets));
                } else {
                    edMoney.setText(String.valueOf(unpaidIncome));
                }
                break;
            case R.id.b_buy_type:
                money = edMoney.getText().toString();
                String value = "^(\\d+)|(\\d+\\.?\\d{1,2})$";
                if (!money.matches(value)) {
                    G.showToast(SgBuyActivity.this, "输入金额不符合规范");
                    return;
                }
                if (!cbAgreement.isChecked()) {
                    G.showToast(SgBuyActivity.this, "请先勾选协议");
                } else if (edMoney.length() <= 0) {
                    if (index == 0) {
                        if (fundCord.equals("999999999")) {
                            G.showToast(SgBuyActivity.this, "转入金额不能为空");
                        } else {
                            G.showToast(SgBuyActivity.this, "申购金额不能为空");
                        }
                    } else {
                        if (fundCord.equals("999999999")) {
                            G.showToast(SgBuyActivity.this, "转出金额不能为空");
                        } else {
                            G.showToast(SgBuyActivity.this, "赎回金额不能为空");
                        }
                    }
                } else if (index == 1 && Double.parseDouble(money) < 0.01 && fundCord.equals("999999999")) {
                    G.showToast(SgBuyActivity.this, "最低转出金额0.01元");
                } else if (index == 1 && Double.parseDouble(money) < 1 && !fundCord.equals("999999999")) {
                    G.showToast(SgBuyActivity.this, "最低赎回份额1份");
                } else if (index == 1 && fundCord.equals("999999999") && Double.parseDouble(money) > Double.parseDouble(assets)) {
                    G.showToast(SgBuyActivity.this, "最多转出金额" + assets + "元");
                } else if (index == 1 && !fundCord.equals("999999999") && Double.parseDouble(money) > unpaidIncome) {
                    G.showToast(SgBuyActivity.this, "最多赎回份额" + unpaidIncome + "份");
                } else if (index == 0 && Double.parseDouble(money) < purchaseLimitMin) {
                    if (fundCord.equals("999999999")) {
                        G.showToast(SgBuyActivity.this, "最低转入金额" + purchaseLimitMin + "元");
                    } else {
                        G.showToast(SgBuyActivity.this, "最低申购金额" + purchaseLimitMin + "元");
                    }
                } else {
                    PinealHelpPopup popupWindow = new PinealHelpPopup(SgBuyActivity.this, index, money, cardNumber, cardType, fundCord, String.valueOf(fundType), payMethod, fundName, cardName);
                    popupWindow.showAtLocation(SgBuyActivity.this.findViewById(R.id.rl_sg_buy), Gravity.BOTTOM, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                    SGSMS songGuoSMS=new SGSMS(PinealBuyActivity.this,"",ic_money);
                }
                break;
        }
    }

    /**
     *  获取银行卡
     */
    private void getBankCardManger() {
        Map<String, String> params = new HashMap<>();
        params.put("token", userInfo.getToken());
        params.put("secretKey", userInfo.getSecretKey());
        params.put("source", "2");
        OkHttpUtil.sendPost(ApiUri.QUERYBANKCARD, params, this);
    }

    /**
     *  获取现金宝余额
     */
    private void getBalance() {
        Map<String, String> params = new HashMap<>();
        params.put("token",userInfo.getToken());
        params.put("secretKey", userInfo.getSecretKey());
        OkHttpUtil.sendPost(ApiUri.SG_QUERYBALANCE, params, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.QUERYBANKCARD)) {
                    OneBankCardVo oneBankCardVo = gson.fromJson(result, OneBankCardVo.class);
                    if (oneBankCardVo.getDate() == null || oneBankCardVo.getDate().equals("") || oneBankCardVo.getDate().equals("null")) {
                        G.showToast(SgBuyActivity.this, "暂未获取到银行卡信息");
                    } else {
                        bankcardListVos = oneBankCardVo.getDate();
                        paymentOptions(bankcardListVos);
                        if (index == 1 && !fundCord.equals("999999999")) {
                            setPaymentManger(0, bankcardListVos);
                        } else {
                            setPaymentManger(1, bankcardListVos);
                        }
                    }
                } else if (uri.equals(ApiUri.SG_QUERYBALANCE)) {
                    SGQueryVo sgQueryVo = gson.fromJson(result, SGQueryVo.class);
                    if (sgQueryVo.getDate() == null || sgQueryVo.getDate().equals("") || sgQueryVo.getDate().equals("null")) {
                        balance = 0;
                    } else {
                        balance = sgQueryVo.getDate().getCityValue();
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
                if (uri.equals(ApiUri.QUERYBANKCARD)) {
                    G.showToast(SgBuyActivity.this, error);
                } else if (uri.equals(ApiUri.SG_QUERYBALANCE)) {
                    G.showToast(SgBuyActivity.this, error);
                }
            }
        });
    }

}
