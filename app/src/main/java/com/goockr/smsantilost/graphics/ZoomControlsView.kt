package com.goockr.smsantilost.graphics

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.goockr.smsantilost.R
import cxx.utils.NotNull

/**
 * Created by LJN on 2018/1/2.
 */

class ZoomControlsView : LinearLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        this.orientation = VERTICAL

        val imageViewAdd = ImageView(context)
        imageViewAdd.setImageResource(R.mipmap.btn_map_plus)
        addView(imageViewAdd, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        val viewLine = View(context)
        viewLine.setBackgroundColor(Color.TRANSPARENT)
        addView(viewLine, LinearLayout.LayoutParams(2, 1))

        val imageViewMinus = ImageView(context)
        imageViewMinus.setImageResource(R.mipmap.btn_map_reduce)
        addView(imageViewMinus, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
               imageViewAdd.setOnClickListener {
            if (NotNull.isNotNull(listener))
                listener?.zoomAdd()
        }
        imageViewMinus.setOnClickListener {
            if (NotNull.isNotNull(listener))
                listener?.zoomMinus()
        }
    }

    private var listener: OnZoomControlsListener? = null

    interface OnZoomControlsListener {
        fun zoomAdd()
        fun zoomMinus()
    }

    fun setOnZoomControlsListener(listener: OnZoomControlsListener) {
        this.listener = listener
    }

}
