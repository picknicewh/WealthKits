package com.cfjn.javacf.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.activity.fund.SgBuyActivity;
import com.cfjn.javacf.adapter.fund.SgBuyAdapter;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.FundBaseInfo;
import com.cfjn.javacf.modle.MenberCent;
import com.cfjn.javacf.modle.UserBankcardListVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.util.PurchaseTotal;
import com.google.gson.Gson;
import com.openhunme.cordova.activity.HMDroidGap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 弹框提示类
 * 版本说明：代码规范整改
 * 附加注释： 根据传过来的值判断显示那个窗口  0 基金详情测算收益，1 为基金详情购买基金选择柜台弹窗，3 松果买入买出输入密码窗口
 *  加载不同的页面
 *  松果的买入  卖出  撤单
 * 主要接口：
 */
public class CalculatePopup extends PopupWindow implements OKHttpListener {

    //PopupWindow上面装载的View
    private View mMenuView;
    // 关闭图标
    private ImageButton popup_image_;

    private ListView list;

    private UserInfo userInfo;
    private List<String> numberList;
    private String source;
    /**
     * 购买的footer
     */
    private View footerView;

    private Button buy_footer_ok;
    private FundBaseInfo fundBaseInfo;
    /**
     * 松果选择银行卡-取消
     */
    private ImageButton sg_bank_image_close;

    /**
     * 松果选择银行卡-listview
     */
    private ListView sg_bank_listview;
    private View sg_headerView;
    private TextView sg_bank_now;
    private  Activity context;
    private LoadingDialog loadingDialog;

    /**
     * 多柜台选择申购弹窗
     * @param context Activity 对象
     * @param numberSource 多个柜台编号值格式如“0，1，2” 0=数米 1=天风 2=松果
     * @param fundBaseInfo 基金对象 获取该基金的信息
     */
    public CalculatePopup(final Activity context, final String numberSource, final FundBaseInfo fundBaseInfo) {
        super(context);
        userInfo = UserInfo.getInstance(context);
        numberList = new ArrayList<>();
        this.context=context;
        this.fundBaseInfo = fundBaseInfo;
        loadingDialog =LoadingDialog.regstLoading(context,"数据处理中，请稍后...");
        // 解析柜台编号字符串 将编号挨个存储
        for (String sss : numberSource.replaceAll("[^0-9]", ",").split(",")) {
            if (sss.length() > 0)
                numberList.add(sss);
        }
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.popup_buy, null);//选择柜台布局
        footerView = inflater.inflate(R.layout.buy_footer, null);//认购布局
        list = (ListView) mMenuView.findViewById(R.id.radio_list);

        popup_image_ = (ImageButton) mMenuView.findViewById(R.id.popup_image_);
        buy_footer_ok = (Button) footerView.findViewById(R.id.b_footer_ok);

