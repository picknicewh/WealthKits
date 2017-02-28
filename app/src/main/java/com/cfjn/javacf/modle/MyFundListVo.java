package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/12.
 */
public class MyFundListVo extends MenberCent implements Serializable{

    public MyFundVo getDate() {
        return date;
    }

    public void setDate(MyFundVo date) {
        this.date = date;
    }

    private MyFundVo date;
}
