package com.cfjn.javacf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 实现单选（我猜的）
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class RadiobuttonUtil extends RadioGroup {
    public RadiobuttonUtil(Context context) {
        super(context);
    }

    public RadiobuttonUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        changButton();
    }

    private void changButton(){
        int count=super.getChildCount();
    }
}
