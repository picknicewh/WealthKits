package com.cfjn.javacf.appService;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 数米提交数据广播注册和关闭
 * 版本说明：代码规范整改
 * 附加注释： 查看广播状态 广播是否是开启状态 广播非开启状态不能关闭 否则空指针
 * 主要接口：
 */
public class ReceiverManger {
    private Context context;
    private NetworkReceiver networkBroadcast;
    private boolean isOpenReceiver=false;
    public ReceiverManger(Context context) {
        this.context = context;
        networkBroadcast=new NetworkReceiver();
    }

    public void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkBroadcast, filter);
        isOpenReceiver=true;
    }

    public void unRegisterNetworkReceiver() {
        isOpenReceiver=false;
        context.unregisterReceiver(networkBroadcast);
    }

    public boolean getReceiverType(){
        return  isOpenReceiver;
    }

}
