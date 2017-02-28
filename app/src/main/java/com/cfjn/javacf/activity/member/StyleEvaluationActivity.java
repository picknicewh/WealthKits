package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.adapter.member.StyleEvaluationAdapter;
import com.cfjn.javacf.modle.StyleCodeVo;
import com.cfjn.javacf.modle.StyleEva;
import com.cfjn.javacf.modle.StyleEvaVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LoadingDialog;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 个人风格测试
 *  版本说明：代码规范整改
 *  附加注释：测试用户投资风格
 *  主要接口：1.获取测试题
 *            2.提交用户测试信息
 */
public class StyleEvaluationActivity extends Activity implements OKHttpListener {
    /**
     * 测试标题
     */
    @Bind(R.id.tv_evaStyle_title)
    TextView tvEvaStyleTitle;
    /**
     * 断网页面
     */
    @Bind(R.id.ll_notNetwork)
    RelativeLayout llNotNetwork;
    /**
     * 测试题listview
     */
    @Bind(R.id.lv_evaStyle)
    ListView lvEvaStyle;
    /**
     * 下一题
     */
    private Button bStyle;

    private StyleEvaluationAdapter adapter;
    /**
     * 测试题list
     */
    private List<List<String>>clist;
    /**
     * 测试标题list
     */
    private List<String> tlist;
    /**
     * 第几题
     */
    private int index = 0;
    public StringBuffer parms;
    private UserInfo userInfo;
    private LoadingDialog loadingDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                loadingDialog.dismiss();
                llNotNetwork.setVisibility(View.GONE);
                adapter = new StyleEvaluationAdapter(StyleEvaluationActivity.this, clist.get(index));
                lvEvaStyle.setAdapter(adapter);
                tvEvaStyleTitle.setText("(" + (index + 1) + "/" + tlist.size() + ")" + " " + tlist.get(index));
            } else if (msg.what == 0x12) {
                loadingDialog.dismiss();
                G.showToast(StyleEvaluationActivity.this, getResources().getString(R.string.notWorkPrompt));
                llNotNetwork.setVisibility(View.VISIBLE);
            } else if (msg.what == 0x13) {
                G.showToast(StyleEvaluationActivity.this, "暂无获取到数据，请稍后再试！");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_evaluation);
        ButterKnife.bind(this);
        loadingDialog=LoadingDialog.regstLoading(this);
        initializeMode();
        initializeView();
        getEvaStyleTest();
    }

    private void initializeMode() {
        parms = new StringBuffer();
        clist = new ArrayList<>();
        tlist = new ArrayList<>();
        userInfo = new UserInfo(this);
    }

    private void initializeView() {
        View v = LayoutInflater.from(this).inflate(R.layout.btn_membercenter_style, null);
        lvEvaStyle.addFooterView(v);
        bStyle = (Button) v.findViewById(R.id.btn_style);
        bStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clist.size() <= 0) {
                    G.showToast(StyleEvaluationActivity.this, "与服务器连接失败，亲请检查是否连接网络");
                    return;
                }
                index++;
                parms.append((StyleEvaluationAdapter.CHANGVALUE + 1) + ",");
                //点击到最后题Button显示查看结果并没有点击Button
                if (index >= tlist.size() - 1) {
                    bStyle.setText("查看结果");
                }
                //最后一题的时候点击了Button执行风格统计
                if (index >= tlist.size()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", userInfo.getToken());
                    map.put("secretKey", userInfo.getSecretKey());
                    map.put("userSelect", parms.toString());
                    OkHttpUtil.sendPost(ApiUri.VETTING, map, StyleEvaluationActivity.this);
                    bStyle.setClickable(false);
                    return;
                }
                adapter.notifyDataSetChanged();
                handler.sendEmptyMessage(0x11);
            }
        });

        llNotNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llNotNetwork.setVisibility(View.GONE);
                tlist.clear();
                clist.clear();
                getEvaStyleTest();
            }
        });
    }

    /**
     * 获取测试题
     */
    private void getEvaStyleTest() {
        loadingDialog.show();
        OkHttpUtil.sendPost(ApiUri.ASSTESTING, null, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.VETTING)) {
                    StyleCodeVo styleCodeVo = gson.fromJson(result, StyleCodeVo.class);
                    String values = styleCodeVo.getObject().toString();
                    userInfo.setStyleEva(values);
                    Intent intent = new Intent(StyleEvaluationActivity.this, StyleActivity.class);
                    intent.putExtra("values", values);
                    startActivity(intent);
                    finish();
                } else if (uri.equals(ApiUri.ASSTESTING)) {
                    StyleEva styleEva = gson.fromJson(result, StyleEva.class);
                    LinkedList<StyleEvaVo> resources = styleEva.getDate();
                    for (Iterator iterator = resources.iterator(); iterator.hasNext(); ) {
                        StyleEvaVo resource = (StyleEvaVo) iterator.next();
                        String[] s = resource.getOptions().split("\\^");
                        clist.add(Arrays.asList(s));
                        tlist.add(resource.getSubject());
                    }
                    if (clist.size() <= 0) {
                        handler.sendEmptyMessage(0x13);
                        return;
                    }
                    handler.sendEmptyMessage(0x11);
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.VETTING)) {
                    //   G.showToast(StyleEvaluationActivity.this, "由于网络原因，您的评测结果未处理，请再次评测");
                    handler.sendEmptyMessage(0x12);
                } else if (uri.equals(ApiUri.ASSTESTING)) {
                    handler.sendEmptyMessage(0x12);
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }
}
