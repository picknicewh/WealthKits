package com.cfjn.javacf.activity.assets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.ExpandAdapter;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.shumi.MyShumiSdkDataBridge;
import com.cfjn.javacf.modle.AllTransactionRecordsDate;
import com.cfjn.javacf.modle.AllTransactionRecordsListVo;
import com.cfjn.javacf.modle.TransactionRecordsVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.LoadingDialog;
import com.cfjn.javacf.widget.NetworkError;
import com.cfjn.javacf.widget.PullToRefreshLayout;
import com.cfjn.javacf.widget.PullableExpandableListView;
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
 *  时间： 2016-5-31
 *  名称： 交易记录
 *  版本说明：代码规范整改
 *  附加注释：1.支持下拉刷新，上拉加载（分页加载）PullToRefreshLayout
 *  主要接口：1.获取交易记录
 */
public class TransactionRecordActivity extends Activity implements OKHttpListener {
    //-----------布局变量--------------
    /**
     * 交易记录二级列表
     */
    @Bind(R.id.lv_expandable)
    PullableExpandableListView lvExpandable;
    /**
     * 上拉 下拉刷新控件
     */
    @Bind(R.id.pull_refresh)
    PullToRefreshLayout pullRefresh;
    /**
     * 页面容器
     */
    @Bind(R.id.ll_transaction)
    LinearLayout llTransaction;
    /**
     * 无网络或者无数据加载也页面
     */
    private NetworkError networkError;
    /**
     * 加载提示框
     */
    private LoadingDialog dialog;

    //-------------逻辑变量-------------------
    /**
     * 二级列表标题list
     */
    private List<String> titleList;
    /**
     * 二级列表内容list
     */
    private List<List<TransactionRecordsVo>> contentList;
    /**
     * 二级列表适配器
     */
    private ExpandAdapter expandAdapter;
    /**
     * 分页加载页码
     */
    private int pageNumber = 1;
    /**
     * 分页加载数目
     */
    private int pageSize = 30;

    private List<AllTransactionRecordsListVo> allList;
    /**
     * 确认中
     */
    private List<TransactionRecordsVo> confirmingList;
    /**
     * 已确认
     */
    private List<TransactionRecordsVo> confirmedList;
    private int type;
    /**
     * 是否是刷新状态
     */
    private boolean isRefresh = false;

    private boolean isCount = false;

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                expandAdapter.notifyDataSetChanged();
                if (isCount == false) {
                    int groupCount = lvExpandable.getCount();
                    for (int i = 0; i < groupCount; i++) {
                        lvExpandable.expandGroup(i);
                    }
                    isCount = true;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();
        initializeControl();
    }

    private void initializeMode(){
        titleList = new ArrayList<>();
        contentList = new ArrayList<>();
        allList = new ArrayList<>();
        confirmingList = new ArrayList<>();
        confirmedList = new ArrayList<>();
        networkError = NetworkError.getInstance(this);
        dialog = LoadingDialog.regstLoading(this);
    }

