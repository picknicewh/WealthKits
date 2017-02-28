package com.exidcard;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;



public final class ExIDCardReco {
	private static final String tag = "ExIDCardDecoder";
	
	public static final int mMaxStreamBuf = 4096;
		
	/////////////////////////////////////////////////////////////
	// NDK STUFF
	static {
		System.loadLibrary("exidcard");
	}
	/////////////////////////////////////////////////////////////
	//Results
	public byte []bResultBuf;
	public int    nResultLen;
	public int []rects;
	public boolean ok;
	public ExIDCardResult cardcode;
	//获取原始图片数据 预备
	private byte[] sourceData;
	public Bitmap  getIMG(){
		return BitmapFactory.decodeByteArray(bResultBuf, 0, bResultBuf.length);
	}
	
//	private Bitmap pic_bitmap;
	public Bitmap getPicBitmap(){
		return BitmapFactory.decodeByteArray(bResultBuf, 0, bResultBuf.length);
	}
//	public void setPicBitmap(Bitmap picBitmap){
//		pic_bitmap = picBitmap;
//	}
	
	public ExIDCardReco(){
		bResultBuf = new byte[mMaxStreamBuf];
		nResultLen = 0;
		rects = new int[32];
		ok = false;
		cardcode = new ExIDCardResult();
	}
	
	/*** @return raw text encoded by the decoder */
	public String getText() {
		//String text = "EXIDCardAPP by www.exocr.com: \n";
		//String text = "EXIDCardAPP\n";
		String text = "";
		text += cardcode.getText();
		//Log.d(tag, text);
		return text;
	}
	
	/*
	 * 
	 * 
	 * @return 所有信息的json格式
	 */
	public JSONObject getjson(){
		return cardcode.getJson();
	}
	
	//decode result stream
	public int DecodeResult(byte []bResultBuf, int nResultLen) {
		int i, len;	
		
		if(nResultLen < 1) return 0;
		
		i = 0;
		cardcode.type = 0;
		cardcode.type = bResultBuf[i++];
		while(i < nResultLen){
			len = cardcode.decode(bResultBuf, nResultLen, i);
			i += len;
		}
		
		if (cardcode.type == 1 && (cardcode.cardnum == null || cardcode.name == null || cardcode.nation == null || cardcode.sex == null || cardcode.address == null) ||
			cardcode.type == 2 && (cardcode.office == null || cardcode.validdate == null ) ||
			cardcode.type == 0 ){
			ok = false;
		}else{
			if (cardcode.type == 1 && (cardcode.cardnum.length() != 18 || cardcode.name.length() < 2 || cardcode.address.length() < 10)) {
				ok = false;
			} else {
				ok = true;
			}
		}
		
		return 1;
	}
	//rects存放各个块的矩形，4个一组，这么做是为了将JNI的接口简单化
	// [0, 1, 2, 3]  idnum			issue
	// [4, 5, 6, 7]	 name			validate
	// [8, 9, 10,11] sex
	// [12,13,14,15] nation
	// [16,17,18,19] address
	// [20,21,22,23] face
	public void SetRects( int []rects)
	{
		if(cardcode.type == 1){
			cardcode.rtIDNum 	= new Rect(rects[0], rects[1], rects[2], rects[3]);
			cardcode.rtName  	= new Rect(rects[4], rects[5], rects[6], rects[7]);
			cardcode.rtSex     	= new Rect(rects[8], rects[9], rects[10], rects[11]);
			cardcode.rtNation  	= new Rect(rects[12], rects[13], rects[14], rects[15]);
			cardcode.rtAddress 	= new Rect(rects[16], rects[17], rects[18], rects[19]);
			cardcode.rtFace    	= new Rect(rects[20], rects[21], rects[22], rects[23]);
		}else if(cardcode.type == 2){
			cardcode.rtOffice 	= new Rect(rects[0], rects[1], rects[2], rects[3]);
			cardcode.rtValid  	= new Rect(rects[4], rects[5], rects[6], rects[7]);			
		}else{
			return;
		}
	}
	//
	public int PrintResult(){
		if (nResultLen > 0){
			int i;
			int []vals = new int[nResultLen];
			for (i = 0; i < nResultLen; ++i){
				vals[i] = (int)bResultBuf[i];
			}
			return 1;
		}
		return 0;
	}
	
	
	//natives/////////////////////////////////////////////////////
	public static native int nativeInit(byte []dbpath);
	public static native int nativeDone();
	public static native int nativeRecoRawdat(byte []imgdata, int width, int height, int pitch, int imgfmt, byte []bresult, int maxsize);
	public static native int nativeRecoBitmap(Bitmap bitmap, byte[]bresult, int maxsize);
	public static native Bitmap nativeGetStdCardImg(byte []NV21, int width, int height, byte []bresult, int maxsize, int []rects);
}