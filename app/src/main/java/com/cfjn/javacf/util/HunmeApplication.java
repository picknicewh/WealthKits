package com.cfjn.javacf.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.appService.ReceiverManger;
import com.lzy.okhttputils.OkHttpUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.shumi.sdk.ShumiSdkInitializer;
import com.shumi.sdk.env.ShumiSdkEnv;
import com.shumi.sdk.ext.diagnostics.ShumiSdkCrashReportHandler;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by DELL on 2016/1/19.
 */
public class HunmeApplication extends Application {
    private static HunmeApplication instance;
    private static List<Activity> activityList = new LinkedList<Activity>();
    private UserInfo userInfo;
    public static ReceiverManger receiverManger;
    public static HunmeApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        userInfo = UserInfo.getInstance(getApplicationContext());
        userInfo.initUserData(); //---赋值给用户中心----
        receiverManger = new ReceiverManger(getApplicationContext());
        initShumiFundTradingSdk();
        initImageLoader(getApplicationContext());
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        OkHttpUtils.init(this);
    }
    /**
     *初始化数米SDK
     */
    private void initShumiFundTradingSdk(){
        ShumiSdkInitializer.init(this);
        ShumiSdkEnv.init(this);
        ShumiSdkCrashReportHandler handler=ShumiSdkCrashReportHandler.getInstance(this);
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    public static void exit() {
        for (Activity activity : activityList) {
            activity.finish();
            JPushInterface.stopPush(getInstance());
        }
        System.exit(0);
    }


    //图片缓存
    private static void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCacheAware<String, Bitmap> memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        // 缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ChatFill");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                        // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                        // int i = 50 * 1024 * 1024;
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // 连接超时5s
                        // 下载时间30s
                .writeDebugLogs() // Remove for release app
                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

}