        list.addFooterView(footerView);
        list.setAdapter(new MyAdapter(context, numberList));
        ininPopupWindow();
        buy_footer_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp == null) {
                    G.showToast(context, "请先选择柜台");
                    return;
                }
                loadingDialog.show();
                queryAccount();//查询用户是否开过户

            }

        });
        popup_image_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }




    /**
     * 定一个接口
     */
    public interface ICoallBack {
        void onClickButton(int index);
    }

    /**
     * 初始化接口变量
     */
    ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack 接口类型
     */
    public void setonClick(ICoallBack iBack) {
        icallBack = iBack;
    }

    /**
     *  松果支付 选择支付方式
     * @param context Activity 对象
     * @param bankcardListVos 银行卡信息
     * @param balance 现金宝余额
     */
    public CalculatePopup(final Activity context, final List<UserBankcardListVo> bankcardListVos, double balance) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_sg_bankcard, null);
        sg_headerView = inflater.inflate(R.layout.sg_bankcard_header, null);
        sg_bank_now = (TextView) sg_headerView.findViewById(R.id.sg_bank_now);
        sg_bank_now.setText("余额￥" + balance);
        sg_bank_image_close = (ImageButton) mMenuView.findViewById(R.id.sg_bank_image_close);
        sg_bank_listview = (ListView) mMenuView.findViewById(R.id.sg_bank_listview);
        sg_bank_listview.addHeaderView(sg_headerView);
        ininPopupWindow();
        SgBuyAdapter sgAdapter = new SgBuyAdapter(context,bankcardListVos);
        sg_bank_listview.setAdapter(sgAdapter);
        sg_bank_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                focusable();
                if (position == 0) {
                    icallBack.onClickButton(0);
                } else {
                    icallBack.onClickButton(1);
                }
                dismiss();
            }
        });
        sg_bank_image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void focusable() {
        super.setFocusable(true);
    }

    /**
     * 查询是否开户
     */
    public void queryAccount() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secretKey", userInfo.getSecretKey());
        params.put("token", userInfo.getToken());
        params.put("source", source);
        OkHttpUtil.sendPost(ApiUri.FUND_REGISTER, params, this);
    }


    @Override
    public void onSuccess(String uri, String result) {
        loadingDialog.dismiss();
        if(uri.equals(ApiUri.FUND_REGISTER)){
            Gson gson = new Gson();
            MenberCent json=gson.fromJson(result,MenberCent.class);
            purchaseFund(json.getResultCode());
        }
    }

    @Override
    public void onError(String uri, String error) {
        loadingDialog.dismiss();
        if(uri.equals(ApiUri.FUND_REGISTER)){
            G.showToast(context,"个人信息获取失败，请检查网络重试！");
        }
    }

    /**
     * popupWindow通用设置
     */
    public void ininPopupWindow() {
        //设置SignPopupWindow的View
        this.setContentView(mMenuView);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
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
     * listview和RadioButton绑定
     */
    RadioButton temp;
    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<String>list;
        public MyAdapter(Context context,List<String>list) {
            super();
            this.context = context;
            this.list=list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final RadioButton radioButton;
            if (convertView == null) {
                radioButton = new RadioButton(context);
            } else {
                radioButton = (RadioButton) convertView;
            }

            switch (list.get(position)) {
                case "0":
                    radioButton.setText("数米");
                    radioButton.setText_(context.getResources().getString(R.string.sm_text));
                    break;
                case "1":
                    radioButton.setText("天风");
                    radioButton.setText_(context.getResources().getString(R.string.tf_text));
                    break;
                case "2":
                    radioButton.setText("松果");
                    radioButton.setText_(context.getResources().getString(R.string.sg_text));
                    break;
            }
            radioButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 模版不为空，则chage.
                    if (temp != null) {
                        temp.ChageImage();
                    }
                    temp = radioButton;
                    radioButton.ChageImage();
                    source = list.get(position);
                }
            });
            return radioButton;
        }
    }

    private void purchaseFund(String isAccount){
        if(isAccount.equals("-1")){
            G.showToast(context,"此基金暂不支持购买");
            return;
        }
        Intent intent;
        String loadUrlStr = null; //跳入地址
        String params;  //地址拼接参数
        switch (source) {
            case "0":
                if (userInfo.getSmToken().equals("")) {
                    G.showToast(context, "请先在数米开户");
                    new PurchaseTotal(context).ShuMiStart();
                } else {
                    new PurchaseTotal(context).shuMiPurchase(fundBaseInfo.getFundCode(), fundBaseInfo.getFundName(), fundBaseInfo.getRiskLevel(), fundBaseInfo.getShareType(), String.valueOf(fundBaseInfo.getFundType()));
                }
                break;
            case "1":
                if (isAccount.equals("1")) {
//                                    loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/trade/buy";
                    loadUrlStr = MainActivity.TFurl + "/www/tap/view/index_android.html#/trade/buy";
                    params = "prodCode=" + fundBaseInfo.getFundCode() + "&prodSource=" + fundBaseInfo.getProdSource() + "&name=" + userInfo.getLoginName() + "&token=" +userInfo.getToken() + "&key=" + userInfo.getSecretKey();
                    loadUrlStr = loadUrlStr + "?" + params;
                } else if (isAccount.equals("0")) {
                    G.showToast(context, "请先在天风开户");
//                                    loadUrlStr = "file:///android_asset/www/tap/view/index_android.html#/open-prepare";
                    loadUrlStr = MainActivity.TFurl + "/www/tap/view/index_android.html#/open-prepare";
                    params = "/" + userInfo.getLoginName() + "/" + userInfo.getToken() + "/" + userInfo.getSecretKey();
                    loadUrlStr = loadUrlStr + params;
                }
//                             loadUrlStr= "file:///android_asset/www/tap/view/index_android.html#/trade/buy";
//                             params="prodCode="+fundBaseInfo.getFundCode()+"&prodSource="+source;
//                            loadUrlStr = loadUrlStr + "?" + params;
                intent = new Intent(context, HMDroidGap.class);
                intent.putExtra("loadUrl", loadUrlStr);
                context.startActivity(intent);
                break;
            case "2":
                if (isAccount.equals("1")) {
                    intent = new Intent(context, SgBuyActivity.class);
                    intent.putExtra("poisition", 0);
                    intent.putExtra("fundCord", fundBaseInfo.getFundCode());
                    intent.putExtra("fundName", fundBaseInfo.getFundNameAbbr());

                    if (fundBaseInfo.getFundType() == 7) {
                        intent.putExtra("fundType", fundBaseInfo.getFundType());
                    } else {
                        intent.putExtra("fundType", fundBaseInfo.getInvestmentType());
                    }

                    if (null==fundBaseInfo.getSgPurchaseLimitMin()||fundBaseInfo.getSgPurchaseLimitMin()<1) {
                        intent.putExtra("purchaseLimitMin", 1.0);
                    } else {
                        intent.putExtra("purchaseLimitMin", fundBaseInfo.getSgPurchaseLimitMin());
                    }
                    context.startActivity(intent);
                } else if (isAccount.equals("0")) {
                    G.showToast(context, "请先在松果开户");
                    intent = new Intent(context, HMDroidGap.class);
                    params = "?token=" + userInfo.getToken() + "&secretKey=" + userInfo.getSecretKey();
                    loadUrlStr = "file:///android_asset/www/sg/html/id_validate.html" + params;
                    intent.putExtra("loadUrl", loadUrlStr);
                    context. startActivity(intent);
                }
                break;
        }
        dismiss();
    }
}