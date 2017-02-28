package com.cfjn.javacf.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cfjn.javacf.shumi.ShumiAsstes;
import com.cfjn.javacf.shumi.ShumiBankCard;
import com.cfjn.javacf.shumi.ShumiTrading;
import com.cfjn.javacf.modle.GetKeyInfoVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.util.ThreadPool;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.cfjn.javacf.modle.GetKeyInfoVo.DateEntity;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 登陆后立即调用 获取数米交易记录提交时间 然后提交数米资产 交易记录 和资产（延迟3秒）
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class UserSyntion implements OKHttpListener {
    private UserInfo userInfo;
    private Context context;
    private boolean isComint;
    private boolean isLogin;
    private GetKeyInfoVo getKeyInfoVo;
    private String startTime;
    public UserSyntion(Context context,boolean isComint,boolean isLogin) {
        this.context = context;
        this.isComint=isComint;
        this.isLogin=isLogin;
        userInfo =new UserInfo(context);
        isOpenAccten();

    }

    private  void  isOpenAccten() {
        Map<String, String> map = new HashMap<>();
        map.put("token", userInfo.getToken());
        map.put("secretKey", userInfo.getSecretKey());
        OkHttpUtil.sendPost(ApiUri.STATUS, map, this);
    }

    @Override
    public void onSuccess(String uri, String result) {
        if (uri.equals(ApiUri.STATUS)){
            Gson gson = new Gson();
            getKeyInfoVo = gson.fromJson(result,GetKeyInfoVo.class);
            if (getKeyInfoVo.getDate() == null || getKeyInfoVo.getDate().size() <= 0) {
                return;
            }
            for (int i = 0; i < getKeyInfoVo.getDate().size(); i++) {
                DateEntity dateEntity = getKeyInfoVo.getDate().get(i);
                if(G.isEmteny(dateEntity.getToken())||G.isEmteny(dateEntity.getToken())){
                    continue;
                }
                for (int j=0;j<dateEntity.getRecordTimeList().size();j++){
                    DateEntity.RecordTimeListEntity rd=dateEntity.getRecordTimeList().get(j);
                    switch (Integer.parseInt(dateEntity.getSource())) {
                        case 0:
                            userInfo.setSmTokenSecret(dateEntity.getTokenSecret());
                            userInfo.setSmToken(dateEntity.getToken());
                            userInfo.initUserData();
                            //数米柜台
                            if(rd.getObjective()==1){
                                //交易记录
//                                    if(rd.getStartTime().equals("2000-01-01")){

//                                    }
                                //如果是登录状态 先提交银行卡信息 等待3秒后提交资产数据
                                //然后再提交交易记录
                                //非登录状态直接提交交易记录
                                if(isLogin){
                                    handler.sendEmptyMessage(0x13);
                                    ThreadPool.fixedThreadPool.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(3000);
                                                handler.sendEmptyMessage(0x12);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                G.log(rd.getStartTime()+"-----------startTime");
                                startTime=rd.getStartTime();
                                handler.sendEmptyMessage(0x11);
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.log(uri+"---------------获取数米信息失败");
    }

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                new ShumiTrading(context, startTime, isComint); //提交交易记录
            } else if (msg.what == 0x12) {
                new ShumiAsstes(context); //提交资产信息
            } else if (msg.what == 0x13) {
                new ShumiBankCard(context);  //提交银行卡信息
            }
        }
    };
}
