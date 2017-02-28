package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/17.
 */
public class FundBaseInfo implements Serializable {

    /**
     * 数米使用的基金代码
     */
    private String fundCode;
    /**
     * 基金代码
     */
    private String aliasCode;
    /**
     * 基金名称（短）
     */
    private String fundName;
    /**
     * 基金名称（长）
     */
    private String fundNameAbbr;
    /**
     * 净值日期
     */
    private String currDate;
    /**
     * 基金类型
     */
    private Integer fundType;
    /**
     * 基金投资类型
     */
    private Integer investmentType;
    /**
     * 单位净值
     */
    private Double netValue;
    /**
     * 累计净值
     */
    private Double totalNetValue;
    /**
     * 当日涨幅
     */
    private Double percents;
    /**
     * 万份收益
     */
    private Double thousandsOfIncome;
    /**
     * 七日年化
     */
    //private Double percentSevenDays;
    private Double yieldSevenDay;
    /**
     * 1个月收益
     */
    private Double yield1M;
    /**
     * 3个月收益
     */
    private Double yield3M;
    /**
     * 6个月收益
     */
    private Double yield6M;
    /**
     * 12个月收益
     */
    private Double yield12M;
    /**
     * 今年以来收益
     */
    private Double yieldThisYear;
    /**
     * 银河3年评级
     */
    private Double cgs3Year;
    /**
     * 最新资产
     */
    private Double lastestTotalAsset;
    /**
     * 是否是数米代销
     */
    private Integer onSale;
    /**
     * 基金风险等级
     */
    private String riskLevel;
    /**
     * 收费方式
     */
    private String shareType;
    /**
     * 是否可申购
     */
    private Integer purchaseState;
    /**
     * 是否可认购
     */
    private Integer subscribeState;
    /**
     * 是否可定投
     */
    private Integer aipState;
    /**
     * 原始费率
     */
    private Double chargeRateValue;
    /**
     * 费率折扣
     */
    private Double discount;
    /**
     * 销售费率
     */
    private Double saleChargeRateValue;
    /**
     * 是否是货币基金
     */
    private Integer isMonetary;
    /**
     * 是否是短期理财基金
     */
    private Integer isStf;
    /**
     * 起购金额
     */
    private Double purchaseLimitMin;

    public String getProdSource() {
        return prodSource;
    }

    public void setProdSource(String prodSource) {
        this.prodSource = prodSource;
    }

    /**
     * 天风来源
     */
    private String prodSource;
    private Double sgPurchaseLimitMin;

    public Double getSgPurchaseLimitMin() {
        return sgPurchaseLimitMin;
    }

