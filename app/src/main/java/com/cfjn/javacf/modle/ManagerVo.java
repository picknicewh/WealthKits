package com.cfjn.javacf.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/5.
 */
public class ManagerVo implements Serializable {

    private int fundManagerId;
    private String fundCode;

    public int getFundManagerId() {
        return fundManagerId;
    }

    public void setFundManagerId(int fundManagerId) {
        this.fundManagerId = fundManagerId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getAccessionDate() {
        return accessionDate;
    }

    public void setAccessionDate(String accessionDate) {
        this.accessionDate = accessionDate;
    }

    public String getDimissionDate() {
        return dimissionDate;
    }

    public void setDimissionDate(String dimissionDate) {
        this.dimissionDate = dimissionDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    private String name;
    private String gender;
    private String birthday;
    private String educationLevel;
    private String accessionDate;
    private String dimissionDate;
    private String country;
    private String background;
}
