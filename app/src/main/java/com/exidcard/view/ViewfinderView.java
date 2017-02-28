/*
 * Copyright (C) 2008 ZXing authors
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

package com.exidcard.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.exidcard.camera.CameraManager;
import com.cfjn.javacf.R;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 * 
 * 
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author io_big@163.com (louisluo)
 */
public final class ViewfinderView extends View {

  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  private static final long ANIMATION_DELAY = 100L;
  private static final int OPAQUE = 0xFF;

  private final Paint paint;
  private final int maskColor;
  private final int corner;
  private final int frameColor;
  private final int laserColor;
  private final int resultPointColor;
  private final int boxColor;
  private int scannerAlpha;
  private int Round;
  private Bitmap logo;
  private final int tipColor;
  private final String tipText;
  private final float tipTextSize;
  private float scannerY;
  private boolean ifReDraw;
  
  
  private int CanvasTop ;
  private int CanvasBottom ;
  private  int CanvasLeft ;
  private int CanvasRight ;
  private int CanvasheightValue;
  private int CanvasWidthValue ;

  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint();
    Resources resources = getResources();
    maskColor   = resources.getColor(R.color.viewfinder_mask);//遮罩在视频上面的层
    corner = resources.getColor(R.color.viewfinder_corner);//角落的颜色
    frameColor  = resources.getColor(R.color.viewfinder_frame);
    laserColor  = resources.getColor(R.color.viewfinder_laser);//十字叉子
    resultPointColor = resources.getColor(R.color.possible_result_points);
    boxColor = resources.getColor(R.color.viewfinder_box);//框的颜色
    scannerAlpha = 0;
    Round = 42;//4个角的横线长度
    tipColor = 0xFF999999;
    tipText  = new String("请将身份证四个角完全放在框内\n避免反光，确保身份证上的文字和人像清晰可辨");
//    tipText  = new String("请将身份证放平，尽量充满屏幕；识别成功后点击屏幕再次识别。");
    tipTextSize = dip2px(context, 20);
    ifReDraw = true;
  }

  /** 
  * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
  */  
  public static int dip2px(Context context, float dpValue) {  
    final float scale = context.getResources().getDisplayMetrics().density;  
    return (int) (dpValue * scale + 0.5f);  
  }  
    
  /** 
  * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
  */  
  public static int px2dip(Context context, float pxValue) {  
    final float scale = context.getResources().getDisplayMetrics().density;  
    return (int) (pxValue / scale + 0.5f);  
  }
  
  @Override
  public void onDraw(Canvas canvas) {
    Rect frame = CameraManager.get().getFramingRect();
    if (frame == null) {
      return;
    }
    int width = canvas.getWidth();
    int height = canvas.getHeight();
    int lw = 16;
    //决定4个角的位置 + 为上或者左边，－为右边
      CanvasTop = frame.top+50;
      CanvasBottom = frame.bottom-50;
      CanvasLeft = frame.left+90;
      CanvasRight = frame.right-200;
      CanvasheightValue = CanvasBottom - CanvasTop;
      CanvasWidthValue = CanvasRight - CanvasLeft;

    canvas.save();
    // Draw the exterior (i.e. outside the framing rect) darkened
    //遮罩层绘制
    
    if(ifReDraw){
    paint.setColor(maskColor);
    canvas.drawRect(0, 0, width, CanvasTop, paint);
    canvas.drawRect(0, CanvasTop, CanvasLeft, CanvasBottom + 1, paint);
    canvas.drawRect(CanvasRight + 1, CanvasTop, width, CanvasBottom + 1, paint);
    canvas.drawRect(0, CanvasBottom + 1, width, height, paint);
    
    //绘制框线
	  paint.setColor(boxColor);
	  canvas.drawRect(CanvasLeft, CanvasTop, CanvasRight, CanvasTop+1, paint);
	  canvas.drawRect(CanvasRight-1, CanvasTop, CanvasRight, CanvasBottom, paint);
	  canvas.drawLine(CanvasLeft, CanvasTop, CanvasLeft+1, CanvasBottom, paint);
	  canvas.drawLine(CanvasLeft, CanvasBottom-1, CanvasRight, CanvasBottom, paint);
      
      // Draw a two pixel solid black border inside the framing rect
//      paint.setColor(frameColor);
      //canvas.drawRect(CanvasLeft, CanvasTop, CanvasRight + 1, CanvasTop + 2, paint);
      //canvas.drawRect(CanvasLeft, CanvasTop + 2, CanvasLeft + 2, CanvasBottom - 1, paint);
      //canvas.drawRect(CanvasRight - 1, CanvasTop, CanvasRight + 1, CanvasBottom - 1, paint);
      //canvas.drawRect(CanvasLeft, CanvasBottom - 1, CanvasRight + 1, CanvasBottom + 1, paint);
      
      //4个角
      //这里的6是线条粗细
	  paint.setColor(corner);
      canvas.drawRect(CanvasLeft, CanvasTop, CanvasLeft+Round, CanvasTop+6, paint);
      canvas.drawRect(CanvasLeft, CanvasTop, CanvasLeft+6, CanvasTop+Round, paint);
      
      canvas.drawRect(CanvasRight-Round, CanvasTop, CanvasRight, CanvasTop+6, paint);
      canvas.drawRect(CanvasRight-6, CanvasTop, CanvasRight, CanvasTop+Round, paint);
      
      canvas.drawRect(CanvasLeft, CanvasBottom-6, CanvasLeft+Round, CanvasBottom, paint);
      canvas.drawRect(CanvasLeft, CanvasBottom-Round, CanvasLeft+6, CanvasBottom, paint);
      
      canvas.drawRect(CanvasRight-Round, CanvasBottom-6, CanvasRight, CanvasBottom, paint);
      canvas.drawRect(CanvasRight-6, CanvasBottom-Round, CanvasRight, CanvasBottom, paint);

    	}
      //+
//      int midx = (CanvasLeft+CanvasRight)/2;
//      int midy = (CanvasTop +CanvasBottom)/2;
      // Draw a red "laser scanner" line through the middle to show decoding is active
      paint.setColor(laserColor);
      
//      //设定Alpha值
//      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//      //这个算法好
//      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//      int middle = frame.height() / 2 + CanvasTop;
//      canvas.drawRect(midx-Round-3, midy-3, midx+Round+3, midy+3, paint);
//      canvas.drawRect(midx-3, midy-Round-3, midx+3, midy+Round+3, paint);
      
      //下面不知道干什么
      //int half = (Round+1)/2;
      //canvas.drawLine(midx-half, midy-half, midx+half, midy-half, paint);
      //canvas.drawLine(midx-half, midy+half, midx+half, midy+half, paint);
      //canvas.drawLine(midx-half, midy-half, midx-half, midy+half, paint);
      //canvas.drawLine(midx+half, midy-half, midx+half, midy+half, paint);
    	
      //我要绘制一个横线
      scannerY += CanvasheightValue*0.025;
      scannerY = scannerY%CanvasheightValue;
      canvas.drawRect(CanvasLeft+5, CanvasTop+scannerY,CanvasRight-5,CanvasTop+scannerY+5,paint);
      
//		if(logo != null){
//		     paint.setAlpha(OPAQUE);
//		     canvas.drawBitmap(logo, CanvasRight - logo.getWidth() - lw/2, CanvasTop+lw/2, paint);
//		}
		
//		if(tipText != null){
//			paint.setTextAlign(Align.CENTER);
//			paint.setColor(tipColor);
//			paint.setTextSize(tipTextSize);
//			canvas.translate(CanvasLeft + CanvasWidthValue/2, CanvasTop + CanvasheightValue/2);
//		    canvas.drawText(tipText, 0, 0, paint);
//		}
      
		canvas.restore();
		
      // Request another update at the animation interval, but only repaint the laser line,
      // not the entire viewfinder mask.
      postInvalidateDelayed(ANIMATION_DELAY, CanvasLeft, CanvasTop, CanvasRight, CanvasBottom);
   }

  public void drawViewfinder() {
    invalidate();
  }

  /**
   * Draw a bitmap with the result points highlighted instead of the live scanning display.
   *
   * @param barcode An image of the decoded barcode.
   */
  public void drawResultBitmap(Bitmap barcode) {
    invalidate();
  }
  
  public void setLogo(Bitmap ilogo){
	  this.logo = ilogo;
  }
  
  public Point getWidthandHeight(){
	  Point val = new Point(CanvasWidthValue-4,CanvasheightValue-4);
	  return val;
  }
  
  public Point getXandY(){
	  Point val = new Point(CanvasLeft+2,CanvasTop+2);
	  return val;
  }
}
