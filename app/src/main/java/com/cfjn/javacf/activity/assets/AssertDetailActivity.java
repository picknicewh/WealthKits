package com.cfjn.javacf.activity.assets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.activity.fund.SgBuyActivity;
import com.cfjn.javacf.activity.fund.FundDetailActivity;
import com.cfjn.javacf.adapter.assets.TransactionDetailAdapter;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.FundBaseInfo;
import com.cfjn.javacf.modle.FundCodeVo;
import com.cfjn.javacf.modle.FundDetailListVo;
import com.cfjn.javacf.modle.FundDetailVo;
import com.cfjn.javacf.modle.MenberCent;
import com.cfjn.javacf.modle.TransactionRecordsListVo;
import com.cfjn.javacf.modle.TransactionRecordsVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.util.PurchaseTotal;
import com.cfjn.javacf.widget.LoadMoreFooterView;
import com.cfjn.javacf.widget.LoadingDialog;
import com.cfjn.javacf.widget.NetworkError;
import com.cfjn.javacf.widget.ScrollableHelper;
import com.cfjn.javacf.widget.ScrollableLayout;
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
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;

/**
 *  作者： zll
 *  时间： 2016-5-31
 *  名称： 单笔资产详情
 *  版本说明：代码规范整改
 *  附加注释：1.上拉加载（分页加载）LoadMoreListViewContainer
 *  主要接口：1.获取单笔基金交易记录
 *            2.获取单一基金信息
 *            3.获取用户是否开过户
 *            4.获取基金资产信息
 */
