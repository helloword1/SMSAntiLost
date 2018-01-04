package com.goockr.smsantilost.graphics

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import com.goockr.smsantilost.R
import cxx.utils.NotNull

/**
 * Created by LJN on 2018/1/3.
 */
class MyAlertDialog(private val context: Context) {
    private var dialog: AlertDialog? = null
    private var builder: AlertDialog.Builder? = null
    private var customView: View? = null
    private var listener: OnDialogListener? = null

    init {
        builder = AlertDialog.Builder(context)
        customView = LayoutInflater.from(context).inflate(R.layout.dialog_permission, null)
        builder?.setView(customView)
        builder?.setIcon(R.mipmap.ic_launcher)
        dialog = builder?.create()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customView?.findViewById<TextView>(R.id.tv_Text3)?.visibility = View.GONE
        customView?.findViewById<TextView>(R.id.tv_Ensure)?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onConfirmListener()
            }
        }
        customView?.findViewById<TextView>(R.id.tv_Cancel)?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onCancelListener()
            }
        }
    }

    fun setTitle(title: String): MyAlertDialog {
        customView?.findViewById<TextView>(R.id.tv_Text1)?.text = title
        return this
    }

    fun setContent(content: String): MyAlertDialog {
        val textView = customView?.findViewById<TextView>(R.id.tv_Text2)
        if (TextUtils.equals(content,"")||!NotNull.isNotNull(content)){
            textView?.visibility=View.GONE
        }else{
            textView?.visibility=View.VISIBLE
            textView?.text = content
        }
        return this
    }

    fun show() {
        if (NotNull.isNotNull(dialog)) {
            dialog?.show()
        }
    }

    fun dismiss() {
        if (NotNull.isNotNull(dialog)) {
            dialog?.dismiss()
            dialog = null
        }
    }
    fun hide() {
        if (NotNull.isNotNull(dialog)) {
            dialog?.hide()
        }
    }
    interface OnDialogListener {
        fun onConfirmListener()
        fun onCancelListener()
    }

    fun setOnDialogListener(listener: OnDialogListener) {
        this.listener = listener
    }
}