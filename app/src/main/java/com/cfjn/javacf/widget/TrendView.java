package com.cfjn.javacf.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.ChartVo;
import com.cfjn.javacf.util.G;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 折线图
 * 版本说明：代码规范整改
 *           2016-6-15： 新增TAGE属性 控制折线图的位移，不控制高度
 * 附加注释：
 * 主要接口：
 */

public class TrendView extends View {
    /**
     * View宽
     */
    private int width = G.size.W;
    /**
     * View高
     */
    private int height = G.size.H / 3;
    /**
     * View的padding
     */
    private float padding = width / 10;
    /**
     * 图表的宽
     */
    private float chartWidth = width - padding * 2;
    /**
     * 图表的高
     */
    private float chartHeight = height - padding * 2;
    /**
     * 图表横向单位
     */
    private float chartUnitX = chartWidth / 6;
    /**
     * 图表纵向单位
     */
    private float chartUnitY = chartHeight / 4;
    /**
     * View横向单位
     */
    private float viewUnitX = chartUnitX;
    /**
     * 当前所处的区间
     */
    private int position = 0;
    /**
     * 图表名称
     */
    private String name = "";
    /**
     * 图表当前数据
     */
    private String value;
    private String value2;
    /**
     * 图表数据
     */
    private List<ChartVo> values ;
    private int maxSize = 0;
    /**
     * 图表当前日期
     */
    private String topDate = "2014-6-4";
    /**
     * 图表开始日期(右侧)
     */
    private String startDate = "2014-6-4";
    /**
     * 图表结束日期（左侧）
     */
    private String endDate = "2014-6-11";
    /**
     * 图标最大值
     */
    private float maxY = 4.0f;
    /**
     * 图表最小值
     */
    private float minY = 1.2f;

    private float x;
    private boolean isDown;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private Paint namePaint;
    private int nameFontHeight;
    private Paint valuePaint;
    private int valueFontHeight;
    private Paint labelPaint;
    private int labelFontHeight;
    private Paint bgLinePaint;
    private Paint trendLinePaint;
    private Paint yLinePaint;
    private Paint trendBgPaint;
    private Path path;
    private Paint chartValuePaint;
    private int chartValueFontHeight;
    private Paint chartValueBgPaint;
    private Paint pointPaint;

    private Paint lablePoitn;
    private Paint trendLine2Paint;
    private Paint chartValue2BgPaint;
    private float space = G.size.W / 96;

    private Paint valuecolor;

    private Paint valueGreen;

    private int curveType = 1;

    private int type=7;

    private List<String>date;

    private int windate=width/20;
    /**
     * 折线位移  40
     */
    private final int TAGE=width/20;
    // 通过xml创建默认调用此构造
    public TrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 图表名称
        namePaint = new Paint();
        namePaint.setColor(Color.RED);
        namePaint.setTextSize(G.size.W / 32);
        namePaint.setAntiAlias(true);// 抗锯齿
        FontMetrics nameFontMetrics = namePaint.getFontMetrics();
        nameFontHeight = (int) (Math.ceil(nameFontMetrics.descent - nameFontMetrics.ascent));

