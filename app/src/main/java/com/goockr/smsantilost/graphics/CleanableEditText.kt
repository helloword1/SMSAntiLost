package com.goockr.smsantilost.graphics


import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import com.goockr.smsantilost.utils.LogUtils

/**
 * 在焦点变化时和输入内容发生变化时均要判断是否显示右边clean图标
 */
class CleanableEditText : android.support.v7.widget.AppCompatEditText {
    private var mRightDrawable: Drawable? = null
    private var isHasFocus: Boolean = false

    private var callBackForTextChange: CallBackForTextChange? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        //getCompoundDrawables:
        //Returns drawables for the left, top, right, and bottom borders.
        val drawables = this.compoundDrawables

        //取得right位置的Drawable
        //即我们在布局文件中设置的android:drawableRight
        mRightDrawable = drawables[2]

        //设置焦点变化的监听
        this.onFocusChangeListener = FocusChangeListenerImpl()
        //设置EditText文字变化的监听
        this.addTextChangedListener(TextWatcherImpl())
        //初始化时让右边clean图标不可见
        setClearDrawableVisible(false)
    }


    /**
     * 当手指抬起的位置在clean的图标的区域
     * 我们将此视为进行清除操作
     * getWidth():得到控件的宽度
     * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
     * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
     * getPaddingRight():clean的图标右边缘至控件右边缘的距离
     * 于是:
     * getWidth() - getTotalPaddingRight()表示:
     * 控件左边到clean的图标左边缘的区域
     * getWidth() - getPaddingRight()表示:
     * 控件左边到clean的图标右边缘的区域
     * 所以这两者之间的区域刚好是clean的图标的区域
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val isClean = event.x > width - totalPaddingRight && event.x < width - paddingRight
                LogUtils.d(javaClass, "isClean = " + isClean)
                if (isClean) {
                    setText("")
                }
            }

            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    private inner class FocusChangeListenerImpl : View.OnFocusChangeListener {
        override fun onFocusChange(v: View, hasFocus: Boolean) {
            isHasFocus = hasFocus
            if (isHasFocus) {
                val isVisible = text.toString().length >= 1
                setClearDrawableVisible(isVisible)
            } else {
                setClearDrawableVisible(false)
            }
        }

    }

    //当输入结束后判断是否显示右边clean的图标
    inner class TextWatcherImpl : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            val isVisible = text.toString().length >= 1
            setClearDrawableVisible(isVisible)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //如果定义了输入框回调事件，则启动回调事件
            if (callBackForTextChange != null) {
                callBackForTextChange!!.textChange()
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

    }

    //隐藏或者显示右边clean的图标
    protected fun setClearDrawableVisible(isVisible: Boolean) {
        val rightDrawable: Drawable?
        if (isVisible) {
            rightDrawable = mRightDrawable
        } else {
            rightDrawable = null
        }
        //使用代码设置该控件left, top, right, and bottom处的图标
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1],
                rightDrawable, compoundDrawables[3])
    }

    // 显示一个动画,以提示用户输入
    fun setShakeAnimation() {
        this.animation = shakeAnimation(5)
    }

    //CycleTimes动画重复的次数
    fun shakeAnimation(CycleTimes: Int): Animation {
        val translateAnimation = TranslateAnimation(0f, 10f, 0f, 10f)
        translateAnimation.interpolator = CycleInterpolator(CycleTimes.toFloat())
        translateAnimation.duration = 1000
        return translateAnimation
    }

    fun setCallBackForTextChange(callBack: CallBackForTextChange) {
        this.callBackForTextChange = callBack
    }

    interface CallBackForTextChange {
        fun textChange()
    }

}
