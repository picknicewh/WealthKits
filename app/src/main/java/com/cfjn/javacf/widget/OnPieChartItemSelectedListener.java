package com.cfjn.javacf.widget;

/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称： 饼图的点击事件接口
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public interface OnPieChartItemSelectedListener {
	
	  public  void onPieChartItemSelected(PieChartView paramPieChartView, int position, String type, float value, float percent);
	
}
