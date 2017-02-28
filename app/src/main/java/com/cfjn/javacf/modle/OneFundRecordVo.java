package com.cfjn.javacf.modle;

/**
 * Created by Administrator on 2016/1/22.
 */
public class OneFundRecordVo {
    private String secretKey;
    /**
     * 基金代码
     */
    private String fundCode;
    /**
     * 收益日期
     */
    private String incomes_date;
    /**
     * 收益
     */
    private String incomes;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIncomes() {
        return incomes;
    }

    public void setIncomes(String incomes) {
        this.incomes = incomes;
    }

    public String getIncomes_date() {
        return incomes_date;
    }

    public void setIncomes_date(String incomes_date) {
        this.incomes_date = incomes_date;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 来源
     */
    private String source;
}
