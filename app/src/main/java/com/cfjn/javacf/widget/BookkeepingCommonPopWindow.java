package com.cfjn.javacf.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：记账--删除对话框的公共部分
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public abstract class BookkeepingCommonPopWindow extends PopupWindow implements View.OnClickListener,OKHttpListener{

    /**
     *PopupWindow上面装载的View
     */
    public View mMenuView;
    /**
     *关闭按钮
     */
    public Button pop_close;
    /**
     *删除按钮
     */
    public Button pop_delete;
    public XStream xstream = new XStream(new DomDriver());
    /**
     * 提交删除账单
     */
    public  Context context;

    public void submitMessage(String xmlStr) {
        Map<String, String> requestParamsMap = new HashMap<>();
        try {
            requestParamsMap.put("accountBook", URLEncoder.encode(xmlStr, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtil.sendPost(ApiUri.SUBMIT_MESSAGE, requestParamsMap, this);
    }
    public void initview(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_delete, null);
        pop_close= (Button) mMenuView.findViewById(R.id.pop_close);
        pop_delete= (Button) mMenuView.findViewById(R.id.pop_delete);
        pop_close.setOnClickListener(this);
        pop_delete.setOnClickListener(this);
    }
    public void inin() {
        initview();
        //设置SignPopupWindow的View
        this.setContentView(mMenuView);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

}
