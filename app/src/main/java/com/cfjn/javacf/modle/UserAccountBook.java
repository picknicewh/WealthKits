package com.cfjn.javacf.modle;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserAccountBook implements Comparable<UserAccountBook> {
    /**
     * 提交类型码，1添加，2修改3，删除
     */
    private int submitType;

    private String id;
    /**
     * 日期     例：2014-12-25 11:53
     */
    private String createDate;

    private String loginName;

    private String type;

    private String content;

    private double money;

    private String unit;

    private int classifyCode;

    private String week;

    private int keyWordNum;

    private String member;

    private String project;

    private String remark;

    private String dealer;

    private String accountType;

    private double budget;

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }


    public int getSubmitType() {
        return submitType;
    }

    public void setSubmitType(int submitType) {
        this.submitType = submitType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getClassifyCode() {
        return classifyCode;
    }

    public void setClassifyCode(int classifyCode) {
        this.classifyCode = classifyCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getKeyWordNum() {
        return keyWordNum;
    }

    public void setKeyWordNum(int keyWordNum) {
        this.keyWordNum = keyWordNum;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


    @Override
    public int compareTo(UserAccountBook another) {
        // TODO Auto-generated method stub
        //2014-12-25 11:53
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String anotherDate = another.getCreateDate();
        long millis = 0;
        long anotherMillis = 0;
        try {
            millis = sdf.parse(createDate).getTime();
            anotherMillis = sdf.parse(anotherDate).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (millis > anotherMillis) ? -1 : 1;
    }

}
