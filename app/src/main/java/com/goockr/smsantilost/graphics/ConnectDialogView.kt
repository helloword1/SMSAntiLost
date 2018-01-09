package com.goockr.smsantilost.graphics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.goockr.smsantilost.R

/**
 * Created by LJN on 2018/1/9.
 */
class ConnectDialogView(context: Context?) : ViewGroup(context) {
    private var src = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureWidth(heightMeasureSpec))
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val specMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val specSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var result = 500
        if (specMode == View.MeasureSpec.AT_MOST) {
            result = specSize
        } else if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        }
        return result
    }

    override fun onLayout(change: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val ll0 = getChildAt(0) as LinearLayout
        val view = LayoutInflater.from(context).inflate(R.layout.connect_dialog_item, null)
        ll0.addView(view)

        val imge = getChildAt(1) as ImageView
        imge.setImageResource(src)

    }

    fun setImageView(src: Int) {
        this.src = src
    }
}