package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.base.UserMessageDb;
import com.cfjn.javacf.modle.MangerVo;
import com.cfjn.javacf.modle.PersonalVo;
import com.cfjn.javacf.modle.SecretkeyVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.LoadingDialog;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 会员中心
 *  版本说明：代码规范整改
 *  附加注释：显示会员信息 查询的系统消息用数据库保存 有系统消息显示小红点
 *  主要接口：1.获取系统消息
 *            2.获取个人基本信息
 *            3.获取个人投资风格
 */
public class MemberCenterActivity extends Activity implements View.OnClickListener, OKHttpListener {
    //-----------布局变量--------------
    /**
     * 用户名
     */
    @Bind(R.id.tv_username)
    TextView tvUsername;
    /**
     * 用户手机号
     */
    @Bind(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    /**
     * 用户投资风格
     */
    @Bind(R.id.tv_StyleEva)
    TextView tvStyleEva;
    /**
     * 手势密码图标
     */
    @Bind(R.id.lv_lock_setting)
    ImageView lvLockSetting;
    /**
     * 线
     */
    @Bind(R.id.v_line)
    View vLine;
    /**
     * 小红点
     */
    @Bind(R.id.iv_redDot)
    ImageView ivRedDot;
    /**
     * 修改手勢密码
     */
    @Bind(R.id.ll_updateLock)
    LinearLayout llUpdateLock;

    //-------------逻辑变量-------------------
    /**
     * 是否开启手势密码
     */
    private boolean hasLock = false;
    /**
     * 是否有网络
     */
    private boolean isNetWork = false;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_center);
        ButterKnife.bind(this);
        userInfo = UserInfo.getInstance(this);
        getSysteManger();
    }

    private void initializeView() {
        SharedPreferences preferences = this.getSharedPreferences(LockActivity.LOCK, Context.MODE_PRIVATE);
        hasLock = preferences.getBoolean(LockSetupActivity.HAS_LOCK + G.user.USER_ID, false);
        switchGesturesPassword(hasLock);

        tvStyleEva.setText(userInfo.getStyleEva());
        tvUsername.setText(userInfo.getUserName());
        if (userInfo.getLoginName().length() == 11) {
            tvPhoneNumber.setText(userInfo.getLoginName().substring(0, 3) + "****" + userInfo.getLoginName().substring(7, 11));
        } else {
            tvPhoneNumber.setText(userInfo.getLoginName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeView();
        initializeControl();
    }

    private void initializeControl() {
        getEvaSystyle();
        getPersonalManger();
        selectSyeteMager();
    }

    @OnClick({R.id.ib_back, R.id.ll_menber, R.id.ll_myAttention, R.id.ll_contentManager,
            R.id.lv_lock_setting, R.id.ll_gesturePassword, R.id.ll_updateLock,
            R.id.ll_systemManger, R.id.ll_more})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_menber:
                intent = new Intent(this, PersonalSettingActivity.class);
                break;
            case R.id.ll_myAttention:
                intent = new Intent(this, MyAttenActivity.class);
                break;
            case R.id.ll_contentManager:
                intent = new Intent(this, CounterMangerActivity.class);
                break;
            case R.id.lv_lock_setting:
                // 手势密码开关
                if (hasLock) {
                    // 去验证手势密码
                    intent = new Intent(this, LockActivity.class);
                    intent.putExtra("isClear", true);
                } else {
                    // 去设置手势密码
                    intent = new Intent(this, LockSetupActivity.class);
                }
                break;
            case R.id.ll_systemManger:
                intent = new Intent(this, SystemInfoActivity.class);
                intent.putExtra("type", isNetWork);
                break;
            case R.id.ll_more:
                intent = new Intent(this, MoreActivity.class);
                break;
            case R.id.ll_updateLock:
                // 修改手势密码
                intent = new Intent(this, LockActivity.class);
                intent.putExtra("isReset", true);
                break;
            case R.id.ib_back:
                finish();
                break;
        }
        if (null != intent) {
            this.startActivity(intent);
        }
    }

    /**
     * 手势密码开关
     */
    private void switchGesturesPassword(boolean lockType) {
        if (lockType) {
            lvLockSetting.setImageResource(R.drawable.ic_s_on);
            llUpdateLock.setVisibility(View.VISIBLE);
            vLine.setVisibility(View.VISIBLE);
        } else {
            lvLockSetting.setImageResource(R.drawable.ic_s_off);
            llUpdateLock.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        }
    }

    /**
     * 获取个人投资风格
     */
    private void getEvaSystyle() {
        Map<String, String> map = new HashMap<>();
        map.put("token", userInfo.getToken());
        map.put("secretKey", userInfo.getSecretKey());
        OkHttpUtil.sendPost(ApiUri.GAINVETTING, map, this);
    }

    /**
     * 设置个人投资风格
     */
    private void setdate(SecretkeyVo response) {
        {
            if (null == response.getDate() || TextUtils.isEmpty(response.getDate().toString())) {
                return;
            }
            if (response.getResultCode().equals("1")) {
                LoadingDialog dialog = new LoadingDialog(MemberCenterActivity.this, R.style.myDialogTheme, 1);
                dialog.show();
                return;
            }
            String value = response.getDate().getUserStyle();
            userInfo.setStyleEva(value);
            tvStyleEva.setText(value);
        }
    }

    /**
     * 获取服务端传过来的系统消息
     */
    private void getSysteManger() {
        Map<String, String> map = new HashMap<>();
        map.put("secretKey", userInfo.getSecretKey());
        map.put("equipmentNumber", "");
        OkHttpUtil.sendPost(ApiUri.SYSTMEMANGER, map, this);
    }

    /**
     * 将服务端传过来的系统消息通过数据库保存起来
     * 把传下来的系统消息size保存起来
     * 每一条消息都加一个NO的type用来判断是否已读
     * 检测到服系统消息有值就显示小红点
     */
    private void setSysteManger(MangerVo response) {
        isNetWork = true;
        if (response.getDate() == null || response.getDate().size() <= 0) {
            return;
        }
        SQLiteDatabase db = MainActivity.UMDB.getWritableDatabase();
        for (int i = 0; i < response.getDate().size(); i++) {
            MangerVo.SystemNotification notification = response.getDate().get(i);
            UserMessageDb.insert(db, "NO", notification.getDateTime(), notification.getSource(), notification.getTitle(), notification.getContent());
        }
        db.close();
        ivRedDot.setVisibility(View.VISIBLE);
        userInfo.setMangerSize(response.getDate().size() + userInfo.getMangerSize());
    }

    /**
     * 查询数据库是否有没有没有看过的系统消息
     * 有的话显示小红点 直接return
     */
    private void selectSyeteMager() {
        SQLiteDatabase db = MainActivity.UMDB.getReadableDatabase();
        for (int i = 0; i < userInfo.getMangerSize(); i++) {
            Cursor cursor = UserMessageDb.select(db, i + 1);
            while (cursor.moveToNext()) {
                String type = cursor.getString(cursor.getColumnIndex("usertype"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                G.log(type + "------------" + time);
                if (type.equals("YES")) {
                    ivRedDot.setVisibility(View.GONE);
                } else {
                    ivRedDot.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        db.close();

    }

    /**
     * 获取个人基本信息
     */
    private void getPersonalManger() {
        Map<String, String> map = new HashMap<>();
        map.put("token", userInfo.getToken());
        map.put("secretKey", userInfo.getSecretKey());
        OkHttpUtil.sendPost(ApiUri.GETUSERINFO, map, this);
    }

    /**
     * 设置个人基本信息
     */
    private void setPersonalManger(PersonalVo personalVo) {
        if ("1".equals(personalVo.getResultCode()) || null != personalVo.getResultDesc() && "非法访问".equals(personalVo.getResultDesc())) {
            LoadingDialog dialog = new LoadingDialog(MemberCenterActivity.this, R.style.myDialogTheme, 1);
            dialog.show();
            return;
        }
        if (null == personalVo.getDate()) {
            return;
        }
        String name = personalVo.getDate().getName();
        String idNumber = personalVo.getDate().getIdNumber();
        if (!G.isEmteny(name)) {
            tvUsername.setText(name);
            userInfo.setUserName(name);
        }
        if (!G.isEmteny(idNumber)) {
            if (idNumber.equals(userInfo.getIdNumber())) {
                return;
            }
            userInfo.setIdNumber(idNumber);
        }
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (uri.equals(ApiUri.GETUSERINFO)) {
                    PersonalVo personalVo = gson.fromJson(result, PersonalVo.class);
                    setPersonalManger(personalVo);
                } else if (uri.equals(ApiUri.SYSTMEMANGER)) {
                    MangerVo mangerVo = gson.fromJson(result, MangerVo.class);
                    setSysteManger(mangerVo);
                } else if (uri.equals(ApiUri.GAINVETTING)) {
                    SecretkeyVo secretkeyVo = gson.fromJson(result, SecretkeyVo.class);
                    setdate(secretkeyVo);
                }
            }
        });
    }

    @Override
    public void onError(final String uri, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.GETUSERINFO)) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                } else if (uri.equals(ApiUri.SYSTMEMANGER)) {
                    isNetWork = false;
                } else if (uri.equals(ApiUri.GAINVETTING)) {
                    G.log("与服务器失去连接去，请检查网络！");
                }
            }
        });
    }

}