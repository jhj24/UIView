package com.jhj.uiview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jhj.uiview.R;

public class RingChartView extends LinearLayout {


    private Paint mPaint;

    private float ringWidth;
    private int ringColor;


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
        ringWidth = typedArray.getFloat(R.styleable.RingChartView_ringWidth, 15 * density);
        ringColor = typedArray.getColor(R.styleable.RingChartView_ringColor, 0xFF68b831);


        mPaint = new Paint();
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = getPaddingLeft() + ringWidth / 2;
        float top = getPaddingTop() + ringWidth / 2;
        float right = getWidth() - getPaddingRight() - ringWidth / 2;
        float bottom = getHeight() - getPaddingBottom() - ringWidth / 2;


    }
}
