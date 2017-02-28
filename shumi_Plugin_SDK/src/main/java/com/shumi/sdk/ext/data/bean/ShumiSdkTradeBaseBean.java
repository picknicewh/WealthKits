package com.shumi.sdk.ext.data.bean;

import java.io.Serializable;

import com.google.myjson.annotations.SerializedName;

/**
 * 数米OPENAPI 返回字段基类
 * 根据返回Json定义一个Bean，并根据Gson {@link SerializedName}做好Annotation标注(不标注也可，保证类型是public并且名称和字段一致即可)<br>
 * 如：public String Code; 和 @SerializedName("Code") public String Code;是等效的<br>
 * 为了保证类型解析正确不会发生异常建议所有字段都必须使用可空类型<br>
 * 数米Openapi使用C#, 下面是C# <-> Java基本对应类型，类型会在openapi help中有所说明<br>
 * ********************************************<br>
 * Int32?			Integer<br>
 * Decimal?			Double<br>
 * String			String<br>
 * Boolean?			Boolean<br>
 * Int64?			Integer<br>
 * Byte?			Integer<br>
 * ********************************************<br>
 * 
 * 为了兼容老版本的Bean，这里的bean字段和v1版类似,不使用getset封装
 */
@SuppressWarnings("serial")
public class ShumiSdkTradeBaseBean implements Serializable {

}