public class AssertDetailActivity extends Activity implements View.OnClickListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 基金名称
     */
    @Bind(R.id.tv_fund_name)
    TextView tvFundName;
    /**
     * 基金编码
     */
    @Bind(R.id.tv_fund_code)
    TextView tvFundCode;
    /**
     * 赎回
     */
    @Bind(R.id.b_redeem)
    Button bRedeem;
    /**
     * 追加
     */
    @Bind(R.id.b_additional)
    Button bAdditional;
    /**
     * 持仓收益
     */
    @Bind(R.id.tv_position_gains)
    TextView tvPositionGains;
    /**
     * 显示涨幅还是万份收益
     */
    @Bind(R.id.tv_profit_name)
    TextView tvProfitName;
    /**
     * 显示涨幅或万份收益的值
     */
    @Bind(R.id.tv_profit_value)
    TextView tvProfitValue;
    /**
     * 显示单位净值还是七日年化率
     */
    @Bind(R.id.tv_particular_name)
    TextView tvParticularName;
    /**
     * 显示单位净值还是七日年化率的值
     */
    @Bind(R.id.tv_particular_value)
    TextView tvParticularValue;
    /**
     * 持有金额
     */
    @Bind(R.id.tv_hold_money)
    TextView tvHoldMoney;
    /**
     * 显示份额还是未付收益
     */
    @Bind(R.id.tv_portion_name)
    TextView tvPortionName;
    /**
     * 显示份额还是未付收益的值
     */
    @Bind(R.id.tv_portion_value)
    TextView tvPortionValue;
    /**
     * 交易记录listview
     */
    @Bind(R.id.lv_transaction)
    ListView lvTransaction;
    /**
     * 上拉加载
     */
    @Bind(R.id.lmlvc_down_refresh)
    LoadMoreListViewContainer lmlvcDownRefresh;
    /**
     * 滑动布局
     */
    @Bind(R.id.sl_scroll)
    ScrollableLayout slScroll;

    @Bind(R.id.ib_fund_archives)
    ImageButton ibFundArchives;
    /**
     * 加载提示框和非法访问
     */
    private LoadingDialog dialog, loadingDialog;

    //-------------逻辑变量-------------------
    private int pagerNumber = 1;

    private final int PAGERSIZE = 30;

    private FundDetailListVo fundDetail;

    private NetworkError mNetworkError;

    private TransactionDetailAdapter particularAdapter;

    private FundBaseInfo fundBaseInfo;
    /**
     * 基金来源
     */
    private String source;
    /**
     * 松果开户状态
     */
    private String accountStatus;

    private DecimalFormat format1;
    private DecimalFormat format2;

    private List<TransactionRecordsVo> recordsVo;
    /**
     * 非法访问时 基金不能购买
     */
    private boolean isNotBuy = false;

    private UserInfo userInfo;
    private String fundCode;
    public static boolean buy = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                loadingDialog.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();
        initializeControl();
    }

    private void initializeMode() {
        userInfo = UserInfo.getInstance(this);
        fundBaseInfo = new FundBaseInfo();
        recordsVo = new ArrayList<>();
        format1 = new DecimalFormat("#0.00");
        format2 = new DecimalFormat("#0.0000");
        fundDetail = (FundDetailListVo) getIntent().getSerializableExtra("fundDetail");
        fundCode = fundDetail.getFundCode();
        source = fundDetail.getSource();
    }

    private void initializeView() {
        dialog = new LoadingDialog(AssertDetailActivity.this, R.style.myDialogTheme, 1);
        loadingDialog = LoadingDialog.regstLoading(this);
        mNetworkError = NetworkError.getInstance(this);
        //添加尾部布局<无网络时显示>
        lvTransaction.addFooterView(mNetworkError.getView());
        mNetworkError.setShowType(false);

        slScroll.getHelper().setCurrentScrollableContainer(new ScrollableHelper.ScrollableContainer() {
            @Override
            public View getScrollableView() {
                return lvTransaction;
            }
        });
        //下拉加载
        lmlvcDownRefresh = (LoadMoreListViewContainer) findViewById(R.id.lmlvc_down_refresh);
        lmlvcDownRefresh.setAutoLoadMore(true);//设置是否自动加载更多
        LoadMoreFooterView customMoreView = new LoadMoreFooterView(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(-2, LocalDisplay.dp2px(80));
        customMoreView.setLayoutParams(lp);
        lmlvcDownRefresh.setLoadMoreView(customMoreView);
        lmlvcDownRefresh.setLoadMoreUIHandler(customMoreView);
        lmlvcDownRefresh.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                //模拟加载更多的业务处理
                lmlvcDownRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pagerNumber++;
                        //查询单一基金的信息
                        getTransactionRecord(false);
                    }
                }, 500);
            }
        });
        particularAdapter = new TransactionDetailAdapter(getApplicationContext(), recordsVo);
        lvTransaction.setAdapter(particularAdapter);
    }

    private void initializeControl(){
        mNetworkError.setPicImagerViewOnClockListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTransactionRecord(false);
                mNetworkError.startAnimation();
            }
        });

        lvTransaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (recordsVo.size() <= 0) {
                    return;
                }
                Intent intent = new Intent(AssertDetailActivity.this, TransactionRecordDetailActivity.class);
                intent.putExtra("allTransation", recordsVo.get(position));
                startActivity(intent);
            }
        });
        setValue(fundDetail);
        getFundInfo();
        getTransactionRecord(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取松果是否开过户，对应现金宝
        getAccountStatus("2");

        //松果申购赎回后刷新资产数据
        if (buy == true || G.ISPURCHASE == true) {
            pagerNumber = 1;
            getTransactionRecord(true);
            getAssetInfomation();
            buy = false;
        }
    }

    @OnClick({R.id.ib_back, R.id.ib_fund_archives, R.id.b_redeem, R.id.b_additional})
    public void onClick(View view) {
        Intent intent = null;
        if (isNotBuy == true) {
            dialog.show();
        } else {
            switch (view.getId()) {
                case R.id.ib_back:
                    finish();
                    break;
                case R.id.ib_fund_archives:
                    intent = new Intent(AssertDetailActivity.this, FundDetailActivity.class);
                    intent.putExtra("position", 1);
                    intent.putExtra("fundCode", fundCode);
                    startActivity(intent);
                    break;
                case R.id.b_redeem:
                    if (source.equals("0")) {
                        if (userInfo.getSmToken().equals("")) {
                            G.showToast(AssertDetailActivity.this, "请先在数米开户");
                            new PurchaseTotal(this).ShuMiStart();
                        } else {
                            if (!G.isEmteny(recordsVo.get(0).getTradeAccount())) {
                                new PurchaseTotal(this).NormalRedeem(fundBaseInfo.getFundCode(), fundBaseInfo.getFundName(), recordsVo.get(0).getTradeAccount());
                            }
                        }
                    } else if (source.equals("1")) {
//                        if (yesNo.equals("1")) {
//                            loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/trade/sell";
                        String loadUrlStr = MainActivity.TFurl + "/www/tap/view/index_android.html#/trade/sell";
//                            params = "prodCode=" + fundBaseInfo.getFundCode() + "&prodSource=" + source;
                        String  params = "prodCode=" + fundBaseInfo.getFundCode() + "&prodSource=" + fundDetail.getProdSource() + "&allotNo=" + fundDetail.getTradeAccount() + "&name=" + userInfo.getLoginName() + "&token=" + userInfo.getToken() + "&key=" + userInfo.getSecretKey();
                        loadUrlStr = loadUrlStr + "?" + params;
                        G.log("天风赎回url---------------" + loadUrlStr);
//                        } else if (yesNo.equals("0")) {
//                            G.showToast(ParticularActivity.this, "请先在天风开户");
//                            loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/open-prepare";
//                            params = "/" + userinfo.getLoginName() + "/" + userinfo.getToken() + "/" + userinfo.getSecretKey();
//                            loadUrlStr = loadUrlStr + params;
//                        }
                        intent= new Intent(AssertDetailActivity.this, HMDroidGap.class);
//                        intent1.putExtra("type",1);
//                        intent1.putExtra("prodCode", fundBaseInfo.getFundCode());
//                        intent1.putExtra("prodSource", source);
                        intent.putExtra("loadUrl", loadUrlStr);
                        startActivityForResult(intent, RESULT_CANCELED);
                    } else {
                        if (accountStatus.equals("1")) {
                            intent = new Intent(AssertDetailActivity.this, SgBuyActivity.class);
                            intent.putExtra("poisition", 1);
                            intent.putExtra("Assets", tvHoldMoney.getText());
                            intent.putExtra("fundCord", fundCode);
                            if (G.isEmteny(fundDetail.getUnpaidIncome())) {
                                intent.putExtra("unpaidIncome", Double.parseDouble(fundDetail.getAssets()) / Double.parseDouble(fundDetail.getYieldSevenDay()));
                            } else {
                                if (fundDetail.getFundType().equals("7")) {
                                    intent.putExtra("unpaidIncome", Double.parseDouble(fundDetail.getAssets()) / 1);
                                } else {
                                    intent.putExtra("unpaidIncome", Double.parseDouble(fundDetail.getUnpaidIncome()));
                                }
                            }
                            if (fundCode.equals("999999999")) {
                                intent.putExtra("fundType", 7);
                                intent.putExtra("fundName", "现金宝");
                            } else {
                                intent.putExtra("fundName", fundBaseInfo.getFundNameAbbr());
                                if (fundBaseInfo.getFundType() == 7) {
                                    intent.putExtra("fundType", fundBaseInfo.getFundType());
                                } else {
                                    intent.putExtra("fundType", fundBaseInfo.getInvestmentType());
                                }
                            }
                            startActivity(intent);
                        } else if (accountStatus.equals("0")) {
                            G.showToast(AssertDetailActivity.this, "请先在松果开户");
                            intent = new Intent(AssertDetailActivity.this, HMDroidGap.class);
                            String params = "?token=" + userInfo.getToken() + "&secretKey=" + userInfo.getSecretKey();
                            String loadUrlStr = "file:///android_asset/www/sg/html/id_validate.html" + params;
                            intent.putExtra("loadUrl", loadUrlStr);
                            startActivity(intent);
                        }
                    }
                    break;
                case R.id.b_additional:
                    if (source.equals("0")) {
                        if (userInfo.getSmToken().equals("")) {
                            G.showToast(AssertDetailActivity.this, "请先在数米开户");
                            new PurchaseTotal(this).ShuMiStart();
                        } else {
                            new PurchaseTotal(this).shuMiPurchase(fundBaseInfo.getFundCode(), fundBaseInfo.getFundName(), fundBaseInfo.getRiskLevel(), fundBaseInfo.getShareType(), String.valueOf(fundBaseInfo.getFundType()));
                        }
                    } else if (source.equals("1")) {
//                        if (yesNo.equals("1")) {
//                            loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/trade/buy";
                        String loadUrlStr = MainActivity.TFurl + "/www/tap/view/index_android.html#/trade/buy";
                        String params  = "prodCode=" + fundBaseInfo.getFundCode() + "&prodSource=" + fundDetail.getProdSource() + "&add=1" + "&name=" + userInfo.getLoginName() + "&token=" + userInfo.getToken() + "&key=" + userInfo.getSecretKey();
                        loadUrlStr = loadUrlStr + "?" + params;
//                        } else if (yesNo.equals("0")) {
//                            G.showToast(ParticularActivity.this, "请先在天风开户");
//                            loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/open-prepare";
//                            params = "/" + userinfo.getLoginName() + "/" + userinfo.getToken() + "/" + userinfo.getSecretKey();
//                            loadUrlStr = loadUrlStr + params;
//                        }
                        intent = new Intent(AssertDetailActivity.this, HMDroidGap.class);
                        intent.putExtra("loadUrl", loadUrlStr);
                        startActivityForResult(intent, RESULT_CANCELED);
                    } else {
                        if (accountStatus.equals("1")) {
                            intent = new Intent(AssertDetailActivity.this, SgBuyActivity.class);
                            intent.putExtra("poisition", 0);
                            intent.putExtra("fundCord", fundCode);
                            if (fundCode.equals("999999999")) {
                                intent.putExtra("fundType", 7);
                                intent.putExtra("fundName", "现金宝");
                            } else {
                                if (fundBaseInfo.getFundType() == 7) {
                                    intent.putExtra("fundType", fundBaseInfo.getFundType());
                                } else {
                                    intent.putExtra("fundType", fundBaseInfo.getInvestmentType());
                                }
                                intent.putExtra("fundName", fundBaseInfo.getFundNameAbbr());
                            }
                            if (G.isEmteny(String.valueOf(fundBaseInfo.getPurchaseLimitMin()))) {
                                intent.putExtra("purchaseLimitMin", 1.0);
                            } else {
                                intent.putExtra("purchaseLimitMin", fundBaseInfo.getPurchaseLimitMin());
                            }
                            startActivity(intent);
                        } else if (accountStatus.equals("0")) {
                            G.showToast(AssertDetailActivity.this, "请先在松果开户");
                            intent = new Intent(AssertDetailActivity.this, HMDroidGap.class);
                            String params = "?token=" + userInfo.getToken() + "&secretKey=" + userInfo.getSecretKey();
                            String loadUrlStr = "file:///android_asset/www/sg/html/id_validate.html" + params;
                            intent.putExtra("loadUrl", loadUrlStr);
                            startActivity(intent);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //天风购买后刷新数据
        if(requestCode==RESULT_CANCELED){
            pagerNumber = 1;
            getTransactionRecord(true);
            getAssetInfomation();
            recordsVo.clear();
        }

    }

    /**
     * 设置各个单元值
     * @param fundDetail 收益对象
     */
    private void setValue(FundDetailListVo fundDetail) {
        if (G.isEmteny(fundDetail.getFundType())) {
            if (fundDetail.getFundCode().equals("999999999")) {
                tvProfitName.setText("万份收益");
                tvParticularName.setText("七日年化率");
                tvPortionName.setText("未付收益");

                tvFundCode.setVisibility(View.GONE);
                ibFundArchives.setVisibility(View.GONE);
                bRedeem.setText("转出");
                bAdditional.setText("转入");

                if (G.isEmteny(fundDetail.getThousandsOfIncome())) {
                    tvProfitValue.setText("0.00");
                } else {
                    tvProfitValue.setText(format2.format(Double.parseDouble(fundDetail.getThousandsOfIncome())));
                }
                if (G.isEmteny(fundDetail.getYieldSevenDay())) {
                    tvParticularValue.setText("0.00");
                } else {
                    tvParticularValue.setText(format1.format(Double.parseDouble(fundDetail.getYieldSevenDay())) + "%");
                }
                if (G.isEmteny(fundDetail.getUnpaidIncome())) {
                    tvPortionValue.setText("0.00");
                    tvPortionValue.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tvPortionValue.setText(format1.format(Double.parseDouble(fundDetail.getUnpaidIncome())));
                    tvPortionValue.setTextColor(getResources().getColor(R.color.red));
                }
            }
        } else {

            if (fundDetail.getFundType().equals("7")) {
                tvProfitName.setText("万份收益");
                tvParticularName.setText("七日年化率");
                tvPortionName.setText("未付收益");
                if (G.isEmteny(fundDetail.getThousandsOfIncome())) {
                    tvProfitValue.setText("0.00");
                } else {
                    tvProfitValue.setText(format2.format(Double.parseDouble(fundDetail.getThousandsOfIncome())));
                }

                if (G.isEmteny(fundDetail.getYieldSevenDay())) {
                    tvParticularValue.setText("0.00");
                } else {
                    tvParticularValue.setText(format1.format(Double.parseDouble(fundDetail.getYieldSevenDay()) * 100) + "%");
                }

                if (G.isEmteny(fundDetail.getUnpaidIncome())) {
                    tvPortionValue.setText("0.00");
                    tvPortionValue.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tvPortionValue.setText(format1.format(Double.parseDouble(fundDetail.getUnpaidIncome())));
                    tvPortionValue.setTextColor(getResources().getColor(R.color.red));
                }
            } else {
                tvProfitName.setText("日涨跌幅");
                tvParticularName.setText("单位净值(元)");
                tvPortionName.setText("份额(份)");
                if (G.isEmteny(fundDetail.getThousandsOfIncome())) {
                    tvProfitValue.setText("0.00");
                    tvProfitValue.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tvProfitValue.setText(format1.format(Double.parseDouble(fundDetail.getThousandsOfIncome()) * 100) + "%");
                    tvProfitValue.setTextColor(getResources().getColor(R.color.red));
                }

                if (G.isEmteny(fundDetail.getYieldSevenDay())) {
                    tvParticularValue.setText("0.00");
                } else {
                    tvParticularValue.setText(format2.format(Double.parseDouble(fundDetail.getYieldSevenDay())));
                }

                if (G.isEmteny(fundDetail.getUnpaidIncome())) {
                    tvPortionValue.setText("0.00");
                } else {
                    tvPortionValue.setText(format1.format(Double.parseDouble(fundDetail.getUnpaidIncome())));
                }
            }
            tvFundCode.setVisibility(View.VISIBLE);
            ibFundArchives.setVisibility(View.VISIBLE);
            tvFundCode.setText(fundCode);
            bRedeem.setText("赎回");
            bAdditional.setText("追加");
        }

        if (G.isEmteny(fundDetail.getTotalIncome())) {
            tvPositionGains.setText("0.00");
        } else {
            tvPositionGains.setText(format1.format(Double.parseDouble(fundDetail.getTotalIncome())));
        }

        tvHoldMoney.setText(format1.format(Double.parseDouble(fundDetail.getAssets())));
        source = fundDetail.getSource();
        tvFundName.setText(fundDetail.getFundName());

        if (fundCode.equals("999999999")) {
            tvFundCode.setVisibility(View.GONE);
            ibFundArchives.setVisibility(View.GONE);
            bRedeem.setText("转出");
            bAdditional.setText("转入");
        } else {
            tvFundCode.setVisibility(View.VISIBLE);
            ibFundArchives.setVisibility(View.VISIBLE);
            tvFundCode.setText(fundCode);
            bRedeem.setText("赎回");
            bAdditional.setText("追加");
        }

    }

    /**
     * 获取单笔基金交易记录
     * @param isShow 是否显示加载提示框
     */
    public void getTransactionRecord(boolean isShow) {
        if (isShow) {
            mHandler.sendEmptyMessage(0x11);
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("token", userInfo.getToken());
        params.put("fundCode", fundCode);
        params.put("source", source);
        params.put("pageNumber", String.valueOf(pagerNumber));
        params.put("pageSize", String.valueOf(PAGERSIZE));
        if (!source.equals("0") || G.isEmteny(fundDetail.getTradeAccount())) {
            params.put("tradeAccount", "");
        } else {
            params.put("tradeAccount", fundDetail.getTradeAccount());
        }
        OkHttpUtil.sendPost(ApiUri.TRANSATION, params, this);
    }

    /**
     * 获取单一基金信息
     */
    private void getFundInfo() {
        if (fundCode.equals("999999999")) {
            return;
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("secretKey", userInfo.getSecretKey());
            params.put("token", userInfo.getToken());
            params.put("fundCode", fundCode);
            OkHttpUtil.sendPost(ApiUri.SOLE_FUND, params, this);
        }
    }

    /**
     * 获取用户是否开过户
     * @param source 账户来源
     */
    private void getAccountStatus(String source) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("token", userInfo.getToken());
        params.put("source", source);
        OkHttpUtil.sendPost(ApiUri.FUND_REGISTER, params, this);
    }

    /**
     * 获取基金资产信息
     */
    private void getAssetInfomation() {
        Map<String, String> params = new HashMap<>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("token", userInfo.getToken());
        OkHttpUtil.sendPost(ApiUri.TOTAL_DETAIL, params, this);
    }


    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.TRANSATION)) {
                    mNetworkError.setShowType(false);
                    mNetworkError.stopAnimation();
                    loadingDialog.dismiss();
                    TransactionRecordsListVo recordsListVo = gson.fromJson(result, TransactionRecordsListVo.class);
                    setTransactionRecord(recordsListVo);
                } else if (uri.equals(ApiUri.SOLE_FUND)) {
                    FundCodeVo fundCodeVo = gson.fromJson(result, FundCodeVo.class);
                    fundBaseInfo = fundCodeVo.getDate().getFundBaseInfo();
                } else if (uri.equals(ApiUri.FUND_REGISTER)) {
                    MenberCent mc = gson.fromJson(result, MenberCent.class);
                    accountStatus = mc.getResultCode();
                } else if (uri.equals(ApiUri.TOTAL_DETAIL)) {
                    FundDetailVo fundDetailVo = gson.fromJson(result, FundDetailVo.class);
                    for (FundDetailListVo d : fundDetailVo.getDate()) {
                        if (d.getFundCode().equals(fundCode)) {
                            tvHoldMoney.setText(format1.format(Double.parseDouble(d.getAssets())));
                            return;
                        }
                        return;
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
                if (uri.equals(ApiUri.TRAdEDCCOUNT)) {

                } else if (uri.equals(ApiUri.SOLE_FUND)) {

                } else if (uri.equals(ApiUri.FUND_REGISTER)) {

                } else if (uri.equals(ApiUri.TRANSATION)) {
                    loadingDialog.dismiss();
                    mNetworkError.stopAnimation();
                    G.showToast(AssertDetailActivity.this, "您的网络似乎出现了一些问题...");
                    if (pagerNumber > 1) {
                        pagerNumber--;
                    }
                    if (recordsVo.size() <= 0) {
                        mNetworkError.setShowType(true);
                        mNetworkError.setPicImagerViewShowType(true);
                        lmlvcDownRefresh.loadMoreFinish(false, false);
                    } else {
                        mNetworkError.setShowType(false);
                        lmlvcDownRefresh.loadMoreFinish(true, true);
                    }
                } else if (uri.equals(ApiUri.TOTAL_DETAIL)) {
                    G.showToast(AssertDetailActivity.this, "您的网络似乎出现了一些问题...");
                }
            }
        });
    }

    /**
     * 设置交易记录
     */
    private void setTransactionRecord(TransactionRecordsListVo response) {
        if (response.getResultDesc() != null && response.getResultDesc().equals("非法访问")) {
            dialog.show();
            isNotBuy = true;
        } else {
            if (null == response.getDate() || response.getDate().size() <= 0) {
                if (recordsVo.size() <= 0) {
                    G.showToast(AssertDetailActivity.this, "暂无数据");
                    lmlvcDownRefresh.loadMoreFinish(false, false);
                    mNetworkError.setPicImagerViewShowType(false);
                    mNetworkError.setShowType(true);
                } else {
                    G.showToast(AssertDetailActivity.this, "暂无更多数据");
                    lmlvcDownRefresh.loadMoreFinish(true, true);
                }
            } else {
                mNetworkError.setShowType(false);
                lmlvcDownRefresh.loadMoreFinish(true, true);
                if (G.ISPURCHASE) {
                    recordsVo.clear();
                    G.ISPURCHASE = false;
                }
                for (TransactionRecordsVo re : response.getDate()) {
                    recordsVo.add(re);
                }
                particularAdapter.notifyDataSetChanged();
            }
        }

    }

}
