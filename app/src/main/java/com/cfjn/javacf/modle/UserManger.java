package com.cfjn.javacf.modle;

/**
 * Created by DELL on 2015/12/14.
 */
public class UserManger {
    private String sign;
    private String resultCode;
    private String resultDesc;
    private String createTime;
    private String date;

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDetails(String details) {
        this.date = details;
    }

    public String getDetails() {
        return date;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getSign() {
        return sign;
    }
}
