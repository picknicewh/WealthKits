package com.cfjn.javacf.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.activity.member.LockActivity;
import com.cfjn.javacf.util.G;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：用户数据中心
 * 版本说明：记录用户数据和各种提交记录
 * 附加注释：
 * 主要接口：
 */
public class UserInfo {
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    private static UserInfo userInfo;
    private Context contex;

    public static UserInfo getInstance(Context context){
        if (userInfo == null){
            userInfo = new UserInfo(context);
        }
        return userInfo;
    }

    public UserInfo(Context context) {
        this.contex=context;
        spf=context.getSharedPreferences("ITcast",Context.MODE_PRIVATE);
        editor=spf.edit();
    }

    public void setLoginName(String values){
        editor.putString("LoginName",values);
        editor.commit();
    }

    public String  getLoginName(){
        return  spf.getString("LoginName","");
    }

    public  void setToken(String values){
        editor.putString("Token",values);
        editor.commit();
    }

    public  String getToken(){
        return  spf.getString("Token","");
    }

    public void setSecretKey(String values){
        editor.putString("SecretKey",values);
        editor.commit();
    }

    public String getSecretKey(){
        return  spf.getString("SecretKey","");
    }

    public void setStyleEva(String values){
        editor.putString("StyleEva",values);
        editor.commit();
    }

    public String getStyleEva(){
        return  spf.getString("StyleEva","未评测");
    }

    public void setUserName(String values){
        editor.putString("UserName",values);
        editor.commit();
    }
    public String getUserName(){
        return  spf.getString("UserName","未认证");
    }

    public void setIdNumber(String values){
        editor.putString("IdNumber",values);
        editor.commit();
    }
    public String getIdNumber(){
        return  spf.getString("IdNumber","未知");
    }

//    public void setPhoneNumber(String values){
//        editor.putString("PhoneNumber",values);
//        editor.commit();
//    }
//
//    public String getPhoneNumber(){
//        return  spf.getString("PhoneNumber","");
//    }

    public void setSmToken(String values){
        editor.putString("SmToken",values);
        editor.commit();
    }

    public  String getSmToken(){
        return spf.getString("SmToken","");
    }

    public void setSmTokenSecret(String values){
        editor.putString("SmTokenSecret",values);
        editor.commit();
    }

    public  String getSmTokenSecret(){
        return spf.getString("SmTokenSecret","");
    }

    public void initUserData() {
        G.user.set("", "", getToken(), getSmToken(), getSmTokenSecret(), "");
    }
    //记录交易记录最后提交时间判断满足15点提交时间
    public void setDateTime(String dateTime){
        editor.putString("dateTime",dateTime);
        editor.commit();
    }

    public String getDateTime(){
        return spf.getString("dateTime","");
    }

    //记录每次提交交易记录的时间
    public void setSubmitTime(String submitTime){
        editor.putString("submitTime",submitTime);
        editor.commit();
    }
    public String getSubmitTime(){
        return spf.getString("submitTime","2000-01-01");
    }
    public void setMangerSize(int values){
        editor.putInt("values", values);
        editor.commit();
    }

    //记录资产每次提交记录的时间
    public void setSubMitAesstes(String aesstesTime){
        editor.putString("aesstesTime", aesstesTime);
        editor.commit();
    }

    public String getSubMitAesstes(){
        return spf.getString("aesstesTime","");
    }

    public int getMangerSize(){
        return  spf.getInt("values", 0);
    }

    /**
     * 清除锁屏密码
     */
    public void clearLock() {
        SharedPreferences preferences =  contex.getSharedPreferences(LockActivity.LOCK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }
    /**
     * 个人信息清除
     */
    public void clean(){
        clearLock();
        if(null!=MainActivity.UMDB){
            cleanSystemMassger();
        }
        editor.clear().commit();
    }

    /**
     * 系统消息清除
     */
    public void cleanSystemMassger(){
        SQLiteDatabase db= MainActivity.UMDB.getWritableDatabase();
        UserMessageDb.delete(db);
        db.close();
    }

    /**
     * 用户是否第一次打开APP
     */
    public void setFirstApp(boolean valuse){
        editor.putBoolean("valuse", valuse);
        editor.commit();
    }

    public boolean getFirstApp(){
        return spf.getBoolean("valuse",true);
    }

}
