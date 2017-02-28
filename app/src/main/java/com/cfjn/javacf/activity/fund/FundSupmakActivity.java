package com.cfjn.javacf.activity.fund;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.LoginActivity;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.activity.WebViewActivity;
import com.cfjn.javacf.activity.member.MemberCenterActivity;
import com.cfjn.javacf.activity.member.SearchActivity;
import com.cfjn.javacf.adapter.SimplePagerAdapter;
import com.cfjn.javacf.adapter.fund.MyFundApapter;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.base.UserSyntion;
import com.cfjn.javacf.modle.AdvertisingChart;
import com.cfjn.javacf.modle.FundListVo;
import com.cfjn.javacf.modle.FundVo;
import com.cfjn.javacf.modle.FundobjectVo;
import com.cfjn.javacf.modle.RecommenVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.DateUtil;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.InitImageLoader;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.BannerViewPager;
import com.cfjn.javacf.widget.LoadMoreFooterView;
import com.cfjn.javacf.widget.ScrollableHelper;
import com.cfjn.javacf.widget.ScrollableLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
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
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 *  作者： zll
 *  时间： 2016-5-27
 *  名称： 基金理财
 *  版本说明：代码规范整改
 *  附加注释：支持下拉刷新，下拉加载，广告图自动轮播（间隔5秒）
 *  主要接口：1.获取所有基金信息
 *            2.获取广告图
 */
public class FundSupmakActivity extends Activity implements View.OnClickListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 顶部广告图
     */
    @Bind(R.id.bvp_advertisement)
    BannerViewPager bvpAdvertisement;
    /**
     * 广告图容器
     */
    @Bind(R.id.ll_advertisement)
    LinearLayout llAdvertisement;
    /**
     * 基金排序状态图标
     */
    @Bind(R.id.iv_sort_type)
    ImageView ivSortType;
    /**
     * 基金list
     */
    @Bind(R.id.lv_fund)
    ListView lvFund;
    /**
     * 上拉加载
     */
    @Bind(R.id.lmlvc_down_refresh)
    LoadMoreListViewContainer lmlvcDownRefresh;
    /**
     * 加载中图片
     */
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    /**
     * 支持某个控件滑到顶部不隐藏的srollview
     */
    @Bind(R.id.sl_scroll)
    ScrollableLayout slScroll;
    /**
     * 下拉刷新
     */
    @Bind(R.id.pcfl_pull_refresh)
    PtrClassicFrameLayout pcflPullRefresh;

    /**
     * 底部view的基金风险提示图片
     */
    private ImageView ivRiskWarning;
    /**
     * 底部view的断网图片容器
     */
    private RelativeLayout rlNotwork;
    /**
     * 基金列表底部view
     * 负责加载断网和基金风险提示图片
     */
    private View footView;

    //-------------逻辑变量-------------------
    /**
     * 基金类型
     * 4.1改版后只有一种基金类型了 mtype需要改进
     */
    private int mtype = 1;
    /**
     * 基金列表的数据集合
     */
    private List<FundListVo> fundListVo;

    private List<FundVo> mfundVos;
    /**
     * 基金列表的适配器
     */
    private MyFundApapter fundAdapter;
    /**
     * 接受服务端广告图对像
     */
    private List<AdvertisingChart> adverst;
    /**
     * 用户基本信息
     */
    private UserInfo userInfo;
    /**
     * 承载广告图的View
     */
    private List<View> mViews;
    /**
     * 计时广告轮播
     */
    private Timer mTimer;
    /**
     * 升序降序的判断
     */
    private String sortord = "desc";
    /**
     * 加载数据动画
     */
    private Animation operatingAnim;
    /**
     * 广告图的Adapter
     */
    private SimplePagerAdapter spAdapter;
    /**
     * 图片缓存加载
     */
    private InitImageLoader imageLoader;
    /**
     * 广告图缓存
     */
    private File file;
    /**
     * 是否排序
     */
    private boolean isSort;
    /**
     * 广告图的高度
     */
    private int ADheight;
    /**
     * 广告图是否被点击
     * 点击会停止广告轮播
     */
    private boolean isUserTouched = false;
    /**
     * 基金列表加载数量
     */
    private final int pageSize = 30;
    /**
     * 基金列表页码
     */
    private int pageNumber = 1;
    private boolean isListClean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fund);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();
        initializeControl();

    }

    private void initializeView() {
        //下拉刷新控件
        pcflPullRefresh.setLastUpdateTimeRelateObject(this);
        pcflPullRefresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return slScroll.canPtr();
                //                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mScrollLayout, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pcflPullRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pcflPullRefresh.refreshComplete();
                        pageNumber = 1;
                        isListClean =true;
//                        fundListVo.clear();
                        getFundDate(mtype);
                    }
                }, 500);
            }
        });

