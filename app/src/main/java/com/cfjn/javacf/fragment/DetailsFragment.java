package com.cfjn.javacf.fragment;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.FundBaseInfo;
import com.cfjn.javacf.modle.ManagerObject;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  作者： zll
 *  时间： 2016-5-30
 *  名称： 基金的一些详情 基金经理申购信息等
 *  版本说明：代码规范整改
 *  附加注释：new 这个 Fragment 时需要传一个 fundBaseInfo
 *  主要接口：1.获取基金经理信息
 *
 */
public class DetailsFragment extends Fragment implements OKHttpListener {
    //-----------布局变量--------------
    /**
     * 基金经理
     */
    @Bind(R.id.tv_manager)
    TextView tvManager;
    /**
     * 基金公司
     */
    @Bind(R.id.tv_company)
    TextView tvCompany;
    /**
     * 基金规模
     */
    @Bind(R.id.tv_scale)
    TextView tvScale;
    /**
     * 风险程度
     */
    @Bind(R.id.tv_Riskdegree)
    TextView tvRiskdegree;
    /**
     * /申购费率 初始值
     */
    @Bind(R.id.tv_rate_initial)
    TextView tvRateInitial;
    /**
     * 申购费率 折扣值
     */
    @Bind(R.id.tv_rate_discount)
    TextView tvRateDiscount;
    /**
     * 赎回时间
     */
    @Bind(R.id.tv_redeem_time)
    TextView tvRedeemTime;
    /**
     * 购买柜台信息
     */
    @Bind(R.id.tv_purchase_counter)
    TextView tvPurchaseCounter;

    //-------------逻辑变量-------------------
    /**
     * 基金信息
     */
    private FundBaseInfo fundBaseInfo;
    /**
     * 解析基金经理
     */
    private StringBuffer managerdate;

    private DecimalFormat format2;

    public DetailsFragment(FundBaseInfo fundBaseInfo) {
        //初始化传一个fundbaseInfo 从基金详情页面传
        this.fundBaseInfo = fundBaseInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        format2 = new DecimalFormat("#0.00");
        setValue();
        getManagerDate();
    }


    /**
     * 设置各个标签参数
     * 基金公司  风险程度  申购费率 申购时间
     */
    private void setValue() {
        BigDecimal db = new BigDecimal(fundBaseInfo.getLastestTotalAsset());
        tvScale.setText(format2.format(Double.parseDouble(db.toPlainString()) / 100000000) + "亿元");

        tvRateInitial.setText(String.valueOf(fundBaseInfo.getChargeRateValue()));
        if (fundBaseInfo.getGuidName() == null || fundBaseInfo.getGuidName().equals("") || fundBaseInfo.getGuidName().equals("null")) {
            tvCompany.setText("暂未获取到基金公司信息");
        } else {
            tvCompany.setText(fundBaseInfo.getGuidName());
        }
        if (fundBaseInfo.getChargeRateValue() == null || "".equals(fundBaseInfo.getChargeRateValue())) {
            tvRateDiscount.setVisibility(View.GONE);
            tvRateInitial.setText("免申购费");
        } else {
            if (fundBaseInfo.getChargeRateValue() == 0) {
                tvRateDiscount.setVisibility(View.GONE);
                tvRateInitial.setText("免申购费");
            } else {
                tvRateInitial.setText(String.valueOf(format2.format(fundBaseInfo.getChargeRateValue() * 100)) + "%");
                tvRateInitial.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tvRateDiscount.setText(String.valueOf(format2.format(fundBaseInfo.getChargeRateValue() * fundBaseInfo.getDiscount() * 100)) + "%");
            }
        }
        if (fundBaseInfo.getFundType() == 7) {
            tvRiskdegree.setText("超低风险");
            tvRiskdegree.setTextColor(getResources().getColor(R.color.blue_x0_7));
        } else {
            if (fundBaseInfo.getFundType() == 2 && fundBaseInfo.getInvestmentType() == 6) {
                tvRiskdegree.setText("低风险");
                tvRiskdegree.setTextColor(getResources().getColor(R.color.gree_xx));
            } else if (fundBaseInfo.getFundType() == 2 && fundBaseInfo.getInvestmentType() == 3) {
                tvRiskdegree.setText("有一定风险");
                tvRiskdegree.setTextColor(getResources().getColor(R.color.org_x0_8));
            } else {
                tvRiskdegree.setText("高风险");
                tvRiskdegree.setTextColor(getResources().getColor(R.color.red));
            }
        }
        if (fundBaseInfo.getRapidRedeem() == 0) {
            tvRedeemTime.setText("T+3");
        } else {
            tvRedeemTime.setText("T+0");
        }
//        Log.i("source", fundBaseInfo.getSource());
        G.log(fundBaseInfo.getSource()+"--------------------基金柜台来源");
        //设置柜台来源
        StringBuilder counter = new StringBuilder();
        if(null==fundBaseInfo.getSource()){
            tvPurchaseCounter.setText("未知");
            return;
        }
        if (fundBaseInfo.getSource().contains("0")) {
            counter.append("数米基金").append("，");
        }
        if (fundBaseInfo.getSource().contains("1")) {
            counter.append("天风证券").append("，");
        }
        if (fundBaseInfo.getSource().contains("2")) {
            counter.append("长量基金").append("，");
        }
        //去掉后面的，号
        if(counter.length()>0){
            counter.deleteCharAt(counter.length()-1);
        }else{
            counter.append("未知");
        }

        tvPurchaseCounter.setText(counter);
    }

    /**
     * 获取基金经理信息
     */
    public void getManagerDate() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fundCode", fundBaseInfo.getFundCode());
        OkHttpUtil.sendPost(ApiUri.MANAGER, params, this);
        // new OkHttpRequest.Builder().url(G.uri.MANAGER).params(params).post(result);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.MANAGER)) {
                    ManagerObject managerObject = new Gson().fromJson(result, ManagerObject.class);
                    managerdate = new StringBuffer();
                    for (int i = 0; i < managerObject.getDate().size(); i++) {
                        managerdate.append(" " + managerObject.getDate().get(i).getName());
                        Log.i("manager", managerdate + "");
                        tvManager.setText(managerdate);
                    }
                }
            }
        });
    }

    @Override
    public void onError(final String uri, final String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.MANAGER)) {

                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
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
