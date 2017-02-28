package com.cfjn.javacf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private final static SimpleDateFormat DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static Date parseDateTime(String dateStr) {
		Date date = null;
		try {
			date = DATE_TIME.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDateTime(Date date) {
		if (date != null) {
			return DATE_TIME.format(date);
		}
		return "";
	}

	/**
	 * yyyy-MM-dd
	 */
	public static Date parseDate(String dateStr) {
		Date date = null;
		try {
			date = DATE.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * yyyy-MM-dd
	 */
	public static String formatDate(Date date) {
		if (date != null) {
			return DATE.format(date);
		}
		return "";
	}

	/**
	 * 获取当前时间
	 */
	public static String getCurrentDateTime() {
		return formatDateTime(new Date());
	}

	/**
	 * 获取当前日期
	 */
	public static String getCurrentDate() {
		return formatDate(new Date());
	}

	/**
	 * 获取N天后的日期
	 * 
	 * @return
	 */
	public static String getNDaysLaterDate(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return formatDate(calendar.getTime());
	}

	/**
	 * 比较目标时间与当前时间的天数差
	 */
	public static int getDaysLeft(String dateStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr));//传
		int targetDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTime(new Date());//现在
		int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		return targetDayOfYear - currentDayOfYear;
	}
	/**
	/**
	 * 获取传入的date为星期几
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	/**
	 * 获取i天后的工作日
	 * @param  ，如今天星期4,2或3或4天后就是星期一
	 * @return yyyy年MM月dd日 HH时
	 */
	public static String nextBusinessDay(int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时");
		Date dt = new Date();
		dt = new Date(dt.getTime()+i*86400000L);
		String s = getWeekOfDate(dt);
		if(s.equals("星期六")){
			dt.setTime(dt.getTime()+2*86400000L);
		}else if(s.equals("星期日")){
			dt.setTime(dt.getTime()+86400000L);
		}else{
			dt.setTime(dt.getTime());
		}
        return sdf.format(dt);
    }
	
	/**
	 * 获取传入的yyyy-MM-dd为星期几
	 * 
	 * @param str
	 * @return
	 */
	public static String getWeekOfDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	/**
	 * 获取传入的yyyy-MM-dd与今天间隔i天的时间
	 *
	 * @param i
	 * @return
	 */
	public static String getIntrventDaysTime(int i){
		String date;
		long l = new Date().getTime()-(1000*60*60*24*i);
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		date = sdformat.format(new Date(l));
		return  date;
	}
	public static void main(String[] args) {
		System.out.println(getDaysLeft("2015-6-27"));
	}
	
	
}
