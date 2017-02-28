package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.adapter.member.CounterAdapter;
import com.cfjn.javacf.modle.AccountManagementVo;
import com.cfjn.javacf.modle.AccountVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LoadingDialog;
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
 * 名称： 柜台管理
 * 版本说明：代码规范整改
 * 附加注释：各个柜台信息 目前有数米/天风/松果
 * 主要接口：1.获取所有柜台信息
 */
public class CounterMangerActivity extends Activity implements OKHttpListener {
    /**
     * 无网络显示
     */
    @Bind(R.id.rl_notNetwork)
    RelativeLayout rlNotNetwork;
    /**
     * 柜台listView
     */
    @Bind(R.id.lv_counter)
    ListView lvCounter;

    private LoadingDialog loadingDialog;
    private List<Map<String, Object>> mapList;
    private CounterAdapter counterAdapter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_management);
        ButterKnife.bind(this);
        userInfo = UserInfo.getInstance(this);
        loadingDialog =LoadingDialog.regstLoading(this);
        mapList = new ArrayList<Map<String, Object>>();
        counterAdapter = new CounterAdapter(mapList, this, this);
        lvCounter.setAdapter(counterAdapter);

        G.log(userInfo.getSmTokenSecret() + "---SmTokenSecret--------------SmToken--------" + userInfo.getSmToken());
        G.log(userInfo.getToken() + "----Token----------------SecretKey-----" + userInfo.getSecretKey());
    }

    /**
     * 界面刷新反返回显示都去获取一遍柜台数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapList.clear();
        loadingDialog.show();
        getCounterManger();
    }

    /**
     * 获取各个柜台的数据
     */
    private void getCounterManger() {
        Map<String, String> map = new HashMap();
        map.put("token", userInfo.getToken());
        map.put("secretKey", userInfo.getSecretKey());
        OkHttpUtil.sendPost(ApiUri.ACCOUNTMANGER, map, this);
    }

    @Override
    public void onSuccess(String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AccountVo accountVo = new Gson().fromJson(result, AccountVo.class);
                {
                    loadingDialog.dismiss();
                    rlNotNetwork.setVisibility(View.GONE);
                    lvCounter.setVisibility(View.VISIBLE);
                    if (null == accountVo) {
                        return;
                    }
                    if (accountVo.getResultCode().equals("1")) {
                        LoadingDialog dialog = new LoadingDialog(CounterMangerActivity.this, R.style.myDialogTheme, 1);
                        dialog.show();
                        return;
                    }
                    mapList.clear();
                    // 通过判断每个柜台返回的的真实姓名是否为空来判断是否开过户
                    for (int i = 0; i < accountVo.getDate().size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        AccountManagementVo acmanger = accountVo.getDate().get(i);
                        if (acmanger.getUserInformation() != null && !G.isEmteny(acmanger.getUserInformation().getName())) {
                            map.put("userInformation", (acmanger.getUserInformation()));
                            map.put("userBankCardList", (acmanger.getUserBankCardList()));
                            map.put("name", acmanger.getSecuritiesAccountName());
                            map.put("bankNuber", String.valueOf(acmanger.getUserBankCardList().size()));
                            map.put("isGoBank", "true");
                        } else {
                            map.put("prompt", acmanger.getPrompt());
                            map.put("isGoBank", "false");
                            map.put("name", acmanger.getSecuritiesAccountName());
                        }
                        mapList.add(map);
                        counterAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onError(String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                rlNotNetwork.setVisibility(View.VISIBLE);
                lvCounter.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.ib_back, R.id.rl_notNetwork})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_notNetwork:
                mapList.clear();
                rlNotNetwork.setVisibility(View.GONE);
                loadingDialog.show();
                getCounterManger();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=loadingDialog){
            loadingDialog.cancel();
        }
    }
}
