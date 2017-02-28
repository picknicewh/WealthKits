package com.cfjn.javacf.JPush;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者： wh
 * 时间： 2016-6-27
 * 名称： 极光推送工具类
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 *
 *
 */
public class JPushUtil {
    public  static  final  String APP_KEY ="JPUSH_APPKEY";
    /**
     *获取申请极光推送的key
     */
    public static String getAppKey(Context context){
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo applicationInfo =  context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo!=null)
            metaData = applicationInfo.metaData;
            if (metaData!=null){
                appKey = metaData.getString(APP_KEY);
                if (appKey==null&&appKey.length()!=24){
                    return  null;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }
    /**
     *集成了 JPush SDK 的应用程序在第一次成功注册到 JPush 服务器时，
     * JPush 服务器会给客户端返回一个唯一的该设备的标识 - RegistrationID
     * 获取RegistrationID
     */
    public  static String getRegid(Context context){
        String regId = JPushInterface.getRegistrationID(context);
        return  regId;
    }
    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }
    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\\u4E00-\\u9FA50-9a-zA-Z_@!#$&*+=.|￥¥]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

}
