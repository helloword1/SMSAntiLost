package com.goockr.smsantilost.graphics

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.goockr.smsantilost.R
/**
 *
 * @Description Toast统一管理类
 */
class MyToast private constructor() {

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        var isShow = true

        var toast: Toast? = null

        /**
         * 短时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showShort(context: Context, message: CharSequence) {
            if (isShow)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        /**
         * 短时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showShort(context: Context, message: Int) {
            if (isShow)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        /**
         * 长时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showLong(context: Context, message: CharSequence) {
            if (isShow)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        /**
         * 长时间显示Toast
         *
         * @param context
         * @param message
         */
        fun showLong(context: Context, message: Int) {
            if (isShow)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        /**
         * 自定义显示Toast时间
         *
         * @param context
         * @param message
         * @param duration
         */
        fun show(context: Context, message: CharSequence, duration: Int) {
            if (isShow)
                Toast.makeText(context, message, duration).show()
        }

        /**
         * 自定义显示Toast时间
         *
         * @param context
         * @param message
         * @param duration
         */
        fun show(context: Context, message: Int, duration: Int) {
            if (isShow)
                Toast.makeText(context, message, duration).show()
        }

        /**
         *
         * @param context
         * @param message
         * @param duration
         */
        fun showToastCustomerStyleText(context: Context, message: Int, duration: Int) {
            var inflate = LayoutInflater.from(context).inflate(R.layout.toast_bg, null)
            val text = inflate.findViewById<View>(R.id.tvContent) as TextView
            text.text = context.getText(message)
            if (toast == null) {
                toast = Toast(context)
                toast?.setGravity(Gravity.CENTER, 0, 0)
                toast?.view = inflate
                toast!!.duration = duration
            } else {
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                toast!!.view = inflate
                toast!!.duration = duration
            }
            if (isShow)
                toast!!.show()

        }

        /**
         *
         * @param context
         * @param message
         */
        fun showToastCustomerStyleText(context: Context, message: String) {
            val inflaterStyle = LayoutInflater.from(context)
            val view = inflaterStyle.inflate(R.layout.toast_bg, null)
            val text = view.findViewById<View>(R.id.tvContent) as TextView
            text.text = message
            if (toast == null) {
                toast = Toast(context)
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                toast!!.view = view
                toast!!.duration = Toast.LENGTH_SHORT
            } else {
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                toast!!.view = view
                toast!!.duration = Toast.LENGTH_SHORT
            }
            if (isShow)
                toast!!.show()

        }


        /**
         *
         * @param context
         */
        fun showToastCustomerStyleText(context: Context, messageId: Int) {
            val inflaterStyle = LayoutInflater.from(context)
            val view = inflaterStyle.inflate(R.layout.toast_bg, null)
            val text = view.findViewById<View>(R.id.tvContent) as TextView
            text.setText(messageId)
            if (toast == null) {
                toast = Toast(context)
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                toast!!.view = view
                toast!!.duration = Toast.LENGTH_SHORT
            } else {
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                toast!!.view = view
                toast!!.duration = Toast.LENGTH_SHORT
            }
            if (isShow)
                toast!!.show()

        }
    }
}
