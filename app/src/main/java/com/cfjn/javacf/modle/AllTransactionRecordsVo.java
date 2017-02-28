package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class AllTransactionRecordsVo extends MenberCent{

    public List<AllTransactionRecordsListVo> getDate() {
        return date;
    }

    public void setDate(List<AllTransactionRecordsListVo> date) {
        this.date = date;
    }

    private List<AllTransactionRecordsListVo> date;
}
