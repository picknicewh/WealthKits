package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.activity.fund.FundDetailActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.adapter.member.SearchAdapter;
import com.cfjn.javacf.modle.FundListVo;
import com.cfjn.javacf.modle.FundobjectVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LoadMoreFooterView;
import com.cfjn.javacf.widget.LoadingDialog;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.util.ThreadPool;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 基金搜索
 * 版本说明：代码规范整改
 * 附加注释：通过简拼和基金代码索搜基金，实现自动搜索
 * 主要接口：获取基金搜索内容
 */
public class SearchActivity extends Activity implements OKHttpListener {
    /**
     * 输入引索
     */
    @Bind(R.id.et_value)
    EditText etValue;
    /**
     * 无网络显示
     */
    @Bind(R.id.ll_notNetwork)
    RelativeLayout llNotNetwork;
    /**
     * 搜索结果显示ListView
     */
    @Bind(R.id.lv_search)
    ListView lvSearch;
    /**
     * 下拉加载提示
     */
    @Bind(R.id.lmlvc_down_refresh)
    LoadMoreListViewContainer lmlvcDownRefresh;
    /**
     * 加载中提示
     */
    private LoadingDialog dialog;

    private List<Map<String, String>> mapList;
    /**
     * 计时器
     */
    private Timer timer;
    private SearchAdapter searchAdapter;
    private Boolean VISIT = true;
    //每页加载的数量
    private final int PAGESIZE = 30;
    //输入框的文字
    private String searchText;
    //页码
    private int pageNumber = 1;
    private boolean isPullRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        dialog = LoadingDialog.regstLoading(this);
        mapList = new ArrayList<>();
        setSearchContent();