        // 图表当前数据
        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);// 抗锯齿
        valuePaint.setColor(Color.argb(0xff, 0xff, 0x3b, 0x30));
        valuePaint.setTextSize(G.size.W / 28);
        FontMetrics valueFontMetrics = valuePaint.getFontMetrics();
        valueFontHeight = (int) (Math.ceil(valueFontMetrics.descent - valueFontMetrics.ascent));

        // 图表当前数据
        labelPaint = new Paint();
        labelPaint.setColor(Color.RED);
        labelPaint.setAntiAlias(true);// 抗锯齿
        labelPaint.setTextSize(G.size.W / 32);
        FontMetrics labelFontMetrics = labelPaint.getFontMetrics();
        labelFontHeight = (int) (Math.ceil(labelFontMetrics.descent - labelFontMetrics.ascent));

        lablePoitn=new Paint();
        lablePoitn.setColor(Color.BLACK);
        lablePoitn.setTextSize(G.size.W / 32);
        lablePoitn.setAntiAlias(true);// 抗锯齿
        FontMetrics pointFontMetrics = labelPaint.getFontMetrics();
        labelFontHeight = (int) (Math.ceil(pointFontMetrics.descent - labelFontMetrics.ascent));


        // 背景线
        bgLinePaint = new Paint();
        bgLinePaint.setAntiAlias(true);// 抗锯齿
        bgLinePaint.setColor(Color.argb(0xff, 0xcc, 0xcc, 0xcc));
        bgLinePaint.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(new float[] { 2, 2 }, 1);
        bgLinePaint.setPathEffect(effects);

        // 走势线
        yLinePaint = new Paint();
        yLinePaint.setAntiAlias(true);// 抗锯齿
        yLinePaint.setColor(Color.parseColor("#FF3B30"));
        yLinePaint.setStrokeWidth(2);


        // 走势线
        trendLinePaint = new Paint();
        trendLinePaint.setAntiAlias(true);// 抗锯齿
        trendLinePaint.setColor(Color.parseColor("#00b4f1"));
        trendLinePaint.setStrokeWidth(2);

        // 走势线
        trendLine2Paint = new Paint();
        trendLine2Paint.setAntiAlias(true);// 抗锯齿
        trendLine2Paint.setColor(Color.parseColor("#ff8402"));
        trendLine2Paint.setStrokeWidth(2);

        // 走势背景
        path = new Path();
        trendBgPaint = new Paint();
        trendBgPaint.setAntiAlias(true);// 抗锯齿
        trendBgPaint.setColor(Color.parseColor("#00000000"));
        trendBgPaint.setStrokeWidth(1);

        // 文字
        chartValuePaint = new Paint();
        chartValuePaint.setAntiAlias(true);// 抗锯齿
        chartValuePaint.setColor(Color.WHITE);
        chartValuePaint.setTextSize(G.size.W / 24);
        FontMetrics chartValueFontMetrics = chartValuePaint.getFontMetrics();
        chartValueFontHeight = (int) (Math.ceil(chartValueFontMetrics.descent - chartValueFontMetrics.ascent));

        // 文字
        valuecolor = new Paint();
        valuecolor.setAntiAlias(true);// 抗锯齿
        valuecolor.setColor(getResources().getColor(R.color.gray_x0_5));
        valuecolor.setTextSize(G.size.W / 28);
        FontMetrics chartValueFontMetrics_ = valuecolor.getFontMetrics();
        chartValueFontHeight = (int) (Math.ceil(chartValueFontMetrics_.descent - chartValueFontMetrics_.ascent));

        // 文字
        valueGreen = new Paint();
        valueGreen.setAntiAlias(true);// 抗锯齿
        valueGreen.setColor(getResources().getColor(R.color.green));
        valueGreen.setTextSize(G.size.W / 28);
        FontMetrics chartValueFontMetrics_green = valuecolor.getFontMetrics();
        chartValueFontHeight = (int) (Math.ceil(chartValueFontMetrics_green.descent - chartValueFontMetrics_.ascent));

        // 文字背景
        chartValueBgPaint = new Paint();
        chartValueBgPaint.setAntiAlias(true);// 抗锯齿
        chartValueBgPaint.setColor(Color.parseColor("#00b4f1"));
        chartValueBgPaint.setTextSize(G.size.W / 28);

        // 文字背景
        chartValue2BgPaint = new Paint();
        chartValue2BgPaint.setAntiAlias(true);// 抗锯齿
        chartValue2BgPaint.setColor(Color.parseColor("#ff8402"));
        chartValue2BgPaint.setTextSize(G.size.W / 28);

        // 圆点
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);// 抗锯齿
//        pointPaint.setColor(Color.argb(0xff, 0xad, 0x7d, 0x7e));
        pointPaint.setColor(Color.parseColor("#ff0000"));
        values=new ArrayList<>();
    }

    private float[] getResult(List<ChartVo> list) {
        float maxValue = 0.0f;
        float minValue = Float.MAX_VALUE;
        for (ChartVo cv : list) {
            float value = cv.getValue();
            if (maxValue < value) {
                maxValue = value;
            }
            if(minValue > value) {
                minValue = value;
            }
        }
        for (ChartVo cv : list) {
            float value = cv.getMoreValue();
            if (maxValue < value) {
                maxValue = value;
            }
            if(minValue > value) {
                minValue = value;
            }
        }
        float[] result = {maxValue, minValue};
        return result;
    }

    public void setValus(List<ChartVo> values, int curveType,int type) {
        this.values.clear();
        this.values.addAll(values);
        this.curveType = curveType;
        this.type=type;

        switch(curveType){
            case 1:
                this.name = "日收益";
                break;
            case 2:
//                this.name = "万份收益走势";
                break;
            case 3:
                this.name = "七日年化走势";
                break;
        }
        if(this.values.size()<=0){
            return;
        }
        maxSize = this.values.size()-1;
        float[] result = getResult(values);
        maxY = result[0];
        minY = result[1];
        viewUnitX = chartWidth / maxSize;
        position = maxSize;
        startDate = this.values.get(maxSize).getDate();
        endDate = this.values.get(0).getDate();
        //横坐标的值
        date=new ArrayList<>();
        for(int i=0;i<values.size();i++){
            date.add(values.get(i).getDate());
        }

        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values.size() <=0) {
            return;
        }
        // 绘制画布颜色
        canvas.drawColor(Color.WHITE);
        // 绘制图表名称
        canvas.drawText(name, space * 2, nameFontHeight, namePaint);

        String unit = "元";
        if(curveType == 2){
            unit = "%";
        }

        value = values.get(position).getValue() + unit;
        if(curveType==2){
            value2= String.valueOf(values.get(position).getMoreValue());
        }else{
            value2= values.get(position).getMoreValue()+ unit;
        }
        topDate = values.get(position).getDate();
        float valueFontWidth = valuePaint.measureText(value);
        float value2FontWidth = valuePaint.measureText(value2);
        float topDateFontWidth = labelPaint.measureText(topDate);
       if(curveType==2){
           String valueS[]=value.split("%");
           // 绘制图表当前数据
           if(type==7){
               canvas.drawText(value2, width - topDateFontWidth - space * 30, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuecolor);
               canvas.drawText("万份收益:", width - topDateFontWidth - space * 45, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuecolor);
               canvas.drawText("七日年化:", width - topDateFontWidth- space * 75, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuecolor);
               if(Double.parseDouble(valueS[0])<0){
                   canvas.drawText(value, width - topDateFontWidth- space * 60, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valueGreen);
               }else{
                   canvas.drawText(value, width - topDateFontWidth- space * 60, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuePaint);
               }
           }else{
               canvas.drawText("日增涨率:", width  - topDateFontWidth - space * 45, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuecolor);
               canvas.drawText(value2, width  - topDateFontWidth- space * 60, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuecolor);
               canvas.drawText("单位净值:", width  - topDateFontWidth- space * 75, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuecolor);
               if(Double.parseDouble(valueS[0])<0){
                   canvas.drawText(value, width  - topDateFontWidth - space * 30, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valueGreen);
               }else{
                   canvas.drawText(value, width  - topDateFontWidth - space * 30, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, valuePaint);
               }
           }
       }else{
           String valueText="其他型:"+value;
           String value2Text="货币型:"+value2;
           float valueTextFontWidth = valuePaint.measureText(valueText);
           float value2TextFontWidth = valuePaint.measureText(value2Text);
           // 绘制图表当前数据
           canvas.drawText(valueText, width - valueTextFontWidth - topDateFontWidth - space * 15, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, chartValueBgPaint);
           canvas.drawText(value2Text, width - value2TextFontWidth - valueTextFontWidth - topDateFontWidth - space * 20, nameFontHeight - (nameFontHeight - valueFontHeight) / 2, chartValue2BgPaint);
       }

        // 绘制图表当前日期
        canvas.drawText(topDate, width - topDateFontWidth - space * 15+TAGE, nameFontHeight - (nameFontHeight - labelFontHeight) / 2, valuecolor);
        float fl=maxY/4;
        for (int i = 0; i < 5; i++) {
            // 绘制y轴的值
            float f=(maxY-minY)/4;
            String yValue=df.format(fl*i);
//            String yValue = df.format(maxY / 4 * (4 - i));
//            String yValue=df.format(maxY - f *i);
            float yFontWidth = labelPaint.measureText(yValue);
            canvas.drawText(yValue+"%", padding - yFontWidth - space*3+TAGE-5, padding + labelFontHeight / 4 + chartUnitY * (4-i), labelPaint);
            // 绘制背景横线
            canvas.drawLine(padding+TAGE, padding + chartUnitY * i, width - padding+TAGE, padding + chartUnitY * i, bgLinePaint);
        }
        for (int i = 0; i < 7; i++) {
            // 绘制背景竖线
            canvas.drawLine(i * chartUnitX + padding+TAGE, padding, i * chartUnitX + padding+TAGE, height - padding, bgLinePaint);
            //绘制x轴的值
//            String Xvalue=date.get(i);
//            if(i==6){
//                Xvalue=Xvalue+" 日期";
//            }
//            canvas.drawText(Xvalue, i * chartUnitX + padding, height - padding + labelFontHeight, lablePoitn);
        }

        // 重置走势背景
        path.reset();
        float cx = 0.0f;
        float cy = 0.0f;
        float cy2=0.0f;
        if(curveType==2){
            trendLinePaint.setColor(Color.parseColor("#FF3B30"));
            trendLine2Paint.setColor(Color.parseColor("#00000000"));
            chartValueBgPaint.setColor(Color.parseColor("#FF3B30"));
        }
        for (int i = 0; i < values.size(); i++) {
            float startX = i * viewUnitX + padding+TAGE;
            float startY = formatY(values.get(i).getValue()) - padding;
            float startY2 = formatY(values.get(i).getMoreValue()) - padding;
            // 最后一个值不需要线
            if (i != maxSize) {
                float stopX = (i + 1) * viewUnitX + padding+TAGE;
                float stopY = formatY(values.get(i + 1).getValue()) - padding;
                float stopY2 = formatY(values.get(i + 1).getMoreValue()) - padding;
                // 绘制走势线
                canvas.drawLine(startX, startY, stopX, stopY, trendLinePaint);
                canvas.drawLine(startX, startY2, stopX, stopY2, trendLine2Paint);

            }
            // 标记走势背景的上边框坐标
            if (i == 0) {
                path.moveTo(startX, startY + 1);
                path.moveTo(startX, startY2 + 1);
            } else {
                path.lineTo(startX, startY + 1);
                path.lineTo(startX, startY2 + 1);
            }
            // 记录当前选中的点
            if (i == position) {
                cx = startX;
                cy = startY;
                cy2=startY2;
            }
        }
        path.lineTo(width - padding+TAGE, height - padding+TAGE);
