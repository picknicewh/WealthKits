package com.cfjn.javacf.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 首页轮播图和导航页适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class SimplePagerAdapter extends PagerAdapter{
	private List<View> mViews;
	public SimplePagerAdapter(List<View> views){
		mViews = views;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		 container.removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViews.get(position));
		return mViews.get(position);
	}

}
