package com.cfjn.javacf.adapter.fund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.FundBaseInfo;
import com.cfjn.javacf.modle.FundListVo;
import com.cfjn.javacf.util.G;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：  基金超市适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class MyFundApapter extends BaseAdapter {
    private Context context;
    private List<FundListVo> fundListVos;
    private int type;
    private DecimalFormat format1 = new DecimalFormat("#0.0000");
    private DecimalFormat format2 = new DecimalFormat("#0.00");

    public MyFundApapter(Context context, List<FundListVo> fundListVos, int type) {
        this.context = context;
        this.fundListVos = fundListVos;
        this.type = type;
    }

    @Override
    public int getCount() {
        return fundListVos.size();
    }

    @Override
    public Object getItem(int position) {
        return fundListVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.litem_fund_fragment, null);
            new ViewHolder(convertView);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        FundListVo fundListVo = fundListVos.get(position);
        FundBaseInfo baseInfo=fundListVo.getFundBaseInfo();
        //基金名称
        viewHolder.tvFundName.setText(baseInfo.getFundName());
        //申购人数
        viewHolder.tvNumber.setText(fundListVo.getPurchasers() + "人");
        //起购金额
        if (G.isEmteny(fundListVo.getFundBaseInfo().getPurchaseLimitMin() + "")) {
            fundListVo.getFundBaseInfo().setPurchaseLimitMin(0.00);
        }
        //	String	type=1（货币基金），type=3（股票基金），type=4（混合型基金），type=2（债券型基金）
        //  4.1 改版后只传货币基金 所以不需要判断
        if (type == 1) {
            viewHolder.tvProfit.setText(format1.format(baseInfo.getThousandsOfIncome()));
            viewHolder.tvGrowthRate.setText(format2.format(baseInfo.getYieldSevenDay() * 100) + "%");

            if (fundListVo.getFundBaseInfo().getYield12M() != null) {
                viewHolder.tvReturnRate.setText(format2.format(baseInfo.getYield12M() * 100) + "%");
            } else {
                viewHolder.tvReturnRate.setText("0.00" + "%");
            }

            if (Double.parseDouble(format2.format(baseInfo.getYieldSevenDay() * 100)) < 0) {  //七日年化小于0 颜色为绿色
                viewHolder.tvGrowthRate.setTextColor(context.getResources().getColor(R.color.gree_xx));
            } else if (Double.parseDouble(format2.format(baseInfo.getYieldSevenDay() * 100)) == 0) { //七日年化等于0 颜色为灰色
                viewHolder.tvGrowthRate.setTextColor(context.getResources().getColor(R.color.gray_x0_5));
            } else { //七日年化大于0 颜色为红色
                viewHolder.tvGrowthRate.setTextColor(context.getResources().getColor(R.color.red));
                viewHolder.tvGrowthRate.setText("+" + format2.format(baseInfo.getYieldSevenDay() * 100) + "%");
            }
        }
        return convertView;
    }


    class ViewHolder {
        /**
         * 基金名称
         */
        @Bind(R.id.tv_fund_name)
        TextView tvFundName;
        /**
         * 万份收益
         */
        @Bind(R.id.tv_profit)
        TextView tvProfit;
        /**
         *  七日年化
         */
        @Bind(R.id.tv_growth_rate)
        TextView tvGrowthRate;
        /**
         * 近一年收益率
         */
        @Bind(R.id.tv_return_rate)
        TextView tvReturnRate;
        /**
         * 购买人数
         */
        @Bind(R.id.tv_number)
        TextView tvNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
