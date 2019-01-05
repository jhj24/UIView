package com.jhj.uiview.widget.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 流动标签
 */
public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = 0;
        int resultHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSize;
        } else {
            //剩余宽度
            int overWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
            int width = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int childWidth = params.leftMargin + params.rightMargin + child.getMeasuredWidth();
                //换行
                if (childWidth > overWidth) {
                    width = childWidth;
                    overWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
                } else {
                    width += childWidth;
                }
                if (resultWidth < width) {
                    resultWidth = width;
                }
                overWidth -= childWidth;
            }
            resultWidth += getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            resultHeight = heightSize;
        } else {
            //剩余宽度
            int overWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
            int maxHeight = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                int childWidth = params.leftMargin + params.rightMargin + child.getMeasuredWidth();
                //换行
                if (childWidth > overWidth) {
                    //上一行中高度最高行的高度
                    resultHeight += maxHeight;
                    //初始化该行行高
                    maxHeight = childHeight;
                    //初始化该行宽度
                    overWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
                } else {
                    maxHeight = Math.max(maxHeight, childHeight);
                }
                if (i == getChildCount() - 1) {
                    resultHeight += maxHeight;
                }
                overWidth -= childWidth;
            }

            resultHeight += getPaddingBottom() + getPaddingTop();
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int maxHeight = 0;
        //每一行的剩余宽度
        int overWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());

        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            maxHeight = Math.max(maxHeight, childHeight);
            //另起一行
            if (childWidth > overWidth) {
                //初始化该行起始Y坐标
                top += maxHeight;
                //初始分该行起始X坐标
                left = getPaddingLeft();
                //初始分该行行高
                maxHeight = childHeight;
                //初始分该行宽度
                overWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
            }
            left += lp.leftMargin;

            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);

            overWidth -= childWidth;
            left += child.getMeasuredWidth() + lp.rightMargin;
        }
    }


    /**
     * 因为我们需要支持margin，所以需要返回MarginLayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
