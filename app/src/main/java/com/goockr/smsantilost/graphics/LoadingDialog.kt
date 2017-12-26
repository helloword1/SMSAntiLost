package com.goockr.smsantilost.graphics

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.goockr.smsantilost.R
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.loading_dialog.*
import kotlinx.android.synthetic.main.loading_dialog.view.*
import kotlin.concurrent.thread

@TargetApi(Build.VERSION_CODES.FROYO)
class LoadingDialog : Dialog {

    private var mContext: Context? = null
    private var message = "正在加载"
    private var flag = true
    internal var rotateAnimation: RotateAnimation? = null
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what?.rem(3)) {
                0 -> {
                    tip.text = "."// 设置加载信息
                }
                1 -> {
                    tip.text = ". ."// 设置加载信息
                }
                2 -> {
                    tip.text = ". . ."// 设置加载信息
                }
            }
        }
    }

    constructor(context: Context?, mesString: String) : super(context, R.style.LoadingDialog) {
        this.mContext = context
        this.message = mesString
    }

    constructor(context: Context, theme: Int) : super(context, theme) {
        this.mContext = context
    }

    constructor(context: Context, mesString: String, theme: Int) : super(context, theme) {
        this.mContext = context
        this.message = mesString
    }

    constructor(context: Context?) : super(context, R.style.LoadingDialog) {
        this.mContext = context
    }

    constructor(context: Context?, mesString: String, cancelable: Boolean) : super(context, R.style.LoadingDialog) {
        this.mContext = context
        Companion.cancelable = cancelable
        this.message = mesString
    }

    constructor(context: Context, cancelable: Boolean) : super(context, R.style.LoadingDialog) {
        this.mContext = context
        Companion.cancelable = cancelable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(mContext)
        val v = inflater.inflate(R.layout.loading_dialog, null)// 得到加载view

        rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation?.duration = 1500
        rotateAnimation?.repeatCount = -1
        rotateAnimation?.interpolator = LinearInterpolator()
        v.ivDialog.animation = rotateAnimation
        var i = 0
        thread {
            while (flag) {
                SystemClock.sleep(500)
                i++
                handler.sendEmptyMessage(i)
            }
        }
        v.tipTextView.text = message
        this.setCancelable(Companion.cancelable)// 不可以用"返回键"取消
        setContentView(v)// 设置布局
    }

    override fun dismiss() {
        super.dismiss()
        flag = false
        if (NotNull.isNotNull(rotateAnimation))
            rotateAnimation!!.cancel()
    }

    companion object {
        private var cancelable = true
    }
}
