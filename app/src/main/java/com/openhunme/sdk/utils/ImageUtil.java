package com.openhunme.sdk.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

/**
 * 图片处理工具类
 *
 * @author xinyan
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    
    /**
     * 将NV21转成jpg
     * @param rgb
     * @param yuv420sp
     * @param width
     * @param height
     * 
     * @author louisluo
     */
    static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        
        for (int j = 0, yp = 0; j < height; j++) {
                int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
                for (int i = 0; i < width; i++, yp++) {
                        int y = (0xff & ((int) yuv420sp[yp])) - 16;
                        if (y < 0) y = 0;
                        if ((i & 1) == 0) {
                                v = (0xff & yuv420sp[uvp++]) - 128;
                                u = (0xff & yuv420sp[uvp++]) - 128;
                        }
                        
                        int y1192 = 1192 * y;
                        int r = (y1192 + 1634 * v);
                        int g = (y1192 - 833 * v - 400 * u);
                        int b = (y1192 + 2066 * u);
                        
                        if (r < 0) r = 0; else if (r > 262143) r = 262143;
                        if (g < 0) g = 0; else if (g > 262143) g = 262143;
                        if (b < 0) b = 0; else if (b > 262143) b = 262143;
                        
                        rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                }
        }
 }
    
    /**
     * convert ARGB_8888 to nv21
     * 这个方法还在测试中，不能使用
     */
    static public byte [] ConvertBitmapToNV21(Bitmap sourceBitmap) {
    	//思路，先将RGB通道的值算出来｀再将yuv的通道值算出来，｀通道间进行转换
    	
    	int width = sourceBitmap.getWidth();
    	int height = sourceBitmap.getHeight();
    	int[] bitmap_array = new int[width * height];
    	byte[] yuv420sp = new byte[width * height * 3 / 2];
//    	byte[] yuv420sp = new byte[width * height];
    	sourceBitmap.getPixels(bitmap_array, 0, width, 0, 0, width, height);
    	
    	//decode data
    	final int frameSize = width * height;
    	
    	int yIndex = 0;
    	int uvIndex = frameSize;
    	int index = 0;
    	   
        int[] U, V;  
        U = new int[frameSize];  
        V = new int[frameSize];  
       
        int r, g, b, y, u, v;  
        for (int j = 0; j < height; j++) {
//            int index = width * j;  
            for (int i = 0; i < width; i++) {  
                r = (bitmap_array[index] & 0xff000000) >> 24;
                g = (bitmap_array[index] & 0xff0000) >> 16;
                b = (bitmap_array[index] & 0xff00) >> 8;
       
                // rgb to yuv  
                y = (66 * r + 129 * g + 25 * b + 128) >> 8 + 16;  
                u = (-38 * r - 74 * g + 112 * b + 128) >> 8 + 128;  
                v = (112 * r - 94 * g - 18 * b + 128) >> 8 + 128;  
       
//                 clip y  
//                yuv420sp[index] = (byte) ((y < 0) ? 0 : ((y > 255) ? 255 : y));
//                U[index] = u;  
//                V[index++] = v;  
                
                yuv420sp[yIndex++] = (byte) ((y < 0) ? 0 : ((y > 255) ? 255 : y));
                if (j % 2 == 0 && index % 2 == 0) { 
                    yuv420sp[uvIndex++] = (byte)((v<0) ? 0 : ((v > 255) ? 255 : v));
                    yuv420sp[uvIndex++] = (byte)((u<0) ? 0 : ((u > 255) ? 255 : u));
                }
                index ++;
            }
        }
        return yuv420sp;
 }
    
    
    /**
     * 将bitmap ARGB_8888转换成NV21 的算法
     * 
     * 该bitmap的编码方式必须是ARGB8888的，不然算法会错误
     * 
     * @param inputWidth
     * @param inputHeight
     * @param scaled
     * @return
     * 
     * @author louisluo
     */
    static public byte [] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {
    	//对于超过1MB的图片来说，没有这么大的计算量，所以得先进行尺寸压缩
//    	scaled =Bitmap.createScaledBitmap(scaled, finalWidth, finalheight, false);
//    	scaled = convertBitmapToARGB8888(scaled);
    	//开始NV21 基本参数设定和转换
		byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
		int[] argb = new int[inputWidth * inputHeight];
		try {
			scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);
			encodeYUV420SP(yuv, argb, inputWidth, inputHeight);
		} catch (Exception e) {
			e.printStackTrace();
			yuv = new byte[1];
		}
