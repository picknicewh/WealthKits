package com.cfjn.javacf.shumi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类型转换
 * @author John
 *
 */
public class MyShumiSdkValueFormator {
	private static SimpleDateFormat mDateFormatIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
	private static SimpleDateFormat mDateFormatOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
	
	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String reformatDate(String date) {
		String str = null;
		try {
			Date d = mDateFormatIn.parse(date);
			str = mDateFormatOut.format(d);
		} catch (Exception e) {
			str = date;
		}
		return str;
	}
}
