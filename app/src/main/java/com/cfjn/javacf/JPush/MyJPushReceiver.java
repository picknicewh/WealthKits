package com.cfjn.javacf.JPush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.GuideActivity;
import com.cfjn.javacf.activity.WebViewActivity;
import com.cfjn.javacf.util.G;
import com.openhunme.cordova.activity.HMDroidGap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者： wh
 * 时间： 2016-6-27
 * 名称： 极光推送广播接收
 * 版本说明：代码规范整改
 * 附加注释：当推送一个信息时，发送广播，在xml中有注册广播，根据不同的action做相应的操作
 * 主要接口：无
 *
 *
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "wanghua";
    private  Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getExtras();
        Log.i("TAG",intent.getAction());
        Log.i(TAG, "[MyJPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (intent.getAction().equals(JPushInterface.ACTION_REGISTRATION_ID)){
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyJPushReceiver] 接收Registration Id : " + regId);
          //  sendRegistrationId(regId,context);
        }else if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
            Log.i(TAG, "[MyJPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
          //  receivingNotification(bundle,context);
        }else if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)){
            Log.i(TAG, "[MyJPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i(TAG, "[MyJPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            // 在这里可以做些统计，或者做些其他工作
        }else {
            if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
                Log.i(TAG, "[MyJPushReceiver] 用户点击打开了通知");
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Intent myIntent = new Intent();
                String message = bundle.getString(JPushInterface.EXTRA_EXTRA);
                //: [value - {"type":"2","note":"","k1":"","url":"https://www.baidu.com/","kind":"1","k2":""}]
                try {

                    JSONObject Object = new JSONObject(message);
                    String value = Object.getString("value");
                    JSONObject jsonObject = new JSONObject(value);
                    String type = (String) jsonObject.get("type");
                    String note = (String) jsonObject.get("note");
                    String url = (String) jsonObject.get("url");
                    String kind = (String) jsonObject.get("kind");
                    switch (type){
                        case  "1":
                            myIntent.setClass(context,GuideActivity.class);
                           // myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            myIntent.putExtra("title", title);
                            myIntent.putExtra("content", content);
                            myIntent.putExtra("notificationId", notificationId);

                            break;
                        case  "2":
                            myIntent.setClass(context, WebViewActivity.class);
                            myIntent.putExtra("source",url);
                            myIntent.putExtra("note",note);
                            break;
                        case  "3":
                            myIntent.setClass(context, HMDroidGap.class);
                            myIntent.putExtra("loadUrl",url);
                            myIntent.putExtra("note",note);
                            break;
                        case  "4":
                            try {
                                myIntent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(url);
                                myIntent.setData(content_url);
                            }catch (Exception e){
                                G.showToast(context, "请您安装浏览器后才可以访问");
                            }
                            break;
                    }
                        try {
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(myIntent);
                        }catch (Exception e){
                                G.showToast(context, "请您安装浏览器后才可以访问");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, message);
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.i(TAG, "[MyJPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.i(TAG, "[MyJPushReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.i(TAG, "[MyJPushReceiver] Unhandled intent - " + intent.getAction());
            }
        }
    }
    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();

    }

    /**
     *  处理返回过来的数据，并发送通知
     */
    public  void receivingNotification(Bundle bundle ,Context context){
        PendingIntent 	default_pendingIntent =
                PendingIntent.getActivity(context, 1, new Intent(context, GuideActivity.class), 0);
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        Log.i("hahah",message);
        // 使用notification
        // 使用广播或者通知进行内容的显示
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.ic_launcher);
        //设置跳转的内容
        builder.setContentIntent(default_pendingIntent);
        Log.i("TAGG",JPushUtil.getRegid(context));
        if (title.equals("")){
            builder.setContentTitle(getApplicationName(context));
        }else {
            builder.setContentTitle(title);
        }
        Notification notification = builder.build();
        notification.flags  = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        notification.defaults = Notification.DEFAULT_SOUND;//设置通知的方式
        manager.notify(0,notification);
    }

    /**
     * 获取包名
     */
    public String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

}
