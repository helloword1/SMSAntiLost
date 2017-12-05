package com.goockr.smsantilost.views.activities


import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.jude.swipbackhelper.SwipeBackHelper

class BlueTeethActivity(override val contentView: Int=R.layout.activity_blueteeth) : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }
    override fun onClick(p0: View?) {

    }

}
