package com.cfjn.javacf.shumi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 数米交易记录提交失败时轻量数据存储记录下来，网络稳定时在提交
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ShuMiTradingDate {

    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    public ShuMiTradingDate(Context context) {
        spf=context.getSharedPreferences("ITcast2",Context.MODE_PRIVATE);
        editor=spf.edit();
    }

    public void setSmTradingDate(String date){
        editor.putString("date",date);
        editor.commit();
    }

    public String getSmTradingDate(){
        return  spf.getString("date","");
    }

    public void setSmTradingType(boolean type){
        editor.putBoolean("type",type);
    }

    public boolean getSmTradinType(){
        return  spf.getBoolean("type",false);
    }

    public void clean(){
        editor.clear().commit();
    }
}
