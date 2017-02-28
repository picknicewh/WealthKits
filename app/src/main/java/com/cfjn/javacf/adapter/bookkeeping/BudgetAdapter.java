package com.cfjn.javacf.adapter.bookkeeping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.RootType;
import com.cfjn.javacf.util.G;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 *  作者： wh
 *  时间： 2016-6-27
 *  名称： 记账分类--预算列表适配器
 *  版本说明：代码规范整改
 *  附加注释：无
 *  主要接口：无
 */
public class BudgetAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;
    private DecimalFormat df = new DecimalFormat("########0.00");
    public BudgetAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int marginwidth = 0;
        int lablewidth = 0;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_bookkeepingbudge, null);
        ViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = new ViewHolder();
            viewHolder.tv_procress_lable = (TextView) convertView.findViewById(R.id.tv_btprogress_label);
            viewHolder.tv_procress_value = (TextView) convertView.findViewById(R.id.tv_btprogress_value);
            viewHolder.tv_value_surp = (TextView) convertView.findViewById(R.id.tv_btvalue_surp);
            viewHolder.v_procress = convertView.findViewById(R.id.v_btprogress);
            viewHolder.v_procress_bg = convertView.findViewById(R.id.v_btprogress_bg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> map = list.get(position);
        RootType rtv = (RootType) map.get("RootTypeVo");
        double budget = Double.parseDouble(String.valueOf(map.get("budget")));
        double balance =  Double.parseDouble(String.valueOf(map.get("balance")));
        String name = (String)map.get("title");
        double percent;
        if (budget == 0) {
            // 预算为0 根据结余是否为0设置百分比
            percent = (balance == 0) ? 0 : 1;
        } else {
            // 预算不为0 根据结余是否大于0设置百分比
            percent = (balance >= 0) ?  ( balance) / budget  : 1;
        }
        RelativeLayout.LayoutParams params;
        int processwidth = G.dp2px(context, 280);
        viewHolder.tv_procress_lable.setText(name);
        marginwidth = convertView.getPaddingLeft() + G.dp2px(context, 48);
        viewHolder.tv_procress_value.setText(G.momeyFormat(budget));
        //设置余额的值
        viewHolder.tv_value_surp.setText(G.momeyFormat(balance));
        //设置长条背景的长度参数
        params = new RelativeLayout.LayoutParams(processwidth, 20);
        params.setMargins(marginwidth, 0, 0, 0);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        viewHolder.v_procress_bg.setLayoutParams(params);
        //设置长条的长度参数以及动画
        lablewidth = G.dp2px(context, 280 * percent);
        params = new RelativeLayout.LayoutParams(lablewidth, 20);
        params.setMargins(marginwidth, 0, 0, 0);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        viewHolder.v_procress.setLayoutParams(params);
        if (Double.parseDouble(map.get("balance").toString()) < 0) {
            viewHolder.v_procress.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.v_procress.setBackgroundColor(context.getResources().getColor(rtv.getColor()));
        }
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1.0f, 1.0f, 1.0f);//伸缩动画设置值，宽度的值从1，到百分比
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        viewHolder.v_procress.startAnimation(scaleAnimation);
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_procress_lable;
        private TextView tv_procress_value;
        private View v_procress;
        private View v_procress_bg;
        private TextView tv_value_surp;
    }
}
