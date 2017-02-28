package com.cfjn.javacf.shumi;

import android.content.Context;
import android.text.TextUtils;

import com.cfjn.javacf.util.G;
import com.google.gson.Gson;
import com.shumi.sdk.data.bean.ShumiSdkCodeMessageBean;
import com.shumi.sdk.data.service.openapi.IShumiSdkOpenApiDataServiceHandler;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeBindedBankCardBean;
import com.shumi.sdk.ext.data.service.ShumiSdkGetBindBankCardsDataService;

/**
 * 作者： zll
 * 时间： 2016-6-29
 * 名称： 获取数米银行卡信息
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ShumiBankCard implements IShumiSdkOpenApiDataServiceHandler {
    private ShumiSdkGetBindBankCardsDataService mDataService;
    private Context context;


    public ShumiBankCard(Context context) {
        this.context = context;
        getBankCardManger();
    }

    private void getBankCardManger(){
        mDataService = new ShumiSdkGetBindBankCardsDataService(context, MyShumiSdkDataBridge.getInstance(context));
        mDataService.setDataServiceCallback(this);
        mDataService.cancel();
        mDataService.get(null);
    }

    @Override
    public void onGetData(Object dataObject, Object sender) {
        try {
            ShumiSdkTradeBindedBankCardBean bean = (ShumiSdkTradeBindedBankCardBean) dataObject;
            Gson gson=new Gson();
            final String values=gson.toJson(bean);
            //向客户端提交数米银行卡信息
            G.log("数米银行卡信息：--"+values);
            new ShuMiSubmitDate(context,values,"2",false);
        } catch (Exception e) {
            G.log(e.toString());
        }
    }

    @Override
    public void onGetError(int code, String message, Throwable err, Object sender) {
        if (mDataService == sender) {
			/*
			 * 判断是否是服务器维护,服务端维护时返回的错误代码code:500或502 并且ShumiSdkCodeMessageBean中，
			 * 维护时返回ShumiSdkCodeMessageBean.Code:99999，
			 * 维护信息返回ShumiSdkCodeMessageBean.Message
			 */
            ShumiSdkCodeMessageBean bean = ShumiSdkGetBindBankCardsDataService.getCodeMessage(message);
            if (!TextUtils.isEmpty(bean.getMessage())) {
                G.showToast(context.getApplicationContext(), bean.getMessage());
            } else if (code == 500 || code == 502) {
                G.showToast(context.getApplicationContext(), "抱歉，交易服务器正在维护中，请稍候再试，给您带来不便敬请谅解。");
            }

        }
    }
}
