package com.cfjn.javacf.shumi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.StyleCodeVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;
import com.shumi.sdk.data.bean.ShumiSdkCodeMessageBean;
import com.shumi.sdk.data.service.openapi.IShumiSdkOpenApiDataServiceHandler;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeFundSharesBean;
import com.shumi.sdk.ext.data.service.ShumiSdkGetFundSharesDataService;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：  获取数米资产 先访问客户端时候需要提交
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ShumiAsstes implements IShumiSdkOpenApiDataServiceHandler, OKHttpListener {
    private ShumiSdkGetFundSharesDataService mDataService;
    private Context context;
    private  StyleCodeVo styleCodeVo;
    public ShumiAsstes(final Context context) {
        this.context=context;
        //查询用户是否需要提交资产
        Map<String,String>map=new HashMap<>();
        map.put("secretKey", UserInfo.getInstance(context).getSecretKey());
        OkHttpUtil.sendPost(ApiUri.TRAdEDCCOUNT,map,this);
    }

    @Override
    public void onGetData(Object dataObject, Object sender) {
        if (sender == mDataService) {
            try {
                ShumiSdkTradeFundSharesBean bean = mDataService.getData(dataObject);
                Gson gson=new Gson();
                final String values=gson.toJson(bean);
                new ShuMiSubmitDate(context,values,"0",false);
            } catch (Exception e) {
                G.log(e);
            }
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
            ShumiSdkCodeMessageBean bean = ShumiSdkGetFundSharesDataService.getCodeMessage(message);
            if (!TextUtils.isEmpty(bean.getMessage())) {
                G.showToast(context.getApplicationContext(), bean.getMessage());
            }
        }
    }

    @Override
    public void onSuccess(String uri, String result) {
        if (ApiUri.TRAdEDCCOUNT.equals(uri)){
            Gson gson = new Gson();
            StyleCodeVo styleCodeVo = gson.fromJson(result,StyleCodeVo.class);
            if(styleCodeVo.getResultCode().equals("0")){
               G.log(" 需要提交资产");
                handler.sendEmptyMessage(0x11);
            }
            G.log(" 不需要提交资产");
        }
    }

    @Override
    public void onError(String uri, String error) {
        if (ApiUri.TRAdEDCCOUNT.equals(uri)){
           G.log("查询资产失败");
        }
    }
   private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x11){
                mDataService = new ShumiSdkGetFundSharesDataService(context, MyShumiSdkDataBridge.getInstance(context));
                mDataService.setDataServiceCallback(ShumiAsstes.this);
                mDataService.cancel();
                mDataService.get(null);
            }
        }
    };
}
