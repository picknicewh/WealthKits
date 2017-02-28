package com.cfjn.javacf.adapter.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.member.BankMangerActivity;
import com.cfjn.javacf.activity.member.UserManagerActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.shumi.MyShumiSdkTradingHelper;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.GetTFMagerUrl;
import com.openhunme.cordova.activity.HMDroidGap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 柜台管理 listView 适配器
 * 版本说明：代码规范整改
 * 附加注释：显示各个柜台开户状态。银行卡数
 * 主要接口：暂无
 */
public class CounterAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private Context context;
    private Activity activity;
    private UserInfo us;

    public CounterAdapter(List<Map<String, Object>> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        us = UserInfo.getInstance(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_countermanger, null);
            new HoldView(convertView);
        }
        HoldView holdView = (HoldView) convertView.getTag();
        final Map<String, Object> map = list.get(position);
        holdView.tvContentname.setText(map.get("name").toString());
        if (String.valueOf(map.get("isGoBank")).equals("true")) {
            holdView.tvContenttype.setVisibility(View.GONE);
            holdView.llContentMangetOff.setVisibility(View.GONE);
            holdView.llContentMangerOn.setVisibility(View.VISIBLE);
        } else {
            holdView.tvContentMangerText.setText(String.valueOf(map.get("prompt")));
            holdView.tvContenttype.setVisibility(View.VISIBLE);
            holdView.llContentMangetOff.setVisibility(View.VISIBLE);
            holdView.llContentMangerOn.setVisibility(View.GONE);
        }

        holdView.llManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到账户管理
                Intent intent = new Intent(context, UserManagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userInformation", (Serializable) list.get(position).get("userInformation"));
                intent.putExtra("name", String.valueOf(map.get("name")));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holdView.llBankmang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当数米没有银行卡直接添加银行卡
                if (Integer.parseInt(map.get("bankNuber") + "") == 0 && map.get("name").equals("数米柜台")) {
                    MyShumiSdkTradingHelper.doAddBankCard(context);
                } else {
                    //点击跳转到银行卡管理
                    Intent intent = new Intent(context, BankMangerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userBankCardList", (Serializable) list.get(position).get("userBankCardList"));
                    intent.putExtras(bundle);
                    intent.putExtra("name", map.get("name").toString());
                    context.startActivity(intent);
                }
            }
        });

        holdView.tvCountManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前往开通
                if (map.get("name").equals("数米柜台")) {
                    MyShumiSdkTradingHelper.doAuthentication(context);
                } else if (map.get("name").equals("天风柜台")) {
                    Intent intent = new Intent(context, HMDroidGap.class);
                    intent.putExtra("loadUrl", GetTFMagerUrl.TFMagerUrl(0, context));
                    context.startActivity(intent);
                } else if (map.get("name").equals("松果柜台")) {
                    Intent intent = new Intent(context, HMDroidGap.class);
                    intent.putExtra("loadUrl", "file:///android_asset/www/sg/html/id_validate.html?token=" + us.getToken() + "&secretKey=" + us.getSecretKey() + "&phone=" + us.getLoginName());
                    G.log("file:///android_asset/www/sg/html/id_validate.html?token=" + us.getToken() + "&secretKey=" + us.getSecretKey() + "&phone=" + us.getLoginName());
                    context.startActivity(intent);
                }
                activity.finish();
            }
        });
        holdView.tvBankNumber.setText(String.valueOf(map.get("bankNuber") + "张"));

        return convertView;
    }


    class HoldView {
        /**
         * 柜台名称
         */
        @Bind(R.id.tv_contentname)
        TextView tvContentname;
        /**
         * 开通状态
         */
        @Bind(R.id.tv_contenttype)
        TextView tvContenttype;
        /**
         * 推荐语
         */
        @Bind(R.id.tv_contentManger_text)
        TextView tvContentMangerText;
        /**
         * 马上开通
         */
        @Bind(R.id.tv_countManger)
        TextView tvCountManger;
        /**
         * 没有开通显示分页面
         */
        @Bind(R.id.ll_contentManget_off)
        LinearLayout llContentMangetOff;
        /**
         * 账户管理
         */
        @Bind(R.id.ll_Manger)
        LinearLayout llManger;
        /**
         * 银行卡数量
         */
        @Bind(R.id.tv_bankNumber)
        TextView tvBankNumber;
        /**
         *  银行卡管理
         */
        @Bind(R.id.ll_bankmang)
        LinearLayout llBankmang;
        /**
         * 已开通页面
         */
        @Bind(R.id.ll_contentManger_on)
        LinearLayout llContentMangerOn;

        public HoldView(View view) {
            ButterKnife.bind(this, view);
            setFlgs(tvCountManger);
            view.setTag(this);
        }
    }

    /**
     * 设置下划线
     *
     * @param v
     */
    private void setFlgs(TextView v) {
        v.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        v.getPaint().setAntiAlias(true);//抗锯齿
    }
}
