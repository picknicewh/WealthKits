package com.cfjn.javacf.adapter.member;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfjn.javacf.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 我的关注基金适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class SwipeMenuAdapter extends BaseAdapter {
    private List<Map<String, String>> list;
    private Context context;
    private DecimalFormat format;
    public SwipeMenuAdapter(List<Map<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
        format = new DecimalFormat("#0.00");
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.iitem_myattention, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Map<String,String>map=list.get(position);
        holder.tvTime.setText(map.get("createDate") + "至今");
        holder.tvFundName.setText(list.get(position).get("fundName"));
        String duringTheGains;
        String sameTypeAVG;
        String surpassSameType;
        if (map.get("duringTheGains").equals("0.0") && map.get("sameTypeAVG").equals("0.0")) {
            duringTheGains = "- -";
            sameTypeAVG = "- -";
            surpassSameType = "- -";
        } else {
            duringTheGains = map.get("duringTheGains");
            sameTypeAVG = map.get("sameTypeAVG");
            surpassSameType = map.get("surpassSameType");
        }
        setViewType(holder.tvIncrease, duringTheGains);
        setViewType(holder.tvAverage, sameTypeAVG);
        setViewType(holder.tvTranscend, surpassSameType);
        return convertView;
    }

    /**
     * 设置 期间涨幅 同类平均 超越同类的值和颜色
     * @param view 类型
     * @param value 值
     */
    private void setViewType(TextView view, String value) {
        if (!value.equals("- -")) {
            double values = Double.parseDouble(value);
            values = values * 100;
            if (values > 0) {
                view.setTextColor(Color.parseColor("#FF3B30"));
            } else if (values == 0) {
                view.setTextColor(Color.parseColor("#868686"));
            } else if (values < 0) {
                view.setTextColor(Color.parseColor("#5AC846"));
            }
            view.setText(format.format(values) + "%");
            return;
        }
        view.setTextColor(Color.parseColor("#868686"));
        view.setText(value);
    }

    class ViewHolder {
        /**
         *  基金名称
         */
        @Bind(R.id.tv_fund_name)
        TextView tvFundName;
        /**
         * 关注时间
         */
        @Bind(R.id.tv_time)
        TextView tvTime;
        /**
         * 期间涨幅
         */
        @Bind(R.id.tv_increase)
        TextView tvIncrease;
        /**
         * 同类平均
         */
        @Bind(R.id.tv_average)
        TextView tvAverage;
        /**
         * 超越同类
         */
        @Bind(R.id.tv_transcend)
        TextView tvTranscend;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
