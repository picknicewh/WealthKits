package com.cfjn.javacf.activity.bookkeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.ChildType;
import com.cfjn.javacf.modle.Segment;
import com.cfjn.javacf.modle.UserAccountBook;
import com.cfjn.javacf.modle.UserBudgetClassify;
import com.cfjn.javacf.modle.UserBudgetStatistics;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.Config;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.widget.DeleteListAccountPopup;
import com.cfjn.javacf.widget.CustomDateTimeDialog;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *  作者： zll
 *  时间： 2016-6-8
 *  名称： 新增记账
 *  版本说明：代码规范整改
 *  附加注释：接入百度语音记账
 *  主要接口：1.提交记账信息
 *            2.查询预算列表
 *            3.查询语义解析
 */
public class VoiceAccountActivity extends FragmentActivity implements View.OnClickListener, OKHttpListener {
    /**
     * 标题
     */
    @Bind(R.id.tv_title)
    TextView tvTitle;
    /**
     * 删除记录
     */
    @Bind(R.id.ib_del)
    ImageButton ibDel;
    /**
     * 支出按钮
     */
    @Bind(R.id.rb_expend)
    RadioButton rbExpend;
    /**
     * 收入按钮
     */
    @Bind(R.id.rb_income)
    RadioButton rbIncome;

    @Bind(R.id.rg_type)
    RadioGroup rgType;
    /**
     * 下滑线
     */
    @Bind(R.id.v_line1)
    View vLine1;
    @Bind(R.id.v_line2)
    View vLine2;
    /**
     * 钱
     */
    @Bind(R.id.tv_value)
    EditText tvValue;
    /**
     * 类型的图片
     */
    @Bind(R.id.iv_image)
    ImageView ivImage;
    /**
     * 类型的名称
     */
    @Bind(R.id.tv_lable)
    TextView tvLable;
    /**
     * 预算的值
     */
    @Bind(R.id.tv_budgetvalue)
    TextView tvBudgetvalue;
    /**
     * 显示隐藏的预算列表
     */
    @Bind(R.id.ll_budeget)
    LinearLayout llBudeget;
    /**
     * 备注
     */
    @Bind(R.id.ed_remark)
    EditText edRemark;
    /**
     * 日期
     */
    @Bind(R.id.tv_date)
    TextView tvDate;
    /**
     * 再记一笔
     */
    @Bind(R.id.b_add)
    Button bAdd;
    /**
     * 保存
     */
    @Bind(R.id.b_save)
    Button bSave;

    /**
     * 添加
     */
    public static final int SOURCE_ADD = 0x000;
    /**
     * 类别添加
     */
    public static final int SOURCE_TYPE_ADD = 0x001;
    /**
     * 编辑添加
     */
    public static final int SOURCE_EDIT = 0x002;
    public static final int SOURCE_CHAR_EDIT = 0x003;
    public static final int SOURCE_CHAR = 0x004;
    /**
     * 支出
     */
    private static final int EXPEND = 0;
    /**
     * 收入
     */
    private static final int INCOME = 1;
    /**
     * 默认的支出分类对象
     */
    private static ChildType tempExpendChildType;
    /**
     * 默认的收入分类对象
     */
    private static ChildType tempImcomeChildType;
    /**
     * 当前的二级分类对象
     */
    private ChildType childType;

    /**
     * 选择单个分类编码
     */
    private int code;
    /**
     * 分类码
     */
    private int classtifycode;
    private long lastClickTime;

    /**
     * 记账对象的ID
     */
    private String id;
    /**
     * 保存id号
     */
    private String edit_id;
    /**
     * 是否正在提交
     */
    private boolean isSubmitting;
    private XStream xstream = new XStream(new DomDriver());
    private int keywordCode = 0;
    /**
     * 时间日期选择对话框
     */
    private CustomDateTimeDialog customDateTimePickerDialog = null;
    /**
     * 查询日期
     */
    private String querydate;
    /**
     * 对话框查询时间
     */
    private String dialogdate;
    /**
     * 是否为再记一笔提交
     */
    private boolean isResubmit;
    private BaiduASRDigitalDialog mDialog = null;
    private int mCurrentTheme = Config.DIALOG_THEME;
    private String segmentStr;
    /**
     * 记账的来源
     */
    private int source;
    /**
     * 记录再记一笔并且金额是否为0时候状态
     */
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeeping_acount);
        ButterKnife.bind(this);
        initializeMode();
        initializeView();
        initializeControl();
    }
