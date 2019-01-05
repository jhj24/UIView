package com.jhj.uiview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

public class FinishButton extends AppCompatTextView {
    public FinishButton(Context context) {
        this(context, null);
    }

    public FinishButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FinishButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        super.setOnClickListener(l);

        startAnimation(anim());
    }

    private Animation anim() {

        

        return null;
    }
}
