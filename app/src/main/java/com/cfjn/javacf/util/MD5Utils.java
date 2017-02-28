package com.cfjn.javacf.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：MD5加密
 * 版本说明：记录用户数据和各种提交记录
 * 附加注释：
 * 主要接口：
 */

public class MD5Utils {
	
	/**
	 * md5
	 * @return
	 */
	public static String encode(String string) {

		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(string.getBytes());
			StringBuffer buffer = new StringBuffer();
			for (byte b : result) {
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				// System.out.println(str);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

}
