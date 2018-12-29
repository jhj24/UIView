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
import android.view.View;

import com.jhj.uiview.R;
import com.jhj.uiview.bean.PieChartBean;

import java.util.List;

/**
 * 饼状图
 */
public class PieChartView extends View {

    private Paint mPaint;
    private Path mPath;
    private double mTotalNum;
    private List<PieChartBean> mDataList;

    //指示线
    private int mIndicatorLineLength;
    private int mIndicatorLineWidth;
    private int mIndicatorLineColor;
    //说明
    private int mIndicatorInfoColor;
    private float mIndicatorInfoSize;


    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float scaleDensity = getResources().getDisplayMetrics().scaledDensity;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
        mIndicatorLineLength = typedArray.getInt(R.styleable.PieChartView_indicatorLineLength, 50);
        mIndicatorLineWidth = typedArray.getInt(R.styleable.PieChartView_indicatorLineWidth, 1);
        mIndicatorLineColor = typedArray.getInt(R.styleable.PieChartView_indicatorLineColor, 0xFF68b831);
        mIndicatorInfoColor = typedArray.getColor(R.styleable.PieChartView_indicatorInfoColor, 0xFF68b831);
        mIndicatorInfoSize = typedArray.getDimension(R.styleable.PieChartView_indicatorInfoSize, 12 * scaleDensity);

        mPaint = new Paint();
        mPath = new Path();

        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = 0;
        float sweepAngle;
        RectF rect = new RectF();
        int rectWidth;
        if ((getHeight() - (getPaddingTop() + getPaddingBottom())) > getWidth() - (getPaddingLeft() + getPaddingRight())) {
            rectWidth = getWidth() - (getPaddingLeft() + getPaddingRight());
        } else {
            rectWidth = getHeight() - (getPaddingTop() + getPaddingBottom());
        }

        rect.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + rectWidth, getPaddingTop() + rectWidth);

        for (int i = 0; i < mDataList.size(); i++) {
            String string = mDataList.get(i).getContent();
            double frequency = mDataList.get(i).getFrequency();
            int color = mDataList.get(i).getColor();

            //扇形
            mPaint.reset();
            mPaint.setColor(color);
            sweepAngle = (float) (360 * (frequency / mTotalNum));
            float angle = startAngle + sweepAngle / 2;
            canvas.drawArc(rect, startAngle, sweepAngle, true, mPaint);
            startAngle += sweepAngle;


            //指示线
            float radios = rectWidth / 2;
            //扇形外弧中心坐标
            float arcX = (float) (getPaddingLeft() + radios + Math.cos(Math.PI * angle / 180) * radios);
            float arcY = (float) (getPaddingTop() + radios + Math.sin(Math.PI * angle / 180) * radios);
            //圆心到扇形外弧中心延迟线的坐标
            float aX = (float) (getPaddingLeft() + radios + Math.cos(Math.PI * angle / 180) * (radios + mIndicatorLineLength));
            float aY = (float) (getPaddingTop() + radios + Math.sin(Math.PI * angle / 180) * (radios + mIndicatorLineLength));
            mPaint.reset();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mIndicatorLineColor);
            mPaint.setStrokeWidth(mIndicatorLineWidth);
            mPath.moveTo(arcX, arcY);
            mPath.lineTo(aX, aY);
            if (angle < 90 || angle > 270) {
                mPath.lineTo(aX + mIndicatorLineLength, aY);
            } else {
                mPath.lineTo(aX - mIndicatorLineLength, aY);
            }
            canvas.drawPath(mPath, mPaint);

            //文字
            Rect textBound = new Rect();
            mPaint.reset();
            mPaint.setColor(mIndicatorInfoColor);
            mPaint.setTextSize(mIndicatorInfoSize);
            mPaint.getTextBounds(string, 0, string.length(), textBound);
            if (angle < 90 || angle > 270) {
                canvas.drawText(string, aX + mIndicatorLineLength, aY + textBound.height() / 2, mPaint);
            } else {
                canvas.drawText(string, aX - mIndicatorLineLength - textBound.width(), aY + textBound.height() / 2, mPaint);
            }

            //频数

        }


    }


    public void setDataList(List<PieChartBean> dataList) {
        this.mDataList = dataList;
        for (PieChartBean pieChartBean : dataList) {
            mTotalNum += pieChartBean.getFrequency();
        }

    }
}
