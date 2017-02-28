package com.cfjn.javacf.adapter.bookkeeping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.ChildType;
import com.cfjn.javacf.util.G;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


/**
 *  作者： wh
 *  时间： 2016-6-27
 *  名称： 记账图表详细信息--列表的适配器
 *  版本说明：代码规范整改
 *  附加注释：无
 *  主要接口：无
 */
public class ChartDetailAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Map<String, Object>> grouplist;
    private List<List<Map<String, Object>>> sublist;
    private DecimalFormat df = new DecimalFormat("########0.00");
    private  int type;
    public ChartDetailAdapter(Context context, List<Map<String, Object>> grouplist, List<List<Map<String, Object>>> sublist, int type) {
        this.context = context;
        this.grouplist = grouplist;
        this.sublist = sublist;
        this.type = type;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return sublist.get(groupPosition).get(childPosition);
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder = null;
        boolean b = false;
        if (convertView != null) {
            ChildViewHolder childHolder = (ChildViewHolder) convertView.getTag();
        }
        if (convertView == null || b) {
            viewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookkeepingdetail_child, null);

            viewHolder.child_second_textview = (TextView) convertView.findViewById(R.id.second_textview);
            viewHolder.child_text = (TextView) convertView.findViewById(R.id.child_text);
            viewHolder.child_image_type = (ImageView) convertView.findViewById(R.id.child_image_type);
            viewHolder.line_view = convertView.findViewById(R.id.line_view);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.detail_groupposition, groupPosition);
            convertView.setTag(R.id.detail_childposition, childPosition);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (sublist.get(groupPosition).size()-1==childPosition) {
            // Log.i("TAG",childPosition+"");
            viewHolder.line_view.setVisibility(View.GONE);
        }else {
            viewHolder.line_view.setVisibility(View.VISIBLE);
        }

        Map<String, Object> map = sublist.get(groupPosition).get(childPosition);
        int classifyCode = Integer.parseInt((map.get("classifyCode").toString()));
        if (classifyCode != 0) {
            ChildType childType = G.type.getChildTypeByCode(classifyCode);
            viewHolder.child_image_type.setImageResource(childType.getIcon());
            viewHolder.child_text.setText(childType.getName());
        }
        viewHolder.child_second_textview.setText(map.get("value").toString());
        return convertView;
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return sublist.get(groupPosition).size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition) {
        return grouplist.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return grouplist.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //设置父item组件
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder = null;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_bookkeepingdetail_group, null);
        Map<String, Object> map = grouplist.get(groupPosition);
        double totlevalue = Double.parseDouble(String.valueOf(map.get("value")));//获取总支出值
        String date = (String) map.get("date");
        if (convertView != null) {
            viewHolder = new GroupViewHolder();
            viewHolder.parent_textview = (TextView) convertView.findViewById(R.id.parent_textview);
            viewHolder.parent_textview_ = (TextView) convertView.findViewById(R.id.parent_textview_);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.detail_groupposition, groupPosition);
            convertView.setTag(R.id.detail_childposition, -1);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        viewHolder.parent_textview.setText(date);
        if (type==0){
            viewHolder.parent_textview_.setText("支：" + G.momeyFormat(totlevalue) + "");
        }
      else {
            viewHolder.parent_textview_.setText("收：" +G.momeyFormat(totlevalue) + "");
        }
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private static class ChildViewHolder {
        private View line_view;
        private TextView child_second_textview;
        private TextView child_text;
        private ImageView child_image_type;
    }
    private static class GroupViewHolder {
        private TextView parent_textview;
        private TextView parent_textview_;
    }
}

