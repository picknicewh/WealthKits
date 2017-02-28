package com.cfjn.javacf.activity.fund;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.LoginActivity;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.fragment.DetailsFragment;
import com.cfjn.javacf.fragment.EarningsFragment;
import com.cfjn.javacf.modle.MenberCent;
import com.cfjn.javacf.modle.AttentionVo;
import com.cfjn.javacf.modle.FundBaseInfo;
import com.cfjn.javacf.modle.FundCodeVo;
import com.cfjn.javacf.modle.FundListVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.widget.CalculatePopup;
import com.cfjn.javacf.widget.CustomViewPager;
import com.cfjn.javacf.widget.DateFormat;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LoadingDialog;
import com.cfjn.javacf.widget.NetworkError;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.PagerSlidingTabStrip;
import com.cfjn.javacf.util.PurchaseTotal;
import com.cfjn.javacf.widget.VerticalScrollView;
import com.google.gson.Gson;
import com.openhunme.cordova.activity.HMDroidGap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-5-30
 *  名称： 基金详情
 *  版本说明：代码规范整改
 *  附加注释：利用 PagerSlidingTabStrip 添加了走势图和基本信息（frgment），VerticalScrollView 解决了走势图左右滑动和 ScrollView上下冲突问题
 *  主要接口：1.查询基金信息
 *            2.基金关注信息
 *            3.用户柜台开户信息
 */
