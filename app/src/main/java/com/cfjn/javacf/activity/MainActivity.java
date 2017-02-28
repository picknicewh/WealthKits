package com.cfjn.javacf.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cfjn.javacf.JPush.JPushUtil;
import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.assets.MyAssetsActivity;
import com.cfjn.javacf.activity.bookkeeping.BookkeepingActivity;
import com.cfjn.javacf.activity.fund.FundSupmakActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.base.UserMessage;
import com.cfjn.javacf.base.UserSyntion;
import com.cfjn.javacf.modle.JpushReslut;
import com.cfjn.javacf.shumi.ShumiAsstes;
import com.cfjn.javacf.shumi.ShumiAsstesDate;
import com.cfjn.javacf.shumi.ShuMiBankDate;
import com.cfjn.javacf.shumi.ShuMiTradingDate;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.HunmeApplication;
import com.cfjn.javacf.util.NetWorkUitls;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.cfjn.javacf.util.ThreadPool;
import com.cfjn.javacf.util.UpdateManager;
import com.google.gson.Gson;
import com.openhunme.cordova.activity.HMDroidGap;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 *  作者： zll
 *  时间： 2016-5-27
 *  名称： 项目首页
 *  版本说明：代码规范整改
 *  附加注释：用tabhost添加4个主页面 RadioGroup和RadioButton配合实现单选
 * 初始化屏幕高度和获取百度定位（百度定位如果没有用，请检查.so文件），双击返回键退出程序
 *  主要接口：没有
 */
public class MainActivity extends TabActivity implements OKHttpListener{
    /**
     * 基金超市
     */
    @Bind(R.id.rb_fund_management)
    RadioButton rbFundManagement;
    /**
     * 担保购机
     */
    @Bind(R.id.rb_purchase_guarantee)
    RadioButton rbPurchaseGuarantee;
    /**
     * 科学记账
     */
    @Bind(R.id.rb_scientific_accounting)
    RadioButton rbScientificAccounting;
    /**
     * 我的资产
     */
    @Bind(R.id.rb_my_assets)
    RadioButton rbMyAssets;
    @Bind(R.id.rg_home_menu)
    RadioGroup rgHomeMenu;

    private TabHost tabHost;
    private TabHost.TabSpec spec;

    private Intent intent;
    /**
     * 消息数据库
     */
    public static UserMessage UMDB;