////1010000
    private void initializeMode() {
        tempExpendChildType = G.type.getChildTypeByCode(1011000);
        tempImcomeChildType = G.type.getChildTypeByCode(6011000);
        childType = tempExpendChildType;
        source = getIntent().getIntExtra("source", -1);
        customDateTimePickerDialog = new CustomDateTimeDialog(this, R.style.MyDialog);
    }

    private void initializeView() {
        setTypeValue(tempExpendChildType);
        setEditContentType();
        setChangetype();
    }

    private void initializeControl(){
        Intent intent=getIntent();
        Log.i("source", source + "");
        switch (source) {
            case SOURCE_ADD://从妙笔一记进入
                voiceAccount();
                id = null;
                querydate = intent.getStringExtra("main_date");
                String currentDate = new SimpleDateFormat("yyyy-MM").format(new Date());
                Log.i("currentDate", currentDate + "====" + querydate);
                if (currentDate.equals(querydate)) {
                    tvDate.setText(getCurrenttime());
                } else {
                    //当前的分秒
                    String time = new SimpleDateFormat("HH:mm").format(new Date());
                    tvDate.setText(querydate + "-01" + " " + time);
                }
                Log.i("TAG", tvDate.getText().toString());
                getData(querydate);
                //设置初始默认的分类码
                if (rbExpend.isChecked()) {
                    classtifycode = 1010000;
                } else {
                    classtifycode = 6010000;
                }
                break;
            case SOURCE_EDIT://从编辑进入
                setEditText();
                break;
            case SOURCE_CHAR_EDIT:
                setEditText();
                break;
            case SOURCE_CHAR:
                voiceAccount();
                id = null;
                querydate = intent.getStringExtra("querydate");
                getData(querydate);
                if (rbExpend.isChecked()) {
                    classtifycode = 1010000;
                } else {
                    classtifycode = 6010000;
                }
                tvDate.setText(getCurrenttime());
                break;
        }
    }

    /**
     * 设置从首页编辑和图标详细信息进入时的设置
     */
    private void setEditText() {
        Intent intent = getIntent();
        ibDel.setVisibility(View.VISIBLE);
        bAdd.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(G.dp2px(this, 210), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        bSave.setLayoutParams(params);
        tvTitle.setText("编辑记账");
        id = intent.getStringExtra("id");
        edit_id = id;
        switch (intent.getIntExtra("edit_type", -1)) {
            case EXPEND:
                rbExpend.setChecked(true);
                setLine(0);
                break;
            case INCOME:
                rbIncome.setChecked(true);
                setLine(1);
                break;
        }
        querydate = intent.getStringExtra("querydate");
        classtifycode = intent.getIntExtra("edit_rcode", -1);
        getData(querydate);
        ChildType childType = G.type.getChildTypeByCode(intent.getIntExtra("edit_classtifycode", -1));
        tvValue.setText(intent.getStringExtra("edit_value"));
        ivImage.setImageResource(intent.getIntExtra("edit_image", -1));
        tvLable.setText(intent.getStringExtra("edit_name"));
        edRemark.setText(intent.getStringExtra("edit_remark"));
        tvDate.setText(intent.getStringExtra("edit_date"));
        this.childType = childType;
    }

    private void setEditContentType() {
        tvValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                int index = str.indexOf(".");
                int count = 0;
                for (int i = 0; i <= str.length() - 1; i++) {
                    String getstr = str.substring(i, i + 1);
                    if (getstr.equals(".")) {
                        count++;
                    }
                }
                if (count > 1) {
                    deleteSelection(s);
                }
                int maxLenth = 10;
                if (index < 0) {
                    if (str.length() > maxLenth)
                        deleteSelection(s);
                } else if ((index == 0) || (str.length() - 1 - index > 2) || index == maxLenth + 1) {
                    deleteSelection(s);
                }

            }

            private void deleteSelection(Editable s) {
                int selection = tvValue.getSelectionStart();
                if (selection > 1) {
                    s.delete(selection - 1, selection);
                }
            }

        });
    }

    @OnClick({R.id.ib_back, R.id.ib_del, R.id.ib_voice, R.id.ll_type, R.id.tv_date, R.id.b_add, R.id.b_save})
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_del://删除记账
                DeleteListAccountPopup popup = new DeleteListAccountPopup(VoiceAccountActivity.this, getApplicationContext(),id);
                popup.showAtLocation(ibDel, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ib_voice://语音记账
                voiceAccount();
                break;
            case R.id.b_add://再记一笔
                isResubmit = true;
                isSubmitting = false;
                submit();
                //如果是再记一笔时，重置
                if (flag == 0) {
                    tvValue.setText("");
                    edRemark.setText("");
                    tvDate.setText(getCurrenttime());
                    if (rbExpend.isChecked()) {
                        tvLable.setText(G.type.getChildTypeByCode(1011000).getName());
                        ivImage.setImageResource(G.type.getChildTypeByCode(1011000).getIcon());
                    } else {
                        tvLable.setText(G.type.getChildTypeByCode(6011000).getName());
                        ivImage.setImageResource(G.type.getChildTypeByCode(6011000).getIcon());
                    }
                }
                break;
            case R.id.b_save:
                isResubmit = false;
                isSubmitting = false;
                submit();
                break;//保存
            case R.id.tv_date://日期选中
                if (isFastDoubleClick()) {
                    return;
                } else {
                    markLastClickTime();
                    customDateTimePickerDialog.show();
                }
                break;
            case R.id.ll_type:    //刚刚进入时默认设置
                intent = new Intent();
                intent.setClass(getApplicationContext(), ClasstifyActivity.class);
                if (rbExpend.isChecked()) {
                    intent.putExtra("type", EXPEND);
                } else {
                    intent.putExtra("type", INCOME);
                }
                startActivityForResult(intent, 0);
                break;
        }
    }

    private void voiceAccount() {
        mCurrentTheme = Config.DIALOG_THEME;
        if (mDialog != null) {
            mDialog.dismiss();
        }
        // 参数，其中apiKey和secretKey为必须配置参数，其他根据实际需要配置
        Bundle params = new Bundle();
        // 配置apkKey
        params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, "gzAwHXkwsTVr2FISu8bva5xj");
        // 配置secretKey
        params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, "whqNbo11Lkn5ElTxc3Ge9NXXkOFS9hEx");
        params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG);
        // 创建百度语音识别对话框
        if (Build.VERSION.RELEASE.compareTo("6.0") >= 0 && Build.VERSION.RELEASE.compareTo("7.0.0") < 0) {
            Toast.makeText(getApplicationContext(), "暂时不支持android 6.0系统！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            BaiduASRDigitalDialog mDialog = new BaiduASRDigitalDialog(this, params);
            //监听事件
            mDialog.setDialogRecognitionListener(new DialogRecognitionListener() {

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> rs = results != null ? results.getStringArrayList(RESULTS_RECOGNITION) : null;
                    if (rs != null && rs.size() > 0) {
                        segmentStr = rs.get(0);
                        edRemark.setText(segmentStr);
                        getQuerySegment();
                    }
                }
            });
            mDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
            //设置对话框开始的声音
            mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_START_TONE_ENABLE, true);
            //设置对话框结束的声音
            mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_END_TONE_ENABLE, true);
            //设置对话框相应的声音
            mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_TIPS_TONE_ENABLE, true);
            mDialog.show();
        }
    }

    private void setChangetype() {
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_expend:
                        setTypes(tempExpendChildType);
                        setLine(0);
                        break;
                    case R.id.rb_income:
                        setTypes(tempImcomeChildType);
                        setLine(1);
                        break;
                }
            }
        });
    }

    private void setTypes(ChildType childType) {
        tvLable.setText(childType.getName());
        ivImage.setImageResource(childType.getIcon());
        this.childType = childType;
    }

    /**
     * 设置当分类对象
     */
    public void setTypeValue(ChildType childType) {
        switch (childType.getType()) {
            case EXPEND:
                // 支出
                tempExpendChildType = childType;
                setLine(0);
                if (rbExpend.isChecked()) {
                    setTypes(childType);
                } else {
                    rbExpend.setChecked(true);
                }
                break;
            case INCOME:
                // 收入
                setLine(1);
                tempImcomeChildType = childType;
                if (rbIncome.isChecked()) {
                    setTypes(childType);
                } else {
                    rbIncome.setChecked(true);
                }
                break;
        }
    }

    /**
     * 设置下划线
     * @param type
     */
    private void setLine(int type) {
        if (type == 0) {
            rbExpend.setChecked(true);
            vLine1.setBackgroundColor(getResources().getColor(R.color.red));
            vLine2.setBackgroundColor(getResources().getColor(R.color.transparent));
            tvValue.setTextColor(getResources().getColor(R.color.red));
            tvValue.setHintTextColor(getResources().getColor(R.color.red));
            llBudeget.setVisibility(View.VISIBLE);
        } else {
            rbIncome.setChecked(true);
            vLine2.setBackgroundColor(getResources().getColor(R.color.red));
            vLine1.setBackgroundColor(getResources().getColor(R.color.transparent));
            tvValue.setTextColor(getResources().getColor(R.color.green));
            tvValue.setHintTextColor(getResources().getColor(R.color.green));
            llBudeget.setVisibility(View.GONE);
        }
    }

    /**
     * 提交添加账单
     */
    private void submit() {
        Log.i("TAG", tvDate.getText().toString());
        String stringmoney = tvValue.getText().toString();
        String stringdate = tvDate.getText().toString();
        if (stringdate.compareTo(getCurrenttime()) > 0) {
           G.showToast(this,"选择的日期不能超过今天");
            return;
        }
        if (stringmoney == null || stringmoney.equals("")) {
          G.showToast(this,"输入金额不能为空");
            flag = 1;
            return;
        }
        flag = 0;
        double money = Double.parseDouble(stringmoney);
        if (money < 0) {
          G.showToast(this,"输入金额有误");
            return;
        }
        UserAccountBook userAccountBook = new UserAccountBook();
        userAccountBook.setMoney(money);
        userAccountBook.setLoginName(new UserInfo(this).getLoginName());
        userAccountBook.setRemark(edRemark.getText().toString().trim());
        userAccountBook.setUnit("元");
        userAccountBook.setCreateDate(tvDate.getText().toString());
        userAccountBook.setContent(childType.getName());
        userAccountBook.setClassifyCode(childType.getCode());
        userAccountBook.setType(String.valueOf(childType.getType()));
        userAccountBook.setKeyWordNum(keywordCode);
        if (id == null) {
            // 新增
            userAccountBook.setSubmitType(1);
        } else {
            // 修改
            userAccountBook.setId(id);
            userAccountBook.setSubmitType(2);

        }
        // 提交
        if (!isSubmitting) {
            submitMessage(userAccountBook);
            isSubmitting = true;
        }
    }

    /**
     * 提交账单
     */
    private void submitMessage(UserAccountBook userAccountBook) {
        if (isFastDoubleClick() || userAccountBook == null) {
            return;
        } else {
            markLastClickTime();
            xstream.alias("UserAccountBook", UserAccountBook.class);
            String accountBook = xstream.toXML(userAccountBook);
            Map<String, String> requestParamsMap = new HashMap<String, String>();
            requestParamsMap.put("loginName", new UserInfo(this).getLoginName());
            try {
                requestParamsMap.put("accountBook", URLEncoder.encode(accountBook, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            OkHttpUtil.sendPost(ApiUri.SUBMIT_MESSAGE, requestParamsMap, this);
        }
    }

    /**
     * 语意解析
     */
    private void getQuerySegment() {
        Map<String, String> param = new HashMap<>();
        param.put("loginName", new UserInfo(this).getLoginName());
        try {
            param.put("segmentStr", URLEncoder.encode(segmentStr, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtil.sendPost(ApiUri.QUERY_SEGMENT, param, this);
    }

    /**
     * 获取当前的时间
     * @return
     */
    private String getCurrenttime() {
        String date;
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        date = sdformat.format(new Date());
        return date;
    }

    /**
     * 设置选择时间
     */
    public void setDateTextView(long millis) {
        if (millis > System.currentTimeMillis()) {
            millis = System.currentTimeMillis();
            Toast.makeText(this, "日期不能设置超过未来的日子哦！", Toast.LENGTH_LONG).show();
        }
        Date date = new Date(millis);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datestr = format.format(date);
        tvDate.setText(datestr);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");
        dialogdate = format2.format(date);
        getData(dialogdate);
    }

    /**
     * 记录最后点击时间
     */
    public void markLastClickTime() {
        lastClickTime = System.currentTimeMillis();
    }

    /**
     * 是否为快速双击
     */
    public boolean isFastDoubleClick() {
        long now = System.currentTimeMillis();
        long offset = now - lastClickTime;
        if (offset <= 1000) {
            return true;
        }
        lastClickTime = now;
        return false;
    }

    /**
     * 查询当前分类的预算信息
     */
    protected void getData(String queryDate) {
        Map<String, String> requestParamsMap = new HashMap<String, String>();
        requestParamsMap.put("loginName", new UserInfo(this).getLoginName());
        requestParamsMap.put("date", queryDate);
        // 放入类型
        OkHttpUtil.sendPost(ApiUri.QUERY_USER_BUDGET, requestParamsMap, this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * 选择分类处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 200:
                Bundle intent = data.getExtras(); //data为B中回传的Intent
                code = intent.getInt("type_code", -1);
                String name = intent.getString("type_name");
                int image = intent.getInt("type_image", -1);
                int rcode = intent.getInt("type_rcode", -1);
                int type = intent.getInt("type_type", -1);
                if (edit_id != null) {
                    id = edit_id;
                } else {
                    id = null;
                }
                classtifycode = rcode;
                getData(querydate);
                if (rcode == 1000000 || rcode == 6000000) {
                    ChildType childType = new ChildType();
                    childType.setCode(code - 1);
                    childType.setName(name);
                    childType.setIcon(image);
                    childType.setType(type);
                    setTypeValue(childType);
                } else {
                    setTypeValue(G.type.getChildTypeByCode(code));
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(final String uri, String result) {
        try {
            final String result2 = URLDecoder.decode(result, "utf-8");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (uri.equals(ApiUri.SUBMIT_MESSAGE)) {
                        xstream.alias("UserAccountBook", UserAccountBook.class);
                        UserAccountBook userAccountBook = (UserAccountBook) xstream.fromXML(result2);
                        if (userAccountBook.getId().equals("0")) {
                            Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                        }
                        if (isResubmit) {
                            return;
                        } else {
                            if (source == SOURCE_CHAR || source == SOURCE_CHAR_EDIT) {
                                Intent intent = new Intent(getApplicationContext(), ChartDetailActivity.class);
                                if (classtifycode == 1000000 || classtifycode == 6000000) {
                                    ChildType childType = G.type.getChildTypeByCode(code - 1);
                                    classtifycode = childType.getRootCode();
                                }
                                intent.putExtra("classtifycode", classtifycode);
                                Log.i("classtifycode", classtifycode + "");
                                intent.putExtra("querydate", tvDate.getText().toString().substring(0, 7));
                                intent.putExtra("date", tvDate.getText().toString().substring(0, 4) + "年" + tvDate.getText().toString().substring(5, 7) + "月");
                                startActivityForResult(intent, 1);
                                finish();
                            } else {
                                //提交后关闭界面，返回主页
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("tab", 3);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        }

                    } else if (uri.equals(ApiUri.QUERY_USER_BUDGET)) {
                        xstream.alias("UserBudgetStatisticsVo", UserBudgetStatistics.class);
                        xstream.alias("UserBudgetClassifyVo", UserBudgetClassify.class);
                        DecimalFormat df = new DecimalFormat("########0.00");
                        // 将xml解析成类
                        UserBudgetStatistics userBudgetStatistics = (UserBudgetStatistics) xstream.fromXML(result2);
                        List<UserBudgetClassify> budgetClassifyList = userBudgetStatistics.getUserBudgetClassifyVos();
                        Log.i("classtifycode", classtifycode + "");
                        for (int i = 0; i < budgetClassifyList.size(); i++) {
                            UserBudgetClassify userBudgetClassify = budgetClassifyList.get(i);
                            int code = userBudgetClassify.getCode();
                            if (classtifycode == code) {
                                tvBudgetvalue.setText(df.format(userBudgetClassify.getSurplus()));
                            }
                        }
                    } else if (uri.equals(ApiUri.QUERY_SEGMENT)) {
                        xstream.alias("SegmentVo", Segment.class);
                        Segment segmentVo = (Segment) xstream.fromXML(result2);
                        keywordCode = segmentVo.getKeywordCode();
                        DecimalFormat df = new DecimalFormat("#0.00");
                        tvValue.setText(df.format(segmentVo.getMoney()));
                        Log.i("TAG", tvValue.getText().toString());
                        ChildType childType = G.type.getChildTypeByCode(segmentVo.getClassifyCode());
                        if (childType == null) {
                            childType = G.type.getChildTypeByCode(1111000);
                        }
                        setTypes(childType);
                        if (childType.getType() == 0) {
                            setLine(0);
                            tempExpendChildType = childType;
                        } else {
                            setLine(1);
                            tempImcomeChildType = childType;
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
                if (uri.equals(ApiUri.SUBMIT_MESSAGE)) {
                    isSubmitting = false;
                } else if (uri.equals(ApiUri.QUERY_SEGMENT)) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}