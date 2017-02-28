package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.ProblemVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.LoadingDialog;
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
 * 名称： 常见问题
 * 版本说明：代码规范整改
 * 附加注释：显示常见问题列表
 * 主要接口：获取问题列表
 */
public class CommonProblemActivity extends Activity implements AdapterView.OnItemClickListener, OKHttpListener {
    /**
     * 无网络状态
     */
    @Bind(R.id.ll_notNetwork)
    RelativeLayout llNotNetwork;
    /**
     * listview
     */
    @Bind(R.id.lv_questions)
    ListView lvQuestions;
    /**
     * 网络不稳定时
     */
    @Bind(R.id.tv_load_tip)
    TextView tvLoadTip;
    /**
     * 加载提示框
     */
    private LoadingDialog loadingDialog;
    /**
     * 适配器
     */
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> mapList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                simpleAdapter.notifyDataSetChanged();
                tvLoadTip.setVisibility(View.GONE);
            } else if (msg.what == 0x12) {
                tvLoadTip.setText("暂无数据");
            } else if (msg.what == 0x13) {
                loadingDialog.dismiss();
                tvLoadTip.setVisibility(View.GONE);
                llNotNetwork.setVisibility(View.VISIBLE);
            } else if (msg.what == 0x14) {
                loadingDialog.dismiss();
                llNotNetwork.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
        ButterKnife.bind(this);
        mapList = new ArrayList<>();
        loadingDialog=LoadingDialog.regstLoading(this);
        loadingDialog.show();

        // 初始化adapter,将list的数据匹配item_cf1080_questions的id中.
        simpleAdapter = new SimpleAdapter(this, mapList, R.layout.item_commproblem, new String[]{"question", "answer"}, new int[]{R.id.tv_question_title, R.id.tv_question_sub_title});
        // 将初始化的adapter放入setAdapter中,然后通过lv_questions调用adapter
        lvQuestions.setAdapter(simpleAdapter);
        getCommomProblem();
        lvQuestions.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(CommonProblemActivity.this, QuestionDetailsActivity.class);
        Map<String, Object> map = mapList.get(position);
        String questionNo = map.get("questionNo").toString();
        String question = map.get("question").toString();
        intent.putExtra("questionNo", questionNo);
        intent.putExtra("question", question);
        startActivity(intent);
    }

    /**
     * 获取问题列表
     */
    private void getCommomProblem() {
        Map<String, String> param = new HashMap<>();
        param.put("questionNo", "-1");
        OkHttpUtil.sendPost(ApiUri.QUESTION, param, this);
    }

    private void setMapList(List<ProblemVo.Date> dateList) {
        for (int i = 0; i < dateList.size(); i++) {
            String answer = dateList.get(i).getAnswer();
            answer = answer.replace(";;", "\n");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("question", dateList.get(i).getQuestion());
            map.put("answer", answer);
            map.put("questionNo", dateList.get(i).getQuestionNo());
            map.put("level", dateList.get(i).getLevel());
            mapList.add(map);
        }
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        if (uri.equals(ApiUri.QUESTION)) {
            handler.sendEmptyMessage(0x14);
            ProblemVo problemvo = new Gson().fromJson(result, ProblemVo.class);
            if (problemvo.getDate().size() > 0) {
                setMapList(problemvo.getDate());
                handler.sendEmptyMessage(0x11);
            } else {
                handler.sendEmptyMessage(0x12);
            }
        }
    }

    @Override
    public void onError(final String uri, String error) {
        handler.sendEmptyMessage(0x13);
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }
}
