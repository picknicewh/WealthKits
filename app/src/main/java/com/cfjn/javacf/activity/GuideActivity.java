package com.cfjn.javacf.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cfjn.javacf.JPush.JPushBaseActivity;
import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.SimplePagerAdapter;
import com.cfjn.javacf.widget.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 首页导航页
 *  版本说明：代码规范整改
 *  附加注释：滑动切换首页 最后一页出现前往Button
 *  主要接口：暂无
 */
public class GuideActivity extends JPushBaseActivity {
    /**
     * 导航页主控件
     */
    @Bind(R.id.bvp_guide)
    BannerViewPager bvpGuide;
    /**
     * 广告图小圆点
     */
    @Bind(R.id.ll_dots)
    LinearLayout llDots;
    /**
     * 立即前往button
     */
    @Bind(R.id.ib_goto)
    ImageButton ibGoto;

    /**
     * 显示立即前往动画
     */
    private Animation animation;
    /**
     * 装载需要展示导航图
     */
    private List<View> guideList;
    /**
     * viewpager适配器
     */
    private SimplePagerAdapter adapter;
    /**
     * 存储是否是第一次打开App
     */
    private SharedPreferences.Editor editor;
    private SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否是第一次打开app
        spf=getSharedPreferences("FirstApp", Context.MODE_PRIVATE);
            if(spf.getBoolean("firstApp",true)){
            setContentView(R.layout.activity_guide);
            ButterKnife.bind(this);
            initWindow();
            initializeMode();
            initializeView();
            initJPushConfiguration();
        }else{
            startActivity(new Intent(this,ShowLockActivity.class));
            finish();
        }
    }

    private void initializeMode() {
        animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        guideList = new ArrayList<>();
        editor = spf.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        JPushInterface.resumePush(this);
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        JPushInterface.onPause(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void initializeView() {
        int[] pics = {R.drawable.ic_guild1, R.drawable.ic_guild2, R.drawable.ic_guild3, R.drawable.ic_guild4};
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            guideList.add(iv);
        }
        adapter = new SimplePagerAdapter(guideList);
        bvpGuide.setAdapter(adapter);
        bvpGuide.setDotsLayout(llDots, guideList.size());
        bvpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == guideList.size() - 1) {
                    ibGoto.setAnimation(animation);
                    ibGoto.setVisibility(View.VISIBLE);
                } else {
                    ibGoto.clearAnimation();
                    ibGoto.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 标题栏透明
     * 仅支持api大于等于19
     */
    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @OnClick(R.id.ib_goto)
    public void onClick() {
        editor.putBoolean("firstApp", false);
        editor.commit();
        startActivity(new Intent(GuideActivity.this, ShowLockActivity.class));
        finish();
    }
}
