package com.cfjn.javacf.activity.bookkeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.bookkeeping.ChartDetailAdapter;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.ChildType;
import com.cfjn.javacf.modle.UserAccountBook;
import com.cfjn.javacf.modle.UserDateAccount;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.DeleteVoiceAccountPopup;
import com.cfjn.javacf.widget.NavigationBar;
import com.cfjn.javacf.widget.NoScrollExpandableList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 作者： wh
 * 时间： 2016-6-27
 * 名称： 记账-单个分类图表详细信息
 * 版本说明：代码规范整改
 * 附加注释：长按单个图表记录删除记录
 * 主要接口：查询当月单个图表详细信息
 *           查询某天的所有明细列表
 */
public class ChartDetailActivity extends Activity implements View.OnClickListener, OKHttpListener {
    /**
     * 导航条
     */
    @Bind(R.id.nb_bar)
    NavigationBar nbBar;
    /**
     * 日期
     */
    @Bind(R.id.tv_date)
    TextView tvDate;
    /**
     * 类型
     */
    @Bind(R.id.tv_total)
    TextView tvTotal;
    /**
     * 总钱数
     */
    @Bind(R.id.tv_money)
    TextView tvMoney;
    /**
     * 数据加载图片
     */
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    /**
     * 没有数据时显示的内容
     */
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    /**
     * 二级列表显示数据
     */
    @Bind(R.id.expendList)
    NoScrollExpandableList expendList;
    /**
     * 无网络加载时显示加载失败
     */
    @Bind(R.id.rl_notNet)
    RelativeLayout llNotNetwork;

    /**
     * 记一笔
     */
    private RelativeLayout account;

    /**
     * 左箭头
     */
    private ImageButton ib_up;
    /**
     * 右箭头
     */
    private ImageButton ib_down;


    /**
     * 一级数据
     */
    private List<Map<String, Object>> groupArray;
    /**
     * 一级数据
     */
    private List<List<Map<String, Object>>> childArray;
    /**
     * 当前展开的groupItem
     */
    private int groupPosition = 0;
    /**
     * 日期记录
     */
    private String date_tmp[] = new String[31];
    /**
     * 适配器
     */
    private ChartDetailAdapter myadapter;
    /**
     * 标题
     */
    private String title;
    /**
     * 总值
     */
    private String value;
    /**
     * 类型
     */
    private int type;

