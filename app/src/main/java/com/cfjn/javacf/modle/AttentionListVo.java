package com.cfjn.javacf.modle;

/**
 * Created by DELL on 2015/12/29.
 */
public class AttentionListVo  {
    private String id;
    private String createDate;
    private String secretKey;
    private String fundCode;
    private String fundName;
    private String fundType;
    private double duringTheGains;
    private double sameTypeAVG;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public double getDuringTheGains() {
        return duringTheGains;
    }

    public void setDuringTheGains(double duringTheGains) {
        this.duringTheGains = duringTheGains;
    }

    public double getSameTypeAVG() {
        return sameTypeAVG;
    }

    public void setSameTypeAVG(double sameTypeAVG) {
        this.sameTypeAVG = sameTypeAVG;
    }
}
