package com.jhj.uiview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.jhj.uiview.R
import com.jhj.uiview.bean.ChartBean
import com.jhj.uiview.scaleDensity

class BarChartView : View {

    private var mAxisLineColor: Int = AXIS_LINE_COLOR
    private var mAxisLineWidth: Float = AXIS_LINE_WIDTH
    private var mYAxisPartNum: Int = Y_PART_LINE_NUM
    private var mYAxisPartLineColor: Int = Y_PART_LINE_COLOR
    private var mYAxisPartLineWidth: Float = Y_PART_LINE_WIDTH
    private var mTextColor: Int = TEXT_COLOR
    private var mTextSize: Float = TEXT_SIZE
    private var mTextWidth: Float = TEXT_WIDTH
    private var mTitleTextColor: Int = TITLE_TEXT_COLOR
    private var mTitleTextSize: Float = TITLE_TEXT_SIZE
    private var mTitleTextWidth: Float = TITLE_TEXT_WIDTH
    private var mExcellentColor: Int = EXCELLENT_COLOR
    private var mQualifiedColor: Int = QUALIFIED_COLOR
    private var mRectDistance: Float = RECT_DISTANCE

    private val mPaint: Paint = Paint()
    private var chartBean: ChartBean? = null
    private var partMaxWidth = 0 //y轴频数字体的最大宽度
    private var partTextHeight = 0
    private var partNum = 0 //y轴频数分段短数
    private var textHeight: Int
    private var mRoundRectWidth: Float = 0f
    private var mRoundRectHeight: Float = 0f
    private var mRoundRectRadio: Float = 0f
    private var titleTextRect = Rect()


