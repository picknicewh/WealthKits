package com.cfjn.javacf.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidation {

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^[1][0-9]{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isCardID(String cardid) {
		Pattern p = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");
		Matcher m = p.matcher(cardid);
		return m.matches();
	}
}
