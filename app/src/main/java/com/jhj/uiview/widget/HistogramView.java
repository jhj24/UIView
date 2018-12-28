package com.jhj.uiview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
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
    private Path mPath;
    private List<HistogramBean> mDataList;
    private int mMaxTextHeight;
    private float num;


    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setColor(mVariableGroupColor);
        mPaint.setTextSize(mVariableGroupSize);
        mPaint.setStrokeWidth(4);


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


        for (int i = 0; i < mDataList.size(); i++) {
            HistogramBean bean = mDataList.get(i);
            float height = getHeight() - (getPaddingBottom() + getPaddingTop() + mMaxTextHeight + mVariableGroupMarginTop + mAxisLineWidth);
            float left = getPaddingLeft() + mAxisLineWidth + mHistogramDistance * (i + 1) + mHistogramWidth * i;
            float top = height * (1 - bean.getPercent() / num);
            float right = getPaddingLeft() + mAxisLineWidth + (mHistogramDistance + mHistogramWidth) * (i + 1);
            float bottom = getHeight() - (getPaddingBottom() + mMaxTextHeight + mVariableGroupMarginTop + mAxisLineWidth);


            //写描述
            Rect bound = new Rect();
            mPaint.reset();
            mPaint.setColor(mVariableGroupColor);
            mPaint.setTextSize(mVariableGroupSize);
            mPaint.setStrokeWidth(4);
            mPaint.getTextBounds(bean.getName(), 0, bean.getName().length(), bound);
            float offset1 = (mHistogramWidth - bound.width()) / 2;
            canvas.drawText(bean.getName(), left + offset1, getHeight() - getPaddingBottom(), mPaint);

            //画柱状图
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

            float offset = (mHistogramWidth - rect.width()) / 2;
            float x = left + offset;
            float y = (int) (top - mFrequencyMarginBottom);
            canvas.drawText(percent, x, y, mPaint);

        }

        //坐标图
        float axisBottom = getHeight() - (getPaddingBottom() + mAxisLineWidth / 2 + mMaxTextHeight + mVariableGroupMarginTop);
        mPaint.reset();
        mPaint.setColor(mAxisLineColor);
        mPaint.setStrokeWidth(mAxisLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(getPaddingLeft() + mAxisLineWidth / 2, getPaddingTop() + mAxisLineWidth / 2);
        mPath.lineTo(getPaddingLeft() + mAxisLineWidth / 2, axisBottom);
        mPath.lineTo(getWidth() - (getPaddingRight() + mAxisLineWidth / 2), axisBottom);
        canvas.drawPath(mPath, mPaint);
    }


    public void setData(List<HistogramBean> list) {
        this.mDataList = list;
        for (int i = 0; i < mDataList.size(); i++) {
            Rect rect = new Rect();
            mPaint.getTextBounds(mDataList.get(i).getName(), 0, mDataList.get(i).getName().length(), rect);
            if (mMaxTextHeight < rect.height()) {
                mMaxTextHeight = rect.height();
            }
            num += mDataList.get(i).getPercent();
        }
    }

}