    private void initializeView(){
        dialog.show();
        expandAdapter = new ExpandAdapter(titleList, contentList, TransactionRecordActivity.this);
        lvExpandable.setAdapter(expandAdapter);
        //添加网络数据转状态页面
        llTransaction.addView(networkError.getView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //设置显示状态
        networkError.setShowType(false);
    }

    private void initializeControl(){
        //设置默认展开
        lvExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        lvExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(TransactionRecordActivity.this, TransactionRecordDetailActivity.class);
                intent.putExtra("allTransation", contentList.get(groupPosition).get(childPosition));
                startActivity(intent);
                return false;
            }
        });

        pullRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                isRefresh = true;
                // 下拉刷新操作 延时2秒
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        pageNumber = 1;
                        getTransactions();
                        // 通知告诉控件刷新完毕！ 刷新控件
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 500);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                isRefresh = false;
                // 上拉加载操作 延时2秒
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        allList.clear();
                        confirmingList.clear();
                        pageNumber++;
                        getTransactions();
                        // 告诉控件加载完毕！刷新控件
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 500);

            }
        });

        //当显示点击图片重新获取数据
        networkError.setPicImagerViewOnClockListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkError.startAnimation();
                pageNumber = 1;
                getTransactions();
            }
        });
        getTransactions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyShumiSdkDataBridge.chedan == true || G.ISOUTORDEER == true) {
            getTransactions();
            MyShumiSdkDataBridge.chedan = false;
            G.ISOUTORDEER = false;
        }
    }

    /**
     * 获取交易记录
     */
    public void getTransactions() {
        Map<String, String> params = new HashMap<>();
        params.put("token", UserInfo.getInstance(this).getToken());
        params.put("secretKey", UserInfo.getInstance(this).getSecretKey());
        params.put("pageNumber", String.valueOf(pageNumber));
        params.put("pageSize", String.valueOf(pageSize));
        OkHttpUtil.sendPost(ApiUri.ALLTRANSATION, params, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.ALLTRANSATION)) {
                    dialog.dismiss();
                    networkError.stopAnimation();
                    pullRefresh.setVisibility(View.VISIBLE);
                    AllTransactionRecordsDate recordsDate = gson.fromJson(result, AllTransactionRecordsDate.class);
                    if (recordsDate.getResultDesc() != null && recordsDate.getResultDesc().equals("非法访问")) {
                        new LoadingDialog(TransactionRecordActivity.this, R.style.myDialogTheme, 1).show();
                    } else {
                        allList = recordsDate.getDate();
//                        if (allList == null || allList.get(1).getList().size() <= 0) {
//                            if(confirmedList.size()==0){
//                                pageNumber = pageNumber == 1 ? pageNumber : pageNumber--;
//                                networkError.setShowType(true);
//                                networkError.setPicImagerViewShowType(false);
//                                networkError.setPromptText("您暂时没有交易记录");
//                                pullRefresh.setVisibility(View.GONE);
//                            }else{
//                               G.showToast(TransactionActivity.this,"没有更多了...");
//                            }
//                        } else {
                            titleList.clear();
                            networkError.setShowType(false);
                            pullRefresh.setVisibility(View.VISIBLE);
                            for (int i = 0; i < allList.size(); i++) {
                                type = allList.get(i).getType();
                                if (type == 1) {
                                    confirmingList.clear();
                                    contentList.clear();
                                    titleList.add("确认中");
                                    confirmingList.addAll(allList.get(i).getList());
                                    contentList.add(confirmingList);
                                } else {
                                    if (isRefresh == true) {
                                        confirmedList.clear();
                                    }
                                    titleList.add("已确认");
                                    confirmedList.addAll(allList.get(i).getList());
                                    contentList.add(confirmedList);
                                }
                            }

//                            只要判断 2个 confirmedListVo（无论是否有数据只都会有2个） 是个是空的list
//                            没有交易记录时候显示财神
                            if (contentList.get(0).size() <= 0 &&contentList.get(1).size()<=0) {
                                pageNumber = pageNumber == 1 ? pageNumber : pageNumber--;
                                networkError.setShowType(true);
                                networkError.setPicImagerViewShowType(false);
                                networkError.setPromptText("您暂时没有交易记录");
                                pullRefresh.setVisibility(View.GONE);
                            }
                            if(allList.get(1).getList().size()<=0&&confirmedList.size()>0){
                                G.showToast(TransactionRecordActivity.this,"没有更多了...");
                            }
                            hand.sendEmptyMessage(0x11);
                        }

//                    }
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.ALLTRANSATION)) {
//                    stop();
//                    b_det_nonet.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    networkError.stopAnimation();
                    pullRefresh.setVisibility(View.GONE);
                    pageNumber = pageNumber == 1 ? pageNumber : pageNumber--;
                    networkError.setShowType(true);
                    networkError.setPicImagerViewShowType(true);
                    networkError.setPromptText("加载失败，点击屏幕重试！");
                }
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }

}