public class FundDetailActivity extends FragmentActivity implements View.OnClickListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 基金名称
     */
    @Bind(R.id.tv_fund_name)
    TextView tvFundName;
    /**
     * 基金编号
     */
    @Bind(R.id.tv_fund_code)
    TextView tvFundCode;
    /**
     * 关注状态图标
     */
    @Bind(R.id.iv_atten_showtype)
    ImageView ivAttenShowtype;
    /**
     * 关注页面
     */
    @Bind(R.id.ll_attention)
    LinearLayout llAttention;
    /**
     * 基金定投
     */
    @Bind(R.id.b_vote)
    Button bVote;
    /**
     * 底部布局 (关注 定投 购买)
     */
    @Bind(R.id.ll_button)
    LinearLayout llButton;
    /**
     * 显示日涨跌幅或七日年化率收益
     */
    @Bind(R.id.tv_rose_name)
    TextView tvRoseName;
    /**
     * 日涨跌幅或七日年化率收益的值
     */
    @Bind(R.id.tv_rose_value)
    TextView tvRoseValue;
    /**
     * 显示单位净值或万份收益
     */
    @Bind(R.id.tv_profit_name)
    TextView tvProfitName;
    /**
     * 显示单位净值或万份收益的值
     */
    @Bind(R.id.tv_profit_value)
    TextView tvProfitValue;
    /**
     * 显示股票基金或货币基金
     */
    @Bind(R.id.tv_fund_type)
    TextView tvFundType;
    /**
     * 显示高风险理财或按日计息
     */
    @Bind(R.id.tv_fund_warning)
    TextView tvFundWarning;
    /**
     * 起购金额
     */
    @Bind(R.id.tv_purchase_money)
    TextView tvPurchaseMoney;
    /**
     * 指示器
     */
    @Bind(R.id.psts_tabstrip)
    PagerSlidingTabStrip pstsTabStrip;
    /**
     * 指示器viewpager
     */
    @Bind(R.id.vp_exchange)
    CustomViewPager vpExchange;
    /**
     * 基金详情容器
     * ScrollView
     */
    @Bind(R.id.vsv_fund_details)
    VerticalScrollView vsvFundDetails;

    //-------------逻辑变量-------------------
    private DecimalFormat format1;
    private DecimalFormat format2;
    private DecimalFormat format3;

    private List<FundListVo> fundListVo;
    /**
     * 基金详情对象
     */
    private FundBaseInfo fundBaseInfo;
    /**
     *  收益走势和产品详情的list容器
     */
    private ArrayList<Fragment> fragmentlist;
    /**
     * 基金关注状态
     */
    private boolean attionType;

    private String fundcode;
    /**
     * 基金状态
     */
    private String source;
    /**
     * 开户状态
     */
    private String accountsSatus;
    /**
     * 非法访问提示。加载提示
     */
    private LoadingDialog dialog, loadingDialog;
    private String loadUrlStr;
    private String paramss;
    private UserInfo userInfo;
    private final int INTENTLOGINCODE = 1;
    /**
     * 无网络加载页面
     */
    private NetworkError networkError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();
        initializeControl();
    }

    private void initializeView() {
        ((RelativeLayout) findViewById(R.id.rl_fund_details)).addView(networkError.getView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        networkError.setShowType(false);
        pstsTabStrip.setTextSize(G.dp2px(this, 18));//设置tab的字体大小
        dialog = new LoadingDialog(FundDetailActivity.this, R.style.myDialogTheme, 1);//显示非法访问页面
        loadingDialog = LoadingDialog.regstLoading(this);
        loadingDialog.show();
    }

    private void initializeMode() {
        userInfo = UserInfo.getInstance(this);

        format1 = new DecimalFormat("#0.0000");
        format2 = new DecimalFormat("#0.00");
        format3 = new DecimalFormat("#0");

        fundListVo = new ArrayList<>();
        fundBaseInfo = new FundBaseInfo();
        fundcode = getIntent().getStringExtra("fundCode");
        networkError = NetworkError.getInstance(this);
    }

    private void initializeControl() {
        networkError.setPicImagerViewOnClockListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkError.startAnimation();
                getFundDetails(fundcode);
            }
        });

        getFundDetails(fundcode);
    }



    @Override
    protected void onResume() {
        super.onResume();
        source = fundBaseInfo.getSource();
    }

    /**
     * 设置基金具体数据
     */
    private void setFundDetails(final FundCodeVo response) {
        if (null == response.getDate() || null == response.getDate().getFundBaseInfo()) {
            source = "0123456789";
            G.showToast(FundDetailActivity.this, "暂无数据！");
        } else {
            fundBaseInfo = response.getDate().getFundBaseInfo();
            if (G.isEmteny(fundBaseInfo.getSource())) {
                source = "0123456789";
            } else {
                source = fundBaseInfo.getSource();
            }

            setBasicDetails(fundBaseInfo);
            setTrendDetails(fundBaseInfo);

            setAttenShowType(response.getDate().isAttention());
            attionType = response.getDate().isAttention();


            llAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String atten;
                    if (attionType) {
                        atten = "2";
                    } else {
                        atten = "1";
                    }
                    if (!G.isEmteny(userInfo.getLoginName())) {
                        getAttention(response.getDate().getFundBaseInfo(), atten);
                    } else {
                        Intent intent = new Intent(FundDetailActivity.this, LoginActivity.class);
                        startActivityForResult(intent, INTENTLOGINCODE);
                    }

                }
            });
        }
    }
    /**
     * 设置 基金名称 基金编码 基金万分收益 七日年华率
     *  基金类型 风险等级和起购金额
     * @param baseInfo 基金基本信息对象  向服务端获取
     */
    public void setBasicDetails(FundBaseInfo baseInfo) {
        //将万份收益的值由小变大的工具类
        SpannableStringBuilder spanBuilder;
        ColorStateList redColors = ColorStateList.valueOf(Color.parseColor("#000000"));

        tvFundName.setText(baseInfo.getFundNameAbbr());
        tvFundCode.setText(baseInfo.getFundCode());

        //判断基金来源是否包含松果  如果有松果 并且是多个柜台 比较柜台之间最低起购金额
        Double productValue;
        if (baseInfo.getSource().indexOf("2") != -1) {
            if(baseInfo.getSource().length()>1){
                if(null==baseInfo.getPurchaseLimitMin()||null==baseInfo.getSgPurchaseLimitMin()
                        ||baseInfo.getSgPurchaseLimitMin()<1||baseInfo.getPurchaseLimitMin()<1){
                    productValue=1.00;
                }else{
                    productValue = baseInfo.getPurchaseLimitMin() < baseInfo.getSgPurchaseLimitMin() ? baseInfo.getPurchaseLimitMin() : baseInfo.getSgPurchaseLimitMin();
                }
            }else{
                if(null==baseInfo.getSgPurchaseLimitMin()){
                    productValue=1.00;
                }else{
                    productValue=baseInfo.getSgPurchaseLimitMin()<1?1.00:baseInfo.getSgPurchaseLimitMin();
                }
            }
        } else {
            if(null==baseInfo.getPurchaseLimitMin()){
                productValue=1.00;
            }else{
                productValue=baseInfo.getPurchaseLimitMin()<1?1.00:baseInfo.getPurchaseLimitMin();
            }
        }

        if (productValue < 10000) {
            tvPurchaseMoney.setText(format3.format(productValue) + "元起购");
        } else {
            tvPurchaseMoney.setText(format3.format(productValue / 10000) + "万元起购");
        }

        if (baseInfo.getFundType() == 7) {
            if (null != baseInfo.getThousandsOfIncome()) {
                spanBuilder = new SpannableStringBuilder(format1.format(baseInfo.getThousandsOfIncome()));
            } else {
                spanBuilder = new SpannableStringBuilder(String.valueOf(0));
            }
            spanBuilder.setSpan(new TextAppearanceSpan(null, TypedValue.COMPLEX_UNIT_SP, 25, redColors, null), spanBuilder.length(), spanBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvRoseName.setText("万份收益(" + DateFormat.dayFormat(baseInfo.getCurrDate()) + ")");
            tvProfitName.setText("七日年化收益率");
            tvRoseValue.setText(spanBuilder);
            tvProfitValue.setText(format2.format(baseInfo.getYieldSevenDay() * 100) + "%");
            tvFundWarning.setText("超低风险");
            bVote.setText("定投");
            tvFundType.setText("货币型基金");
        }
    }

    /**
     * 基金详细信息
     * EarningsFragment 走势图
     *
     * DetailsFragment  基金经理等详细信息
     *
     * @param baseInfo 基金基本信息对象  向服务端获取
     */
    private void setTrendDetails(FundBaseInfo baseInfo) {
        fragmentlist = new ArrayList<Fragment>();
        EarningsFragment earningsFragment = new EarningsFragment(baseInfo);
        DetailsFragment detailsFragment = new DetailsFragment(baseInfo);
        fragmentlist.add(earningsFragment);
        fragmentlist.add(detailsFragment);

        final String[] title = {"收益走势", "产品详情"};

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragmentlist.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentlist.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }

        };
        vpExchange.setAdapter(adapter);
        pstsTabStrip.setViewPager(vpExchange);