    private val textPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = mTextWidth
            mPaint.textSize = mTextSize * ((context?.scaleDensity) ?: 3f)
            mPaint.color = mTextColor
            return mPaint
        }

    private val titleTextPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = mTitleTextWidth
            mPaint.color = mTitleTextColor
            mPaint.textSize = mTitleTextSize * ((context?.scaleDensity) ?: 3f)
            return mPaint
        }

    private val partLinePaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = mYAxisPartLineWidth
            mPaint.color = mYAxisPartLineColor
            return mPaint
        }

    private val axisPaint: Paint
        get() {
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = mAxisLineColor
            mPaint.strokeWidth = mAxisLineWidth
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
            mAxisLineColor = it.getColor(R.styleable.BarChartView_AxisLineColor, AXIS_LINE_COLOR)
            mAxisLineWidth = it.getFloat(R.styleable.BarChartView_AxisLineWidth, AXIS_LINE_WIDTH)
            mYAxisPartNum = it.getInt(R.styleable.BarChartView_yAxisPartNum, Y_PART_LINE_NUM)
            mYAxisPartLineColor = it.getColor(R.styleable.BarChartView_yAxisPartColor, Y_PART_LINE_COLOR)
            mYAxisPartLineWidth = it.getFloat(R.styleable.BarChartView_yAxisPartWidth, Y_PART_LINE_WIDTH)
            mTextColor = it.getColor(R.styleable.BarChartView_textColor, TEXT_COLOR)
            mTextSize = it.getFloat(R.styleable.BarChartView_textSize, TEXT_SIZE)
            mTextWidth = it.getFloat(R.styleable.BarChartView_textWidth, TEXT_WIDTH)
            mTitleTextColor = it.getColor(R.styleable.BarChartView_titleTextColor, TITLE_TEXT_COLOR)
            mTitleTextSize = it.getFloat(R.styleable.BarChartView_titleTextSize, TITLE_TEXT_SIZE)
            mTitleTextWidth = it.getFloat(R.styleable.BarChartView_titleTextWidth, TITLE_TEXT_WIDTH)
            mExcellentColor = it.getColor(R.styleable.BarChartView_excellentColor, EXCELLENT_COLOR)
            mQualifiedColor = it.getColor(R.styleable.BarChartView_qualifiedColor, QUALIFIED_COLOR)
            mRectDistance = it.getFloat(R.styleable.BarChartView_xRectDistance, RECT_DISTANCE)
            mRoundRectWidth = it.getFloat(R.styleable.BarChartView_roundRectWidth, (textHeight * 1.5).toFloat())
            mRoundRectHeight = it.getFloat(R.styleable.BarChartView_roundRectHeight, textHeight.toFloat())
            mRoundRectRadio = it.getFloat(R.styleable.BarChartView_roundRectRadio, RADIO)
        }
        typedArray?.recycle()


        //计算y轴频数字体的最大宽度以及最大高度
        val rect = Rect()
        val maxLength = chartBean?.maxLength ?: 500
        partNum = maxLength / mYAxisPartNum
        for (part in mYAxisPartNum.downTo(0)) {
            val text = (part * partNum).toString()
            textPaint.getTextBounds(text, 0, text.length, rect)
            if (rect.width() > partMaxWidth) {
                partMaxWidth = rect.width() + TEXT_TO_AXIS_DISTANCE
            }
            if (rect.height() > partTextHeight) {
                partTextHeight = rect.height()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val title = chartBean?.title ?: "自量表"
        titleTextPaint.getTextBounds(title, 0, title.length, titleTextRect)

        //条状图的坐标
        val barChartXStart = (paddingLeft + partMaxWidth).toFloat()
        val barChartXEnd = (width - paddingRight).toFloat()
        val barChartYStart = (paddingTop + partTextHeight / 2 + titleTextRect.height() + 40).toFloat()
        val barChartYEnd = (height - (paddingBottom + textHeight + TEXT_TO_AXIS_DISTANCE + textHeight + 40)).toFloat()

        //条状图的宽高
        val barChartWidth = barChartXEnd - barChartXStart //条状图宽度
        val barChartHeight = barChartYEnd - barChartYStart //条状图高度
        val barChartCenterX = barChartXStart + barChartWidth / 2 // 条状图X轴中心点横坐标

        //标题
        canvas?.drawText(title, barChartCenterX - titleTextPaint.measureText(title) / 2, (paddingTop + titleTextRect.height()).toFloat(), titleTextPaint)

        //颜色及说明宽度 = 矩形宽度 × 2 + 间距 × 3 + 优秀宽度 + 合格宽度
        val infoWidth = mRoundRectWidth * 2 + TEXT_TO_AXIS_DISTANCE * 3 + textPaint.measureText("合格") + textPaint.measureText("优良")
        val infoStartX = barChartCenterX - infoWidth / 2
        val infoStartY = height - paddingBottom - mRoundRectHeight
        val excellentRectF = RectF(infoStartX, infoStartY, infoStartX + mRoundRectWidth, (height - paddingBottom).toFloat())
        canvas?.drawRoundRect(excellentRectF, RADIO, RADIO, excellentRectPaint)
        canvas?.drawText("优良", excellentRectF.right + TEXT_TO_AXIS_DISTANCE, (height - paddingBottom).toFloat(), textPaint)

        val qualifiedInfoStartX = infoStartX + mRoundRectWidth + textPaint.measureText("优良") + TEXT_TO_AXIS_DISTANCE * 2
        val qualifiedRectF = RectF(qualifiedInfoStartX, infoStartY, qualifiedInfoStartX + mRoundRectWidth, (height - paddingBottom).toFloat())
        canvas?.drawRoundRect(qualifiedRectF, RADIO, RADIO, qualifiedRectPaint)
        canvas?.drawText("合格", qualifiedRectF.right + TEXT_TO_AXIS_DISTANCE, (height - paddingBottom).toFloat(), textPaint)

        // X坐标轴
        canvas?.drawLine(barChartXStart, barChartYEnd, barChartXEnd, barChartYEnd, axisPaint)
        // Y坐标轴
        canvas?.drawLine(barChartXStart, barChartYStart, barChartXStart, barChartYEnd, axisPaint)

        //y频数分段平均高度
        val partHeight = barChartHeight / mYAxisPartNum
        //频数 及 频数线
        for (part in 0..mYAxisPartNum) {
            val text = ((mYAxisPartNum - part) * partNum).toString()
            if (part != mYAxisPartNum) {
                //频数线
                val partLineY = barChartYStart + part * partHeight
                canvas?.drawLine(barChartXStart, partLineY, barChartXEnd, partLineY, partLinePaint)
            }
            //频数文字
            val textStart = barChartXStart - textPaint.measureText(text)
            val baseLine = (barChartYStart + part * partHeight + partTextHeight / 2)
            canvas?.drawText(text, textStart, baseLine, textPaint)
        }


        val bidWidth = if (chartBean?.list.orEmpty().size > 3) {
            barChartWidth / chartBean?.list.orEmpty().size
        } else {
            barChartWidth / 4
        }
        val rectWidth = (bidWidth - mRectDistance) / 10 * 3
        val rectDistance = (bidWidth - mRectDistance) / 10 * 2
        val maxLength = chartBean?.maxLength ?: 500
        chartBean?.list?.forEachIndexed { index, itemBean ->
            val excellentStartX = barChartXStart + rectDistance + (bidWidth - mRectDistance) * index
            val excellentStartY = barChartYStart + ((1 - (itemBean.excellentNum / maxLength)) * barChartHeight)
            val qualifiedStartX = excellentStartX + rectWidth + mRectDistance
            val qualifiedStartY = barChartYStart + ((1 - (itemBean.qualifiedNum / maxLength)) * barChartHeight)

            canvas?.drawRect(excellentStartX, excellentStartY, excellentStartX + rectWidth, barChartYEnd, excellentRectPaint) //优良
            canvas?.drawRect(qualifiedStartX, qualifiedStartY, qualifiedStartX + rectWidth, barChartYEnd, qualifiedRectPaint) //合格

            //合格优良个数
            val excellentTextStartX = (excellentStartX + rectWidth / 2) - textPaint.measureText(itemBean.excellentNum.toString()) / 2
            val qualifiedTextStartX = (excellentStartX + rectWidth / 2) - textPaint.measureText(itemBean.excellentNum.toString()) / 2
            canvas?.drawText(itemBean.excellentNum.toString(), excellentTextStartX, excellentStartY - TEXT_TO_AXIS_DISTANCE, textPaint)//优良
            canvas?.drawText(itemBean.qualifiedNum.toString(), qualifiedTextStartX, qualifiedStartY - TEXT_TO_AXIS_DISTANCE, textPaint)//合格

            //X标段
            val textStartX = barChartXStart + (bidWidth - textPaint.measureText(itemBean.type)) / 2
            canvas?.drawText(itemBean.type, textStartX, (barChartYEnd + textHeight + TEXT_TO_AXIS_DISTANCE).toFloat(), textPaint)
        }

        // =============测试代码开始=================
        val excellentStartX = barChartXStart + rectDistance + (bidWidth - mRectDistance) * 0
        val excellentStartY = barChartYStart + ((1 - 0.2) * barChartHeight).toFloat()
        val qualifiedStartX = excellentStartX + rectWidth + mRectDistance
        val qualifiedStartY = barChartYStart + ((1 - 0.2) * barChartHeight).toFloat()

        canvas?.drawRect(excellentStartX, excellentStartY, excellentStartX + rectWidth, barChartYEnd, excellentRectPaint) //优良
        canvas?.drawRect(qualifiedStartX, qualifiedStartY, qualifiedStartX + rectWidth, barChartYEnd, qualifiedRectPaint) //优良

        val excellentTextStartX = (excellentStartX + rectWidth / 2) - textPaint.measureText("111") / 2
        val qualifiedTextStartX = (qualifiedStartX + rectWidth / 2) - textPaint.measureText("22") / 2
        canvas?.drawText("111", excellentTextStartX, excellentStartY - TEXT_TO_AXIS_DISTANCE, textPaint)//优良
        canvas?.drawText("22", qualifiedTextStartX, qualifiedStartY - TEXT_TO_AXIS_DISTANCE, textPaint)//合格

        val textStartX = barChartXStart + (bidWidth - textPaint.measureText("标段一")) / 2
        canvas?.drawText("标段一", textStartX, barChartYEnd + textHeight + TEXT_TO_AXIS_DISTANCE, textPaint)
        // =============测试代码结束=================


    }

    companion object {
        const val AXIS_LINE_WIDTH = 1f
        const val AXIS_LINE_COLOR = 0xff111111.toInt()
        const val Y_PART_LINE_COLOR = 0xff111111.toInt()
        const val Y_PART_LINE_WIDTH = 1f //线条宽度
        const val Y_PART_LINE_NUM = 5 //分割成几段
        const val TEXT_SIZE = 12f
        const val TEXT_COLOR = 0xff111111.toInt()
        const val TEXT_WIDTH = 3f
        const val TITLE_TEXT_SIZE = 18f
        const val TITLE_TEXT_COLOR = 0xff008ACD.toInt()
        const val TITLE_TEXT_WIDTH = 3f
        const val QUALIFIED_COLOR = 0xffb6a2de.toInt() // 合格
        const val EXCELLENT_COLOR = 0xff2EC7C9.toInt() // 优良
        const val RECT_DISTANCE = 20f

        const val TEXT_TO_AXIS_DISTANCE = 6 //文字到坐标轴的距离
        const val RADIO = 6f // 圆角矩形半径
    }
}