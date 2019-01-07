package com.jhj.uiview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jhj.uiview.R;
import com.jhj.uiview.bean.PieChartBean;

import java.util.List;

public class RingChartView extends LinearLayout {


    private Paint mPaint;
    private List<PieChartBean> mDataList;
    private double mAllNum;

    private float mRingWidth;
    private float mLineLength;
    private int mLineColor;
    private int mLineWidth;
    private int mInfoColor;
    private float mInfoSize;


    public RingChartView(Context context) {
        this(context, null);
    }

    public RingChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float density = getResources().getDisplayMetrics().density;
        float scaleDensity = getResources().getDisplayMetrics().scaledDensity;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingChartView);
        mRingWidth = typedArray.getFloat(R.styleable.RingChartView_ringWidth, 15 * density);
        mLineLength = typedArray.getFloat(R.styleable.RingChartView_indicatorLineLength, 15 * density);
        mLineColor = typedArray.getColor(R.styleable.RingChartView_indicatorLineColor, 0xFF68b831);
        mLineWidth = typedArray.getInt(R.styleable.RingChartView_indicatorLineWidth, 3);
        mInfoColor = typedArray.getColor(R.styleable.RingChartView_indicatorInfoColor, 0xFF68b831);
        mInfoSize = typedArray.getFloat(R.styleable.RingChartView_indicatorInfoSize, 12 * scaleDensity);

        mPaint = new Paint();
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = 0;
        float sweepAngle = 0;
        RectF rectF = new RectF();
        int rectWidth = Math.min(getWidth() - (getPaddingLeft() + getPaddingRight()), getHeight() - (getPaddingTop() + getPaddingBottom()));
        rectF.set(getPaddingLeft() + mRingWidth / 2, getPaddingTop() + mRingWidth / 2,
                getPaddingLeft() + rectWidth - mRingWidth / 2, getPaddingTop() + rectWidth - mRingWidth / 2);


        for (int i = 0; i < mDataList.size(); i++) {
            PieChartBean bean = mDataList.get(i);

            //圆环
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mRingWidth);
            mPaint.setColor(bean.getColor());
            sweepAngle = (float) (360 * (bean.getFrequency() / mAllNum));
            canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaint);

            //指示线

            Path path = new Path();
            float angle = startAngle + sweepAngle / 2;
            float arcRadio = rectWidth / 2;
            float x = (float) (getPaddingLeft() + arcRadio + arcRadio * Math.cos(Math.PI * angle / 180));
            float y = (float) (getPaddingTop() + arcRadio + arcRadio * Math.sin(Math.PI * angle / 180));
            //圆心到扇形外弧中心延迟线的坐标
            float aX = (float) (getPaddingLeft() + arcRadio + Math.cos(Math.PI * angle / 180) * (arcRadio + mLineLength));
            float aY = (float) (getPaddingTop() + arcRadio + Math.sin(Math.PI * angle / 180) * (arcRadio + mLineLength));
            path.moveTo(x, y);
            path.lineTo(aX, aY);
            if (angle < 90 || angle > 270) {
                path.lineTo(aX + mLineLength, aY);
            } else {
                path.lineTo(aX - mLineLength, aY);
            }
            mPaint.reset();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mLineWidth);
            mPaint.setColor(mLineColor);
            canvas.drawPath(path, mPaint);

            //说明
            Rect textRect = new Rect();
            mPaint.reset();
            mPaint.setColor(mInfoColor);
            mPaint.setTextSize(mInfoSize);
            mPaint.getTextBounds(bean.getContent(), 0, bean.getContent().length(), textRect);
            float textX;
            if (angle < 90 || angle > 270){
                textX = aX + mLineLength;
            }else {
                textX = aX - mLineLength - textRect.width();
            }
            float textY = aY + textRect.height() / 2;
            canvas.drawText(bean.getContent(), textX, textY, mPaint);


            startAngle += sweepAngle;
        }
    }

    public void setDataList(List<PieChartBean> dataList) {
        this.mDataList = dataList;
        for (PieChartBean pieChartBean : dataList) {
            mAllNum += pieChartBean.getFrequency();
        }
    }
}
