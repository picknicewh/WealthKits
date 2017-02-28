package com.cfjn.javacf.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.ChartVo;
import com.cfjn.javacf.modle.CurveDateVo;
import com.cfjn.javacf.modle.CurveInfo;
import com.cfjn.javacf.modle.FundBaseInfo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.RadiobuttonUtil;
import com.cfjn.javacf.widget.TrendView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  作者： zll
 *  时间： 2016-5-31
 *  名称： 基金详情折线图
 *  版本说明：代码规范整改
 *  附加注释： TrendView实现折线绘制，通过cavesThreadView()实现
 *  fundBaseInfo获取各个阶段年化率和收益的值
 *  主要接口：1.获取走势图信息
 *
 */
public class EarningsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 周
     */
    @Bind(R.id.rb_week)
    RadioButton rbWeek;
    /**
     * 月
     */
    @Bind(R.id.rb_month)
    RadioButton rbMonth;
    /**
     * 季
     */
    @Bind(R.id.rb_season)
    RadioButton rbSeason;
    /**
     * 走势图
     */
    @Bind(R.id.trend_char)
    TrendView trendChar;

    /**
     * 加载图片
     */
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    /**
     * 近一月年华率
     */
    @Bind(R.id.tv_month_rate)
    TextView tvMonthRate;
    /**
     * 近一月收益
     */
    @Bind(R.id.tv_month_profit)
    TextView tvMonthProfit;
    /**
     * 近一季年化率
     */
    @Bind(R.id.tv_season_rate)
    TextView tvSeasonRate;
    /**
     * 近一季收益
     */
    @Bind(R.id.tv_season_profit)
    TextView tvSeasonProfit;
    /**
     * 近半年年化率
     */
    @Bind(R.id.tv_hyear_rate)
    TextView tvHyearRate;
    /**
     * 近半年收益
     */
    @Bind(R.id.tv_hyear_profit)
    TextView tvHyearProfit;
    /**
     * 近一年年化率
     */
    @Bind(R.id.tv_year_rate)
    TextView tvYearRate;
    /**
     * 近一年收益
     */
    @Bind(R.id.tv_year_profit)
    TextView tvYearProfit;
    /**
     * 单选框
     */
    @Bind(R.id.ru_date_choose)
    RadiobuttonUtil ruDateChoose;

    //-------------逻辑变量-------------------
    /**
     * 解析数据容器
     */
    private List<ChartVo> threadList;
    private DecimalFormat format1;
    private DecimalFormat format2;
    private FundBaseInfo fundBaseInfo;
    /**
     * 当前天数的间隔
     */
    private int dayTime = 7;
    private Animation operatingAnim;
    private Activity activity;

    public EarningsFragment(FundBaseInfo fundBaseInfo) {
        this.fundBaseInfo = fundBaseInfo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earnimgs, null);
        if (activity == null) {
            activity = getActivity();
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeMode();
        initializeControl();
    }

    public void initializeMode() {
        format1 = new DecimalFormat("#0.0000");
        format2 = new DecimalFormat("#0.00");
        operatingAnim = AnimationUtils.loadAnimation(activity, R.anim.rotate_evey);
        rbWeek.setChecked(true);
        ruDateChoose.setOnCheckedChangeListener(this);
    }

    private void initializeControl(){
        //设置各个阶段的年化率和收益值
        setEarningsText(tvMonthRate, tvMonthProfit, fundBaseInfo.getYield1M() );
        setEarningsText(tvSeasonRate, tvSeasonProfit, fundBaseInfo.getYield3M());
        setEarningsText(tvHyearRate, tvHyearProfit, fundBaseInfo.getYield6M());
        setEarningsText(tvYearRate, tvYearProfit, fundBaseInfo.getYield12M());
        //获取默认7天的折线数据
        getThreadDate(String.valueOf(dayTime));
    }

    /**
     * 绘制折线图
     */
    private void cavesThreadView(List<CurveInfo> curList) {
        threadList = new ArrayList<>();
        boolean isAdd=true;//是否可以添加
        //去掉相同的数据
        for (int i = 0; i < curList.size(); i++) {
            CurveInfo curveInfo=curList.get(i);
            for (int j=i+1;j<curList.size();j++){
                CurveInfo curveInfoAfter=curList.get(j);
                if(curveInfo.getDate().equals(curveInfoAfter.getDate())){
                    isAdd=false;
                    break;
                }else{
                    isAdd=true;
                }
            }
            if(isAdd){
                //横坐标   折线值
                threadList.add(new ChartVo(curveInfo.getDate(), Float.parseFloat(format2.format(curveInfo.getValue() * 100)), Float.parseFloat(format1.format(curveInfo.getValue_add()))));
            }

        }
        //2表示一根线
        trendChar.setValus(threadList, 2, fundBaseInfo.getFundType());
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        ivLoading.setVisibility(View.VISIBLE);
        ivLoading.startAnimation(operatingAnim);
    }

    /**
     * 停止动画
     */
    private void stopAnimation() {
        ivLoading.clearAnimation();
        ivLoading.setVisibility(View.GONE);
    }

    /**
     * 设置阶段收益的值
     *
     * @param rate   年华率
     * @param profit 收益
     * @param date   值
     */
    private void setEarningsText(TextView rate, TextView profit, Double date) {
        if (null == date || "null".equals(date)) {
            rate.setText(0.00 + "%");
            profit.setText(0.00 + "元");
            profit.setTextColor(getResources().getColor(R.color.red));
        } else {
            rate.setText(format2.format(date * 100) + "%");
            profit.setText(format2.format(date * 10000) + "元");

            if (date < 0) {
                profit.setTextColor(getResources().getColor(R.color.green));
            } else {
                profit.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_week:
                rbMonth.setBackgroundResource(R.drawable.january_center);
                rbWeek.setTextColor(getResources().getColor(R.color.white));
                rbMonth.setTextColor(getResources().getColor(R.color.black));
                rbSeason.setTextColor(getResources().getColor(R.color.black));
                dayTime = 7;
                break;
            case R.id.rb_month:
                rbMonth.setBackgroundColor(Color.parseColor("#FF3B30"));
                rbMonth.setTextColor(getResources().getColor(R.color.white));
                rbWeek.setTextColor(getResources().getColor(R.color.black));
                rbSeason.setTextColor(getResources().getColor(R.color.black));
                dayTime = 30;
                break;
            case R.id.rb_season:
                rbMonth.setBackgroundResource(R.drawable.january_center);
                rbSeason.setTextColor(getResources().getColor(R.color.white));
                rbMonth.setTextColor(getResources().getColor(R.color.black));
                rbWeek.setTextColor(getResources().getColor(R.color.black));
                dayTime = 90;
                break;
        }
        getThreadDate(String.valueOf(dayTime));
    }

    private void getThreadDate(String dayTime) {
        Map<String, String> param = new HashMap<>();
        int curveType=fundBaseInfo.getFundType()==7?2:1;
        param.put("curveType", String.valueOf(curveType));
        param.put("fundCode", fundBaseInfo.getFundCode());
        param.put("numberOfDays", dayTime);
        OkHttpUtil.sendPost(ApiUri.GET_CURVE, param, this);
        startAnimation();
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.GET_CURVE)) {
                    stopAnimation();
                    CurveDateVo curveDateVo = new Gson().fromJson(result, CurveDateVo.class);
                    List<CurveInfo>curList=curveDateVo.getDate().getCurveInfo();
                    if (curList.size()> 0) {
                        cavesThreadView(curList);
                    } else {
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.GET_CURVE)) {
                    stopAnimation();
                    G.showToast(getActivity(), getResources().getString(R.string.notWorkPrompt));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
