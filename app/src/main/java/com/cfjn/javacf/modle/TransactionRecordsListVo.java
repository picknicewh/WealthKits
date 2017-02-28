package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 */
public class TransactionRecordsListVo extends MenberCent{

    private List<TransactionRecordsVo> date;

    public List<TransactionRecordsVo> getDate() {
        return date;
    }

    public void setDate(List<TransactionRecordsVo> date) {
        this.date = date;
    }
}
