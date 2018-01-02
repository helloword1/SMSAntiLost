package com.goockr.smsantilost.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.IOException

/**
 * Created by tanzhihao on 2017/11/21.
 */

class CalculateUtils(context: Context?) {
    var mContext: Context? = null
    var mWn: WindowManager? = null
    var mDm: DisplayMetrics? = null

    init {
        this.mContext = context
        this.mWn = mContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        this.mDm = DisplayMetrics()
        mWn!!.defaultDisplay.getMetrics(mDm)
    }

    fun dp2px(dp: Int): Int {
        return ((dp * mDm!!.density + 0.5f).toInt())
    }


}