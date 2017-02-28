package com.cfjn.javacf.modle;

/**
 * Created by DELL on 2016/1/12.
 */
public class FundCodeVo extends MenberCent {

    /**
     * fundBaseInfo : {"id":"b2bc33c61ecb49e8b8778fa6c9d67c88","createDate":"2016-01-07 19:48:10","fundCode":"519879","fundName":"国寿货B","fundNameAbbr":"国寿场内实时申赎货币B","currDate":"2016-01-06","fundType":7,"investmentType":4,"netValue":1,"totalNetValue":0,"percents":0,"thousandsOfIncome":0.5451,"yieldSevenDay":0.0533,"yield1M":0.00296644,"yield3M":0.00855376,"yield6M":0.01568456,"yield12M":0.03899949,"yieldThisYear":9.1811E-4,"cgs3Year":null,"lastestTotalAsset":3.45072492264E9,"onSale":0,"riskLevel":null,"shareType":null,"purchaseState":0,"subscribeState":0,"aipState":0,"chargeRateValue":null,"discount":null,"saleChargeRateValue":null,"isMonetary":1,"isStf":0,"purchaseLimitMin":null,"rapidRedeem":0,"iaGuid":"1951187e-c2ab-4541-92f6-152e1f1d0811","fundManagementFees":"0.2%","fundInitials":null,"source":null}
     * purchasers : 4689
     * financialPeriod :
     * attention : true
     */

    private DateEntity date;

    public void setDate(DateEntity date) {
        this.date = date;
    }

    public DateEntity getDate() {
        return date;
    }

    public static class DateEntity {

        private FundBaseInfo fundBaseInfo;
        private int purchasers;
        private String financialPeriod;
        private boolean attention;

        public void setFundBaseInfo(FundBaseInfo fundBaseInfo) {
            this.fundBaseInfo = fundBaseInfo;
        }

        public void setPurchasers(int purchasers) {
            this.purchasers = purchasers;
        }

        public void setFinancialPeriod(String financialPeriod) {
            this.financialPeriod = financialPeriod;
        }

        public void setAttention(boolean attention) {
            this.attention = attention;
        }

        public FundBaseInfo getFundBaseInfo() {
            return fundBaseInfo;
        }

        public int getPurchasers() {
            return purchasers;
        }

        public String getFinancialPeriod() {
            return financialPeriod;
        }

        public boolean isAttention() {
            return attention;
        }

    }
}
