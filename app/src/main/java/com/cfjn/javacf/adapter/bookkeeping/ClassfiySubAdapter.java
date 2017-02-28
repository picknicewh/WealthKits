package com.cfjn.javacf.adapter.bookkeeping;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.MybookkeepingDbHepler;
import com.cfjn.javacf.modle.ChildType;

import java.util.List;

/**
 *  作者： wh
 *  时间： 2016-6-27
 *  名称： 记账分类--子类适配器
 *  版本说明：代码规范整改
 *  附加注释：无
 *  主要接口：无
 */
public class ClassfiySubAdapter extends BaseAdapter {
    private Context context;
    private List<List<ChildType>> childTypes;
    ViewHolder holder = null;
    private ChildType childType;
    private int selectedPosition = -1;
    private int position;
    private int type;
    /*
   * 数据库
   * */
    private MybookkeepingDbHepler hepler;
    private SQLiteDatabase database;

    public ClassfiySubAdapter(Context context, List<List<ChildType>> childTypes, int position, int type) {
        this.childTypes = childTypes;
        this.context = context;
        this.position = position;
        this.type = type;
    }

    @Override
    public int getCount() {
        return childTypes.get(position).size();
    }

    @Override
    public Object getItem(int position) {
        return childTypes.get(this.position).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final int mposition = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookeepingclassify_child, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_child_name);
            holder.ib_collection = (ImageButton) convertView.findViewById(R.id.ib_collection);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
     /*   childType = childTypes.get(this.position).get(position);
        final List<ChildType> childTypeList = childTypes.get(this.position);
        hepler = new MybookkeepingDbHepler(context);
        database = hepler.getWritableDatabase();*/
          /*  holder.ib_collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cursor = database.rawQuery("select flag ,rcode from user where rcode!=-1", null);
                    Log.i("222", cursor.getCount()+ "");
                    while (cursor.moveToNext()) {
                        int flag = cursor.getInt(cursor.getColumnIndex("flag"));
                        int rcode = cursor.getInt(cursor.getColumnIndex("rcode"));
                        if (flag == 1 && selectedPosition==position) {
                            childType  = childTypeList.get(selectedPosition);
                            RootType rootType = G.type.getRootTypeByCode(childType.getRootCode());
                            Log.i("111", childType.getName() + "");
                            if (type == 0 && rootType.getCode() == rcode) {
                                hepler.insert(database,1000000 + 1,1000000, rootType.getName() + "-" + childType.getName(), 0, childType.getIcon(), 1);
                                holder.ib_collection.setImageResource(R.drawable.ic_attention_on);
                                Toast.makeText(context, "1添加成功", Toast.LENGTH_LONG).show();
                            } else if (type == 1 && rootType.getCode() == rcode) {
                                hepler.insert(database,6000000 + 1, 6000000 ,rootType.getName(), 1, childType.getIcon(), 1);
                                holder.ib_collection.setImageResource(R.drawable.ic_attention_on);
                                Toast.makeText(context, "2添加成功", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            holder.ib_collection.setImageResource(R.drawable.ic_attention_off);
                        }
                    }
                }
            });*/
        holder.tv_name.setText(childTypes.get(this.position).get(position).getName());
        holder.tv_name.setTextColor(Color.BLACK);
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_name;
        public ImageButton ib_collection;
    }

    public void setselectedPosotion(int posotion) {
        this.selectedPosition = posotion;
        Log.i("position", selectedPosition + "");
    }
}
