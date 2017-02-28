package com.cfjn.javacf.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FundDetailListVo implements Serializable{

    /**
     * 资产
     */
    private String assets;
    /**
     * 来源 0=数米 1=天风 2=松果
     */
    private String source;
    /**
     * 昨日收益
     */
    private String yesterdayIncome;
    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 基金类型
     */
    private String fundType;

    /**
     * 基金名称
     */
    private String fundName;
    /**
     * 申购赎回记录
     */
    private List<FundAssetDetailListVo> list;

    /**
     *七日年化/最新净值
     */
    private String yieldSevenDay;

    /**
     *万份收益/净值增长率
     */
    private String thousandsOfIncome;

    /**
     *天风基金来源
     */
    private String prodSource;

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

    /**
     * 基金账号
     */
    private String tradeAccount;

    public String getYieldSevenDay() {
        return yieldSevenDay;
    }

    public void setYieldSevenDay(String yieldSevenDay) {
        this.yieldSevenDay = yieldSevenDay;
    }

    public String getThousandsOfIncome() {
        return thousandsOfIncome;
    }

    public void setThousandsOfIncome(String thousandsOfIncome) {
        this.thousandsOfIncome = thousandsOfIncome;
    }

    /**
     * 未付收益/份额

     */
    private String unpaidIncome;

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getUnpaidIncome() {
        return unpaidIncome;
    }

    public void setUnpaidIncome(String unpaidIncome) {
        this.unpaidIncome = unpaidIncome;
    }

    /**
     * 累计收益
     */
    private String totalIncome;

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(String yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
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

    public List<FundAssetDetailListVo> getList() {
        return list;
    }

    public void setList(List<FundAssetDetailListVo> list) {
        this.list = list;
    }
}
