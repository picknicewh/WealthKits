package com.cfjn.javacf.modle;

/**
 * Created by Administrator on 2016/4/13.
 */
public class SGQueryBalanceVo {

    private String prodSource;

    private String tradeAccount;

    private String secretKey;

    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 父键ID
     */
    private String parentId;

    /**
     * 基金名称
     */
    private String fundName;

    /**
     * 日盈亏
     */
    private double todayMoney;

    /**
     * 市值
     */
    private double cityValue;

    /**
     * 已赎回金额
     */
    private double redeemedValue;

    /**
     * 成本
     */
    private double cost;

    /**
     * 总盈亏
     */
    private double totalRedeemProfitLost;

    /**
     * 持有份额
     */
    private double cityValueRatio;

    /**
     * 持仓盈亏
     */
    private double dayProfitLost;

    /**
     * 持仓盈亏率
     */
    private double dayProfitLostPercent;

    /**
     * 交易日日期
     */
    private String CurrDate;

    /**
     * 净值日期
     */
    private String netValueDay;

    /**
     * 净值/万份收益
     */
    private double netValue;

    /**
     * 已赎回份额
     */
    private double hadRedeemQuotient;

    /**
     * 来源
     */
    private String source;

    /**
     * 未结转收益
     */
    private String undis_incomes;

    /**
     * 7日年化收益率
     */
    private String day7_annual_rate;

    public String getProdSource() {
        return prodSource;
    }

    public void setProdSource(String prodSource) {
        this.prodSource = prodSource;
    }

    public String getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(String tradeAccount) {
        this.tradeAccount = tradeAccount;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public double getTodayMoney() {
        return todayMoney;
    }

    public void setTodayMoney(double todayMoney) {
        this.todayMoney = todayMoney;
    }

    public double getCityValue() {
        return cityValue;
    }

    public void setCityValue(double cityValue) {
        this.cityValue = cityValue;
    }

    public double getRedeemedValue() {
        return redeemedValue;
    }

    public void setRedeemedValue(double redeemedValue) {
        this.redeemedValue = redeemedValue;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTotalRedeemProfitLost() {
        return totalRedeemProfitLost;
    }

    public void setTotalRedeemProfitLost(double totalRedeemProfitLost) {
        this.totalRedeemProfitLost = totalRedeemProfitLost;
    }

    public double getCityValueRatio() {
        return cityValueRatio;
    }

    public void setCityValueRatio(double cityValueRatio) {
        this.cityValueRatio = cityValueRatio;
    }

    public double getDayProfitLost() {
        return dayProfitLost;
    }

    public void setDayProfitLost(double dayProfitLost) {
        this.dayProfitLost = dayProfitLost;
    }

    public double getDayProfitLostPercent() {
        return dayProfitLostPercent;
    }

    public void setDayProfitLostPercent(double dayProfitLostPercent) {
        this.dayProfitLostPercent = dayProfitLostPercent;
    }

    public String getCurrDate() {
        return CurrDate;
    }

    public void setCurrDate(String currDate) {
        CurrDate = currDate;
    }

    public String getNetValueDay() {
        return netValueDay;
    }

    public void setNetValueDay(String netValueDay) {
        this.netValueDay = netValueDay;
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    public double getHadRedeemQuotient() {
        return hadRedeemQuotient;
    }

    public void setHadRedeemQuotient(double hadRedeemQuotient) {
        this.hadRedeemQuotient = hadRedeemQuotient;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUndis_incomes() {
        return undis_incomes;
    }

    public void setUndis_incomes(String undis_incomes) {
        this.undis_incomes = undis_incomes;
    }

    public String getDay7_annual_rate() {
        return day7_annual_rate;
    }

    public void setDay7_annual_rate(String day7_annual_rate) {
        this.day7_annual_rate = day7_annual_rate;
    }
}
