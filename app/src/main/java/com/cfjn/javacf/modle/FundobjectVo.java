package com.cfjn.javacf.modle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/18.
 */
public class FundobjectVo extends MenberCent implements Serializable{

    private List<FundVo> date;

    public List<FundVo> getDate() {
        return date;
    }

    public void setDate(List<FundVo> date) {
        this.date = date;
    }
}
