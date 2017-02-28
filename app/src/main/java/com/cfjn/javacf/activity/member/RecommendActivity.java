package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.StyleCodeVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 推荐给好友
 * 版本说明：代码规范整改
 * 附加注释：填写推荐码，已经推荐过的直接显示推荐码
 * 主要接口：1.获取用户推荐状态
 *           2.提交推荐码
 */
public class RecommendActivity extends Activity implements View.OnClickListener, OKHttpListener {
    /**
     * 推荐码
     */
    @Bind(R.id.et_icode)
    EditText etIcode;
    /**
     * 填写验证码页面
     */
    @Bind(R.id.ll_submit)
    LinearLayout llSubmit;
    /**
     * 显示推荐码
     */
    @Bind(R.id.tv_recmmde_code)
    TextView tvRecmmdeCode;
    /**
     * 显示推荐码页面
     */
    @Bind(R.id.ll_code)
    LinearLayout llCode;

    private final int INVITED = 0x000;
    private Handler handler = new MyHandler();
    private Message msg = Message.obtain();
    private UserInfo userInfo;
    /**
     * 推荐码
     */
    private String inviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recommendation);
        ButterKnife.bind(this);
        userInfo = new UserInfo(this);
        llSubmit.setVisibility(View.VISIBLE);
        llCode.setVisibility(View.GONE);
        getQueryInvite();
    }

    @OnClick({R.id.ib_back, R.id.b_submit, R.id.b_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.b_submit:
                inviteCode = etIcode.getText().toString().trim();
                if (TextUtils.isEmpty(inviteCode)) {
                    G.showToast(this, "请输入推荐码");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("token", userInfo.getToken());
                map.put("secretKey", userInfo.getSecretKey());
                map.put("recommendationCode", inviteCode);
                OkHttpUtil.sendPost(ApiUri.RECOMMEND, map, this);
                break;
            case R.id.b_code:
                Intent  intent = new Intent(RecommendActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    /**
     * 是否邀请过
     */
    private void getQueryInvite() {
        Map<String, String> map = new HashMap<>();
        map.put("token", userInfo.getToken());
        map.put("secretKey", userInfo.getSecretKey());
        OkHttpUtil.sendPost(ApiUri.GETRECOMMEND, map, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (ApiUri.RECOMMEND.equals(uri)) {
                    StyleCodeVo styleCodeVo = gson.fromJson(result, StyleCodeVo.class);
                    if (styleCodeVo.getResultDesc() != null) {
                        G.showToast(RecommendActivity.this, styleCodeVo.getResultDesc());
                    }
                    if (styleCodeVo.getResultCode().equals("0")) {
                        msg.what = INVITED;
                        msg.obj = inviteCode;
                        handler.sendMessage(msg);
                    } else {
                        msg.what = 0x13;
                        handler.sendMessage(msg);
                    }

                } else if (uri.equals(ApiUri.GETRECOMMEND)) {
                    StyleCodeVo styleCodeVo = gson.fromJson(result, StyleCodeVo.class);
                    if (!styleCodeVo.getResultDesc().equals("未提交过推荐码")) {
                        msg.what = INVITED;
                        msg.obj = styleCodeVo.getObject();
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        if (uri.equals(ApiUri.GETRECOMMEND)) {
            handler.sendEmptyMessage(0x12);
        }
    }



    /**
     * handle显示UI机制
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INVITED:
                    llSubmit.setVisibility(View.GONE);
                    llCode.setVisibility(View.VISIBLE);
                    tvRecmmdeCode.setText((CharSequence) msg.obj);
                    break;
                case 0x13:
                    G.showToast(RecommendActivity.this, "提交失败，请重试！");
                    break;
                case 0x12:
                    G.showToast(RecommendActivity.this, "获取信息失败，请检查网络！");
                    break;
            }
        }
    }
}
