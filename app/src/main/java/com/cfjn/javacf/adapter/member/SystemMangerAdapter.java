package com.cfjn.javacf.adapter.member;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserMessageDb;
import com.cfjn.javacf.util.G;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 系统消息适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class SystemMangerAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private UserMessageDb udb;

    public SystemMangerAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
        udb = new UserMessageDb();
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
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_system_manger, null);
            new ViewHolder(convertView);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tvContent.setText(list.get(position).get("content"));
        viewHolder.tvTitle.setText(list.get(position).get("MxTitle"));
        viewHolder.tvTime.setText(list.get(position).get("dateTime"));
        //查询系统消息查看状态
        SQLiteDatabase db = MainActivity.UMDB.getReadableDatabase();
        Cursor cursor = udb.select(db, position + 1);
        while (cursor.moveToNext()) {
            try {
                String type = cursor.getString(cursor.getColumnIndex("usertype"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                //如果为YES即是已经查看过了，显示为灰色 否则显示黑色
                if (type.equals("YES")) {
                    viewHolder.ivDol.setVisibility(View.GONE);
                    viewHolder.tvContent.setTextColor(Color.parseColor("#888888"));
                    viewHolder.tvTitle.setTextColor(Color.parseColor("#888888"));
                    viewHolder.tvTime.setTextColor(Color.parseColor("#888888"));
                } else {
                    viewHolder.tvContent.setTextColor(Color.BLACK);
                    viewHolder.tvTitle.setTextColor(Color.BLACK);
                    viewHolder.tvTime.setTextColor(Color.BLACK);
                    viewHolder.ivDol.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                G.log(e);
            }

        }
        db.close();
        return convertView;
    }

    class ViewHolder {
        /**
         * 小红点
         */
        @Bind(R.id.iv_dol)
        ImageView ivDol;
        /**
         * 公告标题
         */
        @Bind(R.id.tv_title)
        TextView tvTitle;
        /**
         * 公告时间
         */
        @Bind(R.id.tv_time)
        TextView tvTime;
        /**
         * 公告内容
         */
        @Bind(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
