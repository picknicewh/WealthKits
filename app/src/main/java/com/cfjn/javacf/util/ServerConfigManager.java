package com.cfjn.javacf.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 服务器配置文件
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ServerConfigManager {
	/**
	 * 服务器状态地址
	 */
	public static final String SERVER_STATUS_IP = "https://s1.openhunme.com/authuser/queryServer";
	/**
	 * 服务器IP列表
	 */
	public static final List<String> SERVER_IP_LIST = new ArrayList<String>();
	/**
	 * 主服务器地址
	 */
//	public static final String SERVER_IP = "http://192.168.1.188:8080/usermanager";//本机
//	public static final String SERVER_IP = "https://zhu.hunme.net:8443";//测试服务器
//  public static final String SERVER_IP = "https://slave.openhunme.com";//备用服务器
//  public static final String SERVER_IP = "https://slave.openhunme.com";//备用服务器
//	public static final String SERVER_IP = "https://slb.openhunme.com";//主服务器地址
//	public static final String SERVER_IP = "http://slb.openhunme.com";
//	public static final String SERVER_IP = "http://192.168.1.200:8080";
//  public static final String SERVER_IP_LOGIN = "https://slave.openhunme.com";
//	public static final String SERVER_IP_LOGIN = "http://zhu.hunme.net:8080";
	public static final String SERVER_IP = "http://zhu.hunme.net:8080";
//   public static final String SERVER_IP="https://slave.openhunme.com";

}
