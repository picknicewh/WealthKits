package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2016/1/26.
 */
public class AssetanalysisVo extends MenberCent{

    public List<CurveInfo> getDate() {
        return date;
    }

    public void setDate(List<CurveInfo> date) {
        this.date = date;
    }

    private List<CurveInfo> date;
}
