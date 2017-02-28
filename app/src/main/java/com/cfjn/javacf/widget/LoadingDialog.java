package com.cfjn.javacf.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfjn.javacf.activity.MainActivity;
import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.activity.LoginActivity;
import com.openhunme.cordova.activity.HMDroidGap;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 加载提示框 或者非法访问
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class LoadingDialog extends Dialog{

    //type=0,加载dialog
    private ImageView imageView;
    private Animation operatingAnim;
    public TextView titleText;

    //type=1,重新登录dialog
    private Button btnQx;
    private Button btnRestart;
    private Context context;
    private int type;

    public LoadingDialog(final Activity context,int theme,int type) {
        super(context, theme);
        this.context=context;
        this.type=type;
        this.setCanceledOnTouchOutside(false);
        if(type==0){
            this.getWindow().setContentView(R.layout.loading);
            imageView = (ImageView) findViewById(R.id.loading_dialog);
            titleText = (TextView) findViewById(R.id.title_text);
            operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_evey);
            imageView.startAnimation(operatingAnim);
        }else if(type==1){
            //非法访问弹出框
            this.getWindow().setContentView(R.layout.dialog_relogin);
            btnQx = (Button) findViewById(R.id.btn_qx);
            btnRestart = (Button) findViewById(R.id.btn_restart);

            btnQx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    dismiss();
                    HMDroidGap.synCookies(context);
                    context.finish();
                }
            });

            btnRestart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, LoginActivity.class);
                    intent.putExtra("value", -1);
                    context.startActivity(intent);
                    dismiss();
                    HMDroidGap.synCookies(context);
                    context.finish();
                }
            });

            btnQx.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        v.setBackgroundResource(R.drawable.dialog_qx);
                    }else if(event.getAction()==MotionEvent.ACTION_UP){
                        v.setBackgroundResource(R.color.transparent);
                    }
                    return false;
                }
            });
            btnRestart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        v.setBackgroundResource(R.drawable.dialog_re);
                    }else if(event.getAction()==MotionEvent.ACTION_UP){
                        v.setBackgroundResource(R.color.transparent);
                    }
                    return false;
                }
            });
        }
    }

    public void dismiss(){
        super.dismiss();
        if(type==1){
            UserInfo.getInstance(context).clean();
        }
    }


    /**
     * 将dialog进行封装
     */
    private static LoadingDialog dialog;

    /**
     * 初始化
     * @param activity
     */
    public static LoadingDialog regstLoading(Activity activity){
        dialog= new LoadingDialog(activity,R.style.LoadingDialogTheme, 0);
        dialog.titleText.setText("加载中，请稍后...");
        return dialog;
    }

    public static  LoadingDialog  regstLoading(Activity activity,String values){
        dialog= new LoadingDialog(activity,R.style.myDialogTheme, 0);
        dialog.titleText.setText(values);
        return dialog;
    }

}
