package com.cfjn.javacf.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cfjn.javacf.R;


/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：记账---设置预算的对话框
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public class SetBudgetDialog extends Dialog{

    Context context;

    private String tital_text;
    private String ed_text;
    private  String hint;
    private TextView tital;
    private EditText dialog_ed;
    private Button dialog_colse;
    private Button dialog_affirm;

    private OnCustomDialogListener customDialogListener;

    public interface OnCustomDialogListener{
        public void back(String ed_text);
    }

    public SetBudgetDialog(Context context, int themeResId, String tital_text, String hint, OnCustomDialogListener customDialogListener) {
        super(context, themeResId);
        this.context=context;
        this.tital_text=tital_text;
        this.hint = hint;
        this.customDialogListener = customDialogListener;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ys);
        tital= (TextView) findViewById(R.id.title_bar);
        dialog_ed= (EditText) findViewById(R.id.dialog_ed);
        dialog_colse= (Button) findViewById(R.id.dialog_close);
        dialog_affirm= (Button) findViewById(R.id.dialog_affirm);
        dialog_affirm.setOnClickListener(clickListener);
        dialog_ed.setHint(hint);
        tital.setText(tital_text);
        dialog_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetBudgetDialog.this.dismiss();
            }
        });
        if (hint=="请输入预算金额"){
              constructnume();
        }
    }
    //限制对话框的输入的值只能是数字，并且六位
    private  void constructnume(){
        dialog_ed.setKeyListener(new NumberKeyListener() {
            protected char[] getAcceptedChars() {
                return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
            }

            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }
        });
        //设置最大长度为6
        dialog_ed.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            customDialogListener.back(String.valueOf(dialog_ed.getText()));
           // MyDialog.this.dismiss();
        }
    };
}
