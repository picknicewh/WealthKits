package com.cfjn.javacf.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.HunmeApplication;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 资源包更新失败的时候退出程序提示框
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class SynchDateDialog extends Dialog {

    private TextView tvDialog;
    private Button btn_qx;
    private Button btn_restart;
    public SynchDateDialog(Context context, int themeResId,String valuse) {
        super(context, themeResId);
        this.getWindow().setContentView(R.layout.dialog_relogin);
        this.setCanceledOnTouchOutside(false);
        tvDialog = (TextView) findViewById(R.id.tv_dialog);
        btn_qx= (Button) findViewById(R.id.btn_qx);
        btn_restart= (Button) findViewById(R.id.btn_restart);
        btn_restart.setVisibility(View.GONE);
        btn_qx.setText("退出");
        tvDialog.setText(valuse);
        btn_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                HunmeApplication.exit();
            }
        });
    }

    public void dismiss(){
        super.dismiss();
    }

}