    public static String TFurl;
    /**
     * 记录是否是担保购机页面
     */
    private boolean isGuaranteed;
    /**
     * 是否连续点击了两次返回键
     */
    private boolean isQuit = false;
    private Timer timer;
    private UserInfo userInfo;
    /**
     * 记录点击了那个页面
     * 在登录之后继续加载登录之前点击的页面
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
//        getLocation();
        TFurl = "file:///" + this.getFilesDir().getAbsolutePath() + "/tap";
        initLoading();
        timer = new Timer();
        userInfo = UserInfo.getInstance(this);
        tabHost = this.getTabHost();
        UMDB = new UserMessage(this);
//        if(G.isFirst){
//            G.isFirst=false;
//            beginInit();
//        }
        if (!TextUtils.isEmpty(userInfo.getLoginName())){
            Log.i("TAGG","已经是登录的");
            //发送RegistrationID与AppUid对应数据的接口
            sendRegistrationId(JPushUtil.getRegid(getApplicationContext()),getApplicationContext());

        }
        intent = new Intent().setClass(this, FundSupmakActivity.class);
        spec = tabHost.newTabSpec("基金超市").setIndicator("基金超市").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, HMDroidGap.class);
        intent.putExtra("loadUrl", "phone_hunme");
        spec = tabHost.newTabSpec("担保购机").setIndicator("担保购机").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, BookkeepingActivity.class);
        spec = tabHost.newTabSpec("科学记账").setIndicator("科学记账").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MyAssetsActivity.class);
        spec = tabHost.newTabSpec("我的资产").setIndicator("我的资产").setContent(intent);
        tabHost.addTab(spec);

        rgHomeMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isGuaranteed = false;
                type = 0;
                switch (checkedId) {
                    case R.id.rb_fund_management:
                        tabHost.setCurrentTabByTag("基金超市");
                        break;
                    case R.id.rb_purchase_guarantee:
                        tabHost.setCurrentTabByTag("担保购机");
                        isGuaranteed = true;
                        break;
                    case R.id.rb_scientific_accounting:
                        if (TextUtils.isEmpty(userInfo.getLoginName())) {
                            isGoLogin();
                        } else {
                            tabHost.setCurrentTabByTag("科学记账");
                        }
                        type = 3;
                        break;
                    case R.id.rb_my_assets:
                        if (TextUtils.isEmpty(userInfo.getLoginName())) {
                            isGoLogin();
                        } else {
                            tabHost.setCurrentTabByTag("我的资产");
                        }
                        type = 4;
                        break;
                    default:
                }
            }
        });
        Intent intent = getIntent();
        int tab = intent.getIntExtra("tab", -1);
        if (tab == 3) {
            rbScientificAccounting.setChecked(true);
            rbFundManagement.setChecked(false);
        } else if (tab == 2) {
            rbPurchaseGuarantee.setChecked(true);
            rbFundManagement.setChecked(false);
        }
        //初始化获取屏幕宽高
        G.initDisplaySize(this);

        setValuesServiceMoth();
        // 检查版本更新
        new UpdateManager(this).checkUpdate(1);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (G.isEmteny(userInfo.getLoginName())) {
            rbFundManagement.setChecked(true);
            return;
        }
        else {
            if (type == 3) {
                tabHost.setCurrentTabByTag("科学记账");
                rbScientificAccounting.setChecked(true);
            } else if (type == 4) {
                tabHost.setCurrentTabByTag("我的资产");
                rbMyAssets.setChecked(true);
            }
        }
    }

    private void isGoLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //这里处理逻辑代码
            if (isGuaranteed) {
                return super.dispatchKeyEvent(event);
            }
            if (isQuit) {
                // 这是两次点击以后
//                HunmeApplication.getInstance().exit();
                defaultBackPressed();
            } else {
                isQuit = true;
                G.showToast(this.getApplicationContext(), "再按一次退出财富锦囊");
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    //退出程序
    private void defaultBackPressed() {
        super.onBackPressed();
    }

    /**
     * 百度定位
     */
    private void initLoading() {
        final LocationClient locationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 设置定位模式
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                G.PROVINCE = location.getProvince();
                G.CITY = location.getCity();
                G.DISTRICT = location.getDistrict();
                G.log(G.PROVINCE+"--------------"+G.CITY+"-----------------"+G.DISTRICT);
                locationClient.stop();
            }
        });
        //  开启定位
        locationClient.start();
    }

    /**
     * 发送RegistrationID与AppUid对应数据的接口
     */
    private  void  sendRegistrationId(String registrationId,Context context){
        Map<String,String> param = new HashMap<>();
        param.put("registrationId",registrationId);
        param.put("secretKey", new UserInfo(context).getSecretKey());
        param.put("appId","1");
        OkHttpUtil.sendPost(ApiUri.JPUSH,param,this);
    }
    /**
    //---------资源升级新增测试代码-----------
//调用beginInit开始初始化
//在finishInit中添加代码
//showExitDialog加上对话框
//modelUpdateUrl路径在app/build.gradle中修改
//    public static final String MODEL_TAP_MD5_KEY = "modeltap_md5_key";
//    private String modelUpdateUrl = "";
//    private String modelUpgradeType = "1"; // 1为强制升级，2为可选升级
//    private boolean modelUpgradeSuccess = true; //资源包升级是否成功
//    private String tapMd5 = "";
//    private static int NEXTSTEP = 1;
//    private int initStep = 0;
//    @SuppressLint("HandlerLeak")
//    private Handler initHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == NEXTSTEP) {
//                initStep ++;
//                switch (initStep) {
//                    case 1:
//                        asyncInitModel();
//                        break;
//                    case 2:
//                        modelUpgradeType = "1";
//                        modelUpgradeSuccess = true;
//                        tapMd5 = "";
//                        asyncUpgradeModel();
//                        break;
//                    case 3:
//                        asyncVerifyModel();
//                        break;
//                    case 4:
//                        finishInit();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    };
//
//    private void finishInit() {
//        //完成初始化，继续
//        Toast.makeText(this,"初始化已经全部完成...",Toast.LENGTH_SHORT).show();
//        LoadingDialog.stopLoading();
//    }
//
//    private void showExitDialog(String msgContent) {
////        Toast.makeText(MainActivity.this, msgContent, Toast.LENGTH_LONG).show();
////        MainActivity.this.finish();
////        System.exit(0);
//        LoadingDialog.stopLoading();
//        dialog =new SynchDateDialog(this,R.style.myDialogTheme,msgContent);
//        dialog.show();
//        dialog.setCancelable(false);
//    }
//
//    private void beginInit() {
//        initStep = 0;
//        modelUpdateUrl = this.getResources().getString(R.string.model_update_url);
//        nextInit();
//        LoadingDialog.regstLoading(this, "资源文件同步中,请稍后...");
//        LoadingDialog.starcLoading();
//    }
//
//    private void nextInit() {
//        initHandler.obtainMessage(NEXTSTEP).sendToTarget();
//    }
//
//    //检测www环境初始化是否完成，assets下tap.zip解压到运行空间
//    //若assets下tap.zip/tap.txt有变化，版本号一定要增长
//    public int checkInitOkTest() {
//        Log.i("API", "checkInitOkTest");
//        return APIManager.getInstance().checkInitOk(this);
//    }
//
//    //验证运行空间数据完整性
//    public int checkVerifyOkTest(String tapMd5) {
//        Log.i("API", "checkVerifyOkTest");
//        return APIManager.getInstance().checkVerifyOk(this, tapMd5);
//    }
//
//    public void checkModelVersionTest() {
//        Log.i("API", "checkModelVersionTest");
//        try {
//            APIManager.getInstance().checkModelVersion(this, modelUpdateUrl, "tap", new JsonCatcher( ) {
//                @Override
//                public void onCatched(String uri, String json) {
//                    boolean continueInit = true;
//                    try {
//                        if (uri.equals("ERROR")) {
//                            Log.i("ERROR", json);
//                            if ("1".equalsIgnoreCase(modelUpgradeType))
//                                continueInit = false;
//                        } else {
//                            Log.i("json", json);
//                            ModelUpdateManager.getInstance().setUpdateManifestData(json);
//                            JSONObject jo = new JSONObject(json);
//                            String errorNo = jo.getString("error_no");
//                            String errorInfo = jo.getString("error_info");
//                            Log.i("checkModelVersion", "error_no:" + errorNo + " error_info:" + errorInfo);
//                            if (errorNo.equals("0")) {
//                                String dsName = jo.getJSONArray("dsName").getString(0);
//                                String data = jo.getString(dsName);
//                                jo = new JSONObject(data);
//                                String status = jo.getString("status");
//                                Log.i("checkModelVersion", "status:" + status);
//                                if (status.equals("1")) {
//                                    final String bundleInstallPath = jo.getString("bundle_install_path");
//                                    Log.i("checkModelVersion", "bundle_install_path:" + bundleInstallPath);
//                                    final String bundleModel = jo.getString("bundle_model");
//                                    Log.i("checkModelVersion", "bundle_model:" + bundleModel);
//                                    String bundleVersion = jo.getString("bundle_version");
//                                    Log.i("checkModelVersion", "bundle_version:" + bundleVersion);
//                                    final String bundleMd5 = jo.getString("bundle_md5");
//                                    //保存此md5
//                                    tapMd5 = bundleMd5;
//                                    SharedPreferencesUtil.setStringValueByKey(MODEL_TAP_MD5_KEY, tapMd5);
//
//                                    Log.i("checkModelVersion", "bundle_md5:" + bundleMd5);
//                                    String softVersion = jo.getString("soft_version");
//                                    Log.i("checkModelVersion", "soft_version:" + softVersion);
//                                    String upgradeNote = jo.getString("upgrade_note");
//                                    Log.i("checkModelVersion", "upgrade_note:" + upgradeNote);
//                                    modelUpgradeType = jo.getString("upgrade_type");
//                                    Log.i("checkModelVersion", "upgrade_type:" + modelUpgradeType);
//                                    String releaseStatus = jo.getString("release_status");
//                                    Log.i("checkModelVersion", "release_status:" + releaseStatus);
//                                    int bundleSize = 1; //以MB为单位
//                                    if (jo.has("bundle_size"))
//                                        bundleSize = jo.getInt("bundle_size");
//                                    Log.i("checkModelVersion", "bundle_size:" + bundleSize);
//
//                                    //检测是否有足够的空间
//                                    if (!APIUtil.hasEnoughModelSpace(bundleSize * 1024 * 1024 * 2)) {
//                                        if (!"1".equalsIgnoreCase(modelUpgradeType)) {
//                                            //资源包为可选升级
//                                            Toast.makeText(MainActivity.this, "没有足够的系统空间，无法资源升级！", Toast.LENGTH_LONG).show();
//                                            nextInit();
//                                        } else {
//                                            handleError("没有足够的系统空间，无法资源升级！");
//                                        }
//                                        return;
//                                    }
//
//                                    //线程只有在run里面才不算主线程，其他代码一律是主线程
//                                    continueInit = false;
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                ModelUpdateManager.getInstance().doUpdate(MainActivity.this, bundleModel, bundleInstallPath, bundleMd5, new ModelUpdateManager.ModelUpdateInterface() {
//                                                    @Override
//                                                    public void onBegin() {
//                                                        Log.i("mui", "onBegin");
//                                                    }
//                                                    @Override
//                                                    public void onEnd() {
//                                                        Log.i("mui", "onEnd");
//                                                        if (modelUpgradeSuccess) {
//                                                            //资源包升级成功，通知主线程列队，已经可以执行下一个init项目了。
//                                                            Toast.makeText(MainActivity.this, "资源包升级成功", Toast.LENGTH_LONG).show();
//                                                            nextInit();
//                                                        }
//                                                    }
//                                                    @Override
//                                                    public void onProgress(int rate) {
//                                                        Log.i("mui", "onProgress " + rate);
//                                                    }
//                                                    @Override
//                                                    public void onBeginUnzip() {
//                                                        Log.i("mui", "onBeginUnzip");
//                                                    }
//                                                    @Override
//                                                    public void onDownloadFail() {
//                                                        Log.i("mui", "onDownloadFail");
//                                                        handleUpgradeError();
//                                                    }
//                                                    @Override
//                                                    public void onUnzipFail(int type) {
//                                                        Log.i("mui", "onUnzipFail " + type);
//                                                        handleUpgradeError();
//                                                    }
//                                                    @Override
//                                                    public void onVerifyFail(int type) {
//                                                        Log.i("mui", "onVerifyFail " + type);
//                                                        handleUpgradeError();
//                                                    }
//                                                    @Override
//                                                    public void onCopyFail() {
//                                                        Log.i("mui", "onCopyFail");
//                                                        handleUpgradeError();
//                                                    }
//                                                    @Override
//                                                    public void onFail() {
//                                                        Log.i("mui", "onFail");
//                                                        handleUpgradeError();
//                                                    }
//                                                });
//                                                Log.i("doUpdate", "Model Upgrade End!");
//                                            } catch (Exception e) {
//                                                Log.i("doUpdate", e.getMessage());
//                                                handleUpgradeError();
//                                            } finally {
//                                            }
//                                        }
//                                    }).start();
//                                } else {
//                                    //没有升级，保存此md5
//                                    final String bundleMd5 = jo.getString("bundle_md5");
//                                    tapMd5 = bundleMd5;
//                                    SharedPreferencesUtil.setStringValueByKey(MODEL_TAP_MD5_KEY, tapMd5);
//                                }
//                            } else {
//                                if ("1".equalsIgnoreCase(modelUpgradeType))
//                                    continueInit = false;
//                                else
//                                    handleUpgradeError();
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        if ("1".equalsIgnoreCase(modelUpgradeType))
//                            continueInit = false;
//                        else
//                            handleUpgradeError();
//                    }
//                    if (continueInit)
//                        nextInit();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            handleUpgradeError();
//        }
//    }
//
//    private void handleUpgradeError() {
//        modelUpgradeSuccess = false;
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if ("1".equalsIgnoreCase(modelUpgradeType)) {
//                    //资源包为强制升级
//                    showExitDialog("资源包升级失败，请退出！");
//                } else {
//                    //资源包为可选升级
//                    Toast.makeText(MainActivity.this, "资源包升级失败！", Toast.LENGTH_LONG).show();
//                    nextInit();
//                }
//            }
//        });
//    }
//
//    private void handleError(final String error) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                showExitDialog(error);
//            }
//        });
//    }
//
//    private void asyncUpgradeModel(){
//        if (NetworkManager.isConnect())
//            checkModelVersionTest();
//        else
//            handleError("请检查网络");
//    }
//
//    private void asyncVerifyModel() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (StringHelper.isEmpty(tapMd5))
//                    tapMd5 = SharedPreferencesUtil.getStringValueByKey(MODEL_TAP_MD5_KEY, "");
//                if (checkVerifyOkTest(tapMd5) ==  ModelUpdateManager.VERIFY_OK)
//                    nextInit();
//                else
//                    handleError("校验资源失败，请退出！");
//                System.gc();
//                System.gc();
//            }
//        }).start();
//
//    }
//
//    private void asyncInitModel(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (checkInitOkTest() != ModelUpdateManager.INIT_FAIL)
//                    nextInit();
//                else
//                    handleError("初始化资源包失败，请退出！");
//            }
//        }).start();
//    }


    /**
     * 数米提交交易记录
     * 向服务端提交数据
     * 用户两次打开app的时间间隔24h
     * 提交数据
     */
    private void setValuesServiceMoth() {
        if (NetWorkUitls.isNetworkAvailable(getApplicationContext())&&!G.isEmteny(userInfo.getSmToken())&&!G.isEmteny(userInfo.getLoginName())) {
            ThreadPool.cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    URL url= null;//取得资源对象
                    try {
                        url = new URL("http://bjtime.cn/");
                        URLConnection uc = url.openConnection();// 生成连接对象
                        uc.connect(); // 发出连接
                        long ld=uc.getDate(); //取得网站日期时间
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String time = sdf.format(ld);
                        Date d1 = df.parse(userInfo.getSubmitTime());
                        Date d2=df.parse(time);
                        long times = d2.getTime()-d1.getTime();
                        long days = times / (1000 * 60 * 60 * 24);
                        G.log(userInfo.getSubmitTime()+"--------时间差-------"+time);
                        G.NetWorkTime=time;//将网络获取的时间赋值以保存数米交易记录的时间的储存时间
                        if(days>0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    G.log("提交交易记录");
                                    new UserSyntion(getApplicationContext(),false,false);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            /**
             * 如果向服务端提交数据失败的话会出发一个广播搜集数据
             * 等有网络的时侯再次提交
             * 这里是再次提交还是失败的情况下又将数据搜集起来
             * 每次启动APP时候判断收集器是否还有消息
             * 有的话再次提交直到成功为止并清空
             */
            if(!G.isEmteny(new ShuMiBankDate(this).getShuMiBankDate())||!G.isEmteny(new ShuMiTradingDate(this).getSmTradingDate())||!G.isEmteny(new ShumiAsstesDate(this).getShumiAsstesDate())){
                HunmeApplication.receiverManger.registerNetworkReceiver();
            }
            /**
             * 每天提交资产
             */
            new ShumiAsstes(getApplicationContext());
        }
    }

    @Override
    public void onSuccess(final String uri, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.JPUSH)){
                    //极光推送成功的结果
                     Gson gson  = new Gson();
                     JpushReslut jpushReslut  = gson.fromJson(result,JpushReslut.class);
                     Log.i("TAGG","传送成功："+result);
                    Toast.makeText(getApplicationContext(),jpushReslut.getResultDesc(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onError(final String uri, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uri.equals(ApiUri.JPUSH)){
                    //极光推送失败的结果
                    Log.i("TAGG","传送失败："+error);
                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
