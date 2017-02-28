package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by DELL on 2015/12/15.
 */
public class MenberCent implements Serializable{

    private String sign;

    private String resultCode;

    private String resultDesc;

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    private String createTime;

    public void setSign(String sign) {
        this.sign = sign;
    }


    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }


    public String getSign() {
        return sign;
    }


    public String getResultDesc() {
        return resultDesc;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getCreateTime() {
        return createTime;
    }
}
