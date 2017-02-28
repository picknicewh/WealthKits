package com.cfjn.javacf.JPush;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.cfjn.javacf.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * 作者： wh
 * 时间： 2016-6-27
 * 名称： 极光推送的基本设置
 * 版本说明：代码规范整改
 * 附加注释：setAliasAndTags方法中设置自己想要自定义的值
 * 主要接口：暂无
 */
public class JPushBaseActivity extends Activity{
    public static boolean isForeground = false;
     public void initJPushConfiguration(){
         setAliasAndTags(setAlias("cfjn"),setTag("tag"));
         setStyleBasic();
         setPushTime();
   }
    /**
     * 设置将获取的tag字符串，转换成 Set<String>,以逗号分隔
     */
    private Set<String>  setTag(String tag){

        // 检查 tag 的有效性
        if (JPushUtil.isEmpty(tag)) {
            Toast.makeText(getApplicationContext(),R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return null;
        }
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!JPushUtil.isValidTagAndAlias(sTagItme)) {
                Toast.makeText(getApplicationContext(),R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return null;
            }
            tagSet.add(sTagItme);
        }
        return  tagSet;
    }
    /**
     * 设置将获取的alias字符串,进行过滤判断
     */
    private String setAlias(String alias){

        if (JPushUtil.isEmpty(alias)) {
            Toast.makeText(getApplicationContext(),R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!JPushUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(getApplicationContext(),R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return null;
        }
        return  alias;
    }
    /**
     *设置别名
     */
    public  void setAliasAndTags(String alias,Set<String> tags){
        JPushInterface.setAliasAndTags(getApplicationContext(), alias, tags, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                switch (code) {
                    case 0:
                        Log.i("TAG", "设置成功！");
                        break;
                    case 6002:
                        Log.i("TAG", "设置超时！");
                        break;
                }
            }
        });
    }
    /**
     * 设置推送日期,周一至周日的每天0-24小时
     */
    public void setPushTime(){
        Set<Integer> days = new HashSet<>();
        for (int i = 0;i<7;i++){
            days.add(i);
        }
        JPushInterface.setPushTime(getApplicationContext(), days, 0, 24);
        //设置静音时间段晚上10点半到早上八点半
        JPushInterface.setSilenceTime(getApplicationContext(), 22, 30, 8, 30);
    }
    /**
     *设置通知提示方式 - 基础属性
     */
    public void setStyleBasic(){

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

 /*   public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(JPushInterface.ACTION_MESSAGE_RECEIVED);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                String messge = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
                String extras = intent.getStringExtra(JPushInterface.EXTRA_APP_KEY);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(JPushInterface.EXTRA_APP_KEY + " : " + messge + "\n");
                if (!JPushUtil.isEmpty(extras)) {
                    showMsg.append(JPushInterface.EXTRA_APP_KEY + " : " + extras + "\n");
                }
                Toast.makeText(context,showMsg.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
