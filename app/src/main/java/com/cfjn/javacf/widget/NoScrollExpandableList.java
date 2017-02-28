package com.cfjn.javacf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：没有滑动ExpandableList
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public class NoScrollExpandableList extends ExpandableListView {

    public NoScrollExpandableList(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
