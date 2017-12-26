package com.goockr.smsantilost.graphics

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.View
import com.goockr.smsantilost.R
import cxx.utils.NotNull

/**
 *
 * @author LJN
 */
class AccelerateCircularView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                       defStyle: Int = 0) : View(context, attrs, defStyle) {
    /**
     * 圆环半径、图片的旋转半径
     */
    private val mRingRadius = 40f
    protected var currentAngle = -1.0
    private val mCycleTime: Float
    private val mPaint: Paint
    private val bitmap: Bitmap
    private var animator: ValueAnimator? = null

    init {
        val attrsArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.AccelerateCircularView, defStyle, 0)
        mCycleTime = attrsArray.getFloat(
                R.styleable.AccelerateCircularView_cycleTime, 1500f)
        attrsArray.recycle()
        mPaint = Paint()
        bitmap = BitmapFactory.decodeResource(getContext().resources, R.mipmap.icon_in_search)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mWidth = 0
        var mHeight = 0
        val central = Math.max(bitmap.width / 3, bitmap.height / 3)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == View.MeasureSpec.EXACTLY) {
            mWidth = widthSize
        } else {
            mWidth = central
            if (widthMode == View.MeasureSpec.AT_MOST) {
                mWidth = (central.toFloat() + bitmap.width.toFloat() + mRingRadius).toInt()
            }

        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            mHeight = heightSize
        } else {
            mHeight = central
            if (heightMode == View.MeasureSpec.AT_MOST) {
                mHeight = (context.resources.getDimension(R.dimen.x24) + bitmap.height.toFloat() + mRingRadius).toInt()
            }
        }
        setMeasuredDimension(mWidth, mHeight)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val central = mRingRadius.toInt() + 2
        mPaint.style = Style.FILL
        mPaint.isAntiAlias = true
        if (currentAngle == -1.0) {
            startCirMotion()
        }
        // 绘制图片
        drawGlobule(canvas, central.toFloat() + 20)
    }

    /**
     * 绘制tu图片,起始位置为圆环最低点
     *
     */
    private fun drawGlobule(canvas: Canvas, central: Float) {
        val cx = central + (mRingRadius * Math.cos(currentAngle)).toFloat()
        val cy = (central + mRingRadius * Math.sin(currentAngle)).toFloat()
        canvas.drawBitmap(bitmap, cx, cy, mPaint)
    }

    /**
     * 旋转小球
     */
    private fun startCirMotion() {
        animator = ValueAnimator.ofFloat(90f, 450f)
        animator?.setDuration(mCycleTime.toLong())?.repeatCount = ValueAnimator.INFINITE
        animator?.addUpdateListener { animation ->
            val angle = animation.animatedValue as Float
            currentAngle = angle * Math.PI / 180
            invalidate()
        }
        //自定义开始减速到0后加速到初始值的Interpolator
        animator?.interpolator = TimeInterpolator { input ->
            val output: Float = if (input < 0.5) {
                Math.sin(input * Math.PI).toFloat() / 2
            } else {
                1 - Math.sin(input * Math.PI).toFloat() / 2
            }
            output
        }
        animator?.start()
    }


    fun stopAnimator() {
        if (NotNull.isNotNull(animator)) {
            animator?.cancel()
        }
    }
    fun startAnimator() {
        if (NotNull.isNotNull(animator)) {
            animator?.start()
        }
    }
}
