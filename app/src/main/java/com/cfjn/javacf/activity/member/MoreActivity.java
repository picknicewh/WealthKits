package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.CommonAdapter;
import com.cfjn.javacf.adapter.ViewHolder;
import com.cfjn.javacf.util.PackageUtils;
import com.cfjn.javacf.util.UpdateManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 更多
 * 版本说明：代码规范整改
 * 附加注释： 会员中心更多页面选项
 * 主要接口：暂无
 */
public class MoreActivity extends Activity {
    @Bind(R.id.lv_more)
    ListView lvMore;
    private TextView tvVersionCode;

    private CommonAdapter<String> commonAdapter;
    private List<String> mlist;
    /**
     * 系统更新
     */
    private UpdateManager updateManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        updateManager = new UpdateManager(this);
        //添加每个单元选项
        addValues();
        commonAdapter = new CommonAdapter<String>(this, mlist, R.layout.item_more) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.tv_moreadapter, mlist.get(position));
            }
        };
        //添加版本号布局并设置版本号
        View view = LayoutInflater.from(this).inflate(R.layout.more_version, null);
        tvVersionCode = (TextView) view.findViewById(R.id.versionCode_more);
        tvVersionCode.setText("V" + PackageUtils.getVersionName(MoreActivity.this));
        lvMore.addFooterView(view);
        lvMore.setAdapter(commonAdapter);
        listViewOnClick();
    }

    private void addValues() {
        mlist = new ArrayList<>();
        mlist.add("常见问题");
        mlist.add("推荐给好友");
        mlist.add("关于财富锦囊");
        mlist.add("版本更新");
        mlist.add("我的推荐码");
        mlist.add("意见反馈");
    }

    private void listViewOnClick() {
        lvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0://常见问题
                        intent = new Intent(MoreActivity.this, CommonProblemActivity.class);
                        break;
                    case 1://推荐给好友
                        recommend();
                        break;
                    case 2://关于财富锦囊
                        intent = new Intent(MoreActivity.this, AboutActivity.class);
                        break;
                    case 3://版本更新
                        updateManager.checkUpdate(0);
                        break;
                    case 4://我的推荐码
                        intent = new Intent(MoreActivity.this, RecommendActivity.class);
                        break;
                    case 5://意见反馈
                        intent = new Intent(MoreActivity.this, AdviceActivity.class);
                        break;
                }
                if (null != intent) {
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 推荐给好友
     */
    public void recommend() {
        Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
        intent.setType("text/plain"); // 分享发送的数据类型
        String msg = "我正在使用财富锦囊手机理财，跑赢银行活期利息10倍，能记账，还有投资顾问，很好用，极力推荐http://www.hunme.net/download.html";
        intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
        startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }
}
