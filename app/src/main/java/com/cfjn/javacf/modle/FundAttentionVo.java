package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by DELL on 2015/12/29.
 */
public class FundAttentionVo {
    private String fundType;
    private List<AttentionListVo>userAttentionList;

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public List<AttentionListVo> getUserAttentionList() {
        return userAttentionList;
    }

    public void setUserAttentionList(List<AttentionListVo> userAttentionList) {
        this.userAttentionList = userAttentionList;
    }
}
