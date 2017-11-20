package com.goockr.smsantilost.graphics

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.animation.Animation
import com.goockr.smsantilost.R
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.loading_dialog.*

@TargetApi(Build.VERSION_CODES.FROYO)
class LoadingDialog : Dialog {

    private var mContext: Context? = null
    private var message: String? = null
    internal var hyperspaceJumpAnimation: Animation? = null

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
        if (!TextUtils.isEmpty(message)) {
        tipTextView.text = message// 设置加载信息
        }
        this.setCancelable(Companion.cancelable)// 不可以用"返回键"取消
        setContentView(v)// 设置布局
    }

    override fun dismiss() {
        super.dismiss()
        if (NotNull.isNotNull(hyperspaceJumpAnimation))
            hyperspaceJumpAnimation!!.cancel()
    }

    companion object {
        private var cancelable = true
    }
}