//        path.lineTo(padding, height - padding);
        // 封闭 走势背景
        path.close();
        canvas.drawPath(path, trendBgPaint);
        if (isDown) {
            // 绘制竖线（跟随手指）
            canvas.drawLine(x+TAGE, nameFontHeight+padding/2, x+TAGE, height - padding, yLinePaint);
        }
        String chartValue = value;
        String chartValue2=value2;
        float chartValueWidth = chartValuePaint.measureText(chartValue);
        float chartValueWidth2 = chartValuePaint.measureText(chartValue2);
        float left = cx + space;
        float left2 = cx + space;
        float right = cx + chartValueWidth + space * 3;
        float right2=cx+chartValueWidth2+space*3;

        float top = cy - chartValueFontHeight - space;
        float top2 = cy2 -chartValueFontHeight -space;

        float bottom = cy - space;
        float bottom2 = cy2 - space;

        if (right > width) {
            right = width - space;
            left = width - chartValueWidth - space * 3;
        }

        if (right2 > width) {
            right2 = width - space;
            left2 = width - chartValueWidth2 - space * 3;
        }

        /**
         * 黄图稍高的状态
         */
        if(top>top2&&top<bottom2||top==top2){
            top=top+chartValueFontHeight*2;
            bottom=bottom+chartValueFontHeight*2;
        }

        /**
         * 蓝图稍高的状态
         */
        if(top2>top&&top2<bottom){
            top2=top2+chartValueFontHeight*2;
            bottom2=bottom2+chartValueFontHeight*2;
        }

        RectF rect = new RectF(left, top, right, bottom);
        // 绘制文字背景
