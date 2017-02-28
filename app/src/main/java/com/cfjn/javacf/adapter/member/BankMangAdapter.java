package com.cfjn.javacf.adapter.member;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.PurchaseTotal;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-6
 * 名称： 银行卡管理适配器
 * 版本说明：代码规范整改
 * 附加注释：数米银行卡点击会有出现解绑银行卡（目前只支持数米）changeImageVisable
 * 主要接口：
 */
public class BankMangAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    /**
     * 记录点击是那个银行卡
     */
    private int mLastPosition;
    /**
     * 解绑银行卡的状态
     */
    private View mLastView = null;
    private PurchaseTotal purchaseTotal;

    public BankMangAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
        purchaseTotal = new PurchaseTotal(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_bankmanger, null);
            new Holder(convertView);
        }

        holder = (Holder) convertView.getTag();
        Map<String, String> map = list.get(position);
        final String cardName = map.get("cardName");
        final String cardNum = map.get("cardNumber");
        final String status = map.get("status");
        final String tradeAccount = map.get("tradeAccount");
        //限额
        String quota = map.get("quota");
        holder.tvBankName.setText(cardName);
        holder.tvBankNumber.setText("尾号：" + cardNum.substring(cardNum.length() - 4, cardNum.length()));
        if (G.isEmteny(quota)) {
            quota = "未知";
        }
        holder.tvBankQuota.setText("限额：" + quota);
        holder.setBankLogo(cardName);
        holder.llUnbund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (list.size() == 1) {
                        G.showToast(context, "您只有一张银行卡，暂时无法解绑！");
                    } else if (!G.isEmteny(tradeAccount)) {
                        purchaseTotal.UnbindBankCard(tradeAccount, cardName, cardNum, status);
                    }
                } catch (Exception e) {
                    G.log(e.toString());
                }
            }
        });
        return convertView;
    }

    class Holder {
        /**
         * 银行卡图标
         */
        @Bind(R.id.iv_bank_logo)
        ImageView ivBankLogo;
        /**
         * 银行卡名称
         */
        @Bind(R.id.tv_bank_name)
        TextView tvBankName;
        /**
         * 银行卡号
         */
        @Bind(R.id.tv_bank_number)
        TextView tvBankNumber;
        /**
         * 限额
         */
        @Bind(R.id.tv_bank_quota)
        TextView tvBankQuota;
        /**
         * 解绑银行卡
         */
        @Bind(R.id.ll_unbund)
        LinearLayout llUnbund;
        /**
         * 线
         */
        @Bind(R.id.v_line)
        View vLine;

        public Holder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

        public void setBankLogo(String bankName){
            if (null != bankName) {
                if (bankName.equals("工商银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_gongshang);
                } else if (bankName.equals("农业银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_nongye);
                } else if (bankName.equals("建设银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_jianshe);
                } else if (bankName.equals("交通银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_jiaotong);
                } else if (bankName.equals("招商银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_zhaoshang);
                } else if (bankName.equals("光大银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_guangda);
                } else if (bankName.equals("兴业银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_xingye);
                } else if (bankName.equals("民生银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_minsheng);
                } else if (bankName.equals("平安银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_pingan);
                } else if (bankName.equals("北京银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_beijing);
                } else if (bankName.equals("广发银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_guangfa);
                } else if (bankName.equals("浦发银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_pufa);
                } else if (bankName.equals("上海银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_shanghai);
                } else if (bankName.equals("邮储银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_youchu);
                } else if (bankName.equals("中国银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_zhongguo);
                } else if (bankName.equals("中信银行")) {
                    ivBankLogo.setImageResource(R.drawable.ic_bank_zhongxing);
                }
            }

        }
    }

    public void changeImageVisable(View view, int position) {
        if (mLastView != null && mLastPosition != position) {
            Holder holder = (Holder) mLastView.getTag();
            switch (holder.llUnbund.getVisibility()) {
                case View.VISIBLE:
                    holder.llUnbund.setVisibility(View.GONE);
                    holder.vLine.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
        this.mLastPosition = position;
        this.mLastView = view;
        Holder holder = (Holder) view.getTag();
        switch (holder.llUnbund.getVisibility()) {
            case View.VISIBLE:
                holder.llUnbund.setVisibility(View.GONE);
                holder.vLine.setVisibility(View.GONE);
                break;
            case View.GONE:
                holder.llUnbund.setVisibility(View.VISIBLE);
                holder.vLine.setVisibility(View.VISIBLE);
            default:
                break;
        }

    }

}
