package com.cfjn.javacf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 方法回掉 （此类作废）
 * 版本说明：
 * 附加注释：自定义ScrollView，解决ScrollView嵌套ViewPaqger滑动冲突问题
 * 主要接口：
 */
public class VerticalScrollView extends ScrollView {
    private float xDistance, yDistance, xLast, yLast;

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if(xDistance > yDistance){
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
