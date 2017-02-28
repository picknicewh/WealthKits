package com.cfjn.javacf.modle;

/**
 * Created by Administrator on 2016/1/21.
 */
public class CurveInfo {
    private  double value;
    private double value_add;
    private String date;

    public CurveInfo(String date,double value) {
        this.value = value;
        this.date = date;
    }

    public CurveInfo(String date, double value,double value_add) {
        this.date = date;
        this.value = value;
        this.value_add=value_add;
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue_add() {
        return value_add;
    }

    public void setValue_add(double value_add) {
        this.value_add = value_add;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
