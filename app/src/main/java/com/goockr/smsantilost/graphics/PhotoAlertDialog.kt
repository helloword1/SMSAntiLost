package com.goockr.smsantilost.graphics

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import com.goockr.smsantilost.R
import cxx.utils.NotNull

/**
 * Created by LJN on 2018/1/3.
 */
class PhotoAlertDialog(private val context: Context) {
    private var listener: OnDialogListener? = null
    private var bottomDialog: Dialog? = null

    init {
        bottomDialog = Dialog(context, R.style.BottomDialog)
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_profile, null)
        bottomDialog?.setContentView(customView)
        val layoutParams = customView.layoutParams
        layoutParams.width = context.resources.displayMetrics.widthPixels
        customView.layoutParams = layoutParams
        bottomDialog?.window?.setGravity(Gravity.BOTTOM)
        bottomDialog?.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog?.setCancelable(true)
        bottomDialog?.setCanceledOnTouchOutside(true)

        customView?.findViewById<TextView>(R.id.takePhoto)?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onTakePhotoListener()
            }
        }
        customView?.findViewById<TextView>(R.id.fromPic)?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onFromPicListener()
            }
        }
        customView?.findViewById<TextView>(R.id.photoCancel)?.setOnClickListener {
            if (NotNull.isNotNull(listener)) {
                listener?.onPhotoCancelListener()
            }
        }

    }
    fun show() {
        if (NotNull.isNotNull(bottomDialog)) {
            bottomDialog?.show()
        }
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
        fun onTakePhotoListener()
        fun onFromPicListener()
        fun onPhotoCancelListener()
    }

    fun setOnDialogListener(listener: OnDialogListener) {
        this.listener = listener
    }
}