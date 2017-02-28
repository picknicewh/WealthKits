package com.cfjn.javacf.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/12/31.
 */
public class MybookkeepingDbHepler extends SQLiteOpenHelper {
    private static final String DATABASENAMR = "user.db3";
    private static final int DATABASEVERSION = 1;
    private static final String TABLENAME = "user";

    public MybookkeepingDbHepler(Context context) {
        super(context, DATABASENAMR, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLENAME
                + "(id integer primary key autoincrement,"
                + "code Integer not null,"
                + "rcode Integer not null,"
                + "title varchar(50) not null,"
                + "type Integer not null,"
                + "image Integer not null,"
                + "flag Integer not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists" + TABLENAME;
        db.execSQL(sql);
        this.onCreate(db);
    }

    public void insert(SQLiteDatabase db, int code, int rcode, String title, int type, int image, int flag) {
        String sql = "insert into " + TABLENAME + "(code,rcode,title,type,image,flag) values(" + code + "," + rcode + ",'"
                + title + "'," + type + "," + image + "," + flag + ")";
        db.execSQL(sql);
    }

    public void update(SQLiteDatabase db, int code, int flag) {
        String sql = "update " + TABLENAME + " set flag=" + flag + " where code=" + code;
        db.execSQL(sql);
    }

    public void delete(SQLiteDatabase db, String title) {

        String sql ="delete from user where title="+"'"+title+"'";
        db.execSQL(sql);
    }
 /*   public Cursor find(SQLiteDatabase db) {
        // String sql = "select * from user";
        Cursor cursor = db.query(TABLENAME, new String[] { "username",
                "password" }, null, null, null, null, null);
        return cursor;
    }*/
}
