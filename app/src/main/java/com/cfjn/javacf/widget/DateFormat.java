package com.cfjn.javacf.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormat {
    private static SimpleDateFormat mDateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
    private static SimpleDateFormat mDateFormat1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static String returnDate;

    public static String dateFormat(String date){
        try {
            if(date.indexOf("T")==-1){
                return date;
            }
            Date strDate=mDateFormat.parse(date);
            returnDate=mDateFormat1.format(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    private static SimpleDateFormat dayFormat1=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private static SimpleDateFormat dayFormat2=new SimpleDateFormat("MM-dd", Locale.CHINA);
    private static String returnDay;
    public static String dayFormat(String date){
        try {
            Date strDate=dayFormat1.parse(date);
            returnDay=dayFormat2.format(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDay;
    }

    private static SimpleDateFormat mTimeFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static SimpleDateFormat mTimeFormat1= new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private static String returnTime;

    public static String timeDate(String date){
        try {
            Date strDate=mTimeFormat.parse(date);
            returnTime=mTimeFormat1.format(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnTime;
    }

}
