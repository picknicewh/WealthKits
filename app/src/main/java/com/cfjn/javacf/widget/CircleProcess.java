package com.cfjn.javacf.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.cfjn.javacf.R;
import com.cfjn.javacf.util.G;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者： wh
 * 时间： 2016/6/29
 * 名称：记账---首页的圆形进度
 * 版本说明：
 * 附加注释：
 * 主要接口：无
 */
public class CircleProcess extends View {
    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 里面透明圆画笔
     */
    private Paint mInnerCirclePaint;
    /**
     * 外面圆弧画笔
     */
    private Paint mOutCirclePaint;
    /**
     * 圆弧开始角度
     */
    private int starAngle = -90;
    /**
     * 字体大小
     */
    private  int mTextSize;
    /**
     * 字体颜色
     */
    private  int mTextColor;
    /**
     * 内圆颜色
     */
    private int mInnerCircleColor ;
    /**
     * 外圆弧颜色
     */
    private int mOutCircleColor ;
    /**
     * 圆弧的宽度
     */
     private  int mCircleWidth;
    /**
     * 百分比
     */
    private float progress;
    /**
     * 动态百分比
     */
    private float process;
    /**
     * 设置余额
     */
    private  double surPlus;
    /**
     * 动画进行中
     */
    private boolean isMoving;
    /**
     * 用于动画的timer
     */
    private Timer timer;
    /**
     * 动画任务
     */
    private MyTimerTask timerTask;
    /**
     * hander参数
     */
    private final static int TIMER_ID = 0x0010;
    /**
     * 定时器触发间隔时间(ms)
     */
    private static final int TIMER_INTERVAL = 5;
    public CircleProcess(Context context) {
        super(context);
        init();
    }

    public CircleProcess(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CircleProcess(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public double getSurPlus() {
        return surPlus;
    }

    public void setSurPlus(double surPlus) {
        this.surPlus = surPlus;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TIMER_ID) {
                if (!isMoving) {
                    return;
                }
            }
           process= process+0.01f;
            invalidate();
            if (process >= progress) {
                isMoving = false;
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
            }
        }
    };
    /**
     * 初始化数据
     */
    private  void init(){
        mInnerCirclePaint = new Paint();
        mInnerCircleColor = getResources().getColor(R.color.translucence);
        mCircleWidth = G.dp2px(getContext(), 5);
        mInnerCirclePaint.setAntiAlias(true);
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);
        mInnerCirclePaint.setStrokeWidth(mCircleWidth);

        mOutCirclePaint = new Paint();
        mOutCircleColor = getResources().getColor(R.color.white);
        mOutCirclePaint.setAntiAlias(true);
        mOutCirclePaint.setStyle(Paint.Style.STROKE);
        mOutCirclePaint.setStrokeWidth(mCircleWidth);
        mOutCirclePaint.setColor(mOutCircleColor);

        mTextPaint = new Paint();
        mTextColor = getResources().getColor(R.color.white);
        mTextSize = G.dp2px(getContext(), 18);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);

        timer = new Timer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制里面的圆
        int center = getWidth()/2; //获取圆心的x坐标
        int radius =center - mCircleWidth; //圆环的半径
        canvas.drawCircle(center,center,radius,mInnerCirclePaint);
        //绘制文字
        String mText = "预算余额";
        String mProgressText = G.momeyFormat(surPlus)+"元";
        float  centerPlace = setCenterText(mText,mTextPaint);
        canvas.drawText(mText,centerPlace,getHeight()/2,mTextPaint);
        int space =  G.dp2px(getContext(),25);
        float  centerPlace2 = setCenterText(mProgressText,mTextPaint);
        canvas.drawText(mProgressText,centerPlace2,getHeight()/2+space,mTextPaint);
        //绘制外面的圆弧
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        //根据进度画圆弧
        canvas.drawArc(oval,starAngle, 360 * process,false,mOutCirclePaint);
    }
    public synchronized  void  startCartoom(int time){
        if (time <= 0 || isMoving) {
            return;
        }
        isMoving = true;
        process = 0;
        timerTask = new MyTimerTask();
        timer.schedule(timerTask, TIMER_INTERVAL, TIMER_INTERVAL);
    }
    /**
     * 设置画的字体居中位置
     */
    private float setCenterText(String text,Paint paint) {
        return (getWidth() - paint.measureText(text))/2;
    }
    private  class  MyTimerTask extends TimerTask{
        @Override
        public void run() {
            Message msg = handler.obtainMessage(TIMER_ID);
            msg.sendToTarget();
        }
    }
}
