package com.cfjn.javacf.modle;


import java.util.List;

/**
 * Created by DELL on 2016/1/13.
 */
public class AccountVo extends MenberCent {
    private List<AccountManagementVo>date;

    public List<AccountManagementVo> getDate() {
        return date;
    }

    public void setDate(List<AccountManagementVo> date) {
        this.date = date;
    }
}
