package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/25.
 */
public class MyAnalysisListVo implements Serializable{

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