//        //设置延时自动刷新数据
//        mPtrFrame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrame.autoRefresh(false);
//            }
//        }, 200);
        //上拉加载控件
        lmlvcDownRefresh.setAutoLoadMore(true);//设置是否自动加载更多
        LoadMoreFooterView customMoreView = new LoadMoreFooterView(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(-2, LocalDisplay.dp2px(80));
        customMoreView.setLayoutParams(lp);
        lmlvcDownRefresh.setLoadMoreView(customMoreView);
        lmlvcDownRefresh.setLoadMoreUIHandler(customMoreView);
        lmlvcDownRefresh.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                lmlvcDownRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lmlvcDownRefresh.loadMoreFinish(true,true);
                        if (fundListVo.size() <= 0) {
                            pageNumber = 1;
                        } else {
                            pageNumber++;
                        }
                        getFundDate(mtype);
                    }
                }, 500);
            }
        });

        slScroll.getHelper().setCurrentScrollableContainer(new ScrollableHelper.ScrollableContainer() {
            @Override
            public View getScrollableView() {
                return lvFund;
            }
        });

        //listView底部view显示风险提示和无网络加载状态
        footView = LayoutInflater.from(FundSupmakActivity.this).inflate(R.layout.fundfragment_listview_footer, null);
        ivRiskWarning = (ImageView) footView.findViewById(R.id.iv_risk_warning);
        rlNotwork = (RelativeLayout) footView.findViewById(R.id.rl_notwork);
        lvFund.addFooterView(footView);

        // 获取广告图控件的高度 用户设置用户点击升序降序时socrollyView直接从广告图底部显示，实现隐藏广告图
        ViewTreeObserver vto2 = llAdvertisement.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llAdvertisement.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ADheight = lvFund.getHeight();//获取控件高度
            }
        });

    }

    private void initializeMode() {
        mfundVos = new ArrayList<>();
        fundListVo = new ArrayList<>();
        imageLoader = new InitImageLoader();
        mViews = new ArrayList<>();
        userInfo = UserInfo.getInstance(this);
        operatingAnim = AnimationUtils.loadAnimation(FundSupmakActivity.this, R.anim.rotate_evey);
        fundAdapter = new MyFundApapter(this, fundListVo, mtype);
        lvFund.setAdapter(fundAdapter);
    }


    private void initializeControl() {
        startAnimation();
        getAdvertisement();
        getFundDate(mtype);
        //前往基金详情
        lvFund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (fundListVo.size() > 0) {
                    if (position > fundListVo.size()) {
                        //不让点击加载更多
                        return;
                    }
                    //跳转基金详情 传fundcode查询基金信息
                    Intent intent = new Intent(FundSupmakActivity.this, FundDetailActivity.class);
                    intent.putExtra("fundCode", fundListVo.get(position).getFundBaseInfo().getFundCode());
                    startActivity(intent);
                }

            }
        });
        rlNotwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
                if (fundListVo.size() <= 0) {
                    pageNumber = 1;
                } else {
                    pageNumber++;
                }
                getFundDate(mtype);
            }
        });
    }

    /**
     * 向数米提交数据
     * 启动广告
     */
    @Override
    protected void onResume() {
        super.onResume();
        SubmitSmDate();
        if (mViews.size() > 1) {
            if (null != mTimer) {
                mTimer.cancel();
            }
            startBanAdvettise();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mTimer) {
            mTimer.cancel();
        }
    }

    /**
     * 停止加载动画
     */
    private void stopAnimation() {
        ivLoading.clearAnimation();
        ivLoading.setVisibility(View.GONE);
        if (fundListVo.size() <= 0) {
            rlNotwork.setVisibility(View.VISIBLE);
            ivRiskWarning.setVisibility(View.GONE);
        } else {
            rlNotwork.setVisibility(View.GONE);
            ivRiskWarning.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 开始加载动画
     */
    private void startAnimation() {
        rlNotwork.setVisibility(View.GONE);
        ivLoading.setVisibility(View.VISIBLE);
        ivLoading.startAnimation(operatingAnim);
        ivRiskWarning.setVisibility(View.GONE);
    }

    /**
     * 首页广告轮播---启动线程
     */
    private void startBanAdvettise() {
        mTimer = new Timer();
        if (mViews != null && mViews.size() > 1) {
            TimerTask mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!isUserTouched) {
                        FundSupmakActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bvpAdvertisement.setCurrentItem((bvpAdvertisement.getCurrentItem() + 1) % mViews.size());
                            }

                        });
                    }
                }
            };
            mTimer.schedule(mTimerTask, 5000, 5000);
        }
    }

    /**
     * 设置广告图的参数以及相应的监听事件
     */
    private void setAdvertisingChartContent(final List<AdvertisingChart> list) {
        spAdapter = new SimplePagerAdapter(mViews);
        bvpAdvertisement.setAdapter(spAdapter);
        bvpAdvertisement.setOnSingleTouchListener(new BannerViewPager.OnSingleTouchListener() {

            @Override
            public void onSingleTouch(ViewPager v) {
                Intent intent = null;
                if (null == list || list.size() <= 0 || G.isEmteny(list.get(0).getUrl())) {
                    return;
                }
                AdvertisingChart ad = list.get(v.getCurrentItem());
                if (!G.isEmteny(ad.getUrl())) {
                    //跳入web网页
                    intent = new Intent(FundSupmakActivity.this, WebViewActivity.class);
                    intent.putExtra("source", ad.getUrl());
                } else if (!G.isEmteny(ad.getFundCode()) && !G.isEmteny(ad.getSource())) {
                    //跳入基金详情
                    intent = new Intent(FundSupmakActivity.this, FundDetailActivity.class);
                    intent.putExtra("fundCode", ad.getFundCode());
                    intent.putExtra("source", ad.getSource());
                } else if (!G.isEmteny(ad.getRetain()) && ad.getRetain().equals("1")) {
                    //跳入担保购机，传值为2
                    intent = new Intent(FundSupmakActivity.this, MainActivity.class);
                    intent.putExtra("tab", 2);
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        if (null != mTimer) {
            mTimer.cancel();
        }
        startBanAdvettise();
        bvpAdvertisement.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    isUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    isUserTouched = false;
                }
                return false;
            }
        });
    }

    /**
     * 个人中心，基金搜索，基金排序，点击事件
     *
     * @param view
     */
    @OnClick({R.id.ib_member_center, R.id.ib_search, R.id.ll_sort})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ib_member_center:
                if (G.isEmteny(userInfo.getLoginName())) {
                    intent = new Intent(this, LoginActivity.class);
                } else {
                    intent = new Intent(this, MemberCenterActivity.class);
                }
                break;
            case R.id.ib_search:
                intent = new Intent(FundSupmakActivity.this, SearchActivity.class);
                intent.putExtra("showType", 1);
                break;
            case R.id.ll_sort:
                if (sortord.equals("desc")) {
                    sortord = "asc";
                    ivSortType.setImageResource(R.drawable.ic_arrow_up_p);
                } else {
                    sortord = "desc";
                    ivSortType.setImageResource(R.drawable.ic_arrow_top);
                }
                isSort = true;
                isListClean =true;
                pageNumber = 1;
                getFundDate(mtype);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 获取基金超市数据
     * @param type
     */
    public void getFundDate(int type) {
//        //获取基金超市不同类型的基金总数
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("type", type + "");
//        OkHttpUtil.sendPost(G.uri.FUND_TOTAL, map, this);

        //获取基金超市不同类型的基金信息
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", String.valueOf(type));
        params.put("pageNumber", String.valueOf(pageNumber));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("secretKey", userInfo.getSecretKey());
        params.put("sortord", sortord);
        OkHttpUtil.sendPost(ApiUri.QUERY_FUND, params, this,0,"QUERY_FUND");
    }


    /**
     * 获取头部的广告图
     */
    private void getAdvertisement() {
        file = StorageUtils.getOwnCacheDirectory(this, "ChatFill");
        Map<String, String> map = new HashMap<>();
        map.put("loginName", userInfo.getLoginName());
        map.put("screenSize", "0");
        OkHttpUtil.sendPost(ApiUri.RECOMMENDTION, map, this);
    }

    /**
     * 设置头部的广告图
     */
    private void setAdvertisingChart(RecommenVo response) {
        if (null == response.getDate()) {
            return;
        }
        if (response.getDate().size() > 0) {
            adverst = response.getDate();
            mViews.clear();
            imageLoader.setChat();
            for (int i = 0; i < adverst.size(); i++) {
                AdvertisingChart ad = adverst.get(i);
                ImageView imgView = new ImageView(FundSupmakActivity.this);
                imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageLoader.doLoadImg(ad.getImage(), imgView);
                mViews.add(imgView);
            }
            setAdvertisingChartContent(adverst);
        }
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.RECOMMENDTION)) {
                    //获取广告图
                    RecommenVo response = new Gson().fromJson(result, RecommenVo.class);
                    setAdvertisingChart(response);
                    spAdapter.notifyDataSetChanged();
                } else if (uri.equals(ApiUri.QUERY_FUND)) {
                    //获取基金列表
                    FundobjectVo response = new Gson().fromJson(result, FundobjectVo.class);
                    if (response.getResultCode().equals("0")) {
                        if (response.getDate() == null || response.getDate().size() <= 0 || response.getDate().get(0).getFundProductList().size() <= 0) {
                            if (mfundVos.size() > 0) {
                                lmlvcDownRefresh.loadMoreFinish(false, true);
                                G.showToast(FundSupmakActivity.this, "暂无更多数据");
                                return;
                            }
                            stopAnimation();
                            G.showToast(FundSupmakActivity.this, "暂无数据");
                        } else {
                            mfundVos = response.getDate();
//                                  hand.sendEmptyMessage(0x11);
                            if(isListClean){
                                fundListVo.clear();
                                isListClean=false;
                            }
                            fundListVo.addAll(mfundVos.get(0).getFundProductList());
                            fundAdapter.notifyDataSetChanged();
                            stopAnimation();
                            if (isSort) {
                                //点击排序后 scrollView fund_xlistview都进行重置
                                slScroll.scrollTo(0, ADheight * 4);
                                lvFund.setSelection(0);
                                isSort = false;
                            }
                            lmlvcDownRefresh.loadMoreFinish(true, true);
                        }
                    } else {
                        G.showToast(FundSupmakActivity.this, response.getResultDesc());
                    }

//                } else if (uri.equals(G.uri.FUND_TOTAL)) {
////                    //获取对应基金总数
////                    FundTotalVo fundTotalVo = new Gson().fromJson(result, FundTotalVo.class);
////                    mtotal = fundTotalVo.getDate();
                }
            }
        });

    }

    @Override
    public void onError(final String uri, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.RECOMMENDTION)) {
                    getCacheImage();
                    setAdvertisingChartContent(null);
                } else if (uri.equals(ApiUri.QUERY_FUND)) {
                    pageNumber--;
                    fundAdapter.notifyDataSetChanged();
                    stopAnimation();
                    rlNotwork.setClickable(true);
                    lmlvcDownRefresh.loadMoreFinish(false, false);
                    G.showToast(FundSupmakActivity.this, "您的网络好像出了些问题...");
                }
            }
        });

    }

    /**
     * 数米交易记录
     * 下午三点准时向服务端提交数据
     */
    private void SubmitSmDate() {
        String datetime = DateUtil.getCurrentDate();
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数

        if (!G.isEmteny(userInfo.getSmToken()) && !G.isEmteny(userInfo.getLoginName())) {
            if (!userInfo.getDateTime().equals(datetime) && mHour >= 15) {
                new UserSyntion(FundSupmakActivity.this, true, false);
            }
        }
    }

    /**
     * 获取本地缓存图片
     * 遍历查询缓存图片地址
     * 通过 BitmapFactory 进行设置图片
     * 此方式获取缓存不佳  日后必须优化 当网络图片有更新的时候 之前的图片并不会删除，这样缓存的时候会显示网络之前的图片
     */
    private void getCacheImage() {
        mViews.clear();
        File[] fs = file.listFiles();
        for (int i = 0; i < fs.length; i++) {//遍历出所有文件。
            File mf = fs[i];       //拿到每一个文件。
            if (mf.isDirectory()) {//如果不是文件，是文件夹。
                continue;
            }
            String name = mf.getName();
            ImageView imgView = new ImageView(FundSupmakActivity.this);
            imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file + "/" + name));
                imgView.setImageBitmap(bitmap); //设置Bitmap
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                G.log(e1);
            }
            mViews.add(imgView);
        }
    }
}
