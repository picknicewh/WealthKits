package com.cfjn.javacf.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.member.LockActivity;
import com.cfjn.javacf.activity.member.LockSetupActivity;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.NetWorkUitls;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.utility.apibase.APIManager;
import com.cfjn.javacf.utility.apibase.APIUtil;
import com.cfjn.javacf.utility.common.SharedPreferencesUtil;
import com.cfjn.javacf.widget.SynchDateDialog;
import com.openhunme.sdk.update.ModelUpdateManager;
import com.openhunme.sdk.utils.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  作者： zll
 *  时间： 2016-6-2
 *  名称： 资源包下载和手势密码校验
 *  版本说明：代码规范整改
 *  附加注释： 1.担保购机资源包来自服务端，
 *            2.检验资源包-下载资源包- 对比资源包- 完成下载 资源包下载需要在最开始完成，中间下载失败直接退出程序
 *            3.无网络可以进入程序 跳过资源包校验
 *            4.资源包下载完成 进行手势密码校验
 *  主要接口：1.资源包下载
 */
public class ShowLockActivity extends Activity {
    //-----------布局变量--------------
    /**
     * 加载图片
     */
    @Bind(R.id.lv_loading)
    ImageView lvLoading;
    /**
     * 加载提示
     */
    @Bind(R.id.tv_prompt)
    TextView tvPrompt;
    /**
     * 升级失败退出提示框
     */
    private SynchDateDialog dialog;
    /**
     * 加载动画
     */
    private Animation operatingAnim;

