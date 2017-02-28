package com.cfjn.javacf.util;
/**
 * 作者： zll
 * 时间： 2016-6-29
 * 名称： 饼图颜色选择
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class TitleValueColorEntity {
	public TitleValueColorEntity(String title, float value, int color) {
		super();
		this.title = title;
		this.value = value;
		this.color = color;
	}
	private String title;
	private float value;
	private int color;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	

}
