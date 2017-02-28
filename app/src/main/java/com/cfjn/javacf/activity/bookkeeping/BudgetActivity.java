package com.cfjn.javacf.activity.bookkeeping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.bookkeeping.BudgetAdapter;
import com.cfjn.javacf.base.BaseBookingActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.ExpendWarn;
import com.cfjn.javacf.modle.RootType;
import com.cfjn.javacf.modle.UserBudgetClassify;
import com.cfjn.javacf.modle.UserBudgetStatistics;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.NavigationBar;
import com.cfjn.javacf.widget.SetBudgetDialog;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者： wh
 * 时间： 2015-12-24
 * 名称： 记账-预算设置
 * 版本说明：代码规范整改
 * 附加注释：点击任意条图设置预算。如果所有分预算的总和大于设置的总预算，则总预算显示为分预算的总和
 * 主要接口：提交预算
 *          提交总预算
 *          查询预算
 */
public class BudgetActivity extends BaseBookingActivity implements OKHttpListener {
    /**
     * 日期
     */
    private TextView tv_date;
    /**
     * 左箭头
     */
    private ImageButton ib_up;
    /**
     * 右箭头
     */
    private ImageButton ib_down;
    /**
     * 总预算值
     */
    private TextView tv_totle_value;
    /**
     * 总预算总余额
     */
    private TextView tv_totle_surp;
    /**
     * 总预算的百分比
     */
    private View v_precent;
    /**
     * 总预算总的百分比
     */
    private View v_precent_bg;
    /**
     * 列表
     */
    private ListView listView;
    /**
     * 适配器
     */
    private BudgetAdapter adapter;
    /**
     * 数据列表
     */
    private List<Map<String, Object>> list;
    /**
     * 导航条
     */
    private NavigationBar navigationBar;
    /**
     * 设置预算对话框
     */

