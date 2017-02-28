package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FundAssetDetailListVo implements Serializable {
    /**
     * 基金名称
     */
    private String fundName;
    /**
     * 金额
     */
    private String money;
    /**
     * 交易类别 1 转入 2 转出
     */
    private String transactionCategories;
    /**
     * 成功或失败  1 成功 2 失败
     */
    private String successOrFailure;
    /**
     * 是否为确认过程中  0=确认中，1=确认完毕
     */
    private String confirmTheProcess;
    /**
     * 交易状态说明
     */
    private String tradingStatusDescription;

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTransactionCategories() {
        return transactionCategories;
    }

    public void setTransactionCategories(String transactionCategories) {
        this.transactionCategories = transactionCategories;
    }

    public String getSuccessOrFailure() {
        return successOrFailure;
    }

    public void setSuccessOrFailure(String successOrFailure) {
        this.successOrFailure = successOrFailure;
    }

    public String getConfirmTheProcess() {
        return confirmTheProcess;
    }

    public void setConfirmTheProcess(String confirmTheProcess) {
        this.confirmTheProcess = confirmTheProcess;
    }

    public String getTradingStatusDescription() {
        return tradingStatusDescription;
    }

    public void setTradingStatusDescription(String tradingStatusDescription) {
        this.tradingStatusDescription = tradingStatusDescription;
    }
}
