package com.cfjn.javacf.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.cfjn.javacf.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 图片缓存工具类
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class InitImageLoader {
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    public InitImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_ad_default) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_ad_default) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_ad_default) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                        // .bitmapConfig(Bitmap.Config.RGB_565);//
                .bitmapConfig(Bitmap.Config.RGB_565).build(); // 构建完成
        imageLoader =ImageLoader.getInstance();
    }

    public void doLoadImg(String uri, ImageView imageView){

        imageLoader.displayImage(uri, imageView, options);
    }

    public void doSchemImg(String uri,ImageView imageView){
        ImageDownloader.Scheme.FILE.wrap(uri);
        imageLoader.displayImage(uri, imageView, options);
    }

    public void setChat(){
        imageLoader.clearMemoryCache();
        imageLoader.clearDiskCache();
    }
}
