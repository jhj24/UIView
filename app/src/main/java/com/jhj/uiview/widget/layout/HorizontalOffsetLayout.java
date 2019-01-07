package com.jhj.uiview.widget.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 每行横向偏移
 */
public class HorizontalOffsetLayout extends ViewGroup {

    int offset = 50;


    public HorizontalOffsetLayout(Context context) {
        super(context);
    }

    public HorizontalOffsetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalOffsetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(getMeasureWidth(widthMeasureSpec), getMeasureHeight(heightMeasureSpec));
    }

    private int getMeasureHeight(int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int result;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                height = height + child.getMeasuredHeight();
            }
            result = height + getPaddingTop() + getPaddingBottom();
        }
        return result;
    }

    private int getMeasureWidth(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int result;

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int width = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (width < (offset * i) + child.getMeasuredWidth()) {
                    width = (offset * i) + child.getMeasuredWidth();
                }
            }
            result = width + getPaddingLeft() + getPaddingRight();
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == View.GONE) {
                continue;
            }
            int left = getPaddingLeft() + offset * i;
            int top = getPaddingTop() + view.getMeasuredHeight() * i;
            int right = getPaddingLeft() + view.getMeasuredWidth() + offset * i;
            int bottom = getPaddingTop() + view.getMeasuredHeight() * (i + 1);
            view.layout(left, top, right, bottom);
        }
    }
}
