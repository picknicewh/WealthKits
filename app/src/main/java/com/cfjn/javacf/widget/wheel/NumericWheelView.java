package com.cfjn.javacf.widget.wheel;

import android.content.Context;
import android.util.AttributeSet;


/**
 * Numeric Wheel adapter.
 */
public class NumericWheelView extends WheelView {
	
	private NumericWheelAdapter numAdapter;
	
	/**
     * Constructor
     */
    public NumericWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCyclic(true);
    }

    /**
     * Constructor
     */
    public NumericWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCyclic(true);
    }

    /**
     * Constructor
     */
    public NumericWheelView(Context context) {
        super(context);
        setCyclic(true);
    }

	
    protected void notifyChangingListeners(int oldValue, int newValue) {
    	numAdapter = (NumericWheelAdapter)adapter;
    	int minValue = numAdapter.getMinValue();
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue+minValue, newValue+minValue);
        }
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     * 
     * @param index
     *            the item index
     */
    public void setCurrentValue(int index,boolean isAnim) {
    	numAdapter = (NumericWheelAdapter)adapter;
    	int minValue = numAdapter.getMinValue();
    	//直接设置，没有动画 
        setCurrentItem(index-minValue, false);
    }
    
    public void setCurrentValue(int index){
    	setCurrentValue(index, false);
    }
    
    public int getCurrentValue(){
    	numAdapter = (NumericWheelAdapter)adapter;
    	int minValue = numAdapter.getMinValue();
    	return getCurrentItem()+minValue;
    }
}
