package com.jhj.uiview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.jhj.uiview.R;
import com.jhj.uiview.bean.HistogramBean;

import java.util.List;


/**
 * 柱状图
 *
 * 横坐标表示变量分组，纵坐标表示各变量值出现的频数
 *
 */
public class HistogramView extends View {


    //直方图
    private int mHistogramColor;
    private float mHistogramWidth;
    private float mHistogramDistance;
    //坐标轴
    private int mAxisLineColor;
    private float mAxisLineWidth;
    //频数
    private int mFrequencyColor;
    private float mFrequencyMarginBottom;
    private float mFrequencySize;
    //变量分组
    private int mVariableGroupColor;
    private float mVariableGroupSize;
    private float mVariableGroupMarginTop;


    private Paint mPaint;
    private Paint mTextPaint;
    private Path mPath;
    private List<HistogramBean> dataList;
    private int maxTextHeight;


    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mTextPaint = new TextPaint();
        mPath = new Path();
        float scaleDensity = context.getResources().getDisplayMetrics().scaledDensity;
        float density = context.getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);
        mHistogramColor = typedArray.getColor(R.styleable.HistogramView_histogramColor, 0xff68b831);
        mHistogramWidth = typedArray.getDimension(R.styleable.HistogramView_histogramWidth, 20 * density);
        mHistogramDistance = typedArray.getDimension(R.styleable.HistogramView_histogramDistance, 10 * density);
        mFrequencyColor = typedArray.getColor(R.styleable.HistogramView_frequencyColor, 0xff68b831);
        mFrequencySize = typedArray.getFloat(R.styleable.HistogramView_frequencySize, 12 * scaleDensity);
        mFrequencyMarginBottom = typedArray.getDimension(R.styleable.HistogramView_frequencyMarginBottom, 3 * density);
        mAxisLineColor = typedArray.getColor(R.styleable.HistogramView_axisLineColor, 0xffffffff);
        mAxisLineWidth = typedArray.getInt(R.styleable.HistogramView_axisLineWidth, 3);
        mVariableGroupColor = typedArray.getColor(R.styleable.HistogramView_variableGroupColor, 0xff68b831);
        mVariableGroupSize = typedArray.getDimension(R.styleable.HistogramView_variableGroupSize, 12 * scaleDensity);
        mVariableGroupMarginTop = typedArray.getDimension(R.styleable.HistogramView_variableGroupMarginTop, 3 * density);
        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //写描述以及计算描述的最大高度
        for (int i = 0; i < dataList.size(); i++) {
            HistogramBean bean = dataList.get(i);
            int left = (int) (mHistogramDistance * (1 + i) + mHistogramWidth * i + getPaddingLeft());

            Rect textRect = new Rect();
            String text = bean.getName();
            mTextPaint.reset();
            mTextPaint.setColor(mVariableGroupColor);
            mTextPaint.setTextSize(mVariableGroupSize);
            mTextPaint.setStrokeWidth(4);
            mTextPaint.getTextBounds(text, 0, text.length(), textRect);
            if (textRect.height() > maxTextHeight) {
                maxTextHeight = textRect.height();
            }
            int offset1 = (int) ((mHistogramWidth - textRect.width()) / 2);
            canvas.drawText(text, left + offset1, getHeight() - getPaddingBottom(), mTextPaint);
        }

        //坐标图
        mPaint.reset();
        mPaint.setColor(mAxisLineColor);
        mPaint.setStrokeWidth(mAxisLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(getPaddingLeft() + mAxisLineWidth / 2, getPaddingTop() + mAxisLineWidth / 2);
        mPath.lineTo(getPaddingLeft() + mAxisLineWidth / 2, getHeight() - (mAxisLineWidth / 2 + getPaddingBottom() + maxTextHeight + mFrequencyMarginBottom));
        mPath.lineTo(getWidth() - getPaddingRight() - mAxisLineWidth / 2, getHeight() - (mAxisLineWidth / 2 + getPaddingBottom() + maxTextHeight + mFrequencyMarginBottom));
        canvas.drawPath(mPath, mPaint);


        for (int i = 0; i < dataList.size(); i++) {
            HistogramBean bean = dataList.get(i);

            //画柱状图
            int height = (int) (getHeight() - (getPaddingBottom() + getPaddingTop() + maxTextHeight + mFrequencyMarginBottom + mAxisLineWidth));
            int left = (int) (mHistogramDistance * (1 + i) + mHistogramWidth * i + getPaddingLeft() + mAxisLineWidth);
            int top = (int) (height * (1 - bean.getPercent()));
            int right = (int) ((mHistogramDistance + mHistogramWidth) * (1 + i) + getPaddingLeft() + mAxisLineWidth);
            int bottom = (int) (getHeight() - (getPaddingBottom() + maxTextHeight + mFrequencyMarginBottom + mAxisLineWidth));

            mPaint.reset();
            mPaint.setColor(mHistogramColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(left, top, right, bottom, mPaint);

            //写比例
            Rect rect = new Rect();
            String percent = String.valueOf(bean.getPercent());
            mPaint.reset();
            mPaint.setColor(mFrequencyColor);
            mPaint.setTextSize(mFrequencySize);
            mPaint.setStrokeWidth(4);
            mPaint.getTextBounds(percent, 0, percent.length(), rect);

            int offset = (int) ((mHistogramWidth - rect.width()) / 2);
            int x = left + offset;
            int y = (int) (top - mFrequencyMarginBottom);
            canvas.drawText(percent, x, y, mPaint);
        }
    }


    public void setData(List<HistogramBean> list) {
        this.dataList = list;
    }

}
