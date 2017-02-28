package com.cfjn.javacf.base;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：系统消息数据库辅助 负责增删查改
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class UserMessageDb {

    /**
     * 插入数据
     */
    public static  void insert(SQLiteDatabase db,String values,String time,String source,String title,String content){
        String sql="insert into user (usertype,time,source,title,content) values ('"+values+"','"+time+"','"+source+"','"+title+"','"+content+"')";
        db.execSQL(sql);
    }

    /**
     * 修改数据
     */
    public static void update(SQLiteDatabase db,int index,String values){
        String sql="update user set usertype = '"+values+"' where uid = "+index;
        db.execSQL(sql);
    }

    /**
     * 查询数据
     */
    public static Cursor select(SQLiteDatabase db,int index){
        String sql="select * from user where uid = "+index;
        Cursor localCursor= db.rawQuery(sql,null);
        return localCursor;
    }

    /**
     * 删除所有
     * @param db
     */
    public static void delete(SQLiteDatabase db){
        String sql="delete from user";
        db.execSQL(sql);

        String sqls = "DELETE FROM sqlite_sequence";
        db.execSQL(sqls);

    }

}
