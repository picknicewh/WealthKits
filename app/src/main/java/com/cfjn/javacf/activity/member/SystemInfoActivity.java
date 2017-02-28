package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.base.UserMessageDb;
import com.cfjn.javacf.adapter.member.SystemMangerAdapter;
import com.cfjn.javacf.util.G;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 系统消息
 * 版本说明：代码规范整改
 * 附加注释：显示各个基金公司公告或者消息版本维护等
 * 主要接口：暂无
 */
public class SystemInfoActivity extends Activity {
    /**
     * 无网络或者没有消息时显示图片
     */
    @Bind(R.id.iv_notNetwork)
    ImageView ivNotNetwork;
    /**
     * 无网络或者没有消息提示文字
     */
    @Bind(R.id.tv_notNetwork)
    TextView tvNotNetwork;
    /**
     * 无网络或者没有消息容器
     */
    @Bind(R.id.ll_notNetwork)
    RelativeLayout llNotNetwork;
    /**
     * 消息listView
     */
    @Bind(R.id.lv_manger)
    ListView lvManger;

    private List<Map<String, String>> mangerList;
    private SystemMangerAdapter systemMangerAdapter;
    private UserInfo userInfo;
    /**
     * 系统消息数据库
     */
    private UserMessageDb mangerDb;
    /**
     * 因为数据是上一个页面传过来。如果上一个页面传空有两个状态
     * 1 无网络 2 没有数据
     * type判断没有系统消息是不是无网络状态
     */
    private boolean type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_manger);
        ButterKnife.bind(this);

        mangerList = new ArrayList<>();
        userInfo = new UserInfo(this);
        mangerDb = new UserMessageDb();

        type = getIntent().getBooleanExtra("type", false);
        //将消息解析存到mangerList中
        getSystemManger();
        systemMangerAdapter = new SystemMangerAdapter(this, mangerList);
        //显示系统消息
        lvManger.setAdapter(systemMangerAdapter);
        //进入系统消息详情
        lvManger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SystemInfoActivity.this, SystemDetailsActivity.class);
                intent.putExtra("title", mangerList.get(position).get("title"));
                intent.putExtra("source", mangerList.get(position).get("source"));
                intent.putExtra("dateTime", mangerList.get(position).get("dateTime"));
                intent.putExtra("content", mangerList.get(position).get("content"));
                intent.putExtra("MxTitle", mangerList.get(position).get("MxTitle"));
                startActivity(intent);
                SQLiteDatabase db = MainActivity.UMDB.getWritableDatabase();
                mangerDb.update(db, position + 1, "YES");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemMangerAdapter.notifyDataSetChanged();
    }

    /***
     * 查询数据库储存的系统消息
     */
    private void getSystemManger() {
        G.log(userInfo.getMangerSize() + "------------------->>>");
        if (userInfo.getMangerSize() <= 0) {
            llNotNetwork.setVisibility(View.VISIBLE);
            if (type) {
                ivNotNetwork.setImageResource(R.drawable.ic_message);
                tvNotNetwork.setText("您暂时没有收到消息哦！");
            } else {
                ivNotNetwork.setImageResource(R.drawable.ic_failed);
                tvNotNetwork.setText("加载失败，请检查网络！");
            }
            lvManger.setVisibility(View.GONE);
            return;
        }
        //查询消息数据库获取消息
        SQLiteDatabase db = MainActivity.UMDB.getReadableDatabase();
        for (int i = 0; i < userInfo.getMangerSize(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            Cursor cursor = UserMessageDb.select(db, i + 1);
            while (cursor.moveToNext()) {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String Source = cursor.getString(cursor.getColumnIndex("source"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                G.log(Source + "---" + time + "-----" + title + "-----" + content);
                int type = Integer.parseInt(Source);
                String source = null;
                String MxTitle = null;
                switch (type) {
                    case 0:
                        source = "杭州数米网科技有限公司";
                        MxTitle = "数米公告";
                        break;
                    case 1:
                        source = "天风证券股份有限公司";
                        MxTitle = "天风公告";
                        break;
                    case 2:
                        source = "深圳市前海松果互联网金融服务有限公司";
                        MxTitle = "松果公告";
                        break;
                }

                map.put("source", source);
                map.put("MxTitle", MxTitle);
                map.put("title", title);
                map.put("dateTime", time);
                map.put("content", content);
                mangerList.add(map);
            }
        }
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }
}
