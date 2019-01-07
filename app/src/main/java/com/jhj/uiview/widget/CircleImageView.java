package com.jhj.uiview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.jhj.uiview.R;

/**
 * 圆形图片
 */
public class CircleImageView extends AppCompatImageView {

    private Paint mPaint;
    private float radio;

    private boolean isNeedBorder;
    private int borderColor;
    private int borderWidth;


    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        isNeedBorder = typedArray.getBoolean(R.styleable.CircleImageView_isNeedImageBorder, false);
        borderColor = typedArray.getColor(R.styleable.CircleImageView_imageBorderColor, 0xff68b831);
        borderWidth = typedArray.getInt(R.styleable.CircleImageView_imageBorderWidth, 3);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() > getMeasuredWidth()) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        } else {
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        float height = getHeight() - (getPaddingBottom() + getPaddingTop());
        float width = getWidth() - (getPaddingRight() + getPaddingLeft());
        radio = Math.min(height, width) / 2;
        float x = getPaddingLeft() + radio;
        float y = getPaddingTop() + radio;
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setShader(getShader());
        canvas.drawCircle(x, y, radio, mPaint);

        if (isNeedBorder) {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setColor(borderColor);
            mPaint.setStrokeWidth(borderWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(x, y, radio - borderWidth / 2, mPaint);
        }
    }

    private BitmapShader getShader() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return null;
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        }

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        float yScale = (radio * 2) / bitmap.getHeight();
        float xScale = (radio * 2) / bitmap.getWidth();
        matrix.setScale(xScale, yScale);
        matrix.postTranslate(getPaddingLeft(), getPaddingTop());
        shader.setLocalMatrix(matrix);
        return shader;
    }
}
