package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.util.G;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 投资风格测试成功
 *  版本说明：代码规范整改
 *  附加注释：显示用户投资风格，可以重新测试 立即投资
 *  主要接口：暂无
 */
public class StyleActivity extends Activity implements View.OnClickListener {

    /**
     * 投资风格
     */
    @Bind(R.id.tv_style_type)
    TextView tvStyleType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finsh_style_eva);
        ButterKnife.bind(this);
        String type = getIntent().getStringExtra("values");
        if (!G.isEmteny(type)) {
            tvStyleType.setText(type);
        }
    }

    @OnClick({R.id.ib_back, R.id.b_again, R.id.b_investment})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.b_again:
                //重新测试
                intent = new Intent(this, StyleEvaluationActivity.class);
                finish();
                break;
            case R.id.b_investment:
                //立即投资
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                break;
        }
        if (null!=intent){
            startActivity(intent);
        }
    }

}
