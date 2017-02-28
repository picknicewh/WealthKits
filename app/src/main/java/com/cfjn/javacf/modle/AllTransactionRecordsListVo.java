package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/2/18.
 */
public class AllTransactionRecordsListVo {

    private int type;

    public List<TransactionRecordsVo> getList() {
        return list;
    }

    public void setList(List<TransactionRecordsVo> list) {
        this.list = list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private List<TransactionRecordsVo> list;
}
