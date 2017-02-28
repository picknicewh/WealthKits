package com.cfjn.javacf.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.fund.SgBuySuccessActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.MenberCent;
import com.cfjn.javacf.modle.SGBuyOkVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.MD5Utils;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *  作者： zll
 *  时间： 2016-6-17
 *  名称： 松果基金交易处理
 *  版本说明：代码规范整改
 *  附加注释：松果交易工具类 通过传入对应的参数实现不同的操作
 *  主要接口：1.申购/转入
 *            2.转出
 *            3.赎回
 *            4.撤单
 */
public class PinealHelpPopup extends PopupWindow implements OKHttpListener ,View.OnClickListener{
    /**
     * 交易密码
     */
    private String password;
    /**
     *  交易状态
     */
    private int index;
    /**
     * 银行卡号
     */
    private String cardNumber;

    /**
     * 银行卡类型
     */
    private String cardType;

    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 基金类型
     */
    private String fundType;
    /**
     * 判断是转入还是申购
     */
    private int payMethod;
    /**
     * 交易金额 元为单位
     */
    private String money;
    /**
     * 交易金额 分为单位
     */
    private String money_;
    /**
     * 基金名称
     */
    private String fundName;
    /**
     * 银行卡名称
     */
    private String cardName;
    /**
     *PopupWindow上面装载的View
     */
    private View mMenuView;
    /**
     * 松果取消密码输入
     */
    private ImageButton imageClose;

    /**
     * 松果输入密码
     */
    private EditText sg_password_ed;
    /**
     * 松果——确认
     */
    private Button queren_password;
    /**
     * activity 对象
     */
    private Activity context;
    /**
     * 会员中心
     */
    private UserInfo userInfo;
    /**
     * 加载提示框
     */
    private LoadingDialog dialog;

    /**
     *  松果基金操作对象
     * @param context Activity 对象 用户弹对话框和popupWindow
     * @param index   行为 0是申购或者转入  1是转出或者赎回  3是撤单
     * @param money   金额
     * @param cardNumber  银行卡号
     * @param cardType   银行卡类型
     * @param fundCode   基金代码
     * @param fundType    基金类型
     * @param payMethod  转入还是申购
     * @param fundName   基金名称
     * @param cardName  银行名称
     */
    public PinealHelpPopup(final Activity context, final int index, final String money, final String cardNumber, String cardType, final String fundCode, final String fundType, final int payMethod, final String fundName, final String cardName){
        super(context);
        this.context = context;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.fundCode = fundCode;
        this.fundType = fundType;
        this.payMethod = payMethod;
        this.fundName = fundName;
        this.cardName = cardName;
        this.money = money;
        this.index=index;
        userInfo =UserInfo.getInstance(context);
        if (index != 3) {
            money_ = new DecimalFormat("#0").format(Double.parseDouble(money) * 100);
        }
        setMenuView();
        ininPopupWindow();
    }

    /**
     *  显示密码验证
     */
    private void setMenuView(){
        mMenuView = LayoutInflater.from(context).inflate(R.layout.pop_transaction_pswd, null);
        imageClose = (ImageButton) mMenuView.findViewById(R.id.image_close);
        sg_password_ed = (EditText) mMenuView.findViewById(R.id.ed_password);
        queren_password = (Button) mMenuView.findViewById(R.id.queren_password);
        imageClose.setOnClickListener(this);
        queren_password.setOnClickListener(this);
        dialog=new LoadingDialog(context,R.style.myDialogTheme,0);
        dialog.titleText.setText("交易正在处理中，请稍后...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_close:
                dismiss();
                break;
            case R.id.queren_password:
                password = sg_password_ed.getText().toString();
                if(G.isEmteny(password)){
                    G.showToast(context,"密码不能为空");
                    return;
                }
                if (index == 0) {
                    //买入
                    pinealBuy();
                } else if (index == 1) {
                    if (fundCode.equals("999999999")) {
                        //转出
                        pinealCash();
                    } else {
                        //赎回
                        pinealOut();
                    }
                } else if (index == 3) {
                    //撤单
                    pinealRevoke();
                }
                dialog.show();
                dismiss();
                //去掉键盘
//                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
//                        hideSoftInputFromWindow(sg_password_ed.getWindowToken(), 0);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }
    /**
     * popupWindow通用设置
     */
    public void ininPopupWindow() {
        //设置SignPopupWindow的View
        this.setContentView(mMenuView);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
//        this.setTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 松果申购/转入
     */
    private void pinealBuy() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", userInfo.getToken());
        params.put("secretKey", userInfo.getSecretKey());
        params.put("cardType", cardType);
        params.put("amount", money_);
        params.put("cardNumber", cardNumber);
        params.put("checkType", "1");
        params.put("password", MD5Utils.encode(password));
        params.put("identifyingCode", "");
        params.put("fundCode", fundCode);
        params.put("fundType", fundType);
        params.put("payMethod", String.valueOf(payMethod));
        OkHttpUtil.sendPost(ApiUri.SONGGUO_BUY, params, this);
    }

    /**
     * 松果赎回
     */
    public void pinealOut() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", userInfo.getToken());
        params.put("secretKey", userInfo.getSecretKey());
        params.put("redeem_vol", money);
        params.put("fundType", fundType);
        params.put("fundCode", fundCode);
        params.put("password", MD5Utils.encode(password));
        OkHttpUtil.sendPost(ApiUri.SONGGUO_OUT_SH, params, this);

    }

    /**
     * 松果转出
     */
    public void pinealCash() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", userInfo.getToken());
        params.put("secretKey", userInfo.getSecretKey());
        params.put("cardType", cardType);
        params.put("amount", money_);
        params.put("cardNumber", cardNumber);
        params.put("checkType", "1");
        params.put("password", MD5Utils.encode(password));
        params.put("identifyingCode", "");
        OkHttpUtil.sendPost(ApiUri.SONGGUO_OUT, params, this);

    }

