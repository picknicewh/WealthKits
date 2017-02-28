package com.cfjn.javacf.adapter.assets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.TransactionRecordsVo;
import com.cfjn.javacf.widget.DateFormat;
import com.cfjn.javacf.util.G;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 作者： zll
 * 时间： 2016-6-6
 * 名称： 单笔基金交易详情记录适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class TransactionDetailAdapter extends BaseAdapter {
    private Context context;
    private List<TransactionRecordsVo> recordsVoList;
    private DecimalFormat df;
    public TransactionDetailAdapter(Context context, List<TransactionRecordsVo> recordsVoList){
        this.context = context;
        this.recordsVoList  = recordsVoList;
         df = new DecimalFormat("########0.00");
    }
    @Override
    public int getCount() {
        return recordsVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordsVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transation_record, parent, false);
            new ViewHolder(convertView);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        TransactionRecordsVo records =recordsVoList.get(position);

        viewHolder.tvFundName.setText(G.isEmteny(records.getFundName())?"- -":records.getFundName());
        viewHolder.tvRecordTime.setText(DateFormat.dateFormat(records.getApplyDateTime()));

        if(null==records.getStatus()){
            records.setStatus(0);
        }

        //不属于天风的赎回
        if(!"1".equals(records.getSource())&&records.getBusinessType().equals("024")){
            //确认中或者已撤销交易
            if( records.getStatus() == 9||records.getStatus() == 4){
                //份
                viewHolder.tvCompany.setText(" 份");
            }else{
                //元
                viewHolder.tvCompany.setText(" 元");
            }
            //强制调增
        }else if(records.getBusinessType().equals("144")){
            //份
            viewHolder.tvCompany.setText(" 份");
        }else {
            //元
            viewHolder.tvCompany.setText(" 元");
        }
        viewHolder.tvMoney.setText( null==records.getAmount()?df.format(records.getShares()):df.format(records.getAmount()));
        //业务类型：022=申购 024=赎回 143=红利发放 144=强制调增...
        //业务状态：1=确认成功 0=确认失败 4=已撤销交易 9=确认中

        String businessType=records.getBusinessType();

        if (records.getFundCode().equals("999999999")) { //基金属于现金宝
            if (businessType.equals("022")) { //申购
                viewHolder.tvRecordName.setText("转入");
            } else if (businessType.equals("024")) {  //赎回
                viewHolder.tvRecordName.setText("转出");
            }else{
                viewHolder.tvRecordName.setText("其他");
            }
        }else{
            //基金属于普通基金
            if ( G.isEmteny(records.getBusinessTypeToCN())) {
                if (("022").equals(businessType)) {
                    viewHolder.tvRecordName.setText("申购");
                } else if (("024").equals(businessType)) {
                    viewHolder.tvRecordName.setText("赎回");
                } else if (("143").equals(businessType)) {
                    viewHolder.tvRecordName.setText("红利发放");
                } else if (("144").equals(businessType)) {
                    viewHolder.tvRecordName.setText("强制调增");
                } else if (("093".equals(businessType))) {
                    viewHolder.tvRecordName.setText("取消定投");
                } else if (("988").equals(businessType)) {
                    viewHolder.tvRecordName.setText("修改定投");
                } else if (("090").equals(businessType)) {
                    viewHolder.tvRecordName.setText("添加定投");
                } else if (("098").equals(businessType)) {
                    viewHolder.tvRecordName.setText("快速赎回");
                } else if (("036").equals(businessType)) {
                    viewHolder.tvRecordName.setText("基金转换");
                } else if (("124").equals(businessType)) {
                    viewHolder.tvRecordName.setText("赎回确认");
                } else {
                    viewHolder.tvRecordName.setText("其他");
                }
            } else {
                viewHolder.tvRecordName.setText(records.getBusinessTypeToCN());
            }
        }

        viewHolder.tvRecordType.setText(records.getStatusToCN());
        int status=records.getStatus();
        if (status == 9) {
            viewHolder.tvRecordType.setBackgroundResource(R.drawable.bg_confirmed);
            viewHolder.tvRecordType.setTextColor(context.getResources().getColor(R.color.orange_x0_5));
        } else if (status == 0||status==4) {
            viewHolder.tvRecordType.setBackgroundResource(R.drawable.bg_defeated);
            viewHolder.tvRecordType.setTextColor(context.getResources().getColor(R.color.red));
        } else if (status == 1) {
            viewHolder.tvRecordType.setBackgroundResource(R.drawable.bg_succeed);
            viewHolder.tvRecordType.setTextColor(context.getResources().getColor(R.color.green));
        }

        if (businessType.equals("022")) {
            viewHolder.ivRecordLogo.setImageResource(R.drawable.ic_purchase_p);
        } else if (businessType.equals("024")) {
            viewHolder.ivRecordLogo.setImageResource(R.drawable.ic_redeem);
        }  else {
            viewHolder.ivRecordLogo.setImageResource(R.drawable.ic_bonusissue);
        }
        return convertView;
    }



     class ViewHolder {
        /**
         *  交易状态图标
         */
        @Bind(R.id.iv_record_logo)
        ImageView ivRecordLogo;
        /**
         *  交易状态
         */
        @Bind(R.id.tv_record_name)
        TextView tvRecordName;
        /**
         * 确认状态
         */
        @Bind(R.id.tv_record_type)
        TextView tvRecordType;
        /**
         * 基金名称
         */
        @Bind(R.id.tv_fund_name)
        TextView tvFundName;
        /**
         * 交易金额
         */
        @Bind(R.id.tv_money)
        TextView tvMoney;
        /**
         * 交易单位
         */
        @Bind(R.id.tv_company)
        TextView tvCompany;
        /**
         * 交易时间
         */
        @Bind(R.id.tv_record_time)
        TextView tvRecordTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}