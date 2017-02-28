package com.cfjn.javacf.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-2
 * 名称： 首页广告跳入web网页
 * 版本说明：代码规范整改
 * 附加注释：实现web页
 * 主要接口：暂无
 */
public class WebViewActivity extends Activity {
    /**
     * web容器
     */
    @Bind(R.id.webView)
    WebView webView;
    /**
     * 加载圆形进度条
     */
    @Bind(R.id.iv_loadingview)
    ImageView bButLoadingview;
    /**
     * 网络地址
     */
    private String url;
    /**
     * 参数
     */
    private String note;
    /**
     * 标记是否有网络
     */
    private int flag;
    /**
     * 加载动画
     */
    private   Animation operatingAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("source");
        note = getIntent().getStringExtra("note");
        if (G.isEmteny(url)) {
            return;
        }
        initWebView();
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initWebView() {
         operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_evey);
        if (!G.isNetworkConnected(this)) {
            // 清缓存
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            openmyoptiondialog();
            flag = 0;
        } else {
            flag = 1;
        }
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (flag==1){
                    if (progress == 100 ) {
                        bButLoadingview.setVisibility(View.GONE);
                        bButLoadingview.clearAnimation();
                    }else {
                        bButLoadingview.setVisibility(View.VISIBLE);
                        bButLoadingview.startAnimation(operatingAnim);
                    }
                }
            }
        });
    }

    /**
     * 弹出网速过慢的提示框,需要手动关闭  。。网速过慢
     */
    private void openmyoptiondialog() {
        new AlertDialog.Builder(this)
                .setTitle("网络问题")
                .setMessage("网络连接断开")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        System.exit(0);
                    }
                }).show();
    }

    @Override
    // 设置回退
    public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
        // TODO Auto-generated method stub
        Log.i("HD", "-------lu---->>>" + webView.canGoBack());
        if (KeyEvent.KEYCODE_BACK == keyCode && webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
