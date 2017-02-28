package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cfjn.javacf.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 系统消息详情
 * 版本说明：代码规范整改
 * 附加注释：显示系统消息明细
 * 主要接口：暂无
 */
public class SystemDetailsActivity extends Activity {
    /**
     * 返回
     */
    @Bind(R.id.ib_back)
    ImageButton ibBack;
    /**
     * 基金公司名称
     */
    @Bind(R.id.tv_tilte)
    TextView tvTilte;
    /**
     * 公告主题
     */
    @Bind(R.id.tv_them)
    TextView tvThem;
    /**
     * 公告内容
     */
    @Bind(R.id.tv_content)
    TextView tvContent;
    /**
     * 基金公司
     */
    @Bind(R.id.tv_address)
    TextView tvAddress;
    /**
     * 公告时间
     */
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_content);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        tvTilte.setText(intent.getStringExtra("MxTitle"));
        tvThem.setText(intent.getStringExtra("title"));
        tvContent.setText( intent.getStringExtra("content"));
        tvAddress.setText(intent.getStringExtra("source"));
        tvTime.setText( intent.getStringExtra("dateTime"));

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
