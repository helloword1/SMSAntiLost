package com.goockr.smsantilost.graphics

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.LocaleUtil
import cxx.utils.NotNull

/**
 * Created by LJN on 2018/1/3.
 */
class MapAlertDialog(private val context: Context) {
    private var listener: OnDialogListener? = null
    private var bottomDialog: Dialog? = null

    init {
        bottomDialog = Dialog(context, R.style.BottomDialog)
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_map, null)
        bottomDialog?.setContentView(customView)
        val layoutParams = customView.layoutParams
        layoutParams.width = context.resources.displayMetrics.widthPixels
        customView.layoutParams = layoutParams
        bottomDialog?.window?.setGravity(Gravity.BOTTOM)
        bottomDialog?.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog?.setCancelable(true)
        bottomDialog?.setCanceledOnTouchOutside(true)

        customView?.findViewById<TextView>(R.id.tvMapCancel)?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onMapCancelListener()
            }
        }
        customView?.findViewById<TextView>(R.id.systemMap)?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onSystemMapListener()
            }
        }
        val aMap = customView?.findViewById<TextView>(R.id.aMap)
        aMap?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onAMapListener()
            }
        }
        val baiduMap = customView?.findViewById<TextView>(R.id.baiduMap)
        baiduMap?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onBaiduMapListener()
            }
        }
        if (LocaleUtil.isAvilible(context, "com.autonavi.minimap")) {
            aMap?.visibility = View.VISIBLE
        } else if (LocaleUtil.isAvilible(context, "com.baidu.BaiduMap")) {
            baiduMap?.visibility = View.VISIBLE
        }
    }

    fun show(): MapAlertDialog {
        if (NotNull.isNotNull(bottomDialog)) {
            bottomDialog?.show()
        }
        return this
    }

    fun dismiss() {
        if (NotNull.isNotNull(bottomDialog)) {
            bottomDialog?.dismiss()
            bottomDialog = null
        }
    }

    fun hide() {
        if (NotNull.isNotNull(bottomDialog)) {
            bottomDialog?.hide()
        }
    }

    interface OnDialogListener {
        fun onSystemMapListener()
        fun onAMapListener()
        fun onBaiduMapListener()
        fun onMapCancelListener()
    }

    fun setOnDialogListener(listener: OnDialogListener) {
        this.listener = listener
    }
}