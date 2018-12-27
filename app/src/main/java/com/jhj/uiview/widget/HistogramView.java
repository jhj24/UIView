package com.jhj.uiview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jhj.uiview.R;
import com.jhj.uiview.bean.HistogramBean;

import java.util.List;


/**
 * 柱状图
 */
public class HistogramView extends View {

    private Paint mPaint;
    private Path mPath;

    private float rectWidth;
    private float rectInterval;
    private int rectColor;
    private List<HistogramBean> dataList;


    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPath = new Path();
        float density = context.getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistogramView);
        rectWidth = typedArray.getDimension(R.styleable.HistogramView_rectWidth, 20 * density);
        rectInterval = typedArray.getDimension(R.styleable.HistogramView_rectInterval, 10 * density);
        rectColor = typedArray.getColor(R.styleable.HistogramView_rectColor, 0xff68b831);
        typedArray.recycle();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //坐标图
        mPaint.setColor(0xffffffff);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(getPaddingLeft(), getPaddingTop());
        mPath.lineTo(getPaddingLeft(), getHeight() - getPaddingBottom());
        mPath.lineTo(getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        canvas.drawPath(mPath, mPaint);

        //矩形
        mPaint.setColor(rectColor);
        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < dataList.size(); i++) {
            HistogramBean bean = dataList.get(i);
            int height = getHeight() - (getPaddingBottom() + getPaddingTop());
            int left = (int) (rectInterval * (1 + i) + rectWidth * i + getPaddingLeft());
            int top = (int) (height * bean.getPercent());
            int right = (int) ((rectInterval + rectWidth) * (1 + i) + getPaddingLeft());
            int bottom = getHeight() - getPaddingBottom();

            canvas.drawRect(left, top, right, bottom, mPaint);
        }


    }


    public void setData(List<HistogramBean> list) {
        this.dataList = list;
    }

}
