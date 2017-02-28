package com.cfjn.javacf.adapter.bookkeeping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.ChildType;
import com.cfjn.javacf.util.G;;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


/**
 *  作者： wh
 *  时间： 2016-6-27
 *  名称： 记账首页--月记账详细信息列表适配器
 *  版本说明：代码规范整改
 *  附加注释：无
 *  主要接口：无
 */
public class MainAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Map<String, Object>> grouplist;
    private List<List<Map<String, Object>>> sublist;
    private   DecimalFormat df = new DecimalFormat("########0.00");
    private int groupPosition;
    public MainAdapter(Context context, List<Map<String, Object>> grouplist, List<List<Map<String, Object>>> sublist) {
        this.context = context;
        this.grouplist = grouplist;
        this.sublist = sublist;
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
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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
            convertView.setTag(R.id.main_groupposition, groupPosition);
            convertView.setTag(R.id.main_childposition, childPosition);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        Map<String, Object> map = sublist.get(groupPosition).get(childPosition);
        int classifyCode = Integer.parseInt((map.get("classifyCode").toString()));
        int type = Integer.parseInt((map.get("type").toString()));
        String remark = map.get("remark").toString();
        if (sublist.get(groupPosition).size()-1==childPosition) {
               // Log.i("TAG",childPosition+"");
                viewHolder.line_view.setVisibility(View.GONE);
            }else {
                viewHolder.line_view.setVisibility(View.VISIBLE);
            }

        if (classifyCode != 0) {
            ChildType childType = G.type.getChildTypeByCode(classifyCode);
            switch (type) {
                case 0:
                    viewHolder.child_image_type.setImageResource(childType.getIcon());
                    viewHolder.child_text.setText(childType.getName());
                //   viewHolder.child_text.setTextColor(context.getResources().getColor(R.color.red));
                    viewHolder.child_second_textview.setText(df.format(map.get("out")));
                    viewHolder.child_second_textview.setTextColor(context.getResources().getColor(R.color.red));
                    break;
                case 1:
                    viewHolder.child_image_type.setImageResource(childType.getIcon());
                    viewHolder.child_text.setText(childType.getName());
                   // viewHolder.child_text.setTextColor(context.getResources().getColor(R.color.green));
                    viewHolder.child_second_textview.setText(df.format(map.get("in")));
                    viewHolder.child_second_textview.setTextColor(context.getResources().getColor(R.color.green));
                    break;
            }
        }

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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_bookkeeping_main_group, null);
        Map<String, Object> map = grouplist.get(groupPosition);
         String  expendvalue = String.valueOf(map.get("out"));//获取总支出值
        String incomevalue = String.valueOf(map.get("in"));//获取总支出值
        this.groupPosition = groupPosition;
        String date = (String) map.get("dayOfMonth");
        if (convertView != null) {
            viewHolder = new GroupViewHolder();
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.b_mainadt_date);
            viewHolder.tv_expend = (TextView) convertView.findViewById(R.id.b_mainadt_expend);
            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.b_mainadt_income);
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.main_groupposition, groupPosition);
            convertView.setTag(R.id.main_childposition, -1);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        viewHolder.tv_date.setText(date);
        viewHolder.tv_expend.setText("支：" + expendvalue+"");
        viewHolder.tv_income.setText("收：" +incomevalue );
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
        private LinearLayout child_linear;
        private View line_view;
        private TextView child_second_textview;
        private TextView child_text;
        private ImageView child_image_type;
    }

    private static class GroupViewHolder {
        private TextView tv_date;
        private TextView tv_expend;
        private TextView tv_income;
    }

}

