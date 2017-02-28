package com.cfjn.javacf.shumi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 数米银行卡提交失败时轻量数据存储记录下来，网络稳定时在提交
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */

public class ShuMiBankDate {

    private Context context;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    public ShuMiBankDate(Context context) {
        this.context = context;
        spf=context.getSharedPreferences("ITcast1",Context.MODE_PRIVATE);
        editor=spf.edit();
    }

    public void setShuMiBankDate(String date){
        editor.putString("date",date);
        editor.commit();
    }

    public  String getShuMiBankDate(){
        return  spf.getString("date","");
    }

    public void clean(){
        editor.clear().commit();
    }
}
