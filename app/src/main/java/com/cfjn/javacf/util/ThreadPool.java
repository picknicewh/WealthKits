package com.cfjn.javacf.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 线程池
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */

public class ThreadPool {
    /**
     * 创建可缓存的线程池
     */
    public static ExecutorService cachedThreadPool= Executors.newCachedThreadPool();

    /**
     * 创建一个定长线程
     */
    public static ExecutorService fixedThreadPool=Executors.newFixedThreadPool(3);

}
