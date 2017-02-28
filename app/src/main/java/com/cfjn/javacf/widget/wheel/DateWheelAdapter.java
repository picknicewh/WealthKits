package com.cfjn.javacf.widget.wheel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DateWheelAdapter extends NumericWheelAdapter{
	
	private DecimalFormat format;
	private String extraStr;
	public DateWheelAdapter(int minValue,int maxValue,String extraStr){
		super(minValue, maxValue);
		this.format = new DecimalFormat("##00");
		this.extraStr = extraStr;
	}
	
	 @Override
	 public String getItem(int index) {
	    if (index >= 0 && index < getItemsCount()) {
	        value = minValue + index;
	        return format.format(value) + extraStr;
	    }
	    return null;
	 }
	 
	 @Override
	    public int getMaximumLength() {
		return super.getMaximumLength()+extraStr.length();
	 }

}
