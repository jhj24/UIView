package com.jhj.uiview.widget.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jhj.uiview.R;

/**
 * 图片覆盖
 * Created by jhj on 19-1-7.
 */
public class CoverImageLayout extends ViewGroup {

    private float coverSize;

    public CoverImageLayout(Context context) {
        this(context, null);
    }

    public CoverImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoverImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float density = getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CoverImageLayout);
        coverSize = typedArray.getFloat(R.styleable.CoverImageLayout_coverSize, 15 * density);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        int measuredWidth;
        int measuredHeight;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            childWidth = Math.max(childWidth, child.getMeasuredWidth());
            childHeight = Math.max(childHeight, child.getMeasuredHeight());
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

        if (wMode == MeasureSpec.EXACTLY) {
            measuredWidth = wSize;
        } else {
            measuredWidth = (int) ((childWidth - coverSize) * getChildCount() + coverSize + getPaddingLeft() + getPaddingRight());
        }

        if (hMode == MeasureSpec.EXACTLY) {
            measuredHeight = hSize;
        } else {
            measuredHeight = childHeight + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            int left = (int) (getPaddingLeft() + (child.getMeasuredWidth() - coverSize) * i);
            int top = getPaddingTop();
            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);
        }
    }
}
