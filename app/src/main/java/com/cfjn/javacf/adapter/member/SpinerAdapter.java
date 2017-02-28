package com.cfjn.javacf.adapter.member;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfjn.javacf.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：  意见建议类别适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class SpinerAdapter extends BaseAdapter {

    private List<String> title;
    private LayoutInflater mInflater;

    public SpinerAdapter(Context context, List<String> title) {
        this.title = title;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int pos) {
        return title.get(pos).toString();
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_myspinner, null);
            new ViewHolder(convertView);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tvTitle.setText(title.get(pos));
        return convertView;
    }

    class ViewHolder {
        /**
         * 类型标题
         */
        @Bind(R.id.tv_title)
        TextView tvTitle;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
