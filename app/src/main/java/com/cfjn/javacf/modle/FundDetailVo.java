package com.cfjn.javacf.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FundDetailVo extends MenberCent implements Serializable{
    private List<FundDetailListVo> date;

    public List<FundDetailListVo> getDate() {
        return date;
    }

    public void setDate(List<FundDetailListVo> date) {
        this.date = date;
    }
}
