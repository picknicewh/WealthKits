package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by DELL on 2015/12/30.
 */
public class AuthMenuVo {
    private String operatorCode;
    private String attributionCode;
    private String authMenuAble;
    private String longAble;
    private List<ValueAddedBaseInfo> valueAddedBaseInfoList;
    private String userBuy;
    private String authMenuType;
    private String authMenuName;
    private String authMenuCode;

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getAttributionCode() {
        return attributionCode;
    }

    public void setAttributionCode(String attributionCode) {
        this.attributionCode = attributionCode;
    }

    public String getAuthMenuAble() {
        return authMenuAble;
    }

    public void setAuthMenuAble(String authMenuAble) {
        this.authMenuAble = authMenuAble;
    }

    public String getLongAble() {
        return longAble;
    }

    public void setLongAble(String longAble) {
        this.longAble = longAble;
    }

    public List<ValueAddedBaseInfo> getValueAddedBaseInfoList() {
        return valueAddedBaseInfoList;
    }

    public void setValueAddedBaseInfoList(List<ValueAddedBaseInfo> valueAddedBaseInfoList) {
        this.valueAddedBaseInfoList = valueAddedBaseInfoList;
    }

    public String getUserBuy() {
        return userBuy;
    }

    public void setUserBuy(String userBuy) {
        this.userBuy = userBuy;
    }

    public String getAuthMenuType() {
        return authMenuType;
    }

    public void setAuthMenuType(String authMenuType) {
        this.authMenuType = authMenuType;
    }

    public String getAuthMenuName() {
        return authMenuName;
    }

    public void setAuthMenuName(String authMenuName) {
        this.authMenuName = authMenuName;
    }

    public String getAuthMenuCode() {
        return authMenuCode;
    }

    public void setAuthMenuCode(String authMenuCode) {
        this.authMenuCode = authMenuCode;
    }
}
