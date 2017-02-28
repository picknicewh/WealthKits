package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 */
public class OneBankCardVo extends MenberCent{

    public List<UserBankcardListVo> getDate() {
        return date;
    }

    public void setDate(List<UserBankcardListVo> date) {
        this.date = date;
    }

    private List<UserBankcardListVo> date;
}
