package com.goockr.smsantilost.graphics

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.goockr.smsantilost.R
import kotlinx.android.synthetic.main.apple_confir_dialog.*
import kotlinx.android.synthetic.main.apple_two_line_dialog.*
import kotlinx.android.synthetic.main.customer_dialog_update.*

/**
 * Created by ning on 2016/12/16
 */
class LikeAppleComfirDialog : Dialog {
    private var titleId: String? = null
    private var confirmButtonId: String? = null
    private var cacelButtonId: String? = null

    private var index: Int = 0

    interface DialogAppleClickListener {
        fun doConfirm()
        fun doCancel()
    }

    constructor(context: Context, titleId: String,
                confirmButtonId: String, cancelButtonId: String, index: Int) : super(context, R.style.AlertDialog) {
        this.titleId = titleId
        this.confirmButtonId = confirmButtonId
        this.cacelButtonId = cancelButtonId
        this.index = index
    }

    constructor(context: Context, titleId: Int,
                confirmButtonId: Int, cancelButtonId: Int, index: Int) : this(context, context.getString(titleId),
            context.getString(confirmButtonId), context.getString(cancelButtonId), index)


    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        init()
    }

    // 双按钮
    fun init() {
        val inflater = LayoutInflater.from(context)
        var view: View? = null
        if (index == 2) {
            view = inflater.inflate(R.layout.customer_dialog_update, null)
            tvConfirm.setOnClickListener(OnDialogClickListener())
            tvCancel.setOnClickListener(OnDialogClickListener())
        } else if (index == 1) {
            view = inflater.inflate(R.layout.apple_confir_dialog, null)
            tvTitle1Line.text = "这是第一行"
            tvConfirm1Line.setOnClickListener(OnDialogClickListener())
        } else if (index == 3) {// 2行弹框
            view = inflater.inflate(R.layout.apple_two_line_dialog, null)
            tvConfirm2Line.setOnClickListener(OnDialogClickListener())
        }

        setContentView(view)
        val dialogWindow = window
        val lp = dialogWindow.attributes
        val d = context!!.resources.displayMetrics // 获取屏幕宽、高用
        lp.width = (d.widthPixels * 0.8).toInt() // 高度设置为屏幕的0.6
        dialogWindow.attributes = lp
    }

    lateinit var clickListenerInterface: DialogAppleClickListener //接口可以延时加载
    fun setDialogClicklistener(dialogClickListener: DialogAppleClickListener) {
        this.clickListenerInterface = dialogClickListener
    }

    private inner class OnDialogClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            when ( v.id) {
                R.id.tvConfirm -> {
                    clickListenerInterface.doConfirm()
                    dismiss()
                }
                R.id.tvCancel -> clickListenerInterface.doCancel()
                R.id.tvConfirm2Line -> clickListenerInterface.doCancel()
                R.id.tvCancel2Line -> clickListenerInterface.doConfirm()
            }
        }
    }
}
