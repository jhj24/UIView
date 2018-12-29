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

public class LineChartView extends View {

    private List<HistogramBean> mDataList;
    private float num;
    private int mMaxTextHeight;
    private Paint mPaint;
    private Path mPath;

    //直方图
    private int mLineChartColor;
    private float mLineChartWidth;
    private float mLineChartDistance;
    //点
    private boolean isPointRound;
    private float mPointSize;
    private int mPointColor;
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
        mLineChartWidth = typedArray.getInt(R.styleable.LineChartView_lineChartWidth, 3);
        isPointRound = typedArray.getBoolean(R.styleable.LineChartView_isPointRound, true);
        mPointColor = typedArray.getColor(R.styleable.LineChartView_pointColor, 0xff68b831);
        mPointSize = typedArray.getDimension(R.styleable.LineChartView_pointSize, 3 * density);
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
        mPaint.setTextSize(mVariableGroupSize);
        mPaint.setColor(mVariableGroupColor);

        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mDataList.size(); i++) {
            String text = mDataList.get(i).getName();

            float x = getLeft() + mAxisLineWidth + mLineChartDistance * (1 + i);

            //变量分组
            mPaint.reset();
            mPaint.setTextSize(mVariableGroupSize);
            mPaint.setColor(mVariableGroupColor);
            float startX = x - mPaint.measureText(text) / 2;
            canvas.drawText(text, startX, getHeight() - getPaddingBottom(), mPaint);

            //画点
            mPaint.reset();
            mPaint.setColor(mPointColor);
            mPaint.setStrokeWidth(mPointSize);
            mPaint.setStrokeCap(isPointRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);
            float axisHeight = getHeight() - (getPaddingBottom() + getPaddingTop() + mMaxTextHeight + mVariableGroupMarginTop + mAxisLineWidth);
            float y = axisHeight * (1 - (mDataList.get(i).getPercent() / num));
            canvas.drawPoint(x, y, mPaint);

            //比例
            String percent = String.valueOf(mDataList.get(i).getPercent());
            mPaint.reset();
            mPaint.setColor(mFrequencyColor);
            mPaint.setTextSize(mFrequencySize);
            canvas.drawText(percent, x - mPaint.measureText(percent) / 2, y - mFrequencyMarginBottom, mPaint);

            //折线图
            if (i == 0) {
                mPath.moveTo(x, y);
            }
            mPath.lineTo(x, y);
            if (i == mDataList.size() -1){
                mPaint.reset();
                mPaint.setColor(mLineChartColor);
                mPaint.setStrokeWidth(mLineChartWidth);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(mPath, mPaint);
            }
        }

        //坐标轴
        float axisBottom = getHeight() - (getPaddingBottom() + mAxisLineWidth/2 + mMaxTextHeight + mVariableGroupMarginTop);
        mPaint.reset();
        mPaint.setColor(mAxisLineColor);
        mPaint.setStrokeWidth(mAxisLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(getPaddingLeft() + mAxisLineWidth / 2, getPaddingTop() + mAxisLineWidth / 2);
        mPath.lineTo(getPaddingLeft() + mAxisLineWidth / 2, axisBottom);
        mPath.lineTo(getWidth() - (getPaddingRight() + mAxisLineWidth / 2), axisBottom);
        canvas.drawPath(mPath, mPaint);
    }


    public void setDataList(List<HistogramBean> dataList) {
        this.mDataList = dataList;
        for (int i = 0; i < dataList.size(); i++) {
            Rect rect = new Rect();
            String string = dataList.get(i).getName();
            mPaint.getTextBounds(string, 0, string.length(), rect);
            if (mMaxTextHeight < rect.height()) {
                mMaxTextHeight = rect.height();
            }
            num += mDataList.get(i).getPercent();
        }
    }
}
