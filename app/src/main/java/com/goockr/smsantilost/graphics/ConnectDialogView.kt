package com.goockr.smsantilost.graphics

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.connect_dialog_item.view.*


/**
 * Created by LJN on 2018/1/9.
 */
class ConnectDialogView(private val context: Context, private val src: Int) {
    private var connectDialog: Dialog? = null
    private var customView: View? = null

    interface DialogAppleClickListener {
        fun doConfirm()
    }

    init {
        connectDialog = Dialog(context, R.style.BottomDialog)
        customView = LayoutInflater.from(context).inflate(R.layout.connect_dialog_item, null)
        connectDialog?.setContentView(customView)
        val layoutParams = customView?.layoutParams
        layoutParams?.width = context.resources.displayMetrics.widthPixels
        customView?.layoutParams = layoutParams
        connectDialog?.window?.setGravity(Gravity.CENTER)
        connectDialog?.setCancelable(true)
        connectDialog?.setCanceledOnTouchOutside(true)
        customView?.imageConnect?.setImageResource(src)

       /* customView?.imageConnect?.post({
            val layoutParams = customView?.imageConnect?.layoutParams as LinearLayout.LayoutParams
            val height = customView?.imageConnect?.height //height is ready
            layoutParams.topMargin = (-1 * height!! / 2)
            customView?.llConnect?.updateViewLayout(customView?.imageConnect, layoutParams)
        })*/

        customView?.btnDisconnect?.setOnClickListener {
            clickListenerInterface.doConfirm()
        }
        customView?.connectKnow?.setOnClickListener {
            connectDialog?.dismiss()
        }

    }

    fun show(): ConnectDialogView {
        val baseActivity = context as BaseActivity
        if (NotNull.isNotNull(connectDialog) && !baseActivity.isFinishing && !connectDialog!!.isShowing) {
            connectDialog?.show()
        }
        return this
    }

    fun dismiss() {
        if (NotNull.isNotNull(connectDialog)) {
            connectDialog?.dismiss()
            connectDialog = null
        }
    }

    fun setTitle(str: String): ConnectDialogView {
        if (NotNull.isNotNull(customView)) {
            customView?.connectTitle?.text = str
        }
        return this
    }

    fun setSrc(str: Int): ConnectDialogView {
        if (NotNull.isNotNull(customView)) {
            customView?.imageConnect?.setImageResource(src)
        }
        return this
    }

    fun setContent(str: String): ConnectDialogView {
        if (NotNull.isNotNull(customView)) {
            customView?.connectContent?.text = str
        }
        return this
    }

    private lateinit var clickListenerInterface: DialogAppleClickListener //接口可以延时加载
    fun setDialogClicklistener(dialogClickListener: DialogAppleClickListener) {
        this.clickListenerInterface = dialogClickListener
    }

    fun isShowing(): Boolean {
        if (NotNull.isNotNull(connectDialog) && connectDialog!!.isShowing) {
            return true
        }
        return false
    }
}