//        pager.setCurrentItem(position);
    }

    /**
     * 设置关注状态
     *
     * @param isAttention 是否关注
     */
    private void setAttenShowType(boolean isAttention) {
        ivAttenShowtype.setImageResource(isAttention?R.drawable.ic_attention_on:R.drawable.ic_attention_off);
    }

    /**
     *  登录成功返回查询该基金的关注状态
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENTLOGINCODE) {
            if (!G.isEmteny(userInfo.getLoginName())) {
//                start();
                getFundDetails(fundcode);
            }
        }
    }


    @OnClick({R.id.ib_back, R.id.ll_attention, R.id.b_vote, R.id.b_purchase})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_vote:
//                if (product_btn_1.getText().toString().equals("测算收益")) {
//                    CalculatePopup popupWindow = new CalculatePopup(ProductActivity.this, 0, fundBaseInfo.getYieldSevenDay(), 0, "");
//                    popupWindow.showAtLocation(ProductActivity.this.findViewById(R.id.setting_layout), Gravity.BOTTOM, 0, 0);
//                } else if (product_btn_1.getText().toString().equals("定投")) {
                G.showToast(FundDetailActivity.this, "敬请期待");
//                }
                break;
            case R.id.b_purchase:
                //判断是否登录了
                if (!G.isEmteny(userInfo.getLoginName())) {
                    //source =0123456789表示基金异常 无法进行申购处理
                    if (source.equals("0123456789")) {
                        G.showToast(FundDetailActivity.this, "此基金暂不支持购买");
                    } else {
                        //多个柜台申购 source的长度就会大于1 弹出框选择对应柜台进行申购处理 然后return
                        if (source.length() > 1) {
                            //多个柜台申购
                            CalculatePopup popupWindow = new CalculatePopup(FundDetailActivity.this, source, fundBaseInfo);
                            //设置popupwindow的显示位置
                            popupWindow.showAtLocation(FundDetailActivity.this.findViewById(R.id.rl_fund_details), Gravity.BOTTOM, 0, 0);
                            return;
                        }
                        //这是单个柜台申购 先查询客户在对应的柜台开户状态 在进行处理
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("secretKey", userInfo.getSecretKey());
                        params.put("token", userInfo.getToken());
                        params.put("source", source);
                        OkHttpUtil.sendPost(ApiUri.FUND_REGISTER, params, this);
                    }
                } else {
                    Intent intent = new Intent(FundDetailActivity.this, LoginActivity.class);
                    startActivityForResult(intent, INTENTLOGINCODE);
                }
                break;
            case R.id.ib_back:
                finish();
                break;
            case R.id.b_det_nonet:
//                start();
                //无网络重新获取一次数据
                getFundDetails(fundcode);
                break;
            default:
                break;
        }
    }

    /**
     * 查询基金信息
     *
     * @param fundCode
     */
    private void  getFundDetails(final String fundCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", userInfo.getToken());
        params.put("secretKey", userInfo.getSecretKey());
        params.put("fundCode", fundCode);
        OkHttpUtil.sendPost(ApiUri.SOLE_FUND, params, this);
    }

    /**
     * 关注
     */
    public void getAttention(FundBaseInfo baseInfo, String attention) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", userInfo.getToken());
        params.put("secretKey", userInfo.getSecretKey());
        params.put("code", baseInfo.getFundCode() + "");
        params.put("yesNo", attention);
        if (baseInfo.getFundType() == 7) {
            params.put("fundType", baseInfo.getFundType() + "");
        } else {
            G.showToast(this, "此类型基金不支持关注...");
            return;
        }
        params.put("fundName", baseInfo.getFundName());
        OkHttpUtil.sendPost(ApiUri.ATTENTION, params, this);
    }


    /**
     * 购买基金相应的操作，如果没开户进入相应开户界面
     */
    private void FundPurchase(MenberCent menberCent) {
        if (menberCent.getResultDesc() != null && menberCent.getResultDesc().equals("非法访问")) {
            dialog.show();
        } else {
            accountsSatus = menberCent.getResultCode();  //判断是否开过户
            if (source.length() == 1) {
                //单个柜台申购
                if (source.equals("0")) {
                    if (new UserInfo(FundDetailActivity.this).getSmToken().equals("")) {
                        G.showToast(FundDetailActivity.this, "请先在数米开户");
                        new PurchaseTotal(FundDetailActivity.this).ShuMiStart();
                    } else {
                        new PurchaseTotal(FundDetailActivity.this).shuMiPurchase(fundBaseInfo.getFundCode(), fundBaseInfo.getFundName(), fundBaseInfo.getRiskLevel(), fundBaseInfo.getShareType(), String.valueOf(fundBaseInfo.getFundType()));
                    }
                } else if (source.equals("1")) {

                    if (accountsSatus.equals("1")) {
//                   loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/trade/buy";
                        loadUrlStr = MainActivity.TFurl + "/www/tap/view/index_android.html#/trade/buy";
                        paramss = "prodCode=" + fundBaseInfo.getFundCode() + "&prodSource=" + fundBaseInfo.getProdSource() + "&name=" + userInfo.getLoginName() + "&token=" + userInfo.getToken() + "&key=" + userInfo.getSecretKey();
                        loadUrlStr = loadUrlStr + "?" + paramss;
                    } else if (accountsSatus.equals("0")) {
                        G.showToast(FundDetailActivity.this, "请先在天风开户");
//                   loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/open-prepare";
                        loadUrlStr = MainActivity.TFurl + "/www/tap/view/index_android.html#/open-prepare";
                        paramss = "/" + userInfo.getLoginName() + "/" + userInfo.getToken() + "/" + userInfo.getSecretKey();
                        loadUrlStr = loadUrlStr + paramss;
                    }

                    Intent intent1 = new Intent(FundDetailActivity.this, HMDroidGap.class);
//               intent1.putExtra("type", 1);
//               intent1.putExtra("prodCode", fundBaseInfo.getFundCode());
                    intent1.putExtra("loadUrl", loadUrlStr);
                    startActivity(intent1);
                } else {
                    Intent intent = null;
                    if (accountsSatus.equals("1")) {
                        intent = new Intent(FundDetailActivity.this, SgBuyActivity.class);
                        intent.putExtra("poisition", 0);
                        intent.putExtra("fundCord", fundBaseInfo.getFundCode());
                        intent.putExtra("fundName", fundBaseInfo.getFundNameAbbr());
                        if (fundBaseInfo.getFundType() == 7) {
                            intent.putExtra("fundType", fundBaseInfo.getFundType());
                        } else {
                            G.showToast(this, "此类基金不支持购买...");
                            return;
//                            intent.putExtra("fundType", fundBaseInfo.getInvestmentType());
                        }

                        if (G.isEmteny(fundBaseInfo.getPurchaseLimitMin().toString())) {
                            intent.putExtra("purchaseLimitMin", 1);
                        } else {
                            intent.putExtra("purchaseLimitMin", fundBaseInfo.getPurchaseLimitMin());
                        }
                        startActivity(intent);
                    } else if (accountsSatus.equals("0")) {
                        G.showToast(FundDetailActivity.this, "请先在松果开户");
                        intent = new Intent(FundDetailActivity.this, HMDroidGap.class);
                        String params = "?token=" + userInfo.getToken() + "&secretKey=" + userInfo.getSecretKey();
                        String loadUrlStr = "file:///android_asset/www/sg/html/id_validate.html" + params;
                        intent.putExtra("loadUrl", loadUrlStr);
                        startActivity(intent);
                    }
                }

            }
        }

    }


    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.SOLE_FUND)) {
                    //查询单一基金
                    loadingDialog.dismiss();
                    networkError.stopAnimation();
                    networkError.setShowType(false);
                    //停止动画
//                    stop();
                    //此时查到数据，关闭没网络界面
//                    b_det_nonet.setVisibility(View.GONE);
                    vsvFundDetails.setVisibility(View.VISIBLE);
                    llButton.setVisibility(View.VISIBLE);
                    //解析json数据
                    FundCodeVo fundCodeVo = new Gson().fromJson(result, FundCodeVo.class);
                    //设置具体数据
                    setFundDetails(fundCodeVo);
                } else if (uri.equals(ApiUri.FUND_REGISTER)) {
                    //判断用户是否开户
                    MenberCent menberCent = new Gson().fromJson(result, MenberCent.class);
                    FundPurchase(menberCent);
                } else if (uri.equals(ApiUri.ATTENTION)) {
                    //关注或者取消基金
                    AttentionVo attentionVo = new Gson().fromJson(result, AttentionVo.class);
                    if (null == attentionVo) {
                        return;
                    }
                    if (attentionVo.getResultCode().equals("0")) {
                        if (attentionVo.getResultDesc().equals("关注成功")) {
                            attionType = true;
                        } else {
                            attionType = false;
                        }
                        setAttenShowType(attionType);
                    }
                    G.showToast(FundDetailActivity.this, attentionVo.getResultDesc());
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.SOLE_FUND)) {
                    loadingDialog.dismiss();
                    networkError.setShowType(true);
                    networkError.stopAnimation();
                    networkError.setPicImagerViewShowType(true);
//                    stop();
//                    b_det_nonet.setVisibility(View.VISIBLE);
                    vsvFundDetails.setVisibility(View.INVISIBLE);
                    llButton.setVisibility(View.GONE);
                } else if (uri.equals(ApiUri.FUND_REGISTER)) {
                    G.showToast(FundDetailActivity.this, getResources().getString(R.string.notWorkPrompt));
                } else if (uri.equals(ApiUri.ATTENTION)) {
                    G.showToast(FundDetailActivity.this, getResources().getString(R.string.notWorkPrompt));
                }
            }
        });
    }


}
