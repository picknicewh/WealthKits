package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 */
public class CurveVo {
    private List<CurveInfo>  curveInfo;
    private  String fundCode;
    private  String fundType;

    public List<CurveInfo> getCurveInfo() {
        return curveInfo;
    }

    public void setCurveInfo(List<CurveInfo> curveInfo) {
        this.curveInfo = curveInfo;
    }

    private  String investmentType;


    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }
}