//        //save file
//		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date = sDateFormat.format(new java.util.Date());
//		String SYSTEMBASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
//	    String tofile = SYSTEMBASEPATH+"/TF/"+ date + ".jpg";
//		try {
//			ImageUtil.saveBitmap(scaled, tofile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        scaled.recycle();
//        saveImage(argb);
        return yuv;
    }
    static public void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uvIndex = frameSize;

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;

                // well known RGB to YUV algorithm
                Y = ( (  66 * R + 129 * G +  25 * B + 128) >> 8) +  16;
                U = ( ( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128;
                V = ( ( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128;

                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) { 
                    yuv420sp[uvIndex++] = (byte)((V<0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (byte)((U<0) ? 0 : ((U > 255) ? 255 : U));
                }
                index ++;
            }
        }
    }
    
    /**
	 * 存储照片的算法
	 * @param data
	 * @return
	 * 
	 * @author louisluo
	 */
    public static String saveImage(byte[] data) { // 保存jpg到SD卡中
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		String SYSTEMBASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	    String tofile = SYSTEMBASEPATH+"/TF/"+ date + ".jpg";
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
    
    /**
     * 组合图片和源图片
     *
     * @param src       源图片
     * @param watermark 涂鸦图片
     * @return
     */
    public static Bitmap doodle(Bitmap src, Bitmap watermark) {

        // 另外创建一张图片
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(newb);

        canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src

        //在src的右下角画入水印
        canvas.drawBitmap(watermark, src.getWidth() - watermark.getWidth() + 5, 5, null);

        canvas.save(Canvas.ALL_SAVE_FLAG);

        canvas.restore();

        watermark.recycle();

        watermark = null;

        return newb;

    }

    public static Bitmap makeWaterMark(String text) {

        int w = 360, h = 140;

        Bitmap waterMark = Bitmap.createBitmap(w, h, Config.ARGB_8888);

        Canvas canvasTemp = new Canvas(waterMark);

        canvasTemp.drawColor(Color.TRANSPARENT);

        Paint p = new Paint();

        String familyName = "宋体";

//        Typeface font = Typeface.create(familyName, Typeface.BOLD);

        p.setColor(Color.BLUE);

//        p.setTypeface(font);

        p.setTextSize(24);

        canvasTemp.drawText(text, 0, 50, p);

        return waterMark;
    }
    
    /**
     * 将bimap的格式转成argb8888
     * 
     * 算法基于bitmapToByteArray这个方法执行
     * @param bm
     * @author louisluo
     * 
     */
    @SuppressLint("NewApi") 
    public static Bitmap convertBitmapToARGB8888(Bitmap bm){
//    		byte[] dataArray = bitmapToByteArray(bm);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		  // 将Bitmap压缩成PNG编码，质量为100%存储
		bm.compress(Bitmap.CompressFormat.JPEG, 80, os);//除了PNG还有很多常见格式，如jpeg等。
		byte[] dataArray = os.toByteArray();
		//计算图片
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(dataArray, 0, dataArray.length, options);
//        return bitmap;
    	
    	//use canvas
//    	int w = bm.getWidth();  
//        int h = bm.getHeight();  
//        //create the new blank bitmap  
//        Bitmap return_bitmap = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
////        Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图  
//        Canvas cv = new Canvas( return_bitmap );  
//        //draw src into  
//        Paint paint = new Paint();
//        cv.drawBitmap( bm, 0, 0, paint );//在 0，0坐标开始画入src  
//        cv.save( Canvas.ALL_SAVE_FLAG );//保存一次canvas上的动作
//        //store  
//        cv.restore();//存储  后续的操作不会影响上一次save的结果。
        
        
        //save file
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date = sDateFormat.format(new java.util.Date());
//		String SYSTEMBASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
//	    String tofile = SYSTEMBASEPATH+"/TF/"+ date + ".jpg";
//		try {
//			ImageUtil.saveBitmap(bitmap, tofile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		return return_bitmap;
		return bitmap;
    }
    
    public static Bitmap convertRGBToARGB8888(Bitmap bm){
		byte[] dataArray = bitmapToByteArray(bm);
	final ByteArrayOutputStream os = new ByteArrayOutputStream();
	  // 将Bitmap压缩成PNG编码，质量为100%存储
	bm.compress(Bitmap.CompressFormat.JPEG, 100, os);//除了PNG还有很多常见格式，如jpeg等。
	dataArray = os.toByteArray();
	//计算图片
	BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Config.ARGB_8888;
    Bitmap bitmap = BitmapFactory.decodeByteArray(dataArray, 0, dataArray.length, options);
    return bitmap;
}
    
    
    /**
     * 将bimap转成byte[]
     * @param bm
     * @return
     * 
     * @author louisluo
     */
	@SuppressLint("NewApi")
	private static byte[] bitmapToByteArray(Bitmap bm){
		//b is the Bitmap
		//calculate how many bytes our image consists of.
		int bytes = bm.getByteCount();
		//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
		//int bytes = b.getWidth()*b.getHeight()*4; 
		ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
		bm.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
		byte[] dataArray = buffer.array(); //Get the underlying array containing the data.
		return dataArray;
	}

    /**
     * 解析图片为宽高不超过width和height的Bitmap对象
     *
     * @param source 字节数组
     * @param width  宽
     * @param height 高
     * @return 图片对象
     */
    public static Bitmap decodeByteArray(byte[] source, int width, int height) {
        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new ByteArrayInputStream(source), null,
                    o);

            // The new size we want to scale to
            final int REQUIRED_HEIGHT = height;
            final int REQUIRED_WIDTH = width;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;

            System.out.println(width_tmp + "  " + height_tmp);
            Log.w("===", (width_tmp + "  " + height_tmp));

            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_WIDTH
                        && height_tmp / 2 < REQUIRED_HEIGHT)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;

                Log.w("===", scale + "''" + width_tmp + "  " + height_tmp);
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new ByteArrayInputStream(source),
                    null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析图片为宽高不超过width和height的Bitmap对象
     *
     * @param source 字节数组
     * @param width  宽
     * @param height 高
     * @return 图片对象
     */
    public static Bitmap decodeStream(InputStream source, int width, int height) {
        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(source, null, o);

            // The new size we want to scale to
            final int REQUIRED_HEIGHT = height;
            final int REQUIRED_WIDTH = width;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;

            System.out.println(width_tmp + "  " + height_tmp);
            Log.w("===", (width_tmp + "  " + height_tmp));

            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_WIDTH
                        && height_tmp / 2 < REQUIRED_HEIGHT)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;

                Log.w("===", scale + "''" + width_tmp + "  " + height_tmp);
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(source, null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap zipPic(Bitmap sourceBm, float targetWidth,
                                float targetHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPurgeable = true; // 可删除
        newOpts.inInputShareable = true; // 可共享
        // 转成数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sourceBm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] temp = baos.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length,
                newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = targetHeight;
        float ww = targetWidth;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 只进行分辨率压缩，不进行图片的质量压缩
     *
     * @param sourceBm
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap zipPicWithoutCompress(Bitmap sourceBm,
                                               float targetWidth, float targetHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPurgeable = true; // 可删除
        newOpts.inInputShareable = true; // 可共享
        // 转成数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sourceBm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] temp = baos.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length,
                newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = targetHeight;
        float ww = targetWidth;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options > 10) {
                options -= 10;// 每次都减少10
            } else {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 压缩图片大小
     *
     * @param image
     * @param size  目标大小，单位为kb
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        int nKB = 1024;
        do { // 循环判断如果压缩后图片是否大于sizekb,大于继续压缩
            baos.reset();// 重置baos即清空baos

            if (quality > 5) {
                quality -= 5;// 每次都减少10
            } else {
                break;
            }
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        } while (baos.size() / nKB > size);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 将两张位图拼接成一张(纵向拼接)
     *
     * @param first
     * @param second
     * @return
     */
    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {

        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }

    /**
     * 保存bitmap为图片
     *
     * @param bitmap
     * @param filePath
     * @param fileName
     * @return
     * @throws IOException
     */

    public static String saveBitmap(Bitmap bitmap, String filePath,
                                    String fileName) throws IOException {
        File f = null;
        String strPath = "";
        try {
            strPath = filePath + fileName;
            f = new File(strPath);
        } catch (Exception e) {
        }
        if (null != f) {
            f.createNewFile();
        } else {
            return null;
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strPath;
    }

    /**
     * 保存bitmap为图片
     *
     * @param bitmap
     * @param filePath
     * @throws IOException
     */

    public static void saveBitmap(Bitmap bitmap, String filePath)
            throws IOException {
        File f = null;
        String strPath = "";
        try {
            strPath = filePath;
            f = new File(strPath);
        } catch (Exception e) {
        }
        if (null != f) {
            f.createNewFile();
        } else {
            return;
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String uri2FilePath(Context context, Uri uri) {
        File file = null;
        String[] proj = {MediaColumns.DATA};
        Cursor actualimagecursor = ((Activity) context).managedQuery(uri, proj, null,
                null, null);
        int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaColumns.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        return img_path;
    }
}
