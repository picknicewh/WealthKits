package com.cfjn.javacf.activity.fund;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 松果撤单成功
 *  版本说明：代码规范整改
 *  附加注释：松果撤单调提示用户收益进度
 *  主要接口：暂无
 */
public class SgRevokeSuccessActivity extends Activity implements View.OnClickListener {
    /**
     * 确认提示
     */
    @Bind(R.id.tv_prompt)
    TextView tvPrompt;
    /**
     * 是基金还是现金宝判断
     */
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_revoke_ok);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");

        if (type.equals("022")) {
            tvPrompt.setText(R.string.songguo_revoke);
        } else {
            tvPrompt.setText("撤单申请提交成功！");
        }
    }


    @OnClick({R.id.ib_back, R.id.b_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                G.ISOUTORDEER = true;
                finish();
                break;
            case R.id.b_confirm:
                G.ISOUTORDEER = true;
                finish();
                break;
        }
    }
}
