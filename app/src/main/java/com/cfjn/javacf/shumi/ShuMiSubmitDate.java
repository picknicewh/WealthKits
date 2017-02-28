package com.cfjn.javacf.shumi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cfjn.javacf.appService.NetWorkMangerBase;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.DateUtil;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.HunmeApplication;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.util.ThreadPool;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 作者： zll
 * 时间： 2016-6-29
 * 名称： 提交数米信息
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ShuMiSubmitDate implements OKHttpListener {
    private Context context;
    private UserInfo us;
    private ShuMiTradingDate shuMiTradingDate;
    private ShuMiBankDate shumiBankCard;
    private ShumiAsstesDate asstesDate;
    private boolean isComint;
    private String objective;
    private String submitDate;
    public ShuMiSubmitDate(Context context, String submitDate, String objective, boolean isComint){
        this.context = context;
        this.isComint=isComint;
        shuMiTradingDate=new ShuMiTradingDate(context);
        shumiBankCard=new ShuMiBankDate(context);
        asstesDate=new ShumiAsstesDate(context);
        us=UserInfo.getInstance(context);
        this.objective =objective;
        this.submitDate =submitDate;
        submitDate();
    }


    private void submitDate(){
        Map<String,String> map=new HashMap<>();
        map.put("token",us.getToken());
        map.put("secretKey", us.getSecretKey());
        map.put("objective",objective);
        map.put("source", "0");
        map.put("values", submitDate);
        OkHttpUtil.sendPost(ApiUri.CLIENTDATE, map, this);
    }

    private void NetWokeTimeSmSubmit(){
        ThreadPool.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://bjtime.cn/");
                    URLConnection uc = url.openConnection();// 生成连接对象
                    uc.connect(); // 发出连接
                    long ld=uc.getDate(); //取得网站日期时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String time = sdf.format(ld);
                    us.setSubmitTime(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void shumiTradin(){
        G.log("--------数米交易记录数据提交成功------");
        if(isComint){
            //每天下午三点提交的时间记录下来
            us.setDateTime(DateUtil.getCurrentDate());
        }
        if(G.isEmteny(G.NetWorkTime)){
            NetWokeTimeSmSubmit();
        }else{
            us.setSubmitTime(G.NetWorkTime);
        }

        //是否需要关闭广播
        new NetWorkMangerBase(context,1);
    }

    private void shumiBankCard(){
        G.log("-------银行卡信息提交成功-------");
        new NetWorkMangerBase(context,2);
    }

    private void shumiAsstes(){
        G.log("-------资产信息提交成功-------");
        UserInfo.getInstance(context).setSubMitAesstes(DateUtil.getCurrentDate());
        //是否需要关闭广播
        new NetWorkMangerBase(context,0);
    }
  private  Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          if (msg.what==0x01){
              switch (Integer.parseInt(objective)){
                  case 0:
                      shumiAsstes();
                      break;
                  case 1:
                      shumiTradin();
                      break;
                  case 2:
                      shumiBankCard();
                      break;
              }

              if (HunmeApplication.receiverManger.getReceiverType() && G.isEmteny(shuMiTradingDate.getSmTradingDate()) && G.isEmteny(shuMiTradingDate.getSmTradingDate()) && G.isEmteny(asstesDate.getShumiAsstesDate())) {
                  HunmeApplication.receiverManger.unRegisterNetworkReceiver();
              }
          }else if (msg.what==0x02){
              switch (Integer.parseInt(objective)){
                  case 0:
                      G.log("------数米资产数据提交失败--------" );
                      asstesDate.setShumiAsstesDate(submitDate);
                      break;
                  case 1:
                      G.log("------数米交易记录数据提交失败--------");
                      shuMiTradingDate.setSmTradingDate(submitDate);
                      shuMiTradingDate.setSmTradingType(isComint);
                      break;
                  case 2:
                      G.log("------数米银行卡数据提交失败--------" );
                      shumiBankCard.setShuMiBankDate(submitDate);
                      break;
              }
              if(!HunmeApplication.receiverManger.getReceiverType()){
                  HunmeApplication.receiverManger.registerNetworkReceiver();
              }
          }
      }
  };
    @Override
    public void onSuccess(String uri, String result) {
        handler.sendEmptyMessage(0x01);
    }

    @Override
    public void onError(String uri, String error) {
        handler.sendEmptyMessage(0x02);
    }
}
