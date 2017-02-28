package com.cfjn.javacf.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 */
public class MyAnalysisVo extends MenberCent implements Serializable {

    private AnalysisVo date;

    public AnalysisVo getDate() {
        return date;
    }

    public void setDate(AnalysisVo date) {
        this.date = date;
    }

    public static class AnalysisVo{
        /**
         * 货币型
         */
        private double currency;
        /**
         * 非货币
         */
        private double notCurrency;

        private List<MyAnalysisListVo> list;

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

        public List<MyAnalysisListVo> getList() {
            return list;
        }

        public void setList(List<MyAnalysisListVo> list) {
            this.list = list;
        }
    }

}
