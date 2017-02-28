package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 */
public class AnalysisVo {

    /**
     * 货币型
     */
    private double currency;
    /**
     * 非货币性
     */
    private double notCurrency;


    private List<AnalysisListVo> list;

    public double getCurrency() {
        return currency;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public double getNotCurrency() {
        return notCurrency;
    }

    public void setNotCurrency(double notCurrency) {
        this.notCurrency = notCurrency;
    }

    public List<AnalysisListVo> getList() {
        return list;
    }

    public void setList(List<AnalysisListVo> list) {
        this.list = list;
    }
}
