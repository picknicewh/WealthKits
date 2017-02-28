package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/15.
 */
public class SGBuyOkVo extends MenberCent{

    private SgTransaction date;

    public SgTransaction getDate() {
        return date;
    }

    public void setDate(SgTransaction date) {
        this.date = date;
    }

    public static class SgTransaction implements Serializable {
        /**
         * 申请时间
         */
        private String applyDateTime;
        /**
         * 预计确认时间
         */
        private String expectedToConfirm;
        /**
         * 收益到账时间
         */
        private String returnToAccount;
        /**
         * 基金ID
         */
        private String fundCode;
        /**
         * 基金名称
         */
        private String fundName;
        /**
         * 业务类别
         */
        private String businessType;
        /**
         * 金额
         */
        private Double amount;
        /**
         * 份额
         */
        private Double shares;
        /**
         * 申请流水号
         */
        private String applySerial;
        /**
         * 支付状态
         */
        private Integer payResult;
        /**
         * 支付状态中文
         */
        private String payStatusToCN;
        /**
         * 确认状态
         */
        private Integer status;

        public Integer getStatus() {
            return status;
        }
        public void setStatus(Integer status) {
            this.status = status;
        }
        public String getApplyDateTime() {
            return applyDateTime;
        }
        public void setApplyDateTime(String applyDateTime) {
            this.applyDateTime = applyDateTime;
        }
        public String getExpectedToConfirm() {
            return expectedToConfirm;
        }
        public void setExpectedToConfirm(String expectedToConfirm) {
            this.expectedToConfirm = expectedToConfirm;
        }
        public String getReturnToAccount() {
            return returnToAccount;
        }
        public void setReturnToAccount(String returnToAccount) {
            returnToAccount = returnToAccount;
        }
        public String getFundCode() {
            return fundCode;
        }
        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }
        public String getFundName() {
            return fundName;
        }
        public void setFundName(String fundName) {
            this.fundName = fundName;
        }
        public String getBusinessType() {
            return businessType;
        }
        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }
        public Double getAmount() {
            return amount;
        }
        public void setAmount(Double amount) {
            this.amount = amount;
        }
        public Double getShares() {
            return shares;
        }
        public void setShares(Double shares) {
            this.shares = shares;
        }
        public String getApplySerial() {
            return applySerial;
        }
        public void setApplySerial(String applySerial) {
            this.applySerial = applySerial;
        }
        public Integer getPayResult() {
            return payResult;
        }
        public void setPayResult(Integer payResult) {
            this.payResult = payResult;
        }
        public String getPayStatusToCN() {
            return payStatusToCN;
        }
        public void setPayStatusToCN(String payStatusToCN) {
            this.payStatusToCN = payStatusToCN;
        }

    }

}
