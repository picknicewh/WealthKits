package com.cfjn.javacf.activity.bookkeeping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.bookkeeping.ChartAdapter;
import com.cfjn.javacf.base.BaseBookingActivity;
import com.cfjn.javacf.modle.RootType;
import com.cfjn.javacf.modle.UserAccountBookByClassify;
import com.cfjn.javacf.modle.UserAccountBookChart;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.TitleValueColorEntity;
import com.cfjn.javacf.widget.NavigationBar;
import com.cfjn.javacf.widget.OnPieChartItemSelectedListener;
import com.cfjn.javacf.widget.PieChartView;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

;

/**
 * 作者： wh
 * 时间： 2015-12-24
 * 名称： 记账-图表
 * 版本说明：代码规范整改
 * 附加注释：通过显示隐藏方式，显示不同类型图表，对应收入支出的信息
 * 主要接口：查询当月图表详细信息
 *
 */
public class ChartActivity extends BaseBookingActivity implements RadioGroup.OnCheckedChangeListener, OKHttpListener {
    /**
     * 导航栏
     */
    @Bind(R.id.navigetionBar)
    NavigationBar navigetionBar;
    /**
     * 收入按钮
     */
    @Bind(R.id.rb_char_expend)
    RadioButton rbCharExpend;
    /**
     * 支出按钮
     */
    @Bind(R.id.rb_char_income)
    RadioButton rbCharIncome;
    /**
     * 单选框容器
     */
    @Bind(R.id.rg_char_value)
    RadioGroup rgCharValue;
    /**
     * 收入红色下划线
     */
    @Bind(R.id.v_line1)
    View vLine1;
    /**
     * 支出红色下滑线
     */
    @Bind(R.id.v_line2)
    View vLine2;
    /**
     * 上一个日期
     */
    @Bind(R.id.ib_char_up)
    ImageButton ibCharUp;
    /**
     * 日期
     */
    @Bind(R.id.tv_char_date)
    TextView tvCharDate;
    /**
     * 下一个日期
     */
    @Bind(R.id.ib_char_down)
    ImageButton ibCharDown;
    /**
     * 饼图按钮
     */
    @Bind(R.id.rb_char_pie)
    RadioButton rbCharPie;
    /**
     * 条图按钮
     */
    @Bind(R.id.rb_char_column)
    RadioButton rbCharColumn;
    /**
     * 饼图条图按钮容器
     */
    @Bind(R.id.rg_char_chart)
    RadioGroup rgCharChart;
    /**
     * 没有数据
     */
    @Bind(R.id.tv_char_nodata)
    TextView charNodata;
    /**
     * 收入支出文字
     */
    @Bind(R.id.tv_char_lable)
    TextView tvCharLable;
    /**
     * 收入支出值
     */
    @Bind(R.id.tv_char_value)
    TextView tvCharValue;
    /**
     * 支出类型
     */
    private int type = 0;
    /**
     * 支出码
     */
    private static final int EXPEND = 0;
    /**
     * 收入码
     */
    private static final int INCOME = 1;
    /**
     * 图像类型
     */
    private int code;
    /**
     * 支出码
     */
    private static final int PIE = 0;
    /**
     * 收入码
     */
    private static final int COLUMN = 1;
    /**
     * 收入总值
     */
    private double totleexpend = 0.0;
    /**
     * 支出总值
     */
    private double totleincome = 0.0;
    /**
     * 支出数据集合
     */
    private List<Map<String, Object>> expendList;
    /**
     * 收入数据集合
     */
    private List<Map<String, Object>> incomeList;
    /**
     * 数据数据集合
     */
    private List<Map<String, Object>> valueList;
    /**
     * 饼图显示
     */
    private LinearLayout in_piepage;
    private PieChartView pieChartView;
    private TextView tv_value_pie;
    /**
     * 饼图中间分类的名称
     */
    private TextView tv_type_name;
    /**
     * 显示分类的百分比
     */
    private TextView tv_type_persent;
    /**
     * 矩形图中间的分类的百分比
     */
    private TextView tv_percent;
    /**
     * 矩形图中间的分类的名称
     */
    private TextView tv_type;
    /**
     * 下一个分类的信息按钮
     */
    private LinearLayout ll_detali;
    /**
     * 餅圖的位置
     */
    private int position = -1;
    /**
     * 条图显示
     */
    private LinearLayout column_layout;
    /**
     * 条图列表
     */
    private ListView listView;
    /**
     * 条图列表适配器
     */
    private ChartAdapter adapter;
    /**
     * xml解析工具
     */
    private XStream xstream = new XStream(new DomDriver());

