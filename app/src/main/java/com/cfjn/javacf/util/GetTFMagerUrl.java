package com.cfjn.javacf.util;

import android.content.Context;

import com.cfjn.javacf.base.UserInfo;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 获取天风各种状态URL
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class GetTFMagerUrl {
    public static String TFMagerUrl(int type,Context context) {
        String loadUrlStr = null;
        UserInfo userInfo=UserInfo.getInstance(context);
        String params="&name=" + userInfo.getLoginName() + "&token=" + userInfo.getToken() + "&key=" + userInfo.getSecretKey();
        switch (type){
            case 0:
                //天风的开户页面
                //+us.getLoginName()+"/"+us.getToken()+"/"+us.getSecretKey()
                loadUrlStr="open-prepare/"+userInfo.getLoginName()+"/"+userInfo.getToken()+"/"+userInfo.getSecretKey();
//                #/login
//                 loadUrlStr="login?ic_close=1&from=2&op_station=&cordova_version=40";
                break;
            case 1:
                //天风修改交易密码
                loadUrlStr="tab/account-modifyPwd?"+params;
                break;
            case 2:
                //天风修改手机号码
                loadUrlStr="tab/account-modifyPhone?"+params;
                break;
            case 3:
                //天风添加银行卡
                loadUrlStr="tab/account-bindQuickBank?"+params;
                break;
        }
       return "file:///"+context.getFilesDir().getAbsolutePath()+"/tap/www/tap/view/index_android.html#/"+loadUrlStr;
//        return "file:///android_asset/www/tap/view/index_android.html#/"+loadUrlStr;
//        return  "file:///android_asset/www/index_test.html";
    }
}

