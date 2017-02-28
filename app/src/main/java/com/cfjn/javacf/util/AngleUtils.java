package com.cfjn.javacf.util;


public class AngleUtils {
	
	public static int caculateAngle(float x, float y, int pointX, int pointY) {
		
		float dx = x-pointX;
		float dy = y-pointY;		
		int angle = (int) Math.toDegrees(Math.atan2(dy, dx));
//		// TODO Auto-generated method stub
//		if(dx > 0&&dy > 0){
//			return angle;
//		}else if(dx > 0&&dy < 0){
//			return angle;
//		}else if(dx > 0&&dy > 0){
//			return angle;
//		} 
		return angle;
	}
	public static int formatAngel(int angle){
		int formatedAngle = angle%360;
		if(formatedAngle<-180 ){
			return formatedAngle + 360;
		}
		if(formatedAngle > 180){
			return formatedAngle - 360;
		}
		return formatedAngle;
	}

}
