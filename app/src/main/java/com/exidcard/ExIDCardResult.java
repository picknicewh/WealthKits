package com.exidcard;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Rect;


public final class ExIDCardResult {
	
	String imgtype;
	//recognition data
	int type;
	public String cardnum;
	public String name;
	public String sex;
	public String address;
	public String nation;
	public String office;
	public String validdate;
	public String startData;
	public String endData;
	public String birthday;
	
	
	int nColorType;   //1 color, 0 gray
	
	Bitmap stdCardIm = null;
	Rect rtIDNum;
	Rect rtName;
	Rect rtSex;
	Rect rtNation;
	Rect rtAddress;
	Rect rtFace;
	Rect rtOffice;
	Rect rtValid;
	
	public ExIDCardResult() {
		type = 0;
		imgtype = "Preview";
	}
	
	/** decode from stream
	 *  return the len of decoded data int the buf */
	public int decode(byte []pwBuf, int tLen, int index){
		byte code;
		int i = 0;
		int j = index;
		String content = null;
		
		code = pwBuf[j++];
		while(j < tLen){
			i++;
			j++;
			if (pwBuf[j] == 0x20) break;
		}
				
		try {
			content = new String(pwBuf, index+1, i, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (code == 0x21){
			cardnum = content;
			birthday = cardnum.substring(6, 14);
			birthday=formatDateAsYYYYMMDD(birthday);
		}else if (code == 0x22){
			name = content;
		}else if (code == 0x23){
			sex = content;
		}else if (code == 0x24){
			nation = content;
		}else if (code == 0x25){
			address = content;
		}else if (code == 0x26){
			office = content;
		}else if (code == 0x27){
			validdate = content;
			String[] vv = validdate.split("-");
			startData = vv[0];
			endData = vv[1];
			startData=formatDateAsYYYYMMDD(startData);
			if(!endData.equals("长期")){
				endData=formatDateAsYYYYMMDD(endData);
			}
		}
		
		return i+1+1;
	}
	
	public void SetViewType(String viewtype)
	{
		this.imgtype = viewtype;
	}
	
	public void SetColorType(int aColorType)
	{
		nColorType = aColorType;
	}
	public void SetBitmap(Bitmap imcard){
		if(stdCardIm != null)
			stdCardIm.recycle();
		stdCardIm = imcard;
	}
	public Bitmap GetIDNumBitmap(){
		if(stdCardIm == null) return null;
		// 这里的rtlDNum 是不是没有作用？
		Bitmap bmIDNum = Bitmap.createBitmap(stdCardIm, rtIDNum.left, rtIDNum.top, rtIDNum.width(), rtIDNum.height());
		return bmIDNum;
	}
	public Bitmap GetBaseBitmap(){
		if(stdCardIm == null) return null;
		Bitmap bmIDNum = Bitmap.createBitmap(stdCardIm);
		return bmIDNum;
	}
	public Bitmap GetNameBitmap(){
		if(stdCardIm == null) return null;
		Bitmap bmIDNum = Bitmap.createBitmap(stdCardIm, rtName.left, rtName.top, rtName.width(), rtName.height());
		return bmIDNum;
	}

	/**
	 * 返回一个getText提供给身份证扫描后，显示提示文字使用
	 * @return
	 */
	public String getText() {
		String text = "";
//		text +="类型 = " + imgtype;
//		if(nColorType == 1){
//			text += "  类型:  彩色";
//		}else{
//			text += "  类型:  扫描";
//		}
		if(type == 1){
			text += "\n        姓名:" + name;
			text += "\n身份证号:" + cardnum;
//			text += "\n性别:" + sex;
//			text += "\n国籍:" + nation;
			text += "\n        地址:" + address;
		}else if (type == 2){
			text += "\n签发机关:" + office;
			text +=	 "\n起始时间:"+startData;
			text +=	 "\n结束时间:" + endData;
//			text += "\n有效期限:" + validdate;
		}
		return text;
	}
	
	/**
	 * 返回格式化的json数据源
	 * @return
	 */
	public JSONObject getJson(){
		JSONObject jo = new JSONObject();
		try {
			jo.put("imgtype", imgtype);
			jo.put("nColorType", nColorType);
			jo.put("type", type);
			jo.put("name", name);
			jo.put("cardnum", cardnum);
			jo.put("sex", sex);
			jo.put("nation", nation);
			jo.put("address", address);
			jo.put("office", office);
			jo.put("validdate", validdate);
			jo.put("startData", startData);
			jo.put("endData", endData);
			jo.put("birthday", birthday);
			if(type == 1){
				jo.put("office", "");
				jo.put("validdate", "");
				jo.put("startData", "");
				jo.put("endData", "");
			}else{
				jo.put("imgtype", "");
				jo.put("nColorType", "");
				jo.put("type", "0");//反面是0
				jo.put("name", "");
				jo.put("cardnum", "");
				jo.put("sex", "");
				jo.put("nation", "");
				jo.put("address", "");
				jo.put("birthday", "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	/**
	 * 将如 20110729这样的时间改为2011-07-09
	 * @param Date
	 */
	private String formatDateAsYYYYMMDD(String Date){
		String result="";
		if(Date.length()>=8){
			String YYYY = Date.substring(0, 4);
			String MM = Date.substring(4, 6);
			String DD = Date.substring(6, 8);
			result = YYYY+MM+DD;
		}
		return result;
	}
}
