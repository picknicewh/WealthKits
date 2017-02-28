package com.cfjn.javacf.shumi;

import android.content.Context;
import android.text.TextUtils;

import com.cfjn.javacf.util.G;
import com.google.gson.Gson;
import com.shumi.sdk.data.bean.ShumiSdkCodeMessageBean;
import com.shumi.sdk.data.service.openapi.IShumiSdkOpenApiDataServiceHandler;
import com.shumi.sdk.ext.data.bean.ShumiSdkTradeApplyRecordsBean;
import com.shumi.sdk.ext.data.service.ShumiSdkApplyRecordsDataService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 获取数米交易记录
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ShumiTrading implements IShumiSdkOpenApiDataServiceHandler {
    // 是否有更多数据
    private AtomicBoolean mHasMoreData = new AtomicBoolean(false);
    // 交易记录请求
    private ShumiSdkApplyRecordsDataService mDataService;
    // 交易记录请求参数
    private ShumiSdkApplyRecordsDataService.Param mParam;
    private Context context;
    private boolean isComint;
    public ShumiTrading(Context context,String startTime,boolean isComint) {
        this.context = context;
        this.isComint=isComint;
        new ShuMiTradingDate(context);
        initDataService(startTime);
    }

    private void initDataService(String startTime){
        // 获取用户登录之前的交易记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String endTime = sdf.format(new Date());

        mDataService = new ShumiSdkApplyRecordsDataService(context, MyShumiSdkDataBridge.getInstance(context));
        mDataService.setDataServiceCallback(this);
        mDataService.cancel();

        mHasMoreData.set(false);
        mParam = new ShumiSdkApplyRecordsDataService.Param();
        mParam.PageIndex = 1;
        mParam.PageSize = 3000;
        mParam.StartTime = startTime;
        mParam.EndTime = endTime;
        mDataService.get(mParam);
    }

    @Override
    public void onGetData(Object dataObject, Object sender) {
        if (mDataService == sender) {
            try {
                ShumiSdkTradeApplyRecordsBean bean = (ShumiSdkTradeApplyRecordsBean) dataObject;
                Gson gson=new Gson();
                final String valuse=gson.toJson(bean);
                //向服务端提交数米历史交易记录
                G.log("数米交易记录：" + valuse);
                new ShuMiSubmitDate(context,valuse,"1",isComint);
            }catch (Exception e){
                G.log(e.toString());
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
            ShumiSdkCodeMessageBean bean = ShumiSdkApplyRecordsDataService.getCodeMessage(message);
            if (!TextUtils.isEmpty(bean.getMessage())) {
                G.showToast(context.getApplicationContext(), bean.getMessage());
            } else if (code == 500 || code == 502) {
                G.showToast(context.getApplicationContext(), "抱歉，交易服务器正在维护中，请稍候再试，请您带来不便敬请谅解。");
            }
        }
    }

}
