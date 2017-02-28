package com.cfjn.javacf.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class FundVo implements Serializable{

    private List<FundListVo> fundProductList;
    /**
     * 基金类型
     */
    private int fundType;
    /**
     * 投资类型
     */
    private int investmentType;
    /**
     * 基金类型名称
     */
    private String fundTypeName;
    private String content;

    public List<FundListVo> getFundProductList() {
        return fundProductList;
    }

    public void setFundProductList(List<FundListVo> fundProductList) {
        this.fundProductList = fundProductList;
    }

    public int getFundType() {
        return fundType;
    }

    public void setFundType(int fundType) {
        this.fundType = fundType;
    }

    public int getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(int investmentType) {
        this.investmentType = investmentType;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
