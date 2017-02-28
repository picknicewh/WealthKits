package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/12.
 */
public class MyFundVo implements Serializable {

    private String yesterdayIncome;
    private String nationalIncomeOverRanking;
    private String totalAssets;
    private String accumulatedIncome;

    public String getUnpaidIncome() {
        return unpaidIncome;
    }

    public void setUnpaidIncome(String unpaidIncome) {
        this.unpaidIncome = unpaidIncome;
    }

    public String getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(String yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public String getNationalIncomeOverRanking() {
        return nationalIncomeOverRanking;
    }

    public void setNationalIncomeOverRanking(String nationalIncomeOverRanking) {
        this.nationalIncomeOverRanking = nationalIncomeOverRanking;
    }

    public String getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    public String getAccumulatedIncome() {
        return accumulatedIncome;
    }

    public void setAccumulatedIncome(String accumulatedIncome) {
        this.accumulatedIncome = accumulatedIncome;
    }

    private String unpaidIncome;

}
