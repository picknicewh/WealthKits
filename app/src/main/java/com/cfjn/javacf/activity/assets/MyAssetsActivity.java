package com.cfjn.javacf.activity.assets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.LoginActivity;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.activity.member.MemberCenterActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.adapter.CommonAdapter;
import com.cfjn.javacf.adapter.ViewHolder;
import com.cfjn.javacf.modle.FundDetailListVo;
import com.cfjn.javacf.modle.FundDetailVo;
import com.cfjn.javacf.modle.MyAnalysisVo;
import com.cfjn.javacf.modle.MyFundListVo;
import com.cfjn.javacf.modle.MyFundVo;
import com.cfjn.javacf.modle.TFVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LoadingDialog;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.ScrollableHelper;
import com.cfjn.javacf.widget.ScrollableLayout;
import com.google.gson.Gson;
import com.openhunme.cordova.activity.HMDroidGap;

import java.io.Serializable;
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
 *  时间： 2016-5-31
 *  名称： 我的资产
 *  版本说明：代码规范整改
 *  附加注释：1.显示用户昨日收益和基金明细
 *            2.每次打开APP需要请求服务器是否提交数据
 *            3.用户登录成功后等待5秒刷新数据
 *  主要接口：1.获取资产分析
 *            2.获取我的资产
 *            3.获取我的资产明细
 *            4.天风是否需要登录
 */
