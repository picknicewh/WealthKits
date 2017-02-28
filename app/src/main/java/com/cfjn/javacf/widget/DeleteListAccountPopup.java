package com.cfjn.javacf.widget;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.bookkeeping.VoiceAccountActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.UserAccountBook;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.OKHttpListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * 作者： wh
 * 时间： 2016-6-29
 * 名称： 记账的删除对话框
 * 版本说明：
 * 附加注释：
 * 主要接口：记账--提交修改账单
 */
public class DeleteListAccountPopup extends BookkeepingCommonPopWindow implements OKHttpListener {

     private  String id;
     private VoiceAccountActivity accountActivity;
     public DeleteListAccountPopup(VoiceAccountActivity accountActivity, Context context, final String id) {
        this.id =id;
        this.context =context;
        this.accountActivity = accountActivity;
            inin();
        }

    public String getDeleteXml(String id) {
        xstream.alias("UserAccountBook", UserAccountBook.class);
        UserAccountBook userAccountBook = new UserAccountBook();
        userAccountBook.setId(id);
        userAccountBook.setLoginName(new UserInfo(accountActivity).getLoginName());
        userAccountBook.setSubmitType(3);
        String result = xstream.toXML(userAccountBook);
        return result;
    }
    @Override
    public void onSuccess(final String uri, String result) {
        try {
           final String result2 = URLDecoder.decode(result,"utf-8");
            accountActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (uri.equals(ApiUri.SUBMIT_MESSAGE)) {
                        xstream.alias("UserAccountBook", UserAccountBook.class);
                        UserAccountBook userAccountBook = (UserAccountBook) xstream.fromXML(result2);
                        if (userAccountBook.getId().equals("0")) {
                            Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        }

                        //提交后关闭界面，返回主页
                        accountActivity.finish();
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String uri, final String error) {
        accountActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_close:
                dismiss();
                break;
            case R.id.pop_delete:
                submitMessage(getDeleteXml(id));
                break;
        }
    }
}