package com.cfjn.javacf.adapter.member;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.cfjn.javacf.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 投资风格适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class StyleEvaluationAdapter extends BaseAdapter {
    private Context context;
    private List<String> slist;
    public static int CHANGVALUE;
    private int index;
    public StyleEvaluationAdapter(Context context, List<String> slist) {
        this.context = context;
        this.slist = slist;
    }

    @Override
    public int getCount() {
        return slist.size();
    }

    @Override
    public Object getItem(int position) {
        return slist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.radiobtn_membercenter_style, null);
            new ViewHolder(convertView);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.rbSelectContent.setText(slist.get(position));
        viewHolder.rbSelectContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
//                    G.showToast(context,"你选择的是"+ slist.get(position));
                    index = position;
                    CHANGVALUE = position;
                    notifyDataSetChanged();
                }
            }

        });

        if (index == position) {// 选中的条目和当前的条目是否相等
            viewHolder.rbSelectContent.setChecked(true);
        } else {
            viewHolder.rbSelectContent.setChecked(false);
        }
        return convertView;
    }

     class ViewHolder {
        /**
         *  单选内容
         */
        @Bind(R.id.rb_select_content)
        RadioButton rbSelectContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
