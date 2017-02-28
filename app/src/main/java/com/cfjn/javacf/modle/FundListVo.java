package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/17.
 */
public class FundListVo implements Serializable{

    private FundBaseInfo fundBaseInfo;
    private Boolean attention;

    public Boolean getAttention() {
        return attention;
    }

    public void setAttention(Boolean attention) {
        this.attention = attention;
    }

    public FundBaseInfo getFundBaseInfo() {
        return fundBaseInfo;
    }

    public void setFundBaseInfo(FundBaseInfo fundBaseInfo) {
        this.fundBaseInfo = fundBaseInfo;
    }

    public void setPurchasers(String purchasers) {
        this.purchasers = purchasers;
    }

    public void setFinancialPeriod(String financialPeriod) {
        this.financialPeriod = financialPeriod;
    }


    public String getPurchasers() {
        return purchasers;
    }

    public String getFinancialPeriod() {
        return financialPeriod;
    }

    private String purchasers;
    private String financialPeriod;
}