    //---------资源升级新增测试代码-----------
    /**
     * 1.调用beginInit开始初始化
     * 2.在finishInit中添加代码
     * 3.showExitDialog加上对话框
     * 4.modelUpdateUrl路径在app/build.gradle中修改
     */
    public static final String MODEL_TAP_MD5_KEY = "modeltap_md5_key";
    private String modelUpgradeType = "1"; // 1为强制升级，2为可选升级
    private boolean modelUpgradeSuccess = true; //资源包升级是否成功
    private String tapMd5 = "";
    private static int NEXTSTEP = 1;
    private int initStep = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlock);
        ButterKnife.bind(this);
        initWindow();
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_evey);
        lvLoading.startAnimation(operatingAnim);
        beginInit();

    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 手势密码验证
     */
    private void setLockeTO() {
        Intent intent;
        SharedPreferences preferences = this.getSharedPreferences(LockActivity.LOCK, Context.MODE_PRIVATE);
        boolean hasLock = preferences.getBoolean(LockSetupActivity.HAS_LOCK + G.user.USER_ID, false);
        if (hasLock) {
            intent = new Intent(this, LockActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    /**
     * 校验资源文件
     */
    @SuppressLint("HandlerLeak")
    private Handler initHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NEXTSTEP) {
                initStep++;
                switch (initStep) {
                    case 1:
                        asyncInitModel();
                        break;
                    case 2:
                        modelUpgradeType = "1";
                        modelUpgradeSuccess = true;
                        tapMd5 = "";
                        asyncUpgradeModel();
                        break;
                    case 3:
                        asyncVerifyModel();
                        break;
                    case 4:
                        finishInit();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 完成初始化，继续
     */
    private void finishInit() {
        lvLoading.clearAnimation();
        setLockeTO();
    }

    /**
     * 消息提示框
     * @param msgContent 消息提示
     */
    private void showExitDialog(String msgContent) {
        lvLoading.clearAnimation();
        lvLoading.setVisibility(View.GONE);
        dialog = new SynchDateDialog(this, R.style.myDialogTheme, msgContent);
        dialog.show();
        dialog.setCancelable(false);
    }

    /**
     * 校验开始
     */
    private void beginInit() {
        initStep = 0;
        nextInit();
    }
    /**
     * 下一步
     */
    private void nextInit() {
        initHandler.obtainMessage(NEXTSTEP).sendToTarget();
    }
    /**
     *  检测www环境初始化是否完成，assets下tap.zip解压到运行空间
     *  若assets下tap.zip/tap.txt有变化，版本号一定要增长
     * @return
     */
    public int checkInitOkTest() {
        Log.i("API", "checkInitOkTest");
        return APIManager.getInstance().checkInitOk(this);
    }

    //验证运行空间数据完整性
    public int checkVerifyOkTest(String tapMd5) {
        Log.i("API", "checkVerifyOkTest");
        return APIManager.getInstance().checkVerifyOk(this, tapMd5);
    }

    public void checkModelVersionTest() {
        Log.i("API", "checkModelVersionTest");
        try {
            APIManager.getInstance().checkModelVersion(this, ApiUri.SOURCE_DOWNLOAD, "tap", new OKHttpListener() {
                @Override
                public void onSuccess(final String uri, final String result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            {
                                boolean continueInit = true;
                                try {
                                    if (uri.equals("ERROR")) {
                                        Log.i("ERROR", result);
                                        if ("1".equalsIgnoreCase(modelUpgradeType))
                                            continueInit = false;
                                    } else {
                                        Log.i("json", result);
                                        ModelUpdateManager.getInstance().setUpdateManifestData(result);
                                        JSONObject jo = new JSONObject(result);
                                        String errorNo = jo.getString("error_no");
                                        String errorInfo = jo.getString("error_info");
                                        Log.i("checkModelVersion", "error_no:" + errorNo + " error_info:" + errorInfo);
                                        if (errorNo.equals("0")) {
                                            String dsName = jo.getJSONArray("dsName").getString(0);
                                            String data = jo.getString(dsName);
                                            jo = new JSONObject(data);
                                            String status = jo.getString("status");
                                            Log.i("checkModelVersion", "status:" + status);
                                            if (status.equals("1")) {
                                                tvPrompt.setText("正在下载资源文件...");
                                                final String bundleInstallPath = jo.getString("bundle_install_path");
                                                Log.i("checkModelVersion", "bundle_install_path:" + bundleInstallPath);
                                                final String bundleModel = jo.getString("bundle_model");
                                                Log.i("checkModelVersion", "bundle_model:" + bundleModel);
                                                String bundleVersion = jo.getString("bundle_version");
                                                Log.i("checkModelVersion", "bundle_version:" + bundleVersion);
                                                final String bundleMd5 = jo.getString("bundle_md5");
                                                //保存此md5
                                                tapMd5 = bundleMd5;
                                                SharedPreferencesUtil.setStringValueByKey(MODEL_TAP_MD5_KEY, tapMd5);
                                                Log.i("checkModelVersion", "bundle_md5:" + bundleMd5);
                                                String softVersion = jo.getString("soft_version");
                                                Log.i("checkModelVersion", "soft_version:" + softVersion);
                                                String upgradeNote = jo.getString("upgrade_note");
                                                Log.i("checkModelVersion", "upgrade_note:" + upgradeNote);
                                                modelUpgradeType = jo.getString("upgrade_type");
                                                Log.i("checkModelVersion", "upgrade_type:" + modelUpgradeType);
                                                String releaseStatus = jo.getString("release_status");
                                                Log.i("checkModelVersion", "release_status:" + releaseStatus);
                                                int bundleSize = 1; //以MB为单位
                                                if (jo.has("bundle_size"))
                                                    bundleSize = jo.getInt("bundle_size");
                                                Log.i("checkModelVersion", "bundle_size:" + bundleSize);
                                                //检测是否有足够的空间
                                                if (!APIUtil.hasEnoughModelSpace(bundleSize * 1024 * 1024 * 2)) {
                                                    if (!"1".equalsIgnoreCase(modelUpgradeType)) {
                                                        //资源包为可选升级
                                                        Toast.makeText(ShowLockActivity.this, "没有足够的系统空间，无法资源升级！", Toast.LENGTH_LONG).show();
                                                        nextInit();
                                                    } else {
                                                        handleError("没有足够的系统空间，无法资源升级！");
                                                    }
                                                    return;
                                                }

                                                //线程只有在run里面才不算主线程，其他代码一律是主线程
                                                continueInit = false;
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            ModelUpdateManager.getInstance().doUpdate(ShowLockActivity.this, bundleModel, bundleInstallPath, bundleMd5, new ModelUpdateManager.ModelUpdateInterface() {
                                                                @Override
                                                                public void onBegin() {
                                                                    Log.i("mui", "onBegin");
                                                                }

                                                                @Override
                                                                public void onEnd() {
                                                                    Log.i("mui", "onEnd");
                                                                    if (modelUpgradeSuccess) {
                                                                        //资源包升级成功，通知主线程列队，已经可以执行下一个init项目了。
                                                                        Toast.makeText(ShowLockActivity.this, "资源包升级成功", Toast.LENGTH_LONG).show();
                                                                        nextInit();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onProgress(int rate) {
                                                                    Log.i("mui", "onProgress " + rate);
                                                                }

                                                                @Override
                                                                public void onBeginUnzip() {
                                                                    Log.i("mui", "onBeginUnzip");
                                                                }

                                                                @Override
                                                                public void onDownloadFail() {
                                                                    Log.i("mui", "onDownloadFail");
                                                                    handleUpgradeError();
                                                                }

                                                                @Override
                                                                public void onUnzipFail(int type) {
                                                                    Log.i("mui", "onUnzipFail " + type);
                                                                    handleUpgradeError();
                                                                }

                                                                @Override
                                                                public void onVerifyFail(int type) {
                                                                    Log.i("mui", "onVerifyFail " + type);
                                                                    handleUpgradeError();
                                                                }

                                                                @Override
                                                                public void onCopyFail() {
                                                                    Log.i("mui", "onCopyFail");
                                                                    handleUpgradeError();
                                                                }

                                                                @Override
                                                                public void onFail() {
                                                                    Log.i("mui", "onFail");
                                                                    handleUpgradeError();
                                                                }
                                                            });
                                                            Log.i("doUpdate", "Model Upgrade End!");
                                                        } catch (Exception e) {
                                                            Log.i("doUpdate", e.getMessage());
                                                            handleUpgradeError();
                                                        } finally {
                                                        }
                                                    }
                                                }).start();
                                            } else {
                                                //没有升级，保存此md5
                                                final String bundleMd5 = jo.getString("bundle_md5");
                                                tapMd5 = bundleMd5;
                                                SharedPreferencesUtil.setStringValueByKey(MODEL_TAP_MD5_KEY, tapMd5);
                                            }
                                        } else {
                                            if ("1".equalsIgnoreCase(modelUpgradeType))
                                                continueInit = false;
                                            else
                                                handleUpgradeError();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    if ("1".equalsIgnoreCase(modelUpgradeType))
                                        continueInit = false;
                                    else
                                        handleUpgradeError();
                                }
                                if (continueInit)
                                    nextInit();

                            }
                        }
                    });
                }

                @Override
                public void onError(String uri, final String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            handleError("请检查网络");
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            handleUpgradeError();
        }

    }

    private void handleUpgradeError() {
        modelUpgradeSuccess = false;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("1".equalsIgnoreCase(modelUpgradeType)) {
                    //资源包为强制升级
                    showExitDialog("资源包升级失败，请退出！");
                } else {
                    //资源包为可选升级
                    Toast.makeText(ShowLockActivity.this, "资源包升级失败！", Toast.LENGTH_LONG).show();
                    nextInit();
                }
            }
        });
    }

    private void handleError(final String error) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //判断是否有网络连接  是的话直接进去程序
                if (error.equals("请检查网络")) {
                    finishInit();
                } else {
                    showExitDialog(error);
                }
            }
        });
    }

    private void asyncUpgradeModel() {
        if (NetWorkUitls.isConnect())
            checkModelVersionTest();
        else
            handleError("请检查网络");
    }

    private void asyncVerifyModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (StringHelper.isEmpty(tapMd5))
                    tapMd5 = SharedPreferencesUtil.getStringValueByKey(MODEL_TAP_MD5_KEY, "");
                if (checkVerifyOkTest(tapMd5) == ModelUpdateManager.VERIFY_OK)
                    nextInit();
                else
                    handleError("校验资源失败，请退出！");
                System.gc();
                System.gc();
            }
        }).start();

    }

    private void asyncInitModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (checkInitOkTest() != ModelUpdateManager.INIT_FAIL)
                    nextInit();
                else
                    handleError("初始化资源包失败，请退出！");
            }
        }).start();
    }

}
