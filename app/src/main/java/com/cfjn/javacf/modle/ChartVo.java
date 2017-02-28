package com.cfjn.javacf.modle;

/**
 * Created by Administrator on 2015/12/7.
 */
public class ChartVo {
    private float value;
    private String date;
    private float moreValue;

    public ChartVo(String date,float value) {
        this.value = value;
        this.date = date;
    }

    public ChartVo(String date, float value,float moreValue) {
        this.date = date;
        this.value = value;
        this.moreValue=moreValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getMoreValue() {
        return moreValue;
    }

    public void setMoreValue(float moreValue) {
        this.moreValue = moreValue;
    }
}
