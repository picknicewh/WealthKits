package com.cfjn.javacf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.cfjn.javacf.widget.Pullable;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 二级列表自定义上拉下拉刷新
 * 版本说明：代码规范整改
 * 附加注释：任何控件只需实现Pullable都可以上啦下拉刷新，这里只是对数据做了下拉上拉判断
 * 主要接口：
 */

public class PullableExpandableListView extends ExpandableListView implements
		Pullable
{

	public PullableExpandableListView(Context context)
	{
		super(context);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// 滑到顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}

}
