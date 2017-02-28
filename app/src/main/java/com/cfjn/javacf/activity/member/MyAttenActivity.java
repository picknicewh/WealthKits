package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.fund.FundDetailActivity;
import com.cfjn.javacf.adapter.member.SwipeMenuAdapter;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.AttentionListVo;
import com.cfjn.javacf.modle.FundAttentionVo;
import com.cfjn.javacf.modle.FundobjectVo;
import com.cfjn.javacf.modle.QueryAttentionVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.NetWorkUitls;
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
 *  作者： zll
 *  时间： 2016-6-3
 *  名称： 我的关注
 *  版本说明：代码规范整改
 *  附加注释：获取我的关注，SwipeMenuListView实现侧滑删除
 *  主要接口：1.获取所有关注基金
 *            2.取消关注基金
 */
public class MyAttenActivity extends Activity implements OKHttpListener {
    /**
     * 无网络
     */
    @Bind(R.id.ll_notNetwork)
    RelativeLayout llNotNetwork;
    /**
     * 关注基金listview 可滑动删除
     */
    @Bind(R.id.lv_swipeMenu)
    SwipeMenuListView lvSwipeMenu;
    /**
     * 无关注内容
     */
    @Bind(R.id.ll_notAtten)
    LinearLayout llNotAtten;
    private LoadingDialog loadingDialog;
    /**
     * 货币基金
     */
    private List<Map<String, String>> mapList;
    private SwipeMenuAdapter swipeMenuAdapter;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_atten);
        ButterKnife.bind(this);
        loadingDialog= LoadingDialog.regstLoading(this);
        //初始化逻辑变量
        mapList = new ArrayList<>();
        userInfo = UserInfo.getInstance(this);
        //初始化布局变量
        lvSwipeMenu.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lvSwipeMenu.setMenuCreator(setSwipeMenu());
        swipeMenuAdapter = new SwipeMenuAdapter(mapList, MyAttenActivity.this);
        lvSwipeMenu.setAdapter(swipeMenuAdapter);
        //初始化控制变量
        initSwipeAtten(mapList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapList.clear();
        getAttention();
    }
    /**
     * 设置SwipeMenu属性
     * @return
     */
    private SwipeMenuCreator setSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MyAttenActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(G.dp2px(MyAttenActivity.this,90));
                deleteItem.setTitle("取消关注");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(18);
                menu.addMenuItem(deleteItem);
            }
        };
        return creator;
    }

    /**
     * Swip滑动删除和点击查看
     * @param list
     */
    private void initSwipeAtten(final List<Map<String, String>> list) {
        //滑动删除
        lvSwipeMenu.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    if (!NetWorkUitls.isNetworkAvailable(MyAttenActivity.this)) {
                        G.showToast(MyAttenActivity.this, "检测到网络异常，取消失败，请检查网络！");
                        return false;
                    }
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("secretKey", userInfo.getSecretKey());
                    map.put("token", userInfo.getToken());
                    map.put("code", list.get(position).get("code"));
                    map.put("yesNo", "2");
                    map.put("fundType", list.get(position).get("fundType"));
                    map.put("fundName", list.get(position).get("fundName"));
                    OkHttpUtil.sendPost(ApiUri.ATTENTION, map, MyAttenActivity.this);

                    list.remove(position);
                    setValueType(list);
                    swipeMenuAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        //点击查看
        lvSwipeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyAttenActivity.this, FundDetailActivity.class);
                intent.putExtra("fundCode", mapList.get(position).get("fundcode"));
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.ib_back, R.id.ib_add_atten,R.id.ll_notNetwork})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_add_atten:
                startActivity(new Intent(MyAttenActivity.this, SearchActivity.class));
                break;
            case R.id.ll_notNetwork:
                llNotNetwork.setVisibility(View.GONE);
                mapList.clear();
                getAttention();
                break;
        }
    }

    /**
     * 设置无网络状态显示
     * @param list
     */
    private void setValueType(List<Map<String, String>> list) {
        if (list.size() > 0) {
            llNotAtten.setVisibility(View.GONE);
            lvSwipeMenu.setVisibility(View.VISIBLE);
        } else {
            llNotAtten.setVisibility(View.VISIBLE);
            lvSwipeMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 获取我的关注
     */
    private void getAttention() {
        loadingDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("secretKey", userInfo.getSecretKey());
        map.put("token", userInfo.getToken());
        OkHttpUtil.sendPost(ApiUri.QUERYATTERN, map, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.QUERYATTERN)) {
                    QueryAttentionVo attention = gson.fromJson(result, QueryAttentionVo.class);
                    llNotNetwork.setVisibility(View.GONE);
                    loadingDialog.dismiss();
                    List<FundAttentionVo> date = attention.getDate();
                    if (date == null && date.size() <= 0) {
                        setValueType(mapList);
                        swipeMenuAdapter.notifyDataSetChanged();
                        return;
                    }
                    for (int i = 0; i < date.size(); i++) {
                        if (null == date || null == date.get(i).getUserAttentionList()) {
                            setValueType(mapList);
                            swipeMenuAdapter.notifyDataSetChanged();
                            return;
                        }
                        for (int j = 0; j < date.get(i).getUserAttentionList().size(); j++) {
                            AttentionListVo atten = date.get(i).getUserAttentionList().get(j);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("fundName", atten.getFundName());
                            map.put("createDate", atten.getCreateDate());
                            map.put("duringTheGains", atten.getDuringTheGains() + "");
                            map.put("sameTypeAVG", atten.getSameTypeAVG() + "");
                            map.put("surpassSameType", (atten.getDuringTheGains() - atten.getSameTypeAVG()) + "");
                            map.put("code", atten.getFundCode());
                            map.put("fundType", atten.getFundType());
                            map.put("fundcode", atten.getFundCode());
                            if (date.get(i).getFundType().equals("1")) {
                                //货币基金
                                mapList.add(map);
                            }
                        }
                    }
                    setValueType(mapList);
                    swipeMenuAdapter.notifyDataSetChanged();
                } else if (uri.equals(ApiUri.ATTENTION)) {
                    FundobjectVo fund = gson.fromJson(result, FundobjectVo.class);
                    if (null == fund) {
                        return;
                    }
                    if (fund.getResultCode().equals("0")) {
                        G.showToast(MyAttenActivity.this, fund.getResultDesc());
                    }
                }
            }
        });
    }

    @Override
    public void onError(final String uri, String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.QUERYATTERN)) {
                    llNotNetwork.setVisibility(View.VISIBLE);
                    loadingDialog.dismiss();
                } else {

                }
            }
        });
    }

}
