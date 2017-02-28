package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.ProblemVo;
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
 * 名称： 常见问题详细
 * 版本说明：代码规范整改
 * 附加注释：显示对应板块问题详细
 * 主要接口：获取对应板块的问题详细
 */
public class QuestionDetailsActivity extends Activity implements OKHttpListener {
    /**
     * 问题标题
     */
    @Bind(R.id.tv_tilte)
    TextView tvTilte;
    /**
     * 问题列表
     */
    @Bind(R.id.lv_questions)
    ListView lvQuestions;
    /**
     * 加载提示
     */
    @Bind(R.id.tv_load_tip)
    TextView tvLoadTip;
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
                G.showToast(QuestionDetailsActivity.this, "获取数据失败，请再次尝试！");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);
        ButterKnife.bind(this);
        mapList = new ArrayList<>();
        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        String questionNo = intent.getStringExtra("questionNo");
        getDetails(questionNo);
        tvTilte.setText(question);

        simpleAdapter = new SimpleAdapter(QuestionDetailsActivity.this, mapList, R.layout.item_questiondata, new String[]{"question", "answer"}, new int[]{R.id.tv_question_title, R.id.tv_answer});
        lvQuestions.setAdapter(simpleAdapter);
    }

    /**
     * 获取问题详细
     * @param questionNo  板块号（分板块查询）
     */
    private void getDetails(String questionNo) {
        Map<String, String> param = new HashMap<>();
        param.put("questionNo", questionNo);
        OkHttpUtil.sendPost(ApiUri.QUESTION, param, this);
    }

    private void setMapList(List<ProblemVo.Date> dateList) {
        for (int i = 0; i < dateList.size(); i++) {
            String answer = dateList.get(i).getAnswer();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("question", dateList.get(i).getQuestion());
            map.put("answer", answer);
            mapList.add(map);
        }
    }

    @Override
    public void onSuccess(String uri, String result) {
        if (uri.equals(ApiUri.QUESTION)) {
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
    public void onError(String uri, String error) {
        handler.sendEmptyMessage(0x13);
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }
}
