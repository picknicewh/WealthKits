package com.cfjn.javacf.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * 名称：  交易记录适配器
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ExpandAdapter extends BaseExpandableListAdapter {
    private List<String> tlist;    //----头标题
    private List<List<TransactionRecordsVo>> clist;    //----内容
    private Context context;
   private DecimalFormat df;
    public ExpandAdapter(List<String> tlist, List<List<TransactionRecordsVo>> clist, Context context) {
        this.tlist = tlist;
        this.clist = clist;
        this.context = context;
        df = new DecimalFormat("########0.00");
    }

    @Override
    public int getGroupCount() {
        return tlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return clist.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return tlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return clist.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
     
        return getView(getGroup(groupPosition).toString());
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transation_record,null);
            new ViewHolder(convertView);
        }

        viewHolder= (ViewHolder) convertView.getTag();
        TransactionRecordsVo records =clist.get(groupPosition).get(childPosition);

        viewHolder.tvFundName.setText(G.isEmteny(records.getFundName())?"- -":records.getFundName());
        viewHolder.tvRecordTime.setText(DateFormat.dateFormat(records.getApplyDateTime()));
        String businessType=records.getBusinessType();

        //不属于天风的赎回
        if(!"1".equals(records.getSource())&&businessType.equals("024")){
            //确认中或者已撤销交易
            if( records.getStatus() == 9||records.getStatus() == 4){
                //份
                viewHolder.tvCompany.setText(" 份");
            }else{
                //元
                viewHolder.tvCompany.setText(" 元");
            }
            //强制调增
        }else if(businessType.equals("144")){
            //份
            viewHolder.tvCompany.setText(" 份");
        }else {
            //元
            viewHolder.tvCompany.setText(" 元");
        }
        viewHolder.tvMoney.setText( null==records.getAmount()?df.format(records.getShares()):df.format(records.getAmount()));


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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View getView(String textTitle) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        textView.setBackgroundResource(R.color.gray_x0_8);
        textView.setPadding(G.dp2px(context,20), G.dp2px(context,10), 0, G.dp2px(context,10));
        textView.setTextSize(16);
        textView.setWidth(1);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(Color.parseColor("#818181"));
        textView.setText(textTitle);

        View view=new View(context);
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,G.dp2px(context,0.5)));
        view.setBackgroundColor(Color.parseColor("#ececec"));
        
        LinearLayout linearLayout=new LinearLayout(context);
        linearLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);
        linearLayout.addView(view);
        return linearLayout;
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
