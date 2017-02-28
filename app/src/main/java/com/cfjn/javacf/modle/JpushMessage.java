package com.cfjn.javacf.modle;

/**
 * 作者： wh
 * 时间： 2016/6/30
 * 名称：极光推送的数据
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class JpushMessage {

    /**
     * type : 传递过来跳转的类型 1=远程WEB，2=本地WEB，3=原生页面
     * url : 传递的url
     * note :备注
     */

    private int type;
    private String url;
    private String note;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