        //无网络点击再次获取数据 重新加载
        llNotNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = etValue.getText().toString().trim();
                if (G.isEmteny(searchText)) {
                    return;
                }
                llNotNetwork.setVisibility(View.GONE);
                handler.sendEmptyMessage(0x14);
                //初始化页数
                pageNumber = 1;
                ThreadPool.fixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        getSearchContent();
                        VISIT = true;
                    }
                });
            }
        });

        //上拉加载
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

                        isPullRefresh=true;
                        pageNumber++;
                        getSearchContent();
                    }
                }, 500);
            }
        });
        lmlvcDownRefresh.loadMoreFinish(false, false);
    }

    private void setSearchContent() {
        searchAdapter = new SearchAdapter(mapList, SearchActivity.this);
        if (getIntent().getIntExtra("showType", 0) == 0) {
            lvSearch.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_fundsupermarket, null));
        }
        lvSearch.setAdapter(searchAdapter);

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (getIntent().getIntExtra("showType", 0) == 0 && position == 0) {
                    intent = new Intent(SearchActivity.this, MainActivity.class);
                    intent.putExtra("tab", 1);
                    intent.putExtra("showType", 2);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    if (getIntent().getIntExtra("showType", 0) == 1) {
//                        intent.putExtra("fundCode", fundBaselist.get(position).getFundCode());
                        intent.putExtra("fundCode", mapList.get(position).get("fundCode"));
                    } else {
//                        intent.putExtra("fundCode", fundBaselist.get(position - 1).getFundCode());
                        intent.putExtra("fundCode", mapList.get(position-1).get("fundCode"));
                    }
                    intent.putExtra("position", 0);
                    intent.setClass(SearchActivity.this, FundDetailActivity.class);
                    startActivity(intent);
                }
            }
        });
        //监听输入状态，每次输入都开启计时，输入一次将上次计时关闭开启新的计时，这样保证输入完后计时
        //也是最后开启的，但计时达到一定值时自动向服务端请求数据
        //同时清空列表数据，刷新适配器
        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //之前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //改变中

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (VISIT) {
                    VISIT = false;
                    setTrimer();
                } else {
                    timer.cancel();
                    setTrimer();
                }
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x12) {
                //数据获取完毕 刷新适配器
                dialog.dismiss();
                llNotNetwork.setVisibility(View.GONE);
                searchAdapter.notifyDataSetChanged();
            } else if (msg.what == 0x14) {
                //显示加载提示框  有些在异步操作中 所以用handle
                dialog.show();
            } else if (msg.what == 0x33) {
                //无网络 或者数据获取异常显示  点击图标可以再次获取数据 之前的数据全部清空 并将pageNmber减一 因为值没有获取到需要再次获取
                dialog.dismiss();
                llNotNetwork.setVisibility(View.VISIBLE);
                searchAdapter.upadater();
                mapList.clear();
                searchAdapter.notifyDataSetChanged();
                pageNumber--;
            }
        }
    };

    /**
     * 输入间隔时间
     */
    private void setTrimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (G.isEmteny(etValue.getText().toString().trim())) {
                    return;
                }
                searchText = etValue.getText().toString().trim();
                handler.sendEmptyMessage(0x14);
                //初始化页数
                pageNumber = 1;
                getSearchContent();
                VISIT = true;
            }
        };
        timer.schedule(task, 800);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchText = etValue.getText().toString().trim();
        if (G.isEmteny(searchText)) {
            return;
        }
        handler.sendEmptyMessage(0x14);
        //初始化页数
        pageNumber = 1;
        getSearchContent();
        VISIT = true;
    }

    /**
     * 获取搜索数据
     */
    private void getSearchContent() {
        Map<String, String> params = new HashMap<>();
        params.put("key", searchText);
        params.put("pageSize", PAGESIZE + "");
        params.put("pageNumber", pageNumber + "");
        params.put("secretKey", UserInfo.getInstance(this).getSecretKey());
        OkHttpUtil.sendPost(ApiUri.QUERY_FUND, params, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.QUERY_FUND)) {
                    try {
                        dialog.dismiss();
                        FundobjectVo fund = new Gson().fromJson(result, FundobjectVo.class);
                        if (null == fund.getDate() || fund.getDate().size() <= 0) {
                            if (mapList.size() > 0) {
                                G.showToast(SearchActivity.this, "暂无更多基金");
                                lmlvcDownRefresh.loadMoreFinish(true, true);
                            } else {
                                G.showToast(SearchActivity.this, "暂无相关基金");
                                lmlvcDownRefresh.loadMoreFinish(false, false);
                            }
                            return;
                        }
                        lmlvcDownRefresh.loadMoreFinish(true, true);
                        setFundData(fund);
                    } catch (Exception e) {
                        handler.sendEmptyMessage(0x33);
                    }
                }
            }
        });
    }

    private void setFundData(FundobjectVo fund) {
        if(!isPullRefresh){
            mapList.clear();
        }else {
            isPullRefresh=false;
        }
        for (int i = 0; i < fund.getDate().size(); i++) {
            for (int j = 0; j < fund.getDate().get(i).getFundProductList().size(); j++) {

                FundListVo fundListVo = fund.getDate().get(i).getFundProductList().get(j);
                Map<String, String> map = new HashMap<>();
                map.put("fundNameAbbr", fundListVo.getFundBaseInfo().getFundNameAbbr());
                map.put("fundCode", fundListVo.getFundBaseInfo().getFundCode());
                if (fundListVo.getFundBaseInfo().getFundType() == 7) {
                    map.put("fundType", fundListVo.getFundBaseInfo().getFundType() + "");
                } else {
                    map.put("fundType", fundListVo.getFundBaseInfo().getInvestmentType() + "");
                }
                map.put("fundTypeName", fund.getDate().get(i).getFundTypeName());
                map.put("fundName", fundListVo.getFundBaseInfo().getFundName());
                map.put("attention", fundListVo.getAttention() + "");
                mapList.add(map);
            }
        }
        handler.sendEmptyMessage(0x12);
    }

    @Override
    public void onError(String uri, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x33);
                lmlvcDownRefresh.loadMoreFinish(false, false);
            }
        });

    }

    @OnClick({R.id.ib_empty, R.id.b_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_empty:
                etValue.setText("");
                break;
            case R.id.b_cancel:
                finish();
                break;
        }
    }
}
