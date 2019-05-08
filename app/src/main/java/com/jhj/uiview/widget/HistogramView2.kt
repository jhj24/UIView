package com.jhj.uiview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.jhj.uiview.R
import com.jhj.uiview.bean.BimBean
import com.jhj.uiview.scaleDensity
import java.util.*

class HistogramView2 : View {

    private var mTextColor: Int = TEXT_COLOR
    private var mTextSize: Float = TEXT_SIZE
    private var mTextWidth: Float = TEXT_WIDTH
    private var mExcellentColor: Int = EXCELLENT_COLOR
    private var mQualifiedColor: Int = QUALIFIED_COLOR
    private var mRectDistance: Float = RECT_DISTANCE

    private val mPaint: Paint = Paint()
    private var chartBean: List<BimBean>? = null
    private var textHeight: Int
    private var mRoundRectWidth: Float = 0f
    private var mRoundRectHeight: Float = 0f
    private var mRoundRectRadio: Float = 0f
    private val excellentRect = RectF()
    private val qualifiedRect = RectF()

    private val blankRate = 0.5

    fun setChartList(list: List<BimBean>) {
        this.chartBean = list
        invalidate()
    }

    private val textPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = mTextWidth
            mPaint.textSize = mTextSize * ((context?.scaleDensity) ?: 3f)
            mPaint.color = mTextColor
            return mPaint
        }


    private val excellentRectPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = mExcellentColor
            return mPaint
        }

    private val qualifiedRectPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = mQualifiedColor
            return mPaint
        }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //以“优秀” 为基准，获取汉字高度
        val textRect = Rect()
        textPaint.getTextBounds("优秀", 0, "优秀".length, textRect)
        textHeight = textRect.height()

        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.BarChartView)
        typedArray?.let {
            mTextColor = it.getColor(R.styleable.BarChartView_textColor, TEXT_COLOR)
            mTextSize = it.getFloat(R.styleable.BarChartView_textSize, TEXT_SIZE)
            mTextWidth = it.getFloat(R.styleable.BarChartView_textWidth, TEXT_WIDTH)
            mExcellentColor = it.getColor(R.styleable.BarChartView_excellentColor, EXCELLENT_COLOR)
            mQualifiedColor = it.getColor(R.styleable.BarChartView_qualifiedColor, QUALIFIED_COLOR)
            mRectDistance = it.getFloat(R.styleable.BarChartView_xRectDistance, RECT_DISTANCE)
            mRoundRectWidth = it.getFloat(R.styleable.BarChartView_roundRectWidth, (textHeight * 1.5).toFloat())
            mRoundRectHeight = it.getFloat(R.styleable.BarChartView_roundRectHeight, textHeight.toFloat())
            mRoundRectRadio = it.getFloat(R.styleable.BarChartView_roundRectRadio, RADIO)
        }
        typedArray?.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //柱状图的坐标
        val barChartXStart = (paddingLeft).toFloat()
        val barChartXEnd = (width - paddingRight).toFloat()
        val barChartYStart = (paddingTop + mRoundRectHeight * 3 + DISTANCE).toFloat()
        val barChartYEnd = (height - (paddingBottom + textHeight + DISTANCE)).toFloat()

        //条状图的宽高
        val barChartWidth = barChartXEnd - barChartXStart //条状图宽度
        val barChartHeight = barChartYEnd - barChartYStart //条状图高度


        //颜色及说明宽度 = 矩形宽度 × 2 + 间距 × 3 + 优秀宽度 + 合格宽度
        val infoWidth = textPaint.measureText("合格") + textPaint.measureText("优良") + mRoundRectWidth * 2 + DISTANCE * 10
        val excellentInfoStartX = width - (infoWidth + paddingRight)
        val infoEndY = mRoundRectHeight + paddingTop

        val excellentRectF = RectF(excellentInfoStartX, paddingTop.toFloat(), excellentInfoStartX + mRoundRectWidth, (paddingTop + mRoundRectWidth - 10).toFloat())
        canvas?.drawRoundRect(excellentRectF, RADIO, RADIO, excellentRectPaint)
        canvas?.drawText("优良", excellentRectF.right + DISTANCE, infoEndY, textPaint)

        val qualifiedInfoStartX = width - (paddingRight + textPaint.measureText("合格") + DISTANCE + mRoundRectWidth)
        val qualifiedRectF = RectF(qualifiedInfoStartX, paddingTop.toFloat(), qualifiedInfoStartX + mRoundRectWidth, (paddingTop + mRoundRectWidth - 10).toFloat())
        canvas?.drawRoundRect(qualifiedRectF, RADIO, RADIO, qualifiedRectPaint)
        canvas?.drawText("合格", qualifiedRectF.right + DISTANCE, infoEndY, textPaint)

        val size = chartBean.orEmpty().size
        //最少分四块
        val blockSize = if (size > 3) size else 4

        val maxList = chartBean
                ?.map {
                    Math.max(it.excellent, it.qualified)
                }

        val maxLength = if (maxList.orEmpty().isNotEmpty()) {
            Collections.max(maxList).toFloat()
        } else {
            0f
        }


        val blockWidth = barChartWidth / (blockSize + (blockSize - 1) * blankRate) //一个标段宽度
        val rectWidth = blockWidth / 5 * 2 //方块宽度

        chartBean?.forEachIndexed { index, bimBean ->
            //优秀
            val excellentStartX = (barChartXStart + (blockWidth + blockWidth * blankRate) * index).toFloat()
            val excellentTextStartX = (excellentStartX + (rectWidth - textPaint.measureText(bimBean.excellent.toString())) / 2).toFloat()
            val excellentStartY = (barChartYStart + ((1 - bimBean.excellent / maxLength) * barChartHeight)).toFloat()
            excellentRect.set(excellentStartX, excellentStartY, (excellentStartX + rectWidth).toFloat(), barChartYEnd)
            canvas?.drawRoundRect(excellentRect, RADIO, RADIO, excellentRectPaint)
            canvas?.drawText(bimBean.excellent.toString(), excellentTextStartX, excellentStartY - DISTANCE, textPaint)

            //合格
            val qualifiedStartX = (excellentStartX + rectWidth + (blockWidth / 5 * 1)).toFloat()
            val qualifiedStartY = (barChartYStart + ((1 - bimBean.qualified / maxLength) * barChartHeight)).toFloat()
            val qualifiedTextStartX = (qualifiedStartX + (rectWidth - textPaint.measureText(bimBean.qualified.toString())) / 2).toFloat()
            qualifiedRect.set(qualifiedStartX, qualifiedStartY, (qualifiedStartX + rectWidth).toFloat(), barChartYEnd)
            canvas?.drawRoundRect(qualifiedRect, RADIO, RADIO, qualifiedRectPaint)
            canvas?.drawText(bimBean.qualified.toString(), qualifiedTextStartX, qualifiedStartY - DISTANCE, textPaint)

            //标段
            val text = bimBean.section_short
            if (text != null) {
                val typeStartX = (excellentStartX + (blockWidth - textPaint.measureText(bimBean.section_short)) / 2).toFloat()
                canvas?.drawText(bimBean.section_short, typeStartX, (height - paddingBottom).toFloat(), textPaint)
            }
        }
    }

    companion object {
        const val TEXT_SIZE = 14f
        const val TEXT_COLOR = 0xff111111.toInt()
        const val TEXT_WIDTH = 3f
        const val QUALIFIED_COLOR = 0xff0050ff.toInt() // 合格
        const val EXCELLENT_COLOR = 0xff00d0ff.toInt() // 优良
        const val RECT_DISTANCE = 20f

        const val DISTANCE = 6 //文字到坐标轴的距离
        const val RADIO = 6f // 圆角矩形半径
    }
}