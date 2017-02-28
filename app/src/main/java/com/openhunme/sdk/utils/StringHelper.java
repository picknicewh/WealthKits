package com.openhunme.sdk.utils;

/**
* @ClassName: StringHelper
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 熊凯
* @date 2013-1-11 下午4:11:35
*
*/ 
public class StringHelper
{
    public static final boolean isEmpty(String str)
    {
        if(str == null || str.trim().equals("") || str.toLowerCase().trim().equals("null"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
