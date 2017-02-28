package com.cfjn.javacf.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.activity.bookkeeping.BookkeepingActivity;
import com.cfjn.javacf.activity.bookkeeping.ChartDetailActivity;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.UserAccountBook;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.OKHttpListener;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：记账---编辑语音记账删除弹窗
 * 版本说明：
 * 附加注释：
 * 主要接口：提交记账
 */
public class DeleteVoiceAccountPopup extends BookkeepingCommonPopWindow implements OKHttpListener,View.OnClickListener {

    private List<List<Map<String, Object>>> childArray;
    private ChartDetailActivity activity;
    private XStream xstream = new XStream(new DomDriver());
    /**
     *父位置
     */
    private  int groupPosition;
    /**
     *子位置
     */
    private int childPosition;
    private  BookkeepingActivity bookkeepingActivity;
    private boolean isdetailat;
    public DeleteVoiceAccountPopup(BookkeepingActivity bookkeepingActivity, ChartDetailActivity activity, Context context, final List<List<Map<String, Object>>> childArray, final int groupPosition, final int childPosition, boolean isdetailat){
        this.context = context;
        this.childArray = childArray;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        this.activity =activity;
        this.isdetailat = isdetailat;
        this.bookkeepingActivity =bookkeepingActivity;
        inin();
    }
    private String getDeleteXml() {
        xstream.alias("UserAccountBook", UserAccountBook.class);
        UserAccountBook userAccountBook = new UserAccountBook();
        userAccountBook.setLoginName(new UserInfo(context).getLoginName());
        Log.i("qq","groupPosition:"+groupPosition+"childPosition"+childPosition);
        userAccountBook.setId((String) childArray.get(groupPosition).get(childPosition).get("id"));
        userAccountBook.setSubmitType(3);
        String result = xstream.toXML(userAccountBook);
        return result;
    }
    @Override
    public void onSuccess(final String uri, String result) {
        try {
           final  String result2 = URLDecoder.decode(result, "utf-8");
            if (activity!=null ){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (uri.equals(ApiUri.SUBMIT_MESSAGE)) {
                            xstream.alias("UserAccountBook", UserAccountBook.class);
                            UserAccountBook userAccountBook = (UserAccountBook) xstream.fromXML(result2);
                            if (userAccountBook.getId().equals("0")) {
                                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                                if (isdetailat) {
                                    activity.rushData();
                                } else {
                                    bookkeepingActivity.getContentManger();
                                }
                                dismiss();
                            }
                        }
                    }
                });
            }else if(bookkeepingActivity!=null){
                bookkeepingActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (uri.equals(ApiUri.SUBMIT_MESSAGE)) {
                            xstream.alias("UserAccountBook", UserAccountBook.class);
                            UserAccountBook userAccountBook = (UserAccountBook) xstream.fromXML(result2);
                            if (userAccountBook.getId().equals("0")) {
                                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                                if (isdetailat){
                                    activity.rushData();
                                }
                                else {
                                    bookkeepingActivity.getContentManger();
                                }
                                dismiss();
                            }
                        }
                    }
                });
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String uri, final String error) {
        if (activity!=null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            });
        }else if (bookkeepingActivity!=null){
            bookkeepingActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_close:
                dismiss();
                break;
            case R.id.pop_delete:
                submitMessage(getDeleteXml());
                break;
        }
    }
}
