package com.cfjn.javacf.adapter.bookkeeping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.RootType;

import java.util.List;


/**
 *  作者： wh
 *  时间： 2016-6-27
 *  名称： 记账分类--父类适配器
 *  版本说明：代码规范整改
 *  附加注释：无
 *  主要接口：无
 */
public class ClassfiyRootAdapter extends BaseAdapter {
    private Context context;
    private int selectedPosition = -1;
    private List<RootType> rootTypes;
    public ClassfiyRootAdapter(Context context, List<RootType> rootTypes) {
        this.context = context;
        this.rootTypes = rootTypes;
    }
    @Override
    public int getCount() {
        return rootTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return rootTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookeepingclassify_group, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView
                    .findViewById(R.id.tv_rootname);

            holder.layout = (LinearLayout) convertView
                    .findViewById(R.id.colorlayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(selectedPosition ==position) {
            holder.textView.setTextColor(context.getResources().getColor(R.color.red));
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.textView.setTextColor(context.getResources().getColor(R.color.black));
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.silver));
        }
        holder.textView.setText(rootTypes.get(position).getName());
        return convertView;
    }
    public static class ViewHolder {
        public TextView textView;
        public LinearLayout layout;
    }
    public void SelectionPosition(int position){
        selectedPosition =position;
    }
}
