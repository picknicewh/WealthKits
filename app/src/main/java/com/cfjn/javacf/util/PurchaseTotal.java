package com.cfjn.javacf.util;

import android.content.Context;

import com.cfjn.javacf.shumi.MyShumiSdkTradingHelper;
import com.shumi.sdk.data.param.trade.general.ShumiSdkCancelOrderParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkPurchaseFundParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkRedeemParam;
import com.shumi.sdk.data.param.trade.general.ShumiSdkUnbindBankCardParam;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 数米工具类
 * 版本说明：代码规范整改
 * 附加注释：封装数米申购赎回等
 * 主要接口：
 */

public class PurchaseTotal {
    private  Context context;
    public PurchaseTotal(Context context) {
        this.context = context;
    }

    //数米申购  需要传： 基金代码
//                       基金名称
//                       基金风险等级
//                       收费方式
//                       基金类型
    public  void shuMiPurchase(String fundCode,String fundName, String fundRiskLevel,String shareType,String fundType ){
        ShumiSdkPurchaseFundParam param = new ShumiSdkPurchaseFundParam();
        String buyAction = "P";
        param.setParam(buyAction, fundCode, fundName, fundRiskLevel, shareType, fundType);
        MyShumiSdkTradingHelper.doPurchase(context, param);

    }

    //数米赎回 tradeAccunt 交易账号
    public  void NormalRedeem(String FundCode,String FundName,String TradeAccount){
        ShumiSdkRedeemParam param1 = new ShumiSdkRedeemParam();
        param1.setParam(FundCode, FundName, TradeAccount);
        MyShumiSdkTradingHelper.doNormalRedeem(context, param1);
    }

    //数米撤单  需要传 ： 数米申请流水号
    public void shuMiCancelOrder(String ApplySerial){

        ShumiSdkCancelOrderParam param = new ShumiSdkCancelOrderParam();
        param.setParam(ApplySerial);
        MyShumiSdkTradingHelper.doCancelOrder(context, param);
    }

    //数米银行卡解绑
    public void UnbindBankCard(String TradeAccount,String BankName,String No,String Status ){
        if ("1".equals(Status)) {
            // 正常状态可以解绑
            ShumiSdkUnbindBankCardParam param = new ShumiSdkUnbindBankCardParam();
            param.setParam(TradeAccount, BankName, No);
            MyShumiSdkTradingHelper.doUnbindBankCard(context, param);
        } else {
            // 其他状态不可解绑
            G.showToast(context,"抱歉，该银行卡可能已经在解绑中，暂时无法解绑");
        }
    }

    //数米开户页面
    public  void ShuMiStart(){
        MyShumiSdkTradingHelper.doAuthentication(context);
    }


}
