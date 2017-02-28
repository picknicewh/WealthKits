package com.cfjn.javacf.activity.member;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.PackageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 关于财富锦囊
 * 版本说明：代码规范整改
 * 附加注释：点击调用系统拨打电话
 * 主要接口：暂无
 */

public class AboutActivity extends Activity {
    /**
     * 版本号
     */
    @Bind(R.id.tv_app_version)
    TextView tvAppVersion;
    /**
     * 网址
     */
    @Bind(R.id.tv_web_site)
    TextView tvWebSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_wealth);
        ButterKnife.bind(this);
        tvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        //获取软件版本号---设置版本号
        tvAppVersion.setText("V" + PackageUtils.getVersionName(AboutActivity.this));
    }

    @OnClick({R.id.ib_back, R.id.tv_phoneNumber, R.id.tv_web_site})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_phoneNumber:
                Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("ic_tel:400-688-3506"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                startActivity(in);
                break;
            case R.id.tv_web_site:
                try {
                    Uri uri = Uri.parse("http://" + tvWebSite.getText().toString());
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                } catch (Exception e) {
                    G.log(e);
                    G.showToast(AboutActivity.this, "请您安装浏览器后才可以访问");
                }
                break;
        }
    }
}
