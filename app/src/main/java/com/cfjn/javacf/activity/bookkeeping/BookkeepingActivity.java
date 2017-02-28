package com.cfjn.javacf.activity.bookkeeping;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.member.MemberCenterActivity;
import com.cfjn.javacf.adapter.bookkeeping.MainAdapter;
import com.cfjn.javacf.base.BaseBookingActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.ChildType;
import com.cfjn.javacf.modle.FinancialCommon;
import com.cfjn.javacf.modle.UserAccountBook;
import com.cfjn.javacf.modle.UserAccountBookAllStatistics;
import com.cfjn.javacf.modle.UserAccountInfo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.CircleProcess;
import com.cfjn.javacf.widget.DeleteVoiceAccountPopup;
import com.cfjn.javacf.widget.NoScrollExpandableList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-3
 *  名称： 科学记账
 *  版本说明：代码规范整改
 *  附加注释：显示记账信息，分月查询各月份的信息，支持语音记账
 *  主要接口：1.获取记账图标信息
 *            2.获取记账信息
 */
public class BookkeepingActivity extends BaseBookingActivity implements View.OnClickListener, OKHttpListener {
    /**
     * 日期
     */
    @Bind(R.id.tv_date)
    TextView tvDate;
    /**
     * 左箭头
     */
    @Bind(R.id.ib_up)
    ImageButton ibUp;
    /**
     * 右箭头
     */
    @Bind(R.id.ib_down)
    ImageButton ibDown;
    /**
     * 收入
     */
    @Bind(R.id.tv_income)
    TextView tvIncome;
    /**
     * 支出
     */
    @Bind(R.id.tv_expend)
    TextView tvExpend;
    /**
     * 没有数据时显示的内容
     */
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    /**
     * 二级列表
     * 收支详情
     */
    @Bind(R.id.el_expandList)
    NoScrollExpandableList elExpandList;
    /**
     * 滚动
     */
    @Bind(R.id.sv_scroll)
    ScrollView svScroll;
    /**
     * 加载记账详情数据的动画图片
     */
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    /**
     * 无网络加载时显示加载失败
     */
    @Bind(R.id.rl_notNetwork)
    RelativeLayout rlNotNetwork;
    /**
     * 预算余额水位控件
     */
    @Bind(R.id.cp_process)
    CircleProcess cpProcess;
    /**
     * 预算入口
     */
    @Bind(R.id.rl_budge)
    RelativeLayout rlBudge;

    /**
     * 适配器
     */
    private MainAdapter bookeepAdapter;
    /**
     * 一级数据
     */
    private List<Map<String, Object>> groupArray;
    /**
     * 二级数据
     */
    private List<List<Map<String, Object>>> childArray;
    /**
     * 当前展开的group
     */
    private int groupPosition = -1;
    private int childPosition = -1;
    /**
     * 日期记录
     */
    private String dateTmp[] = new String[31];
    /**
     * 是否加载二级数据
     */
    private boolean[] isChildLoaded = new boolean[31];
    /**
     * xml解析工具
     */
    private XStream xStream = new XStream(new DomDriver());
    /**
     * 总支出
     */
    private double totleExpend;
    private String mounthDate[] = new String[31];
    private String dateTemp[] = new String[31];
    private int days;
    private DecimalFormat df = new DecimalFormat("########0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeeping);
        ButterKnife.bind(this);
        initializeView();
        initializeMode();
        Log.i("oooooooo", new UserInfo(this).getSecretKey() + "=========" + new UserInfo(this).getToken());
    }

