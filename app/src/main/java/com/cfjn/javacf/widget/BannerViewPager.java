package com.cfjn.javacf.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;

import java.util.ArrayList;
/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 广告图和导航图banner
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class BannerViewPager extends ViewPager {
	private PointF downPoint = new PointF();
	private OnSingleTouchListener onSingleTouchListener;
	/**
	 * 点的容器
	 */
	private LinearLayout dotsLayout;
	/**
	 * 选中点
	 */
	private int dot_focus_resId = R.drawable.oval_red;
	/**
	 * 未选中点
	 */
	private int dot_normal_resId = R.drawable.oval_light_gray;
	/**
	 * 标题
	 */
	private TextView tvTitle;
	/**
	 * * 装载需要展示的点
	 */
	private ArrayList<View> dots;
	/**
	 * 装载需要展示的标题
	 */
	private ArrayList<String> titles;
	private Context context;
	private int currentItem = 0;
	private int ModeSize;

	public BannerViewPager(Context context) {
		super(context);
	}

	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		super.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public void setDotsLayout(LinearLayout dotsLayout, int size) {
		// TODO Auto-generated method stub
		setDotsLayout(dotsLayout, size, dot_focus_resId, dot_normal_resId);
	}

	public void setDotsLayout(LinearLayout dotsLayout, int size, int focusRes, int normalRes) {
		// TODO Auto-generated method stub
		this.dotsLayout = dotsLayout;
		this.dot_focus_resId = focusRes;
		this.dot_normal_resId = normalRes;
		this.ModeSize=size;
		initDot(size);
	}

	public void addTitle(String title) {
		if (titles == null) {
			titles = new ArrayList<String>();
		}
		titles.add(title);
	}

	public void setTitle(TextView tvTitle) {
		this.tvTitle = tvTitle;
		if (titles != null && titles.size() > 0) {
			tvTitle.setText(titles.get(0));
		}
	}

	private void initDot(int size) {
		dots = new ArrayList<View>();
		dotsLayout.removeAllViews();
		for (int i = 0; i < size; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(G.dp2px(context, 8), G.dp2px(context, 8));
			params.setMargins(G.dp2px(context, 4), 0, G.dp2px(context, 4), 0);
			View dot = new View(context);
			if (i == currentItem) {
				dot.setBackgroundResource(dot_focus_resId);
			} else {
				dot.setBackgroundResource(dot_normal_resId);
			}
			dot.setLayoutParams(params);
			dotsLayout.addView(dot);
			dots.add(dot);
		}
//		if(ModeSize>1){
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextView textView = new TextView(context);
//			textView.setText((currentItem+1)+"/"+size);
//			textView.setLayoutParams(params);
//			textView.setTextSize(18);
//			textView.setTextColor(Color.WHITE);
//			dotsLayout.addView(textView);
//			dots.add(textView);
//		}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent evt) {
		return false;
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		private OnPageChangeListener pageChangeListener;
		int oldPosition = 0;

		public MyOnPageChangeListener() {
			// TODO Auto-generated constructor stub
		}

		public MyOnPageChangeListener(OnPageChangeListener pageChangeListener) {
			this.pageChangeListener = pageChangeListener;
		}

		@Override
		public void onPageSelected(int position) {
			if (dots != null) {
				position = position % dots.size();
//				position = position % ModeSize;
			}
			currentItem = position;
//			G.log(currentItem+"-------rrrr---------");
			if (dots != null && position != oldPosition&&dots.size()>0) {
				dots.get(position).setBackgroundResource(dot_focus_resId);
				dots.get(oldPosition).setBackgroundResource(dot_normal_resId);
//				dots.get(0).setText((currentItem+1)+"/"+ModeSize);
			}
			if (titles != null && titles.size() > 0) {
				tvTitle.setText(titles.get(position));
			}
			oldPosition = position;

			if (pageChangeListener != null) {
				pageChangeListener.onPageSelected(position);
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (pageChangeListener != null) {
				pageChangeListener.onPageScrollStateChanged(arg0);
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (pageChangeListener != null) {
				pageChangeListener.onPageScrolled(arg0, arg1, arg2);
			}

		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
		super.setOnPageChangeListener(new MyOnPageChangeListener(onPageChangeListener));

	};

	private ViewGroup disallowView;

	public ViewGroup getDisallowView() {
		return disallowView;
	}

	public void setDisallowView(ViewGroup disallowView) {
		this.disallowView = disallowView;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		switch (evt.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记录按下时候的坐标
			downPoint.x = evt.getX();
			downPoint.y = evt.getY();
			if (this.getChildCount() > 1 && getParent() != null) { // 有内容，多于1个时
				// 通知其父控件，现在进行的是本控件的操作，不允许拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			if (disallowView != null) {
				disallowView.requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (this.getChildCount() > 1 && getParent() != null) { // 有内容，多于1个时
				// 通知其父控件，现在进行的是本控件的操作，不允许拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
			// 在up时判断是否按下和松手的坐标为一个点
			if (PointF.length(evt.getX() - downPoint.x, evt.getY() - downPoint.y) < 5.0f) {
				onSingleTouch(this);
				return true;
			}
			if (disallowView != null) {
				disallowView.requestDisallowInterceptTouchEvent(false);
			}
			break;
		}
		return super.onTouchEvent(evt);
	}

	public void onSingleTouch(ViewPager v) {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch(v);
		}
	}

	public interface OnSingleTouchListener {
		public void onSingleTouch(ViewPager v);
	}

	public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}

}