    /**
     * xml解析工具
     */
    private XStream xstream = new XStream(new DomDriver());
    /**
     * 是否加载二级数据
     */
    private boolean[] isChildLoaded = new boolean[31];
    /**
     * 分类码
     */
    private int classtifyCode;
    /**
     * 年
     */
    private int year;
    /**
     * 月
     */
    private int month;
    /**
     * 记录当前的月份
     */
    private int mmonth;
    /**
     * 记录当前的年份
     */
    private int myear;
    /**
     * 日历
     */
    private Calendar calendar;
    /**
     * 日期
     */
    protected Date date;
    /**
     * 查询日期
     */
    private String queryDate;
    private Intent intent;
    private Animation operatingAnim;
    private String[] createDate;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeepingdetail);
        ButterKnife.bind(this);
        initializeView();
        initializeMode();
    }

    private void initializeMode() {
        groupArray = new ArrayList<>();
        childArray = new ArrayList<>();
        // 创建一级二级数据容器
        myadapter = new ChartDetailAdapter(this, groupArray, childArray, type);
        expendList.setAdapter(myadapter);
        expendList.setGroupIndicator(null);
        expendList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                editDetail(groupPosition, childPosition);
                return true;
            }
        });
        expendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int groupId = (Integer) view.getTag(R.id.detail_groupposition);
                int childId = (Integer) view.getTag(R.id.detail_childposition);
                if (childId == -1) {//如果长按父类类，返回false，不执行下面的删除
                    return false;
                }
                DeleteVoiceAccountPopup popup = new DeleteVoiceAccountPopup(null, ChartDetailActivity.this, getApplicationContext(), childArray, groupId, childId, true);
                popup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                return true;
            }
        });
    }

    private void editDetail(int mLastGroupPosition, int mLastChildPosition) {
        Map<String, Object> map = childArray.get(mLastGroupPosition).get(mLastChildPosition);
        int classifyCode = Integer.parseInt((map.get("classifyCode").toString()));
        Intent intent = new Intent();
        ChildType childType = G.type.getChildTypeByCode(classifyCode);
        intent.putExtra("edit_name", childType.getName());
        intent.putExtra("edit_image", childType.getIcon());
        intent.putExtra("edit_value", String.valueOf(map.get("value")));
        intent.putExtra("edit_type", childType.getType());
        intent.putExtra("edit_remark", map.get("remark").toString());
        intent.putExtra("edit_date", (String) map.get("createDate"));
        intent.putExtra("edit_classtifycode", classifyCode);
        intent.putExtra("edit_rcode", childType.getRootCode());
        intent.putExtra("id", map.get("id").toString());
        intent.putExtra("source", VoiceAccountActivity.SOURCE_CHAR_EDIT);
        intent.putExtra("querydate", queryDate);
        intent.setClass(this.getApplicationContext(), VoiceAccountActivity.class);
        this.startActivity(intent);
    }

    /**
     * 设置初始化数据
     */
    public void initializeView() {
        intent = getIntent();
        type = intent.getIntExtra("type", -1);
        classtifyCode = intent.getIntExtra("classfiycode", -1);
        String date = intent.getStringExtra("date");
        queryDate = intent.getStringExtra("queryDate");

        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_evey);
        ivLoading.startAnimation(operatingAnim);

        llNotNetwork.setVerticalGravity(1);
        llNotNetwork.setHorizontalGravity(1);
        llNotNetwork.setVisibility(View.GONE);
        setNavigationBar();
        setDate();
        Log.i("ooooo", "type:" + type);
        if (type != -1) {
            //初始化设置日期,查询额日期
            tvDate.setText(date);
            getData(queryDate);
            initDetailData(queryDate);
            year = Integer.parseInt(queryDate.substring(0, 4));
            month = Integer.parseInt(queryDate.substring(5, 7));
        }
    }


    /**
     * 设置图标页传过来的参数
     */
    private void setText(String value) {
        tvMoney.setText(value + "元");
        if (type == 0) {
            tvTotal.setText("总支出");
            tvMoney.setTextColor(getResources().getColor(R.color.red));
        } else {
            tvTotal.setText("总收入");
            tvMoney.setTextColor(getResources().getColor(R.color.green));
        }
    }

    /**
     * 设置导航条
     */
    private void setNavigationBar() {
        nbBar.setLeftButton(null, R.drawable.ic_arrow_lift);
        title = intent.getStringExtra("title");
        nbBar.setTitle(title + "明细");
        nbBar.setNavigationBarClickListener(new NavigationBar.OnNavigationBarClickListener() {
            @Override
            public void OnNavigationBarClick(int tag) {
                if (tag == NavigationBar.TAG_LEFT_BUTTON) {
                    finish();
                }
            }
        });
    }

    protected void setDate() {
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        mmonth = month;//记录当前的月份
        myear = year;//记录当前的年份
    }

    private void setFormatDate() {
        if (month < 10) {
            tvDate.setText(year + "年" + "0" + month + "月");
            queryDate = year + "-" + "0" + month;
        } else {
            tvDate.setText(year + "年" + month + "月");
            queryDate = year + "-" + month;
        }
        ivLoading.setAnimation(operatingAnim);
        rushData();
        flag = false;
    }

    /**
     * 查询某天的所有明细列表 非分类
     * @param queryDate
     */
    public void initDetailData(String queryDate) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put("loginName", new UserInfo(this).getLoginName());
        requestParamsMap.put("startDate", queryDate + "-01 00:00:00");
        requestParamsMap.put("endDate", queryDate + "-31 23:59:59");
        requestParamsMap.put("type", String.valueOf(type));
        OkHttpUtil.sendPost(ApiUri.QUERY_CLASSIFY_STATISTICS_BY_DATE, requestParamsMap, this);
    }

    /**
     * 获取当月记录
     * @param queryDate
     */
    protected void getData(String queryDate) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put("loginName", new UserInfo(this).getLoginName());
        requestParamsMap.put("date", queryDate);
        OkHttpUtil.sendPost(ApiUri.QUERY_STATISTICS_BY_MONTH, requestParamsMap, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (intent.getIntExtra("classtifycode", -1) == -1) {
            return;
        }
        classtifyCode = intent.getIntExtra("classtifycode", -1);
        Log.i("aaa", classtifyCode + "");
        type = G.type.getRootTypeByCode(classtifyCode).getType();
        queryDate = intent.getStringExtra("querydate");
        tvDate.setText(intent.getStringExtra("date"));
        year = Integer.parseInt(queryDate.substring(0, 4));
        month = Integer.parseInt(queryDate.substring(5, 7));
        nbBar.setTitle(G.type.getRootTypeByCode(classtifyCode).getName() + "明细");
        rushData();

    }


    /*@Override
    public void onCatched(String uri, String json) {
        try {
            json = URLDecoder.decode(json,"utf-8");
            if (uri.equals(G.uri.QUERY_CLASSIFY_STATISTICS_BY_DATE)) {
                ivLoading.clearAnimation();
                ivLoading.setVisibility(View.GONE);
                llNotNetwork.setVisibility(View.GONE);
                expendList.setVisibility(View.VISIBLE);
                DecimalFormat df = new DecimalFormat("########0.00");
                xstream.alias("UserDateAccount", UserDateAccount.class);
                xstream.alias("UserAccountBook", UserAccountBook.class);
                List<UserDateAccount> UserDateAccountList = (List<UserDateAccount>) xstream.fromXML(json);
                Collections.sort(UserDateAccountList);
                groupArray.clear();
                childArray.clear();
                double value = 0;
                for (int i = 0; i < UserDateAccountList.size(); i++) {
                    createDate = new String[UserDateAccountList.size()];
                    tvNodata.setVisibility(View.GONE);
                        UserDateAccount userDateAccount = UserDateAccountList.get(i);
                        createDate[i] = userDateAccount.getCreateDate();
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        List<UserAccountBook> userAccountBookList = userDateAccount.getUserAccountBookList();
                        for (int j = 0; j < userAccountBookList.size(); j++) {
                            UserAccountBook userAccountBook = userAccountBookList.get(j);
                            if (classtifyCode == G.type.getChildTypeByCode(userAccountBook.getClassifyCode()).getRootCode()) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("value", df.format(userAccountBook.getMoney()));
                                map.put("classifyCode", String.valueOf(userAccountBook.getClassifyCode()));
                                map.put("remark", userAccountBook.getRemark());
                                map.put("id", userAccountBook.getId());
                                map.put("createDate", userAccountBook.getCreateDate());
                                value = value + userAccountBook.getMoney();
                                list.add(map);
                                if (createDate[i].equals(userAccountBook.getCreateDate().substring(0, 10))) {
                                    HashMap<String, Object> map2 = new HashMap<String, Object>();
                                    map2.put("value", userAccountBook.getMoney());
                                    map2.put("date", userDateAccount.getCreateDate());
                                    groupArray.add(map2);
                                }
                            }
                        }
                        childArray.add(list);
                    }
                    setText(df.format(value));
                    RemovevoidData(childArray);
                    Log.i("chlidarray",childArray+"");
                    RemoveDuplicatesData(groupArray);
                    addItemTolteValue(groupArray, childArray);

                    for (int i = 0; i < groupArray.size(); i++) {
                        expendList.expandGroup(i);
                    }
                    myadapter.notifyDataSetChanged();
                if (groupArray.size() <= 0) {
                    tvNodata.setVisibility(View.VISIBLE);
                    tvNodata.setText("当前月份没有记账哦...");
                    ivLoading.clearAnimation();
                    ivLoading.setVisibility(View.GONE);
                    return;
                } else {
                    tvNodata.setVisibility(View.GONE);
                }
            } else if (json.equals(HttpUtil.errorMsg)) {
                ivLoading.clearAnimation();
                ivLoading.setVisibility(View.GONE);
                llNotNetwork.setVisibility(View.VISIBLE);
                expendList.setVisibility(View.GONE);
                llNotNetwork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContentManger();
                        expendList.setVisibility(View.VISIBLE);
                    }
                });
               if (!flag){
                   Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
                   flag = true;
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 剔除没有数据的list
     * @param childArray
     */
    private void RemovevoidData(List<List<Map<String, Object>>> childArray) {
        List<List<Map<String, Object>>> array = new ArrayList<>();
        for (int i = 0; i < childArray.size(); i++) {
            if (childArray.get(i).size() == 0) {
                array.add(childArray.get(i));
            }
        }
        childArray.removeAll(array);
    }

    /**
     * 剔除相同的数据
     * @param list
     */
    private void RemoveDuplicatesData(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).get("date").equals(list.get(i).get("date"))) {
                    list.remove(j);
                }
            }
        }
    }

    /**
     * 计算其中每一项Value的值
     * @param groupArray
     * @param childArray
     */
    private void addItemTolteValue(List<Map<String, Object>> groupArray, List<List<Map<String, Object>>> childArray) {
        double value = 0;
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < groupArray.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", groupArray.get(i).get("value").toString());
            map.put("date", groupArray.get(i).get("date").toString());
            list.add(map);
        }
        groupArray.clear();
        for (int i = 0; i < childArray.size(); i++) {
            value = 0;
            List<Map<String, Object>> childlist = childArray.get(i);
            for (int j = 0; j < childlist.size(); j++) {
                value += Double.parseDouble(childlist.get(j).get("value").toString());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("value", value);
            map.put("date", list.get(i).get("date").toString());
            groupArray.add(map);
        }
    }

    public void rushData() {
        getData(queryDate);
        initDetailData(queryDate);
        initializeMode();
    }

    @Override
    public void onSuccess(final String uri, String result) {
        try {
            final String result2 = URLDecoder.decode(result, "utf-8");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (uri.equals(ApiUri.QUERY_CLASSIFY_STATISTICS_BY_DATE)) {
                        ivLoading.clearAnimation();
                        ivLoading.setVisibility(View.GONE);
                        llNotNetwork.setVisibility(View.GONE);
                        expendList.setVisibility(View.VISIBLE);
                        DecimalFormat df = new DecimalFormat("########0.00");
                        xstream.alias("UserDateAccount", UserDateAccount.class);
                        xstream.alias("UserAccountBook", UserAccountBook.class);
                        List<UserDateAccount> UserDateAccountList = (List<UserDateAccount>) xstream.fromXML(result2);
                        Collections.sort(UserDateAccountList);
                        groupArray.clear();
                        childArray.clear();
                        double value = 0;
                        for (int i = 0; i < UserDateAccountList.size(); i++) {
                            createDate = new String[UserDateAccountList.size()];
                            tvNodata.setVisibility(View.GONE);
                            UserDateAccount userDateAccount = UserDateAccountList.get(i);
                            createDate[i] = userDateAccount.getCreateDate();
                            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                            List<UserAccountBook> userAccountBookList = userDateAccount.getUserAccountBookList();
                            for (int j = 0; j < userAccountBookList.size(); j++) {
                                UserAccountBook userAccountBook = userAccountBookList.get(j);
                                if (classtifyCode == G.type.getChildTypeByCode(userAccountBook.getClassifyCode()).getRootCode()) {
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put("value", df.format(userAccountBook.getMoney()));
                                    map.put("classifyCode", String.valueOf(userAccountBook.getClassifyCode()));
                                    map.put("remark", userAccountBook.getRemark());
                                    map.put("id", userAccountBook.getId());
                                    map.put("createDate", userAccountBook.getCreateDate());
                                    value = value + userAccountBook.getMoney();
                                    list.add(map);
                                    if (createDate[i].equals(userAccountBook.getCreateDate().substring(0, 10))) {
                                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                                        map2.put("value", userAccountBook.getMoney());
                                        map2.put("date", userDateAccount.getCreateDate());
                                        groupArray.add(map2);
                                    }
                                }
                            }
                            childArray.add(list);
                        }
                        setText(G.momeyFormat(value));
                        RemovevoidData(childArray);
                        Log.i("chlidarray", childArray + "");
                        RemoveDuplicatesData(groupArray);
                        addItemTolteValue(groupArray, childArray);

                        for (int i = 0; i < groupArray.size(); i++) {
                            expendList.expandGroup(i);
                        }
                        myadapter.notifyDataSetChanged();
                        if (groupArray.size() <= 0) {
                            tvNodata.setVisibility(View.VISIBLE);
                            tvNodata.setText("当前月份没有记账哦...");
                            ivLoading.clearAnimation();
                            ivLoading.setVisibility(View.GONE);
                            return;
                        } else {
                            tvNodata.setVisibility(View.GONE);
                        }
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
                if (uri.equals(ApiUri.QUERY_CLASSIFY_STATISTICS_BY_DATE)) {
                    ivLoading.clearAnimation();
                    ivLoading.setVisibility(View.GONE);
                    llNotNetwork.setVisibility(View.VISIBLE);
                    expendList.setVisibility(View.GONE);
                    llNotNetwork.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rushData();
                            expendList.setVisibility(View.VISIBLE);
                        }
                    });
                    if (!flag) {
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }
            }
        });
    }

    @OnClick({R.id.ib_up, R.id.ib_down, R.id.rl_voice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_up:
                month--;
                if (month == 0) {
                    month = 12;
                    year--;
                    tvDate.setText(year + "年" + month + "月");
                }
                setFormatDate();
                break;
            case R.id.ib_down:
                Log.i("ooooo", "year:" + year + "==" + myear + "month:" + month + "===" + mmonth);
                if (year == myear && month == mmonth) {
                    Toast.makeText(getApplicationContext(), "不能在往前啦!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    month++;
                }
                if (month == 13) {
                    month = 1;
                    year++;
                }
                setFormatDate();
                break;
            case R.id.rl_voice:
                Intent intent = new Intent();
                intent.putExtra("source", VoiceAccountActivity.SOURCE_CHAR);
                intent.putExtra("querydate", queryDate);
                intent.setClass(ChartDetailActivity.this, VoiceAccountActivity.class);
                startActivity(intent);
                break;
        }
    }
}