    private SetBudgetDialog myDialog;
    /**
     * 每个预算
     */
    private double budget;
    /**
     * 位置
     */
    private int position;
    /**
     * 所有余额的值
     */
    public static Map<Integer, Object> MAP;
    /**
     * xml解析工具
     */
    private int classifyCode = 0;
    private XStream xstream = new XStream(new DomDriver());
    /**
     * 小数格式化
     */
    /**
     * 类型id数组
     */
    int[] rootTypeIds;
    /**
     * 设置参数
     */
    List<UserBudgetClassify> classifyList;
    /**
     * 设置总预算的值
     */
    LinearLayout ll_totle_budge;
    /**
     * 总预算
     */
    private String totlebudget;
    /**
     * 总余额
     */
    private  String totlesurplus;
    /**
     * 总支出
     */
    private double totleexpend = 0;
    private Intent intent;
    private  String title = "设置预算";
    private ImageView loadingView;
    /**
     * 无网络加载时显示加载失败
     */
    private RelativeLayout layout;
    private Animation operatingAnim;
    private   DecimalFormat df = new DecimalFormat("########0.00");
    private double allBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeepingbudge);
        findviews();
        adapter = new BudgetAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BudgetActivity.this.position = position;
                classifyCode = rootTypeIds[position];
                title ="预算设置-"+ G.type.getRootTypeByCode(rootTypeIds[position]).getName() ;
                setDialog(1);
            }
        });
        ll_totle_budge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "预算设置-总预算";
                setDialog(0);
            }
        });

    }
    private void findviews() {
        intent = getIntent();
        tv_date = (TextView) findViewById(R.id.tv_btdate);
        ib_up = (ImageButton) findViewById(R.id.im_btleft);
        ib_down = (ImageButton) findViewById(R.id.im_btright);
        tv_totle_value = (TextView) findViewById(R.id.tv_budget_totlevalue);
        tv_totle_surp = (TextView) findViewById(R.id.tv_budget_totlesurp);
        v_precent = findViewById(R.id.v_budget_progress);
        ll_totle_budge = (LinearLayout)findViewById(R.id.ll_totle_budge);
        v_precent_bg = findViewById(R.id.v_budget_progress_bg);
        listView = (ListView) findViewById(R.id.b_budge_list);
        navigationBar = (NavigationBar) findViewById(R.id.bt_navigationbar);
        loadingView = (ImageView) findViewById(R.id.b_but_loadingview);
        layout = (RelativeLayout) findViewById(R.id.b_but_nonet);
        layout.setVerticalGravity(1);
        layout.setHorizontalGravity(1);
        layout.setVisibility(View.GONE);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_evey);
        loadingView.startAnimation(operatingAnim);
        list = new ArrayList<Map<String, Object>>();
        MAP = new HashMap<>();
        rootTypeIds = new int[30];
        setNavigationBar();
        setUri(ApiUri.QUERY_USER_BUDGET);
        setDateText(tv_date);
        setUpBtn(ib_up);
        setDownBtn(ib_down);
        setLoadingView(loadingView);
        initDate();
        tv_date.setText(intent.getStringExtra("main_date"));
        queryDate = intent.getStringExtra("main_queryDate");
        getChartManger(queryDate);
        month = Integer.parseInt(queryDate.substring(5, 7));
        year = Integer.parseInt(queryDate.substring(0,4));
    }
    /**
     * 设置总预算值
     */
    private void setText(String totlebudget, String totlesurplus) {

        tv_totle_value.setText(G.momeyFormat(Double.parseDouble(totlebudget)));
        tv_totle_surp.setText(G.momeyFormat(Double.parseDouble(totlesurplus)));
        RelativeLayout.LayoutParams params;
        double precent = Double.parseDouble(totlesurplus) / Double.parseDouble(totlebudget);
        int processwidth = G.dp2px(this, 260);
        int marginwidth =  G.dp2px(this, 60);
        params = new RelativeLayout.LayoutParams(processwidth,20);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(marginwidth, 0, 0, 0);
        v_precent_bg.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams( G.dp2px(getApplicationContext(), 280 * precent), 20);
        params.setMargins(marginwidth, 0, 0, 0);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        v_precent.setLayoutParams(params);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1.0f, 1.0f, 1.0f);//伸缩动画设置值，宽度的值从1，到百分比
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        v_precent.startAnimation(scaleAnimation);
        if (Double.parseDouble(totlesurplus) <0){
            v_precent_bg.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else {
            v_precent_bg.setBackgroundColor(getResources().getColor(R.color.line_gray));
        }
        v_precent.setBackgroundColor(getResources().getColor(R.color.red));
    }
    private void setDialog(final int flag) {
        myDialog = new SetBudgetDialog(BudgetActivity.this, R.style.CustomDialog,  title, "请输入预算金额",
                new SetBudgetDialog.OnCustomDialogListener() {
                    @Override
                    public void back(String ed_text) {
                        if (!ed_text.equals("")) {
                            budget = Double.parseDouble(ed_text);
                            switch (flag){
                                case 0:
                                    submitTotleBudget(budget);
                                    if (budget<Double.parseDouble(totlebudget)){
                                        setText(totlebudget, totlesurplus);
                                    } else if (budget<Double.parseDouble(totlebudget) && budget>allBudget){
                                        setText(ed_text, df.format(budget - totleexpend));
                                    }
                                    else {
                                        setText(ed_text, df.format(budget - totleexpend));
                                    }
                                    break;
                                case 1:
                                    for (int key : MAP.keySet()) {
                                        if (key == position) {
                                            MAP.put(position,budget);
                                        }
                                    }
                                    String xmlStr = getClassifyBudgetXmlStr(ed_text);
                                    submitMessage(xmlStr);
                                    break;
                            }

                            myDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"设置预算成功！",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(BudgetActivity.this, "输入的值不能为空！", Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        myDialog.show();
    }
    // 提交总预算
    private  void submitTotleBudget(double totlebudget){
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put("loginName",new UserInfo(this).getLoginName());
        requestParamsMap.put("totalBudget",df.format(totlebudget));
        requestParamsMap.put("createDate", queryDate);
        OkHttpUtil.sendPost(ApiUri.USER_TOTLTBUDGE_SUBMIT_MESSAGE, requestParamsMap, this);

    }
    /**
     * 设置ListView数据
     */
    private void initListView(UserBudgetStatistics statistics) {
        // 预算
        double budget = 0;
        // 结余
        double surplus = 0;
        // 支出
        double percent = 0;
        if (statistics.getUserBudgetClassifyVos().size() == 0) {
            setuserBudgetStatistics(getuserBudgetClassify());
        } else {
            setuserBudgetStatistics(statistics.getUserBudgetClassifyVos());
            Log.i("statistics",classifyList+"");
        }
        list.clear();
        for (int i = 0; i < classifyList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            UserBudgetClassify userBudgetClassify = classifyList.get(i);
            budget = userBudgetClassify.getTotalBudget();
            allBudget += budget;
            surplus = userBudgetClassify.getSurplus();
            rootTypeIds[i] = userBudgetClassify.getCode();
            // 设置一级分类百分比
            map.put("budget", G.momeyFormat(budget));
            map.put("balance", G.momeyFormat(surplus));
            map.put("title", G.type.getRootTypeByCode(rootTypeIds[i]).getName());  //类型id
            map.put("RootTypeVo", G.type.getRootTypeByCode(rootTypeIds[i]));
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }
    private void setuserBudgetStatistics(List<UserBudgetClassify> userBudgetClassifies) {
        classifyList = new ArrayList<>();
        List<RootType> rootTypeList = G.type.ROOT_TYPES;
        for (int i = 0; i < rootTypeList.size() - 2; i++) {
            UserBudgetClassify classify = userBudgetClassifies.get(i);
            classify.setTotalBudget(classify.getTotalBudget());
            classify.setSurplus(classify.getSurplus());
            classify.setCode(rootTypeList.get(i).getCode());
            classify.setType(String.valueOf(rootTypeList.get(i).getType()));
            classifyList.add(classify);
        }
    }
    /**
     * 初始的Lis值
     */
    private List<UserBudgetClassify> getuserBudgetClassify() {
        List<UserBudgetClassify> budgetClassifyList = new ArrayList<UserBudgetClassify>();
        List<RootType> rootTypeList = G.type.ROOT_TYPES;
        for (int i = 0; i < rootTypeList.size() - 2; i++) {
            UserBudgetClassify classify = new UserBudgetClassify();
            classify.setTotalBudget(0);
            classify.setSurplus(0);
            classify.setCode(rootTypeList.get(i).getCode());
            classify.setType(String.valueOf(rootTypeList.get(i).getType()));
            budgetClassifyList.add(classify);
        }
        return budgetClassifyList;
    }
    /**
     * 获得xml字符串
     */
    public String getClassifyBudgetXmlStr(String classifyBudget_tmp) {
        xstream.alias("ExpendWarn", ExpendWarn.class);
        ExpendWarn expendWarn = new ExpendWarn();
        // 设置用户名
        expendWarn.setLoginName(new UserInfo(this).getLoginName());
        // 设置预算
        expendWarn.setExpendBudget(Double.parseDouble(classifyBudget_tmp));
        // 设置单位
        expendWarn.setUnit("元");
        // 设置类型
        expendWarn.setClassifyCode(classifyCode);
        // 设置临界值
        expendWarn.setWarnValue(0);
        // 设置时间
        expendWarn.setCreateDate(queryDate);
        String result = xstream.toXML(expendWarn);
        return result;
    }
    /**
     * 提交字符串
     */
    private void submitMessage(String xmlStr) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        try {
            requestParamsMap.put("userBudget", URLEncoder.encode(xmlStr, "utf-8"));
            Log.i("111userBudget", URLEncoder.encode(xmlStr, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtil.sendPost(ApiUri.USER_BUDGE_SUBMIT_MESSAGE, requestParamsMap, this);
    }
    /* @Override
     public void onCatched(String uri, String json) {
         try {
             json=  URLDecoder.decode(json, "utf-8");
             if (uri.equals(G.uri.QUERY_USER_BUDGET)) {
                 isLoaded=true;
                 isLoaded2=true;
                 isLoaded3=true;
                 loadingView.clearAnimation();
                 loadingView.setVisibility(View.GONE);
                 layout.setVisibility(View.GONE);
                 // 设置别名
                 xstream.alias("UserBudgetStatisticsVo", UserBudgetStatistics.class);
                 xstream.alias("UserBudgetClassifyVo", UserBudgetClassify.class);
                 // 将xml解析成类
                 UserBudgetStatistics userBudgetStatistics = (UserBudgetStatistics) xstream.fromXML(json);
                 DecimalFormat df = new DecimalFormat("########0.00");
                 totlebudget = df.format(userBudgetStatistics.getTotalBudget());
                 totlesurplus = df.format(userBudgetStatistics.getSurplus());
                 totleexpend = userBudgetStatistics.getTotalBudget()-userBudgetStatistics.getSurplus();
                 setText(totlebudget, totlesurplus);
                 initListView(userBudgetStatistics);
             } else if (uri.equals(G.uri.USER_BUDGE_SUBMIT_MESSAGE)) {
                 getChartManger(queryDate);
                 adapter.notifyDataSetChanged();
             } else  if (uri.equals(G.uri.USER_TOTLTBUDGE_SUBMIT_MESSAGE)){
                Log.i("m", "提交总预算成功！");
             }
             else if (json.equals(HttpUtil.errorMsg)) {
                 loadingView.clearAnimation();
                 loadingView.setVisibility(View.GONE);
                 layout.setVisibility(View.VISIBLE);
                 layout.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         getChartManger(queryDate);
                         layout.setVisibility(View.GONE);
                     }
                 });
                 Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
             }
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
     }*/
    private void setNavigationBar() {
        navigationBar.setTitle("预算");
        navigationBar.setLeftButton(null, R.drawable.ic_arrow_lift);
        navigationBar.setNavigationBarClickListener(new NavigationBar.OnNavigationBarClickListener() {
            @Override
            public void OnNavigationBarClick(int tag) {
                if (tag == NavigationBar.TAG_LEFT_BUTTON) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        try {
            final  String result2=  URLDecoder.decode(result, "utf-8");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (uri.equals(ApiUri.QUERY_USER_BUDGET)) {
                        isLoaded=true;
                        isLoaded2=true;
                        isLoaded3=true;
                        loadingView.clearAnimation();
                        loadingView.setVisibility(View.GONE);
                        layout.setVisibility(View.GONE);
                        // 设置别名
                        xstream.alias("UserBudgetStatisticsVo", UserBudgetStatistics.class);
                        xstream.alias("UserBudgetClassifyVo", UserBudgetClassify.class);
                        // 将xml解析成类
                        UserBudgetStatistics userBudgetStatistics = (UserBudgetStatistics) xstream.fromXML(result2);
                        DecimalFormat df = new DecimalFormat("########0.00");
                        totlebudget = df.format(userBudgetStatistics.getTotalBudget());
                        totlesurplus = df.format(userBudgetStatistics.getSurplus());
                        totleexpend = userBudgetStatistics.getTotalBudget()-userBudgetStatistics.getSurplus();
                        setText(totlebudget, totlesurplus);
                        initListView(userBudgetStatistics);
                    } else if (uri.equals(ApiUri.USER_BUDGE_SUBMIT_MESSAGE)) {
                        getChartManger(queryDate);
                        adapter.notifyDataSetChanged();
                    } else  if (uri.equals(ApiUri.USER_TOTLTBUDGE_SUBMIT_MESSAGE)){
                        Log.i("m", "提交总预算成功！");
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
                if (uri.equals(ApiUri.QUERY_USER_BUDGET)){
                    loadingView.clearAnimation();
                    loadingView.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getChartManger(queryDate);
                            layout.setVisibility(View.GONE);
                        }
                    });
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }else if (uri.equals(ApiUri.USER_BUDGE_SUBMIT_MESSAGE)){
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}