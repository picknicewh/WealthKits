package com.cfjn.javacf.appService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cfjn.javacf.shumi.ShuMiBankDate;
import com.cfjn.javacf.shumi.ShuMiSubmitDate;
import com.cfjn.javacf.shumi.ShuMiTradingDate;
import com.cfjn.javacf.shumi.ShumiAsstesDate;
import com.cfjn.javacf.util.G;


/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 数米提交数据失败时监听网络状态的广播
 * 版本说明：代码规范整改
 * 附加注释： 提交数据失败时启动
 *           网络稳定时去继续提交数据
 *           失败广播关闭直到提交成功
 * 主要接口：
 */
public class NetworkReceiver extends BroadcastReceiver {
    private ShuMiTradingDate shuMiTradingDate;
    private ShuMiBankDate shuMiBankDate;
    private ShumiAsstesDate asstesDate;
    private String SMTeadingdate;
    private String SMBankDate;
    private String SMAsstes;

    public void onReceive(Context context, Intent intent) {
        shuMiTradingDate=new ShuMiTradingDate(context);
        shuMiBankDate=new ShuMiBankDate(context);
        asstesDate=new ShumiAsstesDate(context);

        SMTeadingdate=shuMiTradingDate.getSmTradingDate();
        SMBankDate=shuMiBankDate.getShuMiBankDate();
        SMAsstes=asstesDate.getShumiAsstesDate();

        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        //手机连接网络或者无线网络
        if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED == wifiState || NetworkInfo.State.CONNECTED == mobileState) {

            if(!G.isEmteny(SMTeadingdate)){
                G.log("网络正常-----------------传值交易记录");
                new ShuMiSubmitDate(context,SMTeadingdate,"1",shuMiTradingDate.getSmTradinType());
            }

            if(!G.isEmteny(SMBankDate)){
                G.log("网络正常-----------------传值银行卡信息");

                new ShuMiSubmitDate(context,SMBankDate,"2",false);
            }

            if(!G.isEmteny(SMAsstes)){
                G.log("网络正常-----------------传值用户资产");
                new ShuMiSubmitDate(context,SMAsstes,"0",false);
            }
        }
    }

}