//        canvas.drawRoundRect(rect, space, space, chartValueBgPaint);
        // 绘制文字
//        canvas.drawText(chartValue, left + space, bottom - space, chartValuePaint);
        // 绘制圆点
        canvas.drawCircle(cx, cy, space, pointPaint);

        if(curveType==1) {
            RectF rect2 = new RectF(left2, top2, right2, bottom2);
            // 绘制文字背景
//            canvas.drawRoundRect(rect2, space, space, chartValue2BgPaint);
            // 绘制文字
//            canvas.drawText(chartValue2, left2 + space, bottom2 - space, chartValuePaint);
            // 绘制圆点
            canvas.drawCircle(cx, cy2, space, pointPaint);
        }


        // 绘制结束日期
        canvas.drawText(endDate, padding+TAGE, height - padding + labelFontHeight, valuecolor);
        float startDateFontWidth = labelPaint.measureText(String.valueOf(startDate));
        // 绘制开始日期
        canvas.drawText(startDate, width - padding - startDateFontWidth+TAGE, height - padding + labelFontHeight, valuecolor);
    }

    private float formatY(float value) {
        return height - (value-minY)/(maxY-minY) * chartHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                if (x > padding && x < width - padding) {
                    position = (int) ((x - padding + viewUnitX / 2) / viewUnitX);
                    if (position > maxSize) {
                        position = maxSize;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDown = false;
                invalidate();
                break;
        }
        return true;
    }
}