    /**
     * 松果撤单
     */
    public void pinealRevoke() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("oriSgTransId", cardType);
        params.put("oriFundTransId", cardNumber);
        params.put("password", MD5Utils.encode(password));
        OkHttpUtil.sendPost(ApiUri.SG_REVOKE, params, this);
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        dialog.dismiss();
        Gson gson = new Gson();
        if (uri.equals(ApiUri.SG_REVOKE)){
            // 撤单
            MenberCent menberCent = gson.fromJson(result,MenberCent.class);
            if("0".equals(menberCent.getResultCode())){
                Intent intent = new Intent(context, SgBuySuccessActivity.class);
                intent.putExtra("index", -1);
                context.startActivity(intent);
                context.finish();
            }else
                G.showToast(context, menberCent.getResultDesc());
        } else if (uri.equals(ApiUri.SONGGUO_OUT)) {
            // 转出
            MenberCent menberCent = gson.fromJson(result,MenberCent.class);
            if ("0".equals(menberCent.getResultCode())) {
                Intent intent = new Intent(context, SgBuySuccessActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("money", money_);
                intent.putExtra("fundcode", fundCode);
                intent.putExtra("fundname", fundName);
                intent.putExtra("cardnumber", cardNumber);
                intent.putExtra("cardname", cardName);
                dismiss();
                context.startActivity(intent);
                context.finish();
            } else
                G.showToast(context,  menberCent.getResultDesc());

        }else if (uri.equals(ApiUri.SONGGUO_BUY)){
            // 转入/申购
            SGBuyOkVo sgBuyOkVo =  gson.fromJson(result,SGBuyOkVo.class);
            if ("0".equals(sgBuyOkVo.getResultCode())&&sgBuyOkVo.getDate().getPayResult() == 200) {
                Intent intent = new Intent(context, SgBuySuccessActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("money", money_);
                intent.putExtra("fundcode", fundCode);
                intent.putExtra("fundname", fundName);
                intent.putExtra("sg", sgBuyOkVo.getDate());
                context.startActivity(intent);
                context.finish();
            } else
                G.showToast(context,  sgBuyOkVo.getResultDesc());
        }else if (uri.equals(ApiUri.SONGGUO_OUT_SH)){
            // 赎回
            SGBuyOkVo sgBuyOkVo =  gson.fromJson(result,SGBuyOkVo.class);
            if ("0".equals(sgBuyOkVo.getResultCode())&&9==sgBuyOkVo.getDate().getStatus()) {
                Intent intent = new Intent(context, SgBuySuccessActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("money", money);
                intent.putExtra("fundcode", fundCode);
                intent.putExtra("fundname", fundName);
                intent.putExtra("sg",  sgBuyOkVo.getDate());
                context.startActivity(intent);
                context.finish();
            } else
                G.showToast(context, sgBuyOkVo.getResultDesc());
        }
    }

    @Override
    public void onError(String uri, String error) {
        dialog.dismiss();
        G.showToast(context,OkHttpUtil.errMsg);
    }
}
