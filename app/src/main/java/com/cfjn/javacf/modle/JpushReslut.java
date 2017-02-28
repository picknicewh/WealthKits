package com.cfjn.javacf.modle;

/**
 * 作者： Administrator
 * 时间： 2016/6/30
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class JpushReslut {
    /**
     * sign : null
     * resultCode : 1
     * resultDesc : 用户未注册
     * createTime : 2016-06-30 11:51:14
     * date : null
     */

    private Object sign;
    private String resultCode;
    private String resultDesc;
    private String createTime;
    private Object date;

    public Object getSign() {
        return sign;
    }

    public void setSign(Object sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }
}
