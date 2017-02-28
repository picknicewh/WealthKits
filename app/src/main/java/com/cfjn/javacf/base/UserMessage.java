package com.cfjn.javacf.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：系统消息数据库
 * 版本说明：记录系统消息和查看状态
 * 附加注释：
 * 主要接口：
 */
public class UserMessage extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydata.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private static final String TABLENAME = "user";
    public UserMessage(Context context) {
        super(context, DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table " +TABLENAME+
                " (uid integer primary key autoincrement," +
                "usertype varchar(50)," +
                "time varchar(50)," +
                "source varchar(50)," +
                "title varchar(50)," +
                "content varchar(50))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists" + TABLENAME;
        db.execSQL(sql);
        this.onCreate(db);
    }
}
