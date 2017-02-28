package com.openhunme.cordova.chinapay;

public class ChinaPayHelper {
	public static String substring(String source, String prefix, String surfix)
	{
		if(source != null && source.length() > prefix.length())
		{
			int start = source.indexOf(prefix);
			if(start >= 0)
			{
				start += prefix.length();
				int end = source.indexOf(surfix, start);
				if(end > start)
				{
					return source.substring(start, end);
				}
			}
		}
		return "";
	}
}