    public void setSgPurchaseLimitMin(Double sgPurchaseLimitMin) {
        this.sgPurchaseLimitMin = sgPurchaseLimitMin;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setAliasCode(String aliasCode) {
        this.aliasCode = aliasCode;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public void setFundNameAbbr(String fundNameAbbr) {
        this.fundNameAbbr = fundNameAbbr;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public void setFundType(Integer fundType) {
        this.fundType = fundType;
    }

    public void setInvestmentType(Integer investmentType) {
        this.investmentType = investmentType;
    }

    public void setNetValue(Double netValue) {
        this.netValue = netValue;
    }

    public void setTotalNetValue(Double totalNetValue) {
        this.totalNetValue = totalNetValue;
    }

    public void setPercents(Double percents) {
        this.percents = percents;
    }

    public void setThousandsOfIncome(Double thousandsOfIncome) {
        this.thousandsOfIncome = thousandsOfIncome;
    }

    public void setYieldSevenDay(Double yieldSevenDay) {
        this.yieldSevenDay = yieldSevenDay;
    }

    public void setYield1M(Double yield1M) {
        this.yield1M = yield1M;
    }

    public void setYield3M(Double yield3M) {
        this.yield3M = yield3M;
    }

    public void setYield6M(Double yield6M) {
        this.yield6M = yield6M;
    }

    public void setYield12M(Double yield12M) {
        this.yield12M = yield12M;
    }

    public void setYieldThisYear(Double yieldThisYear) {
        this.yieldThisYear = yieldThisYear;
    }

    public void setCgs3Year(Double cgs3Year) {
        this.cgs3Year = cgs3Year;
    }

    public void setLastestTotalAsset(Double lastestTotalAsset) {
        this.lastestTotalAsset = lastestTotalAsset;
    }

    public void setOnSale(Integer onSale) {
        this.onSale = onSale;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public void setPurchaseState(Integer purchaseState) {
        this.purchaseState = purchaseState;
    }

    public void setSubscribeState(Integer subscribeState) {
        this.subscribeState = subscribeState;
    }

    public void setAipState(Integer aipState) {
        this.aipState = aipState;
    }

    public void setChargeRateValue(Double chargeRateValue) {
        this.chargeRateValue = chargeRateValue;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setSaleChargeRateValue(Double saleChargeRateValue) {
        this.saleChargeRateValue = saleChargeRateValue;
    }

    public void setIsMonetary(Integer isMonetary) {
        this.isMonetary = isMonetary;
    }

    public void setIsStf(Integer isStf) {
        this.isStf = isStf;
    }

    public void setPurchaseLimitMin(Double purchaseLimitMin) {
        this.purchaseLimitMin = purchaseLimitMin;
    }

    public void setRapidRedeem(Integer rapidRedeem) {
        this.rapidRedeem = rapidRedeem;
    }

    public void setIaGuid(String iaGuid) {
        this.iaGuid = iaGuid;
    }

    public void setFundManagementFees(String fundManagementFees) {
        this.fundManagementFees = fundManagementFees;
    }

    public void setFundInitials(String fundInitials) {
        this.fundInitials = fundInitials;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getAliasCode() {
        return aliasCode;
    }

    public String getFundName() {
        return fundName;
    }

    public String getFundNameAbbr() {
        return fundNameAbbr;
    }

    public String getCurrDate() {
        return currDate;
    }

    public Integer getFundType() {
        return fundType;
    }

    public Integer getInvestmentType() {
        return investmentType;
    }

    public Double getNetValue() {
        return netValue;
    }

    public Double getTotalNetValue() {
        return totalNetValue;
    }

    public Double getPercents() {
        return percents;
    }

    public Double getThousandsOfIncome() {
        return thousandsOfIncome;
    }

    public Double getYieldSevenDay() {
        return yieldSevenDay;
    }

    public Double getYield1M() {
        return yield1M;
    }

    public Double getYield3M() {
        return yield3M;
    }

    public Double getYield6M() {
        return yield6M;
    }

    public Double getYield12M() {
        return yield12M;
    }

    public Double getYieldThisYear() {
        return yieldThisYear;
    }

    public Double getCgs3Year() {
        return cgs3Year;
    }

    public Double getLastestTotalAsset() {
        return lastestTotalAsset;
    }

    public Integer getOnSale() {
        return onSale;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getShareType() {
        return shareType;
    }

    public Integer getPurchaseState() {
        return purchaseState;
    }

    public Integer getSubscribeState() {
        return subscribeState;
    }

    public Integer getAipState() {
        return aipState;
    }

    public Double getChargeRateValue() {
        return chargeRateValue;
    }

    public Double getDiscount() {
        return discount;
    }

    public Double getSaleChargeRateValue() {
        return saleChargeRateValue;
    }

    public Integer getIsMonetary() {
        return isMonetary;
    }

    public Integer getIsStf() {
        return isStf;
    }

    public Double getPurchaseLimitMin() {
        return purchaseLimitMin;
    }

    public Integer getRapidRedeem() {
        return rapidRedeem;
    }

    public String getIaGuid() {
        return iaGuid;
    }

    public String getFundManagementFees() {
        return fundManagementFees;
    }

    public String getFundInitials() {
        return fundInitials;
    }

    /**
     * 是否支持快速赎回（T+0）
     */

    private Integer rapidRedeem;
    /**
     * 基金公司GUID
     */
    private String iaGuid;
    /**
     * 基金公司名称
     */
    private String guidName;

    public String getGuidName() {
        return guidName;
    }

    public void setGuidName(String guidName) {
        this.guidName = guidName;
    }

    /**
     * 管理费率
     */
    private String fundManagementFees;
    /**
     * 基金大写首字母
     * @return
     */
    private String fundInitials;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 来源
     */
    private String source;
}
