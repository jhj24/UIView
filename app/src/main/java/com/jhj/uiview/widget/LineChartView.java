package com.jhj.uiview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jhj.uiview.R;
import com.jhj.uiview.bean.HistogramBean;

import java.util.List;

public class LineChartView extends View {

    private List<HistogramBean> mDataList;
    private float num;
    private int mMaxTextHeight;
    private Paint mTextPaint;
    private Paint mPaint;
    private Path mPath;

    //直方图
    private int mLineChartColor;
    private float mLineChartWidth;
    private float mLineChartDistance;
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

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float scaleDensity = getResources().getDisplayMetrics().scaledDensity;
        float density = getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        mLineChartColor = typedArray.getColor(R.styleable.LineChartView_lineChartColor, 0xff68b831);
        mLineChartDistance = typedArray.getDimension(R.styleable.LineChartView_lineChartDistance, 50 * density);
        mAxisLineColor = typedArray.getColor(R.styleable.LineChartView_axisLineColor, 0xFF68b831);
        mAxisLineWidth = typedArray.getInt(R.styleable.LineChartView_axisLineWidth, 3);
        mFrequencyColor = typedArray.getColor(R.styleable.LineChartView_frequencyColor, 0xff68b831);
        mFrequencyMarginBottom = typedArray.getDimension(R.styleable.LineChartView_frequencyMarginBottom, 3 * density);
        mFrequencySize = typedArray.getDimension(R.styleable.LineChartView_frequencySize, 12 * scaleDensity);
        mVariableGroupColor = typedArray.getColor(R.styleable.LineChartView_variableGroupColor, 0xff68b831);
        mVariableGroupSize = typedArray.getDimension(R.styleable.LineChartView_variableGroupSize, 12 * density);
        mVariableGroupMarginTop = typedArray.getDimension(R.styleable.LineChartView_variableGroupMarginTop, 3 * density);

        mPath = new Path();
        mPaint = new Paint();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mVariableGroupSize);
        mTextPaint.setColor(mVariableGroupColor);

        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //坐标轴
        float axisBottom = getHeight() - (getPaddingBottom() + mAxisLineWidth / 2 + mMaxTextHeight + mVariableGroupMarginTop);
        mPaint.setColor(mAxisLineColor);
        mPaint.setStrokeWidth(mAxisLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(getPaddingLeft() + mAxisLineWidth / 2, getPaddingTop() + mAxisLineWidth / 2);
        mPath.lineTo(getPaddingLeft() + mAxisLineWidth / 2, axisBottom);
        mPath.lineTo(getWidth() - (getPaddingRight() + mAxisLineWidth / 2), axisBottom);
        canvas.drawPath(mPath, mPaint);


        for (int i = 0; i < mDataList.size(); i++) {
            String text = mDataList.get(i).getName();

            float x = getLeft() + mAxisLineWidth + mLineChartDistance * (1 + i);

            //变量分组
            Rect bound = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), bound);
            float startX = x - bound.width();
            canvas.drawText(text, startX, getHeight() - getPaddingBottom(), mTextPaint);

            //画点
            mPaint.reset();
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(10);
            float axisHeight = getHeight() - (getPaddingBottom() + getPaddingTop() + mMaxTextHeight + mVariableGroupMarginTop + mAxisLineWidth);
            float y = axisHeight * (1 - (mDataList.get(i).getPercent() / num));
            canvas.drawPoint(x, y, mPaint);

            //折线图

            //比例
            String percent = String.valueOf(mDataList.get(i).getPercent());
            Rect rect = new Rect();
            mPaint.reset();
            mPaint.setColor(mFrequencyColor);
            mPaint.setTextSize(mFrequencySize);
            mPaint.setStrokeWidth(4);
            mPaint.getTextBounds(percent, 0, percent.length(), rect);
            float offset = x - rect.width();
            canvas.drawText(String.valueOf(mDataList.get(i).getPercent()), offset, y - mFrequencyMarginBottom, mPaint);

        }
    }


    public void setDataList(List<HistogramBean> dataList) {
        this.mDataList = dataList;
        for (int i = 0; i < dataList.size(); i++) {
            Rect rect = new Rect();
            mTextPaint.getTextBounds(mDataList.get(i).getName(), 0, mDataList.get(i).getName().length(), rect);
            if (mMaxTextHeight < rect.height()) {
                mMaxTextHeight = rect.height();
            }
            num += mDataList.get(i).getPercent();
        }
    }
}
