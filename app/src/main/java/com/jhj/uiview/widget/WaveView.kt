package com.jhj.uiview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.animation.LinearInterpolator
import com.jhj.uiview.R


/**
 * 正弦型函数解析式：y=Asin(ωx+φ)+b
 * 各常数值对函数图像的影响：
 * φ：决定波形与X轴位置关系或横向移动距离（左加右减）
 * ω：决定周期（最小正周期T=2π/∣ω∣）
 * A：决定峰值（即纵向拉伸压缩的倍数）
 * b：表示波形在Y轴的位置关系或纵向移动距离（上加下减）
 */
class WaveView : View {

    private val mPaint = Paint()
    private val textRect = Rect()
    private val circlePath = Path()
    private val wavePath = Path()


    private var mPercentTextColor: Int = PERCENT_TEXT_COLOR
    private var mPercentTextWidth: Float = PERCENT_TEXT_WIDTH
    private var mPercentTextSize: Float = PERCENT_TEXT_SIZE

    private var mWaveSinColor: Int = 0x88ff0000.toInt()

    /**
     * 振幅
     */
    private var A: Int = 8
    /**
     * 偏距
     */
    private var K: Int = 75

    /**
     * 波形的颜色
     */
    private var waveColor = -0x550081c9

    /**
     * 初相
     */
    private var φ: Float = 0.toFloat()

    /**
     * 波形移动的速度
     */
    private var waveSpeed = 10f

    /**
     * 角速度
     */
    private var ω: Double = 0.toDouble()

    /**
     * 开始位置相差多少个周期
     */
    private var startPeriod: Double = 1.toDouble()

    /**
     * 是否直接开启波形
     */
    private var waveStart: Boolean = true


    private val textPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = mPercentTextColor
            mPaint.strokeWidth = mPercentTextWidth
            mPaint.textSize = mPercentTextSize
            return mPaint
        }

    private val wavePaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = mWaveSinColor
            mPaint.style = Paint.Style.FILL_AND_STROKE;
            mPaint.strokeWidth = 1f
            return mPaint
        }

    private val wavePaint1: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = mWaveSinColor
            mPaint.style = Paint.Style.FILL_AND_STROKE;
            mPaint.strokeWidth = 1f
            return mPaint
        }

    private val testPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = 0xff000000.toInt()
            mPaint.style = Paint.Style.STROKE;
            mPaint.strokeWidth = 1f
            return mPaint
        }
    val circle = SparseArray<Double>()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.WaveView)
        typedArray?.let {
            mPercentTextColor = it.getColor(R.styleable.WaveView_wavePercentTextColor, PERCENT_TEXT_COLOR)
            mPercentTextWidth = it.getFloat(R.styleable.WaveView_wavePercentTextWidth, PERCENT_TEXT_WIDTH)
            mPercentTextSize = it.getFloat(R.styleable.WaveView_wavePercentTextSize, PERCENT_TEXT_SIZE)
        }

        typedArray?.recycle()


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredHeight > measuredWidth) {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
        } else {
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec)
        }
    }

    private var radio: Int = 0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        radio = width / 2
        for (i in 0..width) {
            val y = Math.sqrt(Math.pow(radio.toDouble(), 2.0) - Math.pow((radio - i).toDouble(), 2.0))
            circle.put(i, y)
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 中间字体百分比
        val percent = "60%"
        textPaint.getTextBounds(percent, 0, percent.length, textRect)
        val startTextX = (width / 2 - textRect.width() / 2).toFloat()
        val baseLine = (height / 2 + textRect.height() / 2).toFloat()
        canvas?.drawText(percent, startTextX, baseLine, textPaint)
        ω = 2 * Math.PI / width


        fillBottom(canvas, 0.toDouble(), wavePaint)
        fillBottom(canvas, 0.5.toDouble(), wavePaint1)
        postInvalidateDelayed(200)
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), testPaint)

    }

    private fun fillBottom(canvas: Canvas?, startPeriod: Double, paint: Paint) {

        φ -= waveSpeed / 100
        var y: Float

        wavePath.reset()
        // wavePath.moveTo(0f, 0f)

        K = 50
        var x = 0f
        while (x <= width) {
            y = (A * Math.sin(ω * x + φ.toDouble() + Math.PI * startPeriod) + K).toFloat()
            val endY = width / 2 + circle[x.toInt()].toFloat()
            val startY = width / 2 - circle[x.toInt()].toFloat()
            val a = Math.sqrt(Math.pow(radio.toDouble(), 2.0) - Math.pow((radio - K - A / 2).toDouble(), 2.0))

            if (K < width / 2) {
                if (x.toInt() < (radio - a.toInt()) || (x > radio && (x - radio) > a.toInt())) {
                    wavePath.moveTo(x, startY)
                    wavePath.lineTo(x, endY)
                } else {
                    wavePath.moveTo(x, endY)
                    wavePath.lineTo(x, y)
                }
            } else {

            }
            x++
        }
        canvas?.save()
        canvas?.drawPath(wavePath, paint)
        canvas?.restore()

    }

    companion object {
        const val PERCENT_TEXT_COLOR = 0xff000000.toInt()
        const val PERCENT_TEXT_SIZE = 40f
        const val PERCENT_TEXT_WIDTH = 9f

        const val WAVE_SIN_COLOR = 0xffff0000.toInt()
    }

    private fun initAnimation() {
        val valueAnimator = ValueAnimator.ofInt(0, width)
        valueAnimator.duration = 10000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener {
            /**
             * 刷新页面调取onDraw方法，通过变更φ 达到移动效果
             */
            postInvalidate()
        }
        if (waveStart) {
            valueAnimator.start()
        }
    }

}