package com.cfjn.javacf.modle;

/**
 * Created by Administrator on 2016/1/25.
 */
public class AnalysisListVo {
    /**
     * 基金名称
     */
    private String fundName;
    /**
     * 市值
     */
    private String percentage;

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
