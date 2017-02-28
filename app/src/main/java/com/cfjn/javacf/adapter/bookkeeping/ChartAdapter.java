package com.cfjn.javacf.adapter.bookkeeping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.RootType;
import com.cfjn.javacf.util.G;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  作者： wh
 *  时间： 2016-6-27
 *  名称： 记账图表--条图适配器
 *  版本说明：代码规范整改
 *  附加注释：无
 *  主要接口：无
 */
public class ChartAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater  inflater;
    int item_width;//每个item的长度
    private Map<Integer,Double> percents = new HashMap<>();
    float maxpersecent=0.0f;
    private   DecimalFormat df = new DecimalFormat("########0.00");
    public ChartAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        View view = inflater.from(context).inflate(R.layout.item_bookeeping_char_detail, null);
        int screenWidth = G.size.W;
        item_width = screenWidth- 2*view.getPaddingLeft();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.item_bookeeping_char_detail, null);
            holder = new ViewHolder();
            holder.iv_progress_icon = (ImageView) convertView.findViewById(R.id.iv_progress_icon1);//图片
            holder.tv_progress_label = (TextView) convertView.findViewById(R.id.tv_progress_label);//名称
            holder.tv_progress_percent = (TextView) convertView.findViewById(R.id.tv_progress_percent);//百分比;
            holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);//所用的钱
            holder.v_progress = convertView.findViewById(R.id.v_progres);//所占百分比的背景
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> map = list.get(position);
        RootType rootType = (RootType) map.get("RootTypeVo");
        DecimalFormat format = new DecimalFormat("##0.00");
        float percent = Float.parseFloat(map.get("percent")+"");
        double value = (double) map.get("value");
        holder.tv_value.setText(G.momeyFormat(value)+ "元"); //设置所用钱的值
        holder.tv_progress_label.setText(rootType.getName());   //设置名称的参数
        //设置图片的参数
        holder.iv_progress_icon .setImageResource(rootType.getIcon());
        //设置百分比的参数
        holder.tv_progress_percent.setTextColor(context.getResources().getColor(rootType.getColor()));
        holder.tv_progress_percent.setText(format.format(percent) + "%");   //设置百分比的参数以及
        if (percent>maxpersecent){
            maxpersecent = percent;
            percent = 100.0f;
        }
        else {
            percent = percent/maxpersecent*100.0f;
        }
        //设置进度条的参数
        int processwidth = (int)(item_width*0.64*percent/100+0.5);
        //设置动画的进度条背景参数
        holder.v_progress.setBackgroundResource(rootType.getColor());
        LayoutParams lp;
        lp = new LayoutParams(processwidth,20);
        lp.setMargins((int) (item_width * 0.12), 0, 0, 0);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);//设置其为垂直居中
        holder.v_progress.setLayoutParams(lp);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1.0f, 1.0f, 1.0f);//伸缩动画设置值，宽度的值从1，到百分比
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        holder.v_progress.startAnimation(scaleAnimation);
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_progress_icon;
        TextView tv_progress_percent;
        View v_progress;
        TextView tv_progress_label;
        TextView tv_value;
    }

}

