package com.cfjn.javacf.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;
/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：记账---导航条
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public class NavigationBar extends RelativeLayout implements View.OnClickListener {
    public static final int TAG_LEFT_BUTTON = -1;
    public static final int TAG_MIDDLE_TITLE = 0;
    public static final int TAG_RIGHT_TITLE = 1;
    private Context context;
    private OnNavigationBarClickListener mListener;
    public NavigationBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavigationBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.setBackgroundColor(getResources().getColor(R.color.red));
    }

    /**
     * 设置左侧按钮
     *
     * @param text
     *            按钮文字
     * @param resid
     *            资源文件
     */
    public void setLeftButton(String text, int resid) {
        setButton(text, resid, TAG_LEFT_BUTTON);
    }
    public void setRightButton(String text, int resid) {
        setButton(text, resid, TAG_RIGHT_TITLE);

    }

    /**
     * 设置标题
     *
     *  标题
     */
    public void setTitle(String text) {
        TextView view = new TextView(context);
        // set text
        view.setText(text);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        view.setTextColor(Color.WHITE);
        setView(view, false, 0, TAG_MIDDLE_TITLE);
    }

    /**
     * 设置右侧按钮
     *
     * @param text
     *            按钮文字
     * @param resid
     *            资源文件
     */
    private void setButton(String text, int resid, int tag) {
        Button view = new Button(context);
        if (text != null) {
            view.setText(text);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            view.setTextColor(Color.WHITE);
            setView(view, false, resid, tag);
        } else {
            setView(view, true, resid, tag);
        }
    }
    public void remove(int tag) {
        // remove old view (if exists)
        TextView oldView = (TextView) this.findViewWithTag(tag);
        if (oldView != null)
            this.removeView(oldView);
    }
    private void setView(View view, boolean hasIcon, int resid, int tag) {
        // remove old view (if exists)
        remove(tag);
        if (view == null) {
            return;
        }
        view.setTag(tag);
        int buttonSize = G.dp2px(context, 25);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, buttonSize);
        if (hasIcon) {
            lp = new LayoutParams(buttonSize, buttonSize);
        }
        switch (tag) {
            case TAG_LEFT_BUTTON:
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT|RelativeLayout.CENTER_VERTICAL);

                break;
            case TAG_MIDDLE_TITLE:
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;
            case TAG_RIGHT_TITLE:
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT|RelativeLayout.CENTER_VERTICAL);
                break;
            default:
                throw new IllegalArgumentException("请使用NavigationBar的TAG常量,例如：NavigationBar.TAG_LEFT_BUTTON,谢谢合作!");
        }
        view.setLayoutParams(lp);
        // set view drawable
        if (resid != 0) {
            view.setBackgroundResource(resid);
        }
        view.setOnClickListener(this);
        this.addView(view);
    }
    /**
     * 设置监听器
     */
    public void setNavigationBarClickListener(OnNavigationBarClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        if (mListener != null) {
            mListener.OnNavigationBarClick(tag);
        }
    }

    /**
     * 导航栏监听器
     */
    public interface OnNavigationBarClickListener {

        /**
         * 当用户点击导航栏上的view时调用
         *
         * @param tag
         *            被点击view的标识
         */
        public void OnNavigationBarClick(int tag);
    }

}
