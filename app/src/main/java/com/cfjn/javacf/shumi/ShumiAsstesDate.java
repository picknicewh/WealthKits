package com.cfjn.javacf.shumi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 数米提交资产数据失败时候保存起来
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ShumiAsstesDate {
    private Context context;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    public ShumiAsstesDate(Context context) {
        this.context = context;
        spf=context.getSharedPreferences("ITcast3",Context.MODE_PRIVATE);
        editor=spf.edit();
    }

    public void setShumiAsstesDate(String date){
        editor.putString("date",date);
        editor.commit();
    }

    public  String getShumiAsstesDate(){
        return  spf.getString("date","");
    }

    public void clean(){
        editor.clear().commit();
    }
}