    /**
     * 总预算显示
     */
    //private  LinearLayout rl_totleValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeepingchar);
        ButterKnife.bind(this);
        findviews();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_char_expend:
                initdata(EXPEND);
                setLine(0);
                break;
            case R.id.rb_char_income:
                initdata(INCOME);
                setLine(1);
                break;
            case R.id.rb_char_pie:
                code = PIE;
                //切换显示
                in_piepage.setVisibility(View.VISIBLE);
                column_layout.setVisibility(View.GONE);
                //  rl_totleValue.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_char_column:
                //切换显示
                code = COLUMN;
                column_layout.setVisibility(View.VISIBLE);
                in_piepage.setVisibility(View.GONE);
                //  rl_totleValue.setVisibility(View.GONE);
                break;
        }
        if (valueList != null) {
            setPiePage();
            setColumnPager();
        } else {
            in_piepage.setVisibility(View.GONE);
            column_layout.setVisibility(View.GONE);
        }
    }

    private void initdata(int type) {
        if (type == 0) {
            rbCharExpend.setChecked(true);
            tvCharLable.setText("总支出");
            tvCharValue.setTextColor(getResources().getColor(R.color.red));
            tvCharValue.setText(G.momeyFormat(totleexpend) + "元");
            valueList = expendList;
            //刷新数据
            this.type = EXPEND;
        } else {
            rbCharIncome.setChecked(true);
            tvCharLable.setText("总收入");
            tvCharValue.setTextColor(getResources().getColor(R.color.green));
            tvCharValue.setText(G.momeyFormat(totleincome) + "元");
            //刷新数据
            valueList = incomeList;
            this.type = INCOME;
        }
        if (code == PIE) {
            in_piepage.setVisibility(View.VISIBLE);
            column_layout.setVisibility(View.GONE);
//            rl_totleValue.setVisibility(View.VISIBLE);
            //     pieChartView.setRun(true);
        } else {
            column_layout.setVisibility(View.VISIBLE);
            in_piepage.setVisibility(View.GONE);
            //  rl_totleValue.setVisibility(View.GONE);
            //    pieChartView.setRun(false);
        }
    }

    /**
     * 设置饼图页
     */
    private void setPiePage() {
        if (valueList.size() == 0) {
            charNodata.setVisibility(View.VISIBLE);
            column_layout.setVisibility(View.GONE);
            in_piepage.setVisibility(View.GONE);
            //     pieChartView.setRun(false);
        } else {
            //  pieChartView.setRun(true);
            charNodata.setVisibility(View.GONE);
            List<TitleValueColorEntity> data = new ArrayList<TitleValueColorEntity>();//饼图所需的数据集合
            TitleValueColorEntity entity;    //饼图所需的数据实体
            for (int j = 0; j < valueList.size(); j++) {
                Map<String, Object> map = valueList.get(j);
                int rootTypeId = (Integer) map.get("rootTypeId");
                //金额
                double value = (Double) map.get("value");
                //颜色
                int color = getResources().getColor(G.type.getRootTypeByCode(rootTypeId).getColor());
                String title = G.type.getRootTypeByCode(rootTypeId).getName();
                //创建entity
                entity = new TitleValueColorEntity(title, (float) value, color);
                data.add(entity);
            }
            pieChartView.setData(data);
            pieChartView.setOnItemSelectedListener(new PieChartItemSelectedListener());
            //获取map
            for (int i = 0; i < valueList.size(); i++) {
                Map map = valueList.get(i);
                RootType rootType = (RootType) map.get("RootTypeVo");
                tv_type.setText(rootType.getName());
                tv_type_name.setText(rootType.getName());
                tv_percent.setText(map.get("percent") + "%");
                tv_type_persent.setText(map.get("percent") + "%");
                //设置金额
                double value = (double) map.get("value");
                tv_value_pie.setText(G.momeyFormat(value) + "元");
            }
            ll_detali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDatail(position);
                }
            });
        }
    }

    /**
     * 设置条图页面
     */
    private void setColumnPager() {
        //    pieChartView.setRun(false);
        if (valueList.size() == 0) {
            charNodata.setVisibility(View.VISIBLE);
            in_piepage.setVisibility(View.GONE);
            column_layout.setVisibility(View.GONE);
        } else {
            charNodata.setVisibility(View.GONE);
            adapter = new ChartAdapter(this, valueList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getDatail(position);
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 进入详情页
     *
     * @param position
     */
    private void getDatail(int position) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ChartDetailActivity.class);
        Map<String, Object> map = valueList.get(position);
        RootType rootType = (RootType) map.get("RootTypeVo");
        String value = String.valueOf(map.get("value"));
        intent.putExtra("title", rootType.getName());
        intent.putExtra("value", value);
        intent.putExtra("type", type);
        intent.putExtra("classfiycode", rootType.getCode());
        intent.putExtra("date", tvCharDate.getText().toString());
        intent.putExtra("queryDate", queryDate);
        Log.i("querydate", queryDate);
        startActivity(intent);
    }

    /*
      * 获取数据
      * */
    private void processData(UserAccountBookChart userAccountBookChart) {
        valueList.clear();
        // 金额
        double value = 0;
        // 百分比
        double percent = 0;
        // 类型
        int rootTypeId = 0;

        // 得到各类型List
        List<UserAccountBookByClassify> UserAccountBookByClassifyList = userAccountBookChart.getUserAccountBookByClassifyVos();// 总支出
        totleexpend = userAccountBookChart.getTotalExpend();// 总收入
        totleincome = userAccountBookChart.getTotalIncome();// 支出List
        expendList = new ArrayList<Map<String, Object>>();// 收入List
        incomeList = new ArrayList<Map<String, Object>>();
        DecimalFormat df1 = new DecimalFormat("########0.00");// 分类收入与支出
        int i = 0;
        if (UserAccountBookByClassifyList.size() != 0) {
            for (UserAccountBookByClassify info : UserAccountBookByClassifyList) {// 所有的数据（包括收入和支出）
                Map<String, Object> map = new HashMap<String, Object>();
                //总金额
                double totalValue;
                List<Map<String, Object>> ValueList;
                if (info.getType() == 0) {// 表示支出类型
                    value = info.getTotalExpend();
                    totalValue = totleexpend;
                    ValueList = expendList;
                } else {// 收入类型
                    value = info.getTotalIncome();
                    totalValue = totleincome;
                    ValueList = incomeList;
                }
                percent = value / totalValue * 100;
                //添加所有的百分比
                rootTypeId = info.getCode();
                //类型
                map.put("RootTypeVo", G.type.getRootTypeByCode(rootTypeId));
                //类型id
                map.put("rootTypeId", rootTypeId);
                //金额
                map.put("value", value);
                //百分比
                map.put("percent", df1.format(percent));
                //放入收入或支出List
                ValueList.add(map);
                if (type == EXPEND) {
                    valueList = expendList;
                    tvCharValue.setText(G.momeyFormat(totleexpend));
                } else {
                    valueList = incomeList;
                    tvCharValue.setText(G.momeyFormat(totleincome));
                }
                i++;
            }
        } else {
            //  Toast.makeText(getApplicationContext(), "没有记录", Toast.LENGTH_LONG).show();
            valueList.clear();
        }
        setColumnPager();
        setPiePage();
    }

    /* @Override
     public void onCatched(String uri, String xml) {
         try {
              xml=  URLDecoder.decode(xml, "utf-8");
             if (uri.equals(G.uri.QUERY_CHART_BY_YEAR)) {
                 isLoaded = true;
                 isLoaded2 = true;
                 isLoaded3= true;
                 // 取别名
                 xstream.alias("UserAccountBookChartVo", UserAccountBookChart.class);
                 xstream.alias("UserAccountBookByClassifyVo", UserAccountBookByClassify.class);
                 // 将xml文件转成Bean
                 UserAccountBookChart userAccountBookChart = (UserAccountBookChart) xstream.fromXML(xml);
                 processData(userAccountBookChart);
                 if (valueList.size()!=0){
                     if (type==0){
                         initdata(EXPEND);
                     }
                     else {
                         initdata(INCOME);
                     }
                     if (code ==PIE) {
                         in_piepage.setVisibility(View.VISIBLE);
                     }
                    else {
                         column_layout.setVisibility(View.VISIBLE);
                     }
                 }
                 else {
                     tvCharValue.setText(0.0 + "元");
                     if (code ==PIE) {
                         in_piepage.setVisibility(View.GONE);
                     }
                     else {
                         column_layout.setVisibility(View.GONE);
                     }
                 }
                 // 处理数据
                 isLoading = false;

                 pieChartView.setRun(false);
             } else if (xml.equals(HttpUtil.errorMsg)){
                 pieChartView.setRun(false);
             }
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
     }
 */
    @Override
    public void onSuccess(final String uri, String result) {
        try {
            final String result2 = URLDecoder.decode(result, "utf-8");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (uri.equals(ApiUri.QUERY_CHART_BY_YEAR)) {
                        isLoaded = true;
                        isLoaded2 = true;
                        isLoaded3 = true;
                        // 取别名
                        xstream.alias("UserAccountBookChartVo", UserAccountBookChart.class);
                        xstream.alias("UserAccountBookByClassifyVo", UserAccountBookByClassify.class);
                        // 将xml文件转成Bean
                        UserAccountBookChart userAccountBookChart = (UserAccountBookChart) xstream.fromXML(result2);
                        processData(userAccountBookChart);
                        if (valueList.size() != 0) {
                            if (type == 0) {
                                initdata(EXPEND);
                            } else {
                                initdata(INCOME);
                            }
                            if (code == PIE) {
                                in_piepage.setVisibility(View.VISIBLE);
                            } else {
                                column_layout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tvCharValue.setText(0.0 + "元");
                            if (code == PIE) {
                                in_piepage.setVisibility(View.GONE);
                            } else {
                                column_layout.setVisibility(View.GONE);
                            }
                        }
                        // 处理数据
                        isLoading = false;

                        pieChartView.setRun(false);
                    } /*else if (result.equals(HttpUtil.errorMsg)){
                pieChartView.setRun(false);
            }*/
                }
            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String uri, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pieChartView.setRun(false);
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 饼图Item选中监听器
     */
    class PieChartItemSelectedListener implements OnPieChartItemSelectedListener {
        @Override
        public void onPieChartItemSelected(PieChartView paramPieChartView, int position, String type, float itemValue, float percent) {
            // TODO Auto-generated method stub
            if (position < 0) {
                position = 0;
            }
            Map map = valueList.get(position);  //获取map
            RootType rootType = (RootType) map.get("RootTypeVo");
            //设置名称与百分比
            tv_type.setText(rootType.getName());
            tv_type_name.setText(rootType.getName());
            tv_percent.setText(map.get("percent") + "%");
            tv_type_persent.setText(map.get("percent") + "%");
            //设置金额
            double value = (double) map.get("value");
            tv_value_pie.setText(G.momeyFormat(value)+ "元");
            ChartActivity.this.position = position;
        }
    }

    private void findviews() {
        Intent intent = getIntent();
        //  ib_back = (ImageButton) findViewById(R.id.ib_char_back);
        rgCharValue = (RadioGroup) findViewById(R.id.rg_char_value);
        rbCharExpend = (RadioButton) findViewById(R.id.rb_char_expend);
        rbCharIncome = (RadioButton) findViewById(R.id.rb_char_income);
        rbCharPie = (RadioButton) findViewById(R.id.rb_char_pie);
        rgCharChart = (RadioGroup) findViewById(R.id.rg_char_chart);
        rbCharColumn = (RadioButton) findViewById(R.id.rb_char_column);
        tvCharLable = (TextView) findViewById(R.id.tv_char_lable);
        tvCharDate = (TextView) findViewById(R.id.tv_char_date);
//        rl_totleValue = (LinearLayout)findViewById(R.id.b_char_ll2);
        setDateText(tvCharDate);
        tvCharValue = (TextView) findViewById(R.id.tv_char_value);
        ibCharDown = (ImageButton) findViewById(R.id.ib_char_down);
        setDownBtn(ibCharDown);
        ibCharUp = (ImageButton) findViewById(R.id.ib_char_up);
        setUpBtn(ibCharUp);
        in_piepage= (LinearLayout) findViewById(R.id.in_piepage);
        in_piepage.setVisibility(View.GONE);

        pieChartView = (PieChartView) in_piepage.findViewById(R.id.pie);
        tv_type_name = (TextView) in_piepage.findViewById(R.id.pie_tv_type_name);
        tv_type_persent = (TextView) in_piepage.findViewById(R.id.pie_tv_type_persent);
        ll_detali = (LinearLayout) in_piepage.findViewById(R.id.pie_detail);
        tv_type = (TextView) in_piepage.findViewById(R.id.pie_tv_type);
        tv_percent = (TextView) in_piepage.findViewById(R.id.pie_tv_percent);
        tv_value_pie = (TextView) in_piepage.findViewById(R.id.pie_tv_value);

        column_layout= (LinearLayout) findViewById(R.id.in_columnpage);
        column_layout.setVisibility(View.GONE);
        listView = (ListView) column_layout.findViewById(R.id.column_pagelist);
        valueList = new ArrayList<>();
        rgCharValue.setOnCheckedChangeListener(this);
        rgCharChart.setOnCheckedChangeListener(this);
        setUri(ApiUri.QUERY_CHART_BY_YEAR);
        initDate();
        setNavigationBar();
        tvCharDate.setText(intent.getStringExtra("main_date"));
        queryDate = intent.getStringExtra("main_queryDate");
        getChartManger(queryDate);
        month = Integer.parseInt(queryDate.substring(5, 7));
        year = Integer.parseInt(queryDate.substring(0, 4));
        //setLineParam();
        setLine(0);
        setPieView();
    }

    /**
     * 如果有底部虚拟按键把饼图的宽度设置为其宽度的90%
     */
    private void setPieView() {
        if (G.checkDeviceHasNavigationBar(getApplicationContext())) {
            pieChartView.screenWith = (int) (G.size.W * 0.9);
            Log.i("TAG", "0.9w");
        } else {
            pieChartView.screenWith = G.size.W;
            Log.i("TAG", "w");
        }
    }

    /**
     * 设置下划线
     *
     * @param type
     */
    private void setLine(int type) {
        if (type == 0) {
            rbCharExpend.setChecked(true);
            vLine1.setBackgroundColor(getResources().getColor(R.color.red));
            vLine2.setBackgroundColor(getResources().getColor(R.color.transparent));
            tvCharValue.setTextColor(getResources().getColor(R.color.red));
            tvCharValue.setHintTextColor(getResources().getColor(R.color.red));

        } else {
            rbCharIncome.setChecked(true);
            vLine2.setBackgroundColor(getResources().getColor(R.color.red));
            vLine1.setBackgroundColor(getResources().getColor(R.color.transparent));
            tvCharValue.setTextColor(getResources().getColor(R.color.green));
            tvCharValue.setHintTextColor(getResources().getColor(R.color.green));

        }
    }

    private void setNavigationBar() {
        navigetionBar.setTitle("图表");
        navigetionBar.setLeftButton(null, R.drawable.ic_arrow_lift);
        navigetionBar.setNavigationBarClickListener(new NavigationBar.OnNavigationBarClickListener() {
            @Override
            public void OnNavigationBarClick(int tag) {
                if (tag == NavigationBar.TAG_LEFT_BUTTON) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pieChartView.setRun(false);
    }
}
