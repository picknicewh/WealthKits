package com.cfjn.javacf.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/5/19.
 */
public class CalculateIncomePopup extends PopupWindow implements View.OnClickListener{

    //PopupWindow上面装载的View
    private View mMenuView;
    private Context context;
    //输入框
    private EditText et1;
    private EditText et2;
    //取消
    private ImageButton popup_image;
    //购买金额
    private double total;
    //购买天数
    private double day;
    //计算收益
    private Button btn_financial_period;
    //预期收益
    private TextView financial_period_text1;
    //银行预期收益
    private TextView financial_period_text2;
    // 七年年化率
    private  double yieldSevenDay;
    public CalculateIncomePopup(final Context context,final double yieldSevenDay){
        this.context =context;
        this.yieldSevenDay = yieldSevenDay;
        inin();
    }
    /**
     * 将xml布局初始化为View,并初始化上面的控件
     */
    private  void  setMenuView(){
        mMenuView = LayoutInflater.from(context).inflate(R.layout.pop_earnings, null);
        et1 = (EditText) mMenuView.findViewById(R.id.financial_period_ed1);
        et2 = (EditText) mMenuView.findViewById(R.id.financial_period_ed2);
        btn_financial_period = (Button) mMenuView.findViewById(R.id.btn_financial_period);
        financial_period_text1 = (TextView) mMenuView.findViewById(R.id.financial_period_text1);
        //  financial_period_text2 = (TextView) mMenuView.findViewById(R.id.financial_period_text2);
        popup_image = (ImageButton) mMenuView.findViewById(R.id.popup_image);
        popup_image.setOnClickListener(this);
        btn_financial_period.setOnClickListener(this);
    }
    /**
     * popupWindow通用设置
     */
    public void inin() {
        //设置SignPopupWindow的View
        this.setContentView(mMenuView);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popup_image:
                dismiss();
                break;
            case R.id.btn_financial_period:
                DecimalFormat format1 = new DecimalFormat("#0.00");
                double yield= Double.parseDouble(format1.format(yieldSevenDay));
                if (et1.length() <= 0 || et2.length() <= 0) {
                    G.showToast(context, "请输入金额和天数");
                } else {
                    total = Double.parseDouble(et1.getText().toString());
                    day = Double.parseDouble(et2.getText().toString());
                    financial_period_text1.setText(format1.format(total * day * yield / 365));
                    financial_period_text2.setText(format1.format(total * day * 0.0035 / 365));
                }
                break;
        }
    }
}
