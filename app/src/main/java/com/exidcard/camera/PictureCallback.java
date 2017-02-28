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

package com.exidcard.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Message;

import com.exidcard.ExIDCardReco;
import com.cfjn.javacf.activity.CaptureActivity;
import com.cfjn.javacf.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

final class PictureCallback implements Camera.PictureCallback {

	private static final String TAG = PreviewCallback.class.getSimpleName();
	private CaptureActivity activity;

	PictureCallback() {
		this.activity = null;
	}
	
	void SetActivity(CaptureActivity activity) {
		this.activity = activity;
	}

	public void onPictureTaken(byte[] data, Camera camera) {
		//save the jpeg image if needed
		if (CameraManager.get().getCCM().getPictureFormat() == PixelFormat.JPEG) {
			saveImage(data);
		}
		saveImage(data);

		//call for recognition the card by take picture get
		if (this.activity != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int ret = 0;
			ExIDCardReco excard = new ExIDCardReco();
			/*
			byte[] data1 = new byte[width * height];
			// get gray image to recogition
			convert2Gray(bitmap, data1, width, height);			
			ret = ExIDCardReco.nativeRecoRawdat(data1, width, height, width, 1, excard.bResultBuf, excard.bResultBuf.length);
			*/
			
			ret = ExIDCardReco.nativeRecoBitmap(bitmap, excard.bResultBuf, excard.bResultBuf.length);
			
			if (ret > 0) {
				excard.nResultLen = ret;
				excard.cardcode.SetViewType("TakePicture");
				excard.DecodeResult(excard.bResultBuf, excard.nResultLen);
				//if we have the text to show
				//if ( excard.ok && activity.CheckIsEqual(excard.cardcode) ) {
				if ( excard.ok ) {
					Message message = Message.obtain(activity.getHandler(),	R.id.decode_succeeded, excard);
					message.sendToTarget();
				}
			}
			this.activity = null;
		}
		CameraManager.get().startPreview();
	}
	
	//convert rgb bitmpa to gray bitmap data
	static private void convert2Gray(Bitmap bmp, byte []data, int width, int height)
	{
		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
		int nPixelCount = 0;
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int gray = pixels[nPixelCount];

				int red = ((gray & 0x00FF0000) >> 16);
				int green = ((gray & 0x0000FF00) >> 8);
				int blue = (gray & 0x000000FF);

				gray = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
				data[nPixelCount] = (byte)gray;
				nPixelCount++;
			}
		}
	}
	
	static private String saveImage(byte[] data) { // 保存jpg到SD卡中
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		String SYSTEMBASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	    String tofile = SYSTEMBASEPATH+"/TF/"+ date + ".jpg";
//		String tofile = Environment.getExternalStorageDirectory()
//				+ File.separator + Environment.DIRECTORY_DCIM + File.separator
//				+ date + ".jpg";
		
		try {
			File file = new File(tofile);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tofile;
	}
}
