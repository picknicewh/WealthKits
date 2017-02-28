package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/3/3.
 */
public class UserDateAccount implements Comparable<UserDateAccount> {
    private String createDate;
    private double classifyMoney;

    public int getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(int classifyCode) {
        this.classifyCode = classifyCode;
    }

    private int classifyCode;
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    private List<UserAccountBook> userAccountBookList;


    public double getClassifyMoney() {
        return classifyMoney;
    }

    public void setClassifyMoney(double classifyMoney) {
        this.classifyMoney = classifyMoney;
    }

    public List<UserAccountBook> getUserAccountBookList() {
        return userAccountBookList;
    }

    public void setUserAccountBookList(List<UserAccountBook> userAccountBookList) {
        this.userAccountBookList = userAccountBookList;
    }

    @Override
    public int compareTo(UserDateAccount another) {
        int temp;
        String anotherDay =another.getCreateDate();
        temp = anotherDay.compareTo(createDate);
        return temp;
    }
}
