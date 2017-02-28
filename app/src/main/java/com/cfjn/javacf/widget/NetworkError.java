package com.cfjn.javacf.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 程序断网提示
 * 版本说明：代码规范整改
 * 附加注释：1.没有网络时显示断网机制，可以重新加载！设置显示的内容
 * 断网都用这个好了，统一一下
 *  2016-5-25 ：增加了没有数据显示的页面 处理机制和断网机制一样
 * 用setPicImagerViewShowType（显示的类型）、
 * true 断网状态  false 无数据加载状态
 *
 * 主要接口：
 */

public class NetworkError {
    private ImageView picImagerView;
    private ImageView animImagerView;
    private TextView promptTextView;
    private Animation rotateAnim;
    private RelativeLayout notNetworkLayout;
    private static Context mcontext;
    private View mview;
    private String proText;

    public static NetworkError getInstance(Context context){
        mcontext=context;
        return new NetworkError();
    }


    public View getView(){
        mview= LayoutInflater.from(mcontext).inflate(R.layout.networkw_error,null);
        initViewID(mview);
        return  mview;
    }

    private void initViewID(View view){
        picImagerView= (ImageView) view.findViewById(R.id.img_notNetwork);
        animImagerView= (ImageView) view.findViewById(R.id.image_footer);
        promptTextView= (TextView) view.findViewById(R.id.tv_notNetwork);
        notNetworkLayout= (RelativeLayout) view.findViewById(R.id.b_det_nonet);
        rotateAnim = AnimationUtils.loadAnimation(mcontext, R.anim.rotate_evey);
    }

    /**
     *  设置View隐藏还是显示
     * @param isShow 显示状态 true为显示 false为不显示
     */
    public void setShowType(boolean isShow){
        notNetworkLayout.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    /**
     * 启动动画
     */
    public void startAnimation(){
        picImagerView.setVisibility(View.GONE);
        promptTextView.setVisibility(View.GONE);
        animImagerView.setVisibility(View.VISIBLE);
        animImagerView.startAnimation(rotateAnim);
    }

    /**
     * 停止动画
     */
    public void stopAnimation(){
        picImagerView.setVisibility(View.VISIBLE);
        promptTextView.setVisibility(View.VISIBLE);
        animImagerView.setVisibility(View.GONE);
        animImagerView.clearAnimation();
    }

    /**
     * 设置提示信息
     * @param text 提示
     */
    public void setPromptText(String text){
        proText=text;
        promptTextView.setText(text);
    }

    /**
     * 设置断网图片的点击事件，再次点击重新加载数据
     * @param view 点击事件
     */
    public void  setPicImagerViewOnClockListen(View.OnClickListener view){
        picImagerView.setOnClickListener(view);
    }

    /**
     * 设置图片显示的是断网还是没有值
     * 默认是断网状态
     * @param isNetWorkErrror 状态
     */
    public void setPicImagerViewShowType(boolean isNetWorkErrror){
        if(isNetWorkErrror){
            picImagerView.setImageResource(R.drawable.ic_failed);
            promptTextView.setText(null==proText?"加载失败，点击屏幕重试!":proText);
        }else{
            picImagerView.setImageResource(R.drawable.ic_mammon);
            promptTextView.setText(null==proText?"暂无相关记录...":proText);
        }
    }
}
