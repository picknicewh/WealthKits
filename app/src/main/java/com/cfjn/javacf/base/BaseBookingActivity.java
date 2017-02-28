package com.cfjn.javacf.base;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/1/8.
 */
public abstract class BaseBookingActivity extends Activity implements OKHttpListener {

    /**
     * 查询日期 格式：YYYY-MM
     */
    protected String queryDate;
    /**
     * 需访问的uri
     */
    private String uri;
    /**
     * 日期TextView
     */
    private TextView dateText;
    /**
     * 是否正在请求数据
     */
    protected boolean isLoading;
    /**
     * 上一个时间
     */
    private ImageButton ib_up;
    /**
     * 下一个时间
     */
    private ImageButton ib_down;
    /**
     * 加载图片
     */
    private ImageView loadingView;
    /**
     * 日历
     */
    protected Calendar calendar;

    /**
     * 日期
     */
    protected Date date;

    /**
     * 年
     */
    protected int year;
    /**
     * 当前年
     */
    private int myear;

    /**
     * 月
     */
    protected int month;
    /**
     * 当前月
     */
    protected int mmonth;
    private Animation operatingAnim;
    public   boolean is =false;
    public long lastClickTime;
    public  boolean isLoaded = false;
    public  boolean isLoaded2 = false;
    public  boolean isLoaded3= false;
    protected void initDate() {
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_evey);
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        mmonth = month;//记录当前的月份
        myear = year;//记录当前的年份
        if (uri== ApiUri.USER_ACCOUNT_MAIN){
            setFormatDate();
        }
        ib_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoaded&& isLoaded2 && isLoaded3){
                    month--;
                    if (month == 0) {
                        month = 12;
                        year--;
                        dateText.setText(year + "年" + month + "月");
                    }
                    setFormatDate();
                }
            }
        });
        ib_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("isloaded1",isLoaded+"");
                if (isLoaded && isLoaded2 && isLoaded3){
                    if (!ApiUri.QUERY_USER_BUDGET.equals(uri)) {
                        if (year == myear && month == mmonth) {
                            Toast.makeText(getApplicationContext(), "不能在往前啦0.0", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            month++;
                        }
                    } else {
                        month++;
                    }
                    if (month == 13) {
                        month = 1;
                        year++;
                    }
                    setFormatDate();
                }
            }
        });
    }
    /**
     * 记录最后点击时间
     */
    public void markLastClickTime() {
        lastClickTime = System.currentTimeMillis();
    }

    private void setFormatDate() {
        if (month < 10) {
            dateText.setText(year + "年" + "0" + month + "月");
            queryDate = year+"-"+"0"+month;
        } else {
            dateText.setText(year + "年" + month + "月");
            queryDate = year +"-"+month;
        }
        if (!uri.equals(ApiUri.QUERY_CHART_BY_YEAR)){
            loadingView.startAnimation(operatingAnim);
        }
        getChartManger(queryDate);
        /*首页查询数据使用的*/
        getAccountManger(queryDate);
    }

    /**
     * 按年查询图表信息
     * @param queryDate 年份
     */
    protected void getChartManger(String queryDate) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put("loginName", new UserInfo(this).getLoginName());
        requestParamsMap.put("date", queryDate);
        if (uri.equals(ApiUri.QUERY_CHART_BY_YEAR)){
            requestParamsMap.put("type", "");
        }
        // 放入类型
        OkHttpUtil.sendPost(uri, requestParamsMap, this);
        isLoaded = false;
    }

    /**
     * 按月查询记账信息
     * @param queryDate 月份
     */
    protected void getAccountManger(String queryDate) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put("loginName", new UserInfo(this).getLoginName());
        requestParamsMap.put("date", queryDate);
        OkHttpUtil.sendPost(ApiUri.QUERY_STATISTICS_BY_MONTH, requestParamsMap, this);
        isLoaded2=false;
    }
    public String getQueryTime(){
        return queryDate;
    }

    public void setDateText(TextView tv_date) {
        this.dateText = tv_date;
    }

    public void setUpBtn(ImageButton ib_up) {
        this.ib_up = ib_up;
    }

    public void setDownBtn(ImageButton ib_down) {
        this.ib_down = ib_down;
    } public void setUri(String uri) {
        this.uri = uri;
    }
    public void setLoadingView(ImageView loadingView) {
        this.loadingView = loadingView;
    }
}
