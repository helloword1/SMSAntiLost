package com.goockr.smsantilost.graphics

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.mcxtzhang.swipemenulib.R

/**
 * Created by LJN on 2017/11/21.
 */
class SwipeMenuLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    private var mScaleTouchSlop: Int = 0
    private var mMaxVelocity: Int = 0
    private var mPointerId: Int = 0
    private var mHeight: Int = 0
    private var mRightMenuWidths: Int = 0
    private var mLimit: Int = 0
    private var mContentView: View? = null
    private val mLastP: PointF
    private var isUnMoved: Boolean = false
    private val mFirstP: PointF
    private var isUserSwiped: Boolean = false
    private var mVelocityTracker: VelocityTracker? = null
    private val LogUtils: Log? = null
    var isSwipeEnable: Boolean = false
    private var isIos: Boolean = false
    private var iosInterceptFlag: Boolean = false
    private var isLeftSwipe: Boolean = false
    private var mExpandAnim: ValueAnimator? = null
    private var mCloseAnim: ValueAnimator? = null
    private var isExpand: Boolean = false

    init {
        this.mLastP = PointF()
        this.isUnMoved = true
        this.mFirstP = PointF()
        if (attrs != null) {
            this.init(context, attrs, defStyleAttr)
        }
    }

    fun isIos(): Boolean {
        return this.isIos
    }

    fun setIos(ios: Boolean): SwipeMenuLayout {
        this.isIos = ios
        return this
    }

    fun isLeftSwipe(): Boolean {
        return this.isLeftSwipe
    }

    fun setLeftSwipe(leftSwipe: Boolean): SwipeMenuLayout {
        this.isLeftSwipe = leftSwipe
        return this
    }

    private fun init(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        this.mScaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        this.mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        this.isSwipeEnable = true
        this.isIos = true
        this.isLeftSwipe = true
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0)
        val count = ta.indexCount

        for (i in 0 until count) {
            val attr = ta.getIndex(i)
            if (attr == R.styleable.SwipeMenuLayout_swipeEnable) {
                this.isSwipeEnable = ta.getBoolean(attr, true)
            } else if (attr == R.styleable.SwipeMenuLayout_ios) {
                this.isIos = ta.getBoolean(attr, true)
            } else if (attr == R.styleable.SwipeMenuLayout_leftSwipe) {
                this.isLeftSwipe = ta.getBoolean(attr, true)
            }
        }

        ta.recycle()
    }

    @SuppressLint("WrongConstant")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        this.isClickable = true
        this.mRightMenuWidths = 0
        var contentWidth = 0
        val childCount = this.childCount
        val measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != 1073741824
        var isNeedMeasureChildHeight = false

        for (i in 0 until childCount) {
            val childView = this.getChildAt(i)
            childView.isClickable = true
            if (childView.visibility != 8) {
                this.measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                val lp = childView.layoutParams as MarginLayoutParams
                this.mHeight = Math.max(this.mHeight, childView.measuredHeight)
                if (measureMatchParentChildren && lp.height == -1) {
                    isNeedMeasureChildHeight = true
                }

                if (i > 0) {
                    this.mRightMenuWidths += childView.measuredWidth
                } else {
                    this.mContentView = childView
                    contentWidth = childView.measuredWidth
                }
            }
        }

        this.setMeasuredDimension(this.paddingLeft + this.paddingRight + contentWidth, this.mHeight + this.paddingTop + this.paddingBottom)
        this.mLimit = this.mRightMenuWidths * 4 / 10
        if (isNeedMeasureChildHeight) {
            this.forceUniformHeight(childCount, widthMeasureSpec)
        }

    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(this.context, attrs)
    }

    @SuppressLint("WrongConstant")
    private fun forceUniformHeight(count: Int, widthMeasureSpec: Int) {
        val uniformMeasureSpec = MeasureSpec.makeMeasureSpec(this.measuredHeight, 1073741824)

        for (i in 0 until count) {
            val child = this.getChildAt(i)
            if (child.visibility != 8) {
                val lp = child.layoutParams as MarginLayoutParams
                if (lp.height == -1) {
                    val oldWidth = lp.width
                    lp.width = child.measuredWidth
                    this.measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0)
                    lp.width = oldWidth
                }
            }
        }

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = this.childCount
        var left = 0 + this.paddingLeft
        var right = 0 + this.paddingLeft

        for (i in 0 until childCount) {
            val childView = this.getChildAt(i)
            if (childView.visibility != 8) {
                if (i == 0) {
                    childView.layout(left, this.paddingTop, left + childView.measuredWidth, this.paddingTop + childView.measuredHeight)
                    left += childView.measuredWidth
                } else if (this.isLeftSwipe) {
                    childView.layout(left, this.paddingTop, left + childView.measuredWidth, this.paddingTop + childView.measuredHeight)
                    left += childView.measuredWidth
                } else {
                    childView.layout(right - childView.measuredWidth, this.paddingTop, right, this.paddingTop + childView.measuredHeight)
                    right -= childView.measuredWidth
                }
            }
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (this.isSwipeEnable) {
            this.acquireVelocityTracker(ev)
            val verTracker = this.mVelocityTracker
            when (ev.action) {
                0 -> {
                    this.isUserSwiped = false
                    this.isUnMoved = true
                    this.iosInterceptFlag = false
                    if (isTouching) {
                        return false
                    }

                    isTouching = true
                    this.mLastP.set(ev.rawX, ev.rawY)
                    this.mFirstP.set(ev.rawX, ev.rawY)
                    if (viewCache != null) {
                        if (viewCache !== this) {
                            viewCache!!.smoothClose()
                            this.iosInterceptFlag = this.isIos
                            isOpenListener.invoke(false)
                        }

                        this.parent.requestDisallowInterceptTouchEvent(true)
                    }

                    this.mPointerId = ev.getPointerId(0)
                }
                1, 3 -> {
                    if (Math.abs(ev.rawX - this.mFirstP.x) > this.mScaleTouchSlop.toFloat()) {
                        this.isUserSwiped = true
                    }

                    if (!this.iosInterceptFlag) {
                        verTracker!!.computeCurrentVelocity(1000, this.mMaxVelocity.toFloat())
                        val velocityX = verTracker.getXVelocity(this.mPointerId)
                        if (Math.abs(velocityX) > 1000.0f) {
                            if (velocityX < -1000.0f) {
                                if (this.isLeftSwipe) {
                                    isOpenListener.invoke(true)
                                    this.smoothExpand()
                                } else {
                                    this.smoothClose()
                                }
                            } else if (this.isLeftSwipe) {
                                isOpenListener.invoke(false)
                                this.smoothClose()
                            } else {
                                isOpenListener.invoke(true)
                                this.smoothExpand()
                            }
                        } else if (Math.abs(this.scrollX) > this.mLimit) {
                            isOpenListener.invoke(true)
                            this.smoothExpand()
                        } else {
                            this.smoothClose()
                        }
                    }

                    this.releaseVelocityTracker()
                    isTouching = false
                }
                2 -> if (!this.iosInterceptFlag) {
                    val gap = this.mLastP.x - ev.rawX
                    if (Math.abs(gap) > 10.0f || Math.abs(this.scrollX) > 10) {
                        this.parent.requestDisallowInterceptTouchEvent(true)
                    }

                    if (Math.abs(gap) > this.mScaleTouchSlop.toFloat()) {
                        this.isUnMoved = false
                    }

                    this.scrollBy(gap.toInt(), 0)
                    if (this.isLeftSwipe) {
                        if (this.scrollX < 0) {
                            this.scrollTo(0, 0)
                        }

                        if (this.scrollX > this.mRightMenuWidths) {
                            this.scrollTo(this.mRightMenuWidths, 0)
                        }
                    } else {
                        if (this.scrollX < -this.mRightMenuWidths) {
                            this.scrollTo(-this.mRightMenuWidths, 0)
                        }

                        if (this.scrollX > 0) {
                            this.scrollTo(0, 0)
                        }
                    }

                    this.mLastP.set(ev.rawX, ev.rawY)
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            1 -> {
                if (this.isLeftSwipe) {
                    if (this.scrollX > this.mScaleTouchSlop && ev.x < (this.width - this.scrollX).toFloat()) {
                        if (this.isUnMoved) {
                            isOpenListener.invoke(false)
                            this.smoothClose()
                        }

                        return true
                    }
                } else if (-this.scrollX > this.mScaleTouchSlop && ev.x > (-this.scrollX).toFloat()) {
                    if (this.isUnMoved) {
                        this.smoothClose()
                    }

                    return true
                }

                if (this.isUserSwiped) {
                    return true
                }
            }
            2 -> if (Math.abs(ev.rawX - this.mFirstP.x) > this.mScaleTouchSlop.toFloat()) {
                return true
            }
        }

        return if (this.iosInterceptFlag) true else super.onInterceptTouchEvent(ev)
    }

    fun smoothExpand() {
        viewCache = this
        if (null != this.mContentView) {
            this.mContentView!!.isLongClickable = false
        }

        if (this.mCloseAnim != null && this.mCloseAnim!!.isRunning) {
            this.mCloseAnim!!.cancel()
        }

        this.mExpandAnim = ValueAnimator.ofInt(*intArrayOf(this.scrollX, if (this.isLeftSwipe) this.mRightMenuWidths else -this.mRightMenuWidths))
        this.mExpandAnim!!.addUpdateListener { animation -> this@SwipeMenuLayout.scrollTo((animation.animatedValue as Int).toInt(), 0) }
        this.mExpandAnim!!.interpolator = OvershootInterpolator()
        this.mExpandAnim!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@SwipeMenuLayout.isExpand = true

            }
        })
        this.mExpandAnim!!.setDuration(300L).start()
    }

    fun smoothClose() {
        viewCache = null
        if (null != this.mContentView) {
            this.mContentView!!.isLongClickable = true
        }

        if (this.mExpandAnim != null && this.mExpandAnim!!.isRunning) {
            this.mExpandAnim!!.cancel()
        }

        this.mCloseAnim = ValueAnimator.ofInt(*intArrayOf(this.scrollX, 0))
        this.mCloseAnim!!.addUpdateListener { animation -> this@SwipeMenuLayout.scrollTo((animation.animatedValue as Int).toInt(), 0) }
        this.mCloseAnim!!.interpolator = AccelerateInterpolator()
        this.mCloseAnim!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@SwipeMenuLayout.isExpand = false

            }
        })
        this.mCloseAnim!!.setDuration(300L).start()
    }

    private fun acquireVelocityTracker(event: MotionEvent) {
        if (null == this.mVelocityTracker) {
            this.mVelocityTracker = VelocityTracker.obtain()
        }

        this.mVelocityTracker!!.addMovement(event)
    }

    private fun releaseVelocityTracker() {
        if (null != this.mVelocityTracker) {
            this.mVelocityTracker!!.clear()
            this.mVelocityTracker!!.recycle()
            this.mVelocityTracker = null
        }

    }

    override fun onDetachedFromWindow() {
        if (this === viewCache) {
            viewCache!!.smoothClose()
            viewCache = null
        }

        super.onDetachedFromWindow()
    }

    override fun performLongClick(): Boolean {
        return if (Math.abs(this.scrollX) > this.mScaleTouchSlop) false else super.performLongClick()
    }

    fun quickClose() {
        if (this === viewCache) {
            if (null != this.mExpandAnim && this.mExpandAnim!!.isRunning) {
                this.mExpandAnim!!.cancel()
            }

            viewCache!!.scrollTo(0, 0)
            viewCache = null
        }

    }

    companion object {
        private val TAG = "zxt"
        var viewCache: SwipeMenuLayout? = null
            private set
        private var isTouching: Boolean = false
    }

    fun isOpen(listener: (Boolean) -> Unit) {
        isOpenListener = listener
    }

    lateinit var isOpenListener: (Boolean) -> Unit
}