    private void initializeView() {
        disableAutoScrollToBottom();
        setCircleParam();
        setDateText(tvDate);
        setUpBtn(ibUp);
        setDownBtn(ibDown);
        setUri(ApiUri.USER_ACCOUNT_MAIN);
        setLoadingView(ivLoading);
        initDate();
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_evey);
        ivLoading.startAnimation(operatingAnim);
}

    /**
     * 禁止ScrollView的childview自动滑动到底部
     */
    private void disableAutoScrollToBottom() {
        svScroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        svScroll.setFocusable(true);
        svScroll.setFocusableInTouchMode(true);
        svScroll.fullScroll(ScrollView.FOCUS_UP);
        svScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }

    /**
     * 设置圆的参数
     */
    private void setCircleParam() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int W = mDisplayMetrics.widthPixels;
        int width = W / 7 * 3;
        int height = width;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        rlBudge.setLayoutParams(layoutParams);
        cpProcess.setLayoutParams(layoutParams);
        LinearLayout ll_expend = (LinearLayout) findViewById(R.id.ll_expend);
        LinearLayout ll_income = (LinearLayout) findViewById(R.id.ll_income);
        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(W / 7 * 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        Params.gravity = Gravity.CENTER;
        ll_expend.setLayoutParams(Params);
        ll_income.setLayoutParams(Params);
    }

    @OnClick({R.id.ib_member_center, R.id.ib_pie, R.id.cp_process, R.id.rl_budge, R.id.rl_account})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_pie:
                intent = new Intent();
                intent.setClass(this, ChartActivity.class);
                intent.putExtra("main_date", tvDate.getText().toString());
                intent.putExtra("main_queryDate", queryDate);
                startActivity(intent);
                break;
            case R.id.rl_budge:
                intent = new Intent();
                intent.setClass(this, BudgetActivity.class);
                intent.putExtra("main_date", tvDate.getText().toString());
                intent.putExtra("main_queryDate", queryDate);
                Log.i("queryDate", queryDate);
                startActivity(intent);
                break;
            case R.id.rl_account:
                intent = new Intent();
                intent.putExtra("source", VoiceAccountActivity.SOURCE_ADD);
                intent.putExtra("main_title", "新增记账");
                intent.putExtra("main_date", queryDate);
                intent.setClass(this, VoiceAccountActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.cp_process:
                intent = new Intent();
                intent.setClass(this, BudgetActivity.class);
                intent.putExtra("main_date", tvDate.getText().toString());
                intent.putExtra("main_queryDate", queryDate);
                intent.putExtra("main_totlexpend", totleExpend);
                Log.i("main_totlexpend", totleExpend + "");
                startActivity(intent);
                break;
            case R.id.ib_member_center:
                intent = new Intent();
                intent.setClass(this, MemberCenterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 设置预算的值
     * @param totlebudget 消费
     * @param totlebalance 收益 （我猜的）
     */
    private void setBudgetText(double totlebudget, double totlebalance) {
        if (totlebalance == 0 || totlebudget == 0) {
            rlBudge.setVisibility(View.VISIBLE);
            cpProcess.setVisibility(View.GONE);
        } else {
            rlBudge.setVisibility(View.GONE);
            cpProcess.setVisibility(View.VISIBLE);
            float percent = (float) (totlebalance / totlebudget);
            cpProcess.setSurPlus(totlebalance);
            cpProcess.setProgress(percent);
            cpProcess.startCartoom(100);
            //setPercent(percent);
            //球形体加载完全时，关闭加载.....
        }
    }

    private void initializeMode() {
        groupArray = new ArrayList<Map<String, Object>>();
        childArray = new ArrayList<List<Map<String, Object>>>();
        // 创建一级二级数据容器
        //  Log.i("22222",childArray+"");
        bookeepAdapter = new MainAdapter(this, groupArray, childArray);
        elExpandList.setAdapter(bookeepAdapter);
        elExpandList.setGroupIndicator(null);
        elExpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                editDeitail(groupPosition, childPosition);
                return false;
            }
        });
        elExpandList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int groupId = (Integer) view.getTag(R.id.main_groupposition);
                int childId = (Integer) view.getTag(R.id.main_childposition);
                if (childId == -1) {//如果长按父类类，返回false，不执行下面的删除
                    return false;
                }
                Log.i("groupId", "groupId:" + groupId + "childId:" + childId);
                DeleteVoiceAccountPopup popup = new DeleteVoiceAccountPopup(BookkeepingActivity.this, null, getApplicationContext(), childArray, groupId, childId, false);
                popup.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                return true;
            }
        });

    }

    /**
     * 进入编辑页面
     * @param groupposition 一级列表 position
     * @param childposition  二级列表 position
     */
    private void editDeitail(int groupposition, int childposition) {
        Map<String, Object> map = childArray.get(groupposition).get(childposition);
        int classifyCode = Integer.parseInt((map.get("classifyCode").toString()));
        double totlevalue;//获取总值
        Intent intent = new Intent();
        ChildType childType = G.type.getChildTypeByCode(classifyCode);
        if (childType.getType() == 0) {
            totlevalue = (double) map.get("out");
        } else {
            totlevalue = (double) map.get("in");
        }
        intent.putExtra("edit_value", df.format(totlevalue));
        intent.putExtra("edit_name", childType.getName());
        intent.putExtra("edit_image", childType.getIcon());
        intent.putExtra("edit_remark", String.valueOf(map.get("remark")));
        intent.putExtra("edit_type", childType.getType());
        intent.putExtra("edit_date", (String) map.get("createDate"));
        intent.putExtra("edit_rcode", childType.getRootCode());
        intent.putExtra("edit_classtifycode", classifyCode);
        intent.putExtra("id", map.get("id").toString());
        intent.putExtra("source", VoiceAccountActivity.SOURCE_EDIT);
        intent.putExtra("querydate", queryDate);
        intent.setClass(getApplicationContext(), VoiceAccountActivity.class);
        this.startActivity(intent);
    }

    /**
     * 处理一级数据
     */
    private void processGroupData(List<FinancialCommon> userAccountBookMonthList) {
        Collections.sort(userAccountBookMonthList);
        groupArray.clear();
        childArray.clear();
        // 计算每日实体类中有多少天
        days = userAccountBookMonthList.size();
        // 定义每日实体类
        FinancialCommon financialCommon;
        // 每日收入
        String income;
        // 每日支出
        String expend;
        // 周几
        String weekName;
        // 几号
        String dayName;
        // 存放数据的map
        Map<String, Object> map;
        for (int i = 0; i < days; i++) {
            financialCommon = userAccountBookMonthList.get(i);
            income = G.momeyFormat(financialCommon.getTotalIncome());
            expend = G.momeyFormat(financialCommon.getTotalExpend());
            weekName = financialCommon.getWeekName();
            dayName = financialCommon.getDayName();
            dateTmp[i] = dayName;
            mounthDate[i] = queryDate + "-" + dayName;
            // 将一级数据放入容器中n
            map = new HashMap<String, Object>();
            map.put("dayOfWeek", weekName);
            map.put("dayOfMonth", getQueryTime() + "-" + dayName);
            map.put("in", income);
            map.put("out", expend);
            groupArray.add(map);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            map = new HashMap<String, Object>();
            map.put("id", "");
            map.put("type", "0");
            map.put("content", "");
            map.put("classifyCode", 0);
            map.put("in", "");
            map.put("out", "");
            map.put("time_tmp", "");
            map.put("createDate", "");
            map.put("remark", "");
            list.add(map);
            childArray.add(list);
        }
        if (groupArray.size() <= 0) {
            tvNodata.setVisibility(View.VISIBLE);
            tvNodata.setText("当前月份没有记账哦...");
            ivLoading.clearAnimation();
            ivLoading.setVisibility(View.GONE);
            rlNotNetwork.setVisibility(View.GONE);
            bookeepAdapter.notifyDataSetChanged();
            isLoaded3 = true;
            return;
        } else {
            tvNodata.setVisibility(View.GONE);
            initMessageByMonth(queryDate);
        }
    }

    /**
     * 处理二级数据
     * @param userAccountBookList 记账信息实体类
     */
    private void processChildData(List<UserAccountBook> userAccountBookList) {
        Collections.sort(userAccountBookList);
        if (childArray.size() == 0) {
            return;
        } else {
            childArray.clear();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // id
        String id;
        // 日期时间戳
        String datetime_tmp;
        // 类型
        int type;
        // 名称
        String name;
        // 金额
        double money;
        // 类型代码
        int classifyCode;
        // 备注
        String remark;
        DecimalFormat df = new DecimalFormat("########0.00");
        for (int i = 0; i < userAccountBookList.size(); i++) {
            UserAccountBook userAccountBook = userAccountBookList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            id = userAccountBook.getId();
            dateTemp[i] = userAccountBook.getCreateDate().substring(0, 10);
            Log.i("dateTemp", dateTemp[i] + "");
            // 例：2014-12-25 11:53
            datetime_tmp = userAccountBook.getCreateDate();
            type = Integer.parseInt(userAccountBook.getType());
            name = userAccountBook.getContent();
            money = Double.parseDouble(df.format(userAccountBook.getMoney()));
            classifyCode = userAccountBook.getClassifyCode();
            remark = userAccountBook.getRemark();
            map.put("id", id);
            map.put("type", type);
            map.put("content", name);
            map.put("classifyCode", classifyCode);
            map.put("in", money);
            map.put("out", money);
            map.put("createDate", datetime_tmp);
            map.put("remark", remark);
            list.add(map);
        }
        for (int k = 0; k < days; k++) {
            List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < list.size(); i++) {
                if (mounthDate[k].equals(dateTemp[i])) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", list.get(i).get("id"));
                    map.put("type", list.get(i).get("type"));
                    map.put("content", list.get(i).get("content"));
                    map.put("classifyCode", list.get(i).get("classifyCode"));
                    map.put("in", list.get(i).get("in"));
                    map.put("out", list.get(i).get("out"));
                    map.put("time_tmp", list.get(i).get("time_tmp"));
                    map.put("createDate", list.get(i).get("createDate"));
                    map.put("remark", list.get(i).get("remark"));
                    clist.add(map);
                }
            }
            childArray.add(clist);
        }
        for (int i = 0; i < bookeepAdapter.getGroupCount(); i++) {
            elExpandList.expandGroup(i);

        }
    }

    /**
     * 获取图表数据和记账信息
     */
    public void getContentManger() {
        getChartManger(queryDate);
        getAccountManger(queryDate);
        //   initMessageByMonth(queryDate);
        initializeMode();
    }

    /**
     * 页面重新展现时重新获取数据
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        getContentManger();
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_evey);
        ivLoading.startAnimation(operatingAnim);
    }

    /**
     * 查询某月余额的具体明细列表
     */
    private void initMessageByMonth(String queryDate) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put("loginName", new UserInfo(this).getLoginName());
        requestParamsMap.put("date", queryDate);
        OkHttpUtil.sendPost(ApiUri.QUERY_MESSAGE_BY_DAY, requestParamsMap, this);
        isLoaded3 = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                getChartManger(queryDate);
                getAccountManger(queryDate);
                //     initMessageByMonth(queryDate);
                initializeMode();
                break;
        }
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        try {
            final String result2 = URLDecoder.decode(result, "utf-8");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (uri.equals(ApiUri.USER_ACCOUNT_MAIN)) {
                        isLoaded = true;
                        xStream.alias("UserAccountBookVo", UserAccountInfo.class);
                        UserAccountInfo userAccountInfo = (UserAccountInfo) xStream.fromXML(result2);
                        String income =G.momeyFormat(userAccountInfo.getTotalIncome());
                        String expend = G.momeyFormat(userAccountInfo.getTotalExpend());
                        Log.i("income",userAccountInfo.getTotalIncome()+"");
                        Log.i("income",userAccountInfo.getTotalExpend()+"");
                        tvExpend.setText(expend);
                        tvIncome.setText(income);
                        setBudgetText(userAccountInfo.getBudget(), userAccountInfo.getBalance());
                        totleExpend = userAccountInfo.getTotalExpend();
                    } else if (uri.equals(ApiUri.QUERY_STATISTICS_BY_MONTH)) {
                        isLoaded2 = true;
                        // 设置别名
                        xStream.alias("UserAccountBookAllStatisticsVo", UserAccountBookAllStatistics.class);
                        xStream.alias("FinancialCommonVo", FinancialCommon.class);
                        // xml转记账明细每月实体类
                        UserAccountBookAllStatistics userAccountBookAllStatistics = (UserAccountBookAllStatistics) xStream.fromXML(result2);
                        // 获得每日实体类集合
                        List<FinancialCommon> userAccountBookMonthList = userAccountBookAllStatistics.getUserAccountBookMonthList();
                        processGroupData(userAccountBookMonthList);
                        isChildLoaded = new boolean[31];
                        bookeepAdapter.notifyDataSetChanged();
                    } else if (uri.equals(ApiUri.QUERY_MESSAGE_BY_DAY)) {
                        isLoaded3 = true;
                        ivLoading.clearAnimation();
                        ivLoading.setVisibility(View.GONE);
                        //    tvNodata.setVisibility(View.GONE);
                        rlNotNetwork.setVisibility(View.GONE);
                        elExpandList.setVisibility(View.VISIBLE);
                        xStream.alias("UserAccountBook", UserAccountBook.class);
                        // xml转记账实体类
                        Log.i("TAG", xStream.fromXML(result2) + "");
                        List<UserAccountBook> userAccountBookList = (List<UserAccountBook>) xStream.fromXML(result2);
                        processChildData(userAccountBookList);
                        bookeepAdapter.notifyDataSetInvalidated();

                    }
                }
            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(final String uri, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.QUERY_STATISTICS_BY_MONTH)) {
                    ivLoading.clearAnimation();
                    ivLoading.setVisibility(View.GONE);
                    tvNodata.setVisibility(View.GONE);
                    rlNotNetwork.setVisibility(View.VISIBLE);
                    elExpandList.setVisibility(View.GONE);
                    setBudgetText(1, -1);
                    tvExpend.setText("0.00");
                    tvIncome.setText("0.00");
                    rlNotNetwork.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getContentManger();
                        }
                    });
                } else if (uri.equals(ApiUri.USER_ACCOUNT_MAIN)) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
