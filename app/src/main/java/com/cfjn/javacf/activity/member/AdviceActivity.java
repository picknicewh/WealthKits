package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.adapter.member.SpinerAdapter;
import com.cfjn.javacf.modle.AdviceVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

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
 * 名称： 意见建议
 * 版本说明：代码规范整改
 * 附加注释：提交用户意见
 * 主要接口：1.提交用户意见
 */
public class AdviceActivity extends Activity implements OKHttpListener {
    /**
     * 标题选择器
     */
    @Bind(R.id.s_title)
    Spinner sTitle;
    /**
     * 内容编辑框
     */
    @Bind(R.id.et_advice)
    EditText etAdvice;

    /**
     * 建议标题
     */
    private String title;
    /**
     * 建议类型
     */
    private int type;
    /**
     * 建议内容
     */
    private String content;
    private SpinnerAdapter spinnerAdapter;
    private List<String> list;
    private Map<String, String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        list.add("建议");
        list.add("缺陷");
        list.add("疑问");
        list.add("求助");
        spinnerAdapter = new SpinerAdapter(this, list);
        sTitle.setAdapter(spinnerAdapter);
        sTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title = sTitle.getItemAtPosition(position).toString();
                type = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 提交
     * @param view 提交点击事件
     */
    public void submit(View view) {
        content = etAdvice.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            G.showToast(this, "内容不能为空哦！");
        } else {
            sendAdvice(content);
        }
    }

    /**
     * 提交意见建议
     *
     * @param content 意见内容
     */
    private void sendAdvice(String content) {
        map = new HashMap<>();
        map.put("loginName", UserInfo.getInstance(this).getLoginName());
        map.put("title", title);
        map.put("type", String.valueOf(type));
        map.put("contents", content);
        OkHttpUtil.sendPost(ApiUri.USER_ADVICE, map, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.USER_ADVICE)) {
                    AdviceVo adviceVo = new Gson().fromJson(result, AdviceVo.class);
                    if (null == adviceVo) {
                        G.showToast(AdviceActivity.this, "数据提交失败，请再次重试！谢谢亲的反馈");
                        return;
                    }
                    G.showToast(AdviceActivity.this, adviceVo.getMessage() + "  谢谢亲的反馈");
                    finish();
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.USER_ADVICE)) {
                    G.showToast(AdviceActivity.this, "网络连接失败，请再次重试！谢谢亲的反馈");
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }
}