public class MyAssetsActivity extends Activity implements View.OnClickListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 标题
     */
    @Bind(R.id.tv_tilte)
    TextView tvTilte;
    /**
     * 总资产
     */
    @Bind(R.id.tv_total_assets)
    TextView tvTotalAssets;
    /**
     * 昨日收益
     */
    @Bind(R.id.tv_yesterday_income)
    TextView tvYesterdayIncome;
    /**
     * 收益超越率
     */
    @Bind(R.id.tv_yesterday_rate)
    TextView tvYesterdayRate;
    /**
     * 持仓收益
     */
    @Bind(R.id.tv_position_gains)
    TextView tvPositionGains;
    /**
     * 未付收益
     */
    @Bind(R.id.tv_unpaid_income)
    TextView tvUnpaidIncome;
    /**
     * 用户资产
     */
    @Bind(R.id.lv_assets)
    ListView lvAssets;
    /**
     * 无网络布局
     */
    @Bind(R.id.rl_notwork)
    RelativeLayout rlNotwork;
    /**
     * 支持某个控件滑到顶部不隐藏的srollview
     */
    @Bind(R.id.sl_scroll)
    ScrollableLayout slScroll;

    /**
     * 无资产提示
     */
    private View footView;

    /**
     * 加载弹框
     */
    private LoadingDialog dialog;

    /**
     * 无资产页面
     */
    private LinearLayout llNotAssets;

   //-------------逻辑变量-------------------
    /**
     * 昨日收益值
     */
    private double yesterdayIncome;

    /**
     * 超越白分比
     */
    private String percent;

    /**
     *  我的资产
     */
    public static String totalAssets;

    /**
     * 持仓收益值
     */
    private double positionGains;

    /**
     * 未付收益值
     */
    private double unpaidIncome;

    //接收值
    private List<FundDetailListVo> DateailList;
    private CommonAdapter<FundDetailListVo> AssetsAdapter;
    private MyFundVo fundVo;
    private MyAnalysisVo analysisVo;
    private UserInfo userInfo;
    private DecimalFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myassets);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();
    }

    private void initializeView() {
        footView = LayoutInflater.from(this).inflate(R.layout.fund_footerlistview, null);
        llNotAssets = (LinearLayout) footView.findViewById(R.id.my_total_linear);
        lvAssets.addFooterView(footView);
        dialog = LoadingDialog.regstLoading(MyAssetsActivity.this);
        dialog.show();
        slScroll.getHelper().setCurrentScrollableContainer(new ScrollableHelper.ScrollableContainer() {
            @Override
            public View getScrollableView() {
                return lvAssets;
            }
        });
        slScroll.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                if (slScroll.isSticked()) {
                    tvTilte.setText("我的资产");
                } else {
                    tvTilte.setText("昨日收益");
                }
            }
        });

    }

    private void initializeMode() {
        userInfo = UserInfo.getInstance(this);
        analysisVo = new MyAnalysisVo();
        format = new DecimalFormat("#0.00");
        DateailList = new ArrayList<>();
    }

    private void initializeControl(){
        //用户登录时候查询是否提交数据
        if (LoginActivity.isOK == true) {
            initializationDate();
        } else {
            //只查询一次天风登录
            if (!G.isTFlogin) {
                getTFloginType();
                G.isTFlogin =true;
            }
            getMyAssets();
            getAssetsDetail();
            getAssetsAnalysis();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeControl();
    }

    /**
     * 设置资产明细listview值
     * @param listItem 每个资产对象
     */
    private void setAssetsList(final List<FundDetailListVo> listItem) {
        //当值不为空时，将值填入item，若为空，显示listview的footerview
        if (listItem.size() > 0) {
            llNotAssets.setVisibility(View.GONE);
            AssetsAdapter = new CommonAdapter<FundDetailListVo>(MyAssetsActivity.this, listItem, R.layout.item_my_fund) {
                @Override
                public void convert(ViewHolder helper, FundDetailListVo item, int position) {
                    helper.setText(R.id.tv_fund_name, item.getFundName());

                    if (G.isEmteny(item.getAssets())) {
                        helper.setText(R.id.tv_asstes, "0.00");
                    } else {
                        helper.setText(R.id.tv_asstes, format.format(Double.parseDouble(item.getAssets())));
                    }

                    if (G.isEmteny(item.getYesterdayIncome())) {
                        helper.setText(R.id.tv_profit, "0.00");
                    } else {
                        if (item.getYesterdayIncome().equals("暂无收益")) {
                            helper.setText(R.id.tv_profit, "0.00");
                        } else {
                            helper.setText(R.id.tv_profit, format.format(Double.parseDouble(item.getYesterdayIncome())));
                            if (Double.parseDouble(item.getYesterdayIncome()) < 0) {
                                helper.setTextColor(R.id.tv_profit, getResources().getColor(R.color.green));
                            } else {
                                helper.setTextColor(R.id.tv_profit, getResources().getColor(R.color.red));
                            }
                        }
                    }

                    if (item.getSource().equals("0")) {
                        helper.setImageResource(R.id.iv_fund_logo, R.drawable.ic_shumi_logo);
                        helper.setText(R.id.tv_counter, "数米基金");
                    } else if (item.getSource().equals("1")) {
                        helper.setImageResource(R.id.iv_fund_logo, R.drawable.ic_tf_logo);
                        helper.setText(R.id.tv_counter, "天风证券");
                    } else {
                        helper.setImageResource(R.id.iv_fund_logo, R.drawable.ic_sg_logo);
                        helper.setText(R.id.tv_counter, "松果基金");
                    }

                    RelativeLayout relativeLayout = helper.getView(R.id.rl_my_fund);
                    relativeLayout.setVisibility(View.GONE);
                    View view = helper.getView(R.id.view_line);
                    view.setVisibility(View.GONE);
                }
            };
        } else {
            llNotAssets.setVisibility(View.VISIBLE);
        }
        lvAssets.setAdapter(AssetsAdapter);
        lvAssets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listItem.size() > 0) {
//                    index = position;
                    Intent intent = new Intent(MyAssetsActivity.this, AssertDetailActivity.class);
                    intent.putExtra("fund", "1");
//                    intent.putExtra("fundIndex", index);
//                    intent.putExtra("fundDetail", (Serializable) listitem.get(position));
                    intent.putExtra("fundDetail", listItem.get(position));
                    startActivity(intent);
                } else {
                    G.showToast(MyAssetsActivity.this, "您还未购买基金");
                }

            }
        });
    }

    /**
     * 点击事件
     * 会员中心  交易记录  资产分析
     * @param view
     */
    @OnClick({R.id.ib_member_center, R.id.ib_trading_records, R.id.ib_assets_analysis})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ib_assets_analysis:
                intent = new Intent(this, AssetanalysisActivity.class);
                if(null==analysisVo.getDate()||null==analysisVo.getDate().getList()||analysisVo.getDate().getList().size()<=0){
                    G.showToast(this,"暂无资产分析...");
                    return;
                }
                intent.putExtra("analysis", (Serializable) analysisVo.getDate().getList());
                break;
            case R.id.ib_trading_records:
                intent = new Intent(this, TransactionRecordActivity.class);
                break;
            case R.id.ib_member_center:
                intent=new Intent(this, MemberCenterActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 设置昨日收益的值
     */
    public void setYesterdayValue() {
        tvYesterdayIncome.setText(format.format(yesterdayIncome));
        tvTotalAssets.setText(format.format(Double.parseDouble(totalAssets)));
        tvPositionGains.setText(format.format(positionGains));
        tvUnpaidIncome.setText(format.format(unpaidIncome));
        tvYesterdayRate.setText("收益超过了全国" + percent + "%的用户");
        tvPositionGains.setTextColor(getResources().getColor(R.color.red));
    }

    /**
     * 用户登录后刷新5秒，5秒后开始加载我的资产数据，服务器初始化数据最多5秒
     */
    private void initializationDate() {
        final LoadingDialog dialog = new LoadingDialog(this, R.style.myDialogTheme, 0);
        dialog.show();
        lvAssets.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                getMyAssets();
                getAssetsDetail();
                getAssetsAnalysis();
                getTFloginType();
                //初始化状态
                G.isTFlogin = true;
                LoginActivity.isOK = false;
            }
        }, 5000);
    }


    /**
     * 天风登录
     */
    private void TFlogin() {
        Intent intent = new Intent(this, HMDroidGap.class);
        String params = userInfo.getLoginName() + "/" + userInfo.getToken() + "/" + userInfo.getSecretKey();
//        String loadUrlStr = "file:///android_asset/www/tap_1/view/index_android.html#/tab/login_android/" + params;
        String loadUrlStr = MainActivity.TFurl + "/www/tap_1/view/index_android.html#/tab/login_android/" + params;
        intent.putExtra("loadUrl", loadUrlStr);
        startActivityForResult(intent, 0);
    }


    /**
     * 获取资产分析的数据
     */
    public void getAssetsAnalysis() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("token", userInfo.getToken());
        OkHttpUtil.sendPost(ApiUri.ANALYSIS, params, this);
    }

    /**
     * 获取我的资产
     */
    private void getMyAssets() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("token", userInfo.getToken());
        OkHttpUtil.sendPost(ApiUri.MY_TOTAL, params, this,0,"MY_TOTAL");
    }

    /**
     * 获取我的资产明细
     */
    public void getAssetsDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("token", userInfo.getToken());
        OkHttpUtil.sendPost(ApiUri.TOTAL_DETAIL, params, this,0,"TOTAL_DETALL");
    }


    /**
     * 判断天风是否需要登录
     */
    private void getTFloginType() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        OkHttpUtil.sendPost(ApiUri.TFLOGIN, params, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.TOTAL_DETAIL)) {
                    rlNotwork.setVisibility(View.GONE);
                    dialog.dismiss();
                    FundDetailVo fundDetailVo = gson.fromJson(result, FundDetailVo.class);
                    if (fundDetailVo.getResultDesc() != null && fundDetailVo.getResultDesc().equals("非法访问")) {
                       new LoadingDialog(MyAssetsActivity.this, R.style.myDialogTheme, 1).show();
                    } else {
                        if (null==fundDetailVo.getDate()||fundDetailVo.getDate().size()<=0) {
                            G.showToast(MyAssetsActivity.this, "暂无数据");
                        } else {
                            DateailList.clear();
                            DateailList.addAll(fundDetailVo.getDate());
                            setAssetsList(DateailList);
                            lvAssets.setVisibility(View.VISIBLE);
                        }
                    }
                    // Log.i("TAG","TOTAL_DETAIL");
                } else if (uri.equals(ApiUri.ANALYSIS)) {
                    analysisVo = gson.fromJson(result, MyAnalysisVo.class);;
                    // Log.i("TAG","ANALYSIS");
                } else if (uri.equals(ApiUri.MY_TOTAL)) {
                    //   Log.i("TAG","MY_TOTAL");
                    MyFundListVo myFundListVo = gson.fromJson(result, MyFundListVo.class);{
                        if (myFundListVo.getResultDesc() != null && myFundListVo.getResultDesc().equals("非法访问")) {
                            new LoadingDialog(MyAssetsActivity.this, R.style.myDialogTheme, 1).show();
                        } else {
                            if (myFundListVo.getDate() == null || myFundListVo.getDate().equals("") || myFundListVo.getDate().equals("null")) {
                                G.showToast(MyAssetsActivity.this, "暂无数据");
                            } else {
                                fundVo = myFundListVo.getDate();
                                String yesterDayIncome = fundVo.getYesterdayIncome();
                                if (!G.isEmteny(yesterDayIncome)) {
                                    yesterdayIncome = Double.parseDouble(yesterDayIncome);
                                } else {
                                    yesterdayIncome = Double.parseDouble(format.format(0));
                                }

                                String totalAssets = fundVo.getTotalAssets();
                                if (!G.isEmteny(totalAssets)) {
                                    MyAssetsActivity.totalAssets = totalAssets;
                                } else {
                                    MyAssetsActivity.totalAssets =format.format(0);
                                }

                                String nationaIncome = fundVo.getNationalIncomeOverRanking();
                                if (!G.isEmteny(nationaIncome)) {
                                    percent = format.format(Double.parseDouble(nationaIncome));
                                } else {
                                    percent = "0.00%";
                                }

                                String accumulated = fundVo.getAccumulatedIncome();
                                if (!G.isEmteny(accumulated)) {
                                    positionGains = Double.parseDouble(accumulated);
                                } else {
                                    positionGains = Double.parseDouble(format.format(0));
                                }

                                String unpaidIncome = fundVo.getUnpaidIncome();
                                if (!G.isEmteny(unpaidIncome)) {
                                    if (unpaidIncome.length() > 10) {
                                        MyAssetsActivity.this.unpaidIncome = Double.parseDouble(unpaidIncome.substring(0, unpaidIncome.length() - 4));
                                    }
                                } else {
                                    MyAssetsActivity.this.unpaidIncome = Double.parseDouble(format.format(0));
                                }
                                setYesterdayValue();
                                //ininPopupWindow(yesterday);
                            }
                        }
                    }

                } else if (uri.equals(ApiUri.TFLOGIN)) {
                    TFVo tfVo = gson.fromJson(result, TFVo.class);
                    if (tfVo.getResultCode().equals("0")) {
                        //等于0需要登录
                        TFlogin();
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
                if (uri.equals(ApiUri.ANALYSIS)) {

                } else if (uri.equals(ApiUri.TOTAL_DETAIL)) {
                    //无网络处理
                    dialog.dismiss();
                    G.showToast(MyAssetsActivity.this, getResources().getString(R.string.notWorkPrompt));
//                    rlNotwork.setVisibility(View.VISIBLE);
//                    rlNotwork.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            rlNotwork.setVisibility(View.GONE);
//                            dialog.show();
//                            getAssetsDetail();
//                            getMyAssets();
//                            getAssetsAnalysis();
//                        }
//                    });
                } else if (uri.equals(ApiUri.MY_TOTAL)) {

                }
            }
        });
    }

}
