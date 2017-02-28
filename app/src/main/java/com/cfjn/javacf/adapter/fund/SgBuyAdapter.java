package com.cfjn.javacf.adapter.fund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.UserBankcardListVo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 松果选择银行卡支付适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class SgBuyAdapter extends BaseAdapter {
    private Context context;
    private List<UserBankcardListVo> bankCardList;

    public SgBuyAdapter(Context context, List<UserBankcardListVo> bankcardList) {
        this.context = context;
        this.bankCardList = bankcardList;
    }

    @Override
    public int getCount() {
        return bankCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return bankCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sg_bankcard, null);
            new ViewHolder(convertView);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        UserBankcardListVo bankCard = bankCardList.get(pos);
        String bankNumber=bankCard.getCardNumber();
        //设置银行卡名字
        viewHolder.tvBankName.setText(bankCard.getCardName());
        //设置银行卡号
        viewHolder.tvBankNumber.setText("尾号:" + "(" + bankNumber.substring(bankNumber.length() - 4, bankNumber.length()) + ")");
        //设置银行卡状态
        if (bankCard.getInUse().equals("1")) {
            viewHolder.tvBankType.setText("正在使用中");
        }else {
            viewHolder.tvBankType.setText("暂时无法使用");
        }
        /*} else {
            viewHolder.sg_bank_ok.setVisibility(View.GONE);
            viewHolder.sg_bank_now.setText("历史使用，需预留手机号短信验证");
            viewHolder.sg_bank_name.setTextColor(context.getResources().getColor(R.color.gray_x0_5));
            viewHolder.sg_bank_number.setTextColor(context.getResources().getColor(R.color.gray_x0_5));*/
        //设置银行卡logo
        viewHolder.setBankLogo(bankCard.getCardName());
        return convertView;
    }

    class ViewHolder {
        /**
         * 银行标志
         */
        @Bind(R.id.iv_bank_logo)
        ImageView ivBankLogo;
        /**
         * 银行名字
         */
        @Bind(R.id.tv_bank_name)
        TextView tvBankName;
        /**
         * 银行卡号
         */
        @Bind(R.id.tv_bank_number)
        TextView tvBankNumber;
        /**
         * 银行卡使用状态
         */
        @Bind(R.id.tv_bank_type)
        TextView tvBankType;

        ViewHolder(View view) {
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
}