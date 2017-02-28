/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exidcard.decoding;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.exidcard.ExIDCardReco;
import com.cfjn.javacf.activity.CaptureActivity;
import com.cfjn.javacf.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

final class DecodeHandler extends Handler {

private static final String TAG = DecodeHandler.class.getSimpleName();
private final CaptureActivity activity;
private int gcount;

  DecodeHandler(CaptureActivity activity) {
    this.activity = activity;
    gcount = 0;
  }
  
  
  @Override
  public void handleMessage(Message message) {
    if (message.what == R.id.decode) {
		//Log.d(TAG, "Got decode message");
        decode((byte[]) message.obj, message.arg1, message.arg2);
	} else if (message.what == R.id.quit) {
		Looper.myLooper().quit();
	}
  }

  /**
   * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
   * reuse the same reader objects from one decode to the next.
   *
   * @param data   The YUV preview frame.
   * @param width  The width of the preview frame.
   * @param height The height of the preview frame.
   */
	private void decode(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		// arg
		int ret = 0;
		ExIDCardReco excard = new ExIDCardReco();		
		//savetofile(data, width, height);
		//savetoJPEG(data, width, height);
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		ret = ExIDCardReco.nativeRecoRawdat(data, width, height, width, 1, excard.bResultBuf, excard.bResultBuf.length);
		excard.cardcode.SetColorType(CardColorJudge(data, width, height));
		
		if (ret > 0) {
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found text (" + (end - start) + " ms):\n");

			excard.nResultLen = ret;
			excard.cardcode.SetViewType("Preview");
			excard.cardcode.SetColorType(CardColorJudge(data, width, height));
			excard.DecodeResult(excard.bResultBuf, excard.nResultLen);
			//if we have the text to show
			//if ( excard.ok && activity.CheckIsEqual(excard.cardcode) ) {
			if ( excard.ok ) {
				activity.SetRecoResult(excard.cardcode);
				
				//下面这些代码是提取标准身份证图像的，如果客户有需求请打开
				//API For Image Return;
				Bitmap imcard = ExIDCardReco.nativeGetStdCardImg(data, width, height, excard.bResultBuf, excard.bResultBuf.length, excard.rects);
				//如果需要保存图像，请您打开保存图像的语句
				try{saveBitmap(imcard); }catch (IOException e) {e.printStackTrace();}
				excard.cardcode.SetBitmap(imcard);
				//保存各个条目的矩形框
				excard.SetRects(excard.rects);
				//Bitmap imNum = excard.cardcode.GetIDNumBitmap();
				//返回图像结束-------END
				
				Message message = Message.obtain(activity.getHandler(),	R.id.decode_succeeded, excard);
				message.sendToTarget();
				return;
			}
		}
		
		// retry to focus to the text
		Message message = Message.obtain(activity.getHandler(), R.id.decode_failed);
		message.sendToTarget();
	}
	
	private void savetofile(byte[] data, int width, int height)
	{
		gcount++;
		String tofile = "/mnt/sdcard/test_"+gcount+".raw";
		String ssize = "size=width="+width+"height="+height;
		byte bsize[] = new byte[ssize.length()];
		
		for (int i = 0; i < ssize.length(); ++i){
			bsize[i] = (byte)ssize.charAt(i);
		}
		
		try {
		File file = new File(tofile);
		OutputStream fs = new FileOutputStream(file);// to为要写入sdcard中的文件名称
		fs.write(data, 0, width*height);
		fs.write(bsize);
		fs.close();
		} catch (Exception e) {
			return;
		}
	}
	
	private int CardColorJudge(byte []data, int width, int height)
	{
		int offset = width*height;
		int i;
		int iTht = 144;
		int iCnt = 255;
		int nNum = 0;
		int iSize = width*height/2;
		
		for(i = 0; i < iSize; ++i ){
			int val = data[i+offset]&0xFF;
			if( val > iTht){ ++nNum; }
		}
		
		if(nNum > iCnt) return 1;
		else return 0;
	}
	// save to jpeg
	private void savetoJPEG(byte[] data, int width, int height) {
		int w, h;
		gcount++;
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String date = sDateFormat.format(new java.util.Date());
		
		String tofile = Environment.getExternalStorageDirectory()+File.separator+Environment.DIRECTORY_DCIM+File.separator+date+"_"+gcount+".jpg";
		//String tofile = Environment.()+File.separator+Environment.DIRECTORY_DCIM+File.separator+date+"_"+gcount+".jpg";
		//String tofile = "/sdcard/DCIM/"+"NV21_"+ date+"_"+gcount+".jpg";

		int imageFormat = ImageFormat.NV21;
		Rect frame = new Rect(0, 0, width-1, height-1);
		if (imageFormat == ImageFormat.NV21) {
			YuvImage img = new YuvImage(data, ImageFormat.NV21, width, height, null);
			OutputStream outStream = null;
			File file = new File(tofile);
			try {
				outStream = new FileOutputStream(file);
				img.compressToJpeg(frame, 100, outStream);
				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveBitmap(Bitmap bitmap) throws IOException {
		String tofile = Environment.getExternalStorageDirectory()+File.separator+Environment.DIRECTORY_DCIM+File.separator+"image_idcard.jpg";
		File file = new File(tofile);
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
