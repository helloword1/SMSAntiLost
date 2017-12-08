package com.goockr.smsantilost.views.activities.msm

import android.os.Bundle
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_setting_contact.*

/**
 * Created by LJN on 2017/11/14.
 */

class SettingContactActivity(override val contentView: Int = R.layout.activity_setting_contact) : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun initView() {
        title?.text = "通讯录管理"

        //复制SIM卡联系人到通讯录
        tvCopyContact.setOnClickListener {
            MyToast.showToastCustomerStyleText(this, "功能开发中")
        }
        //批量删除联系人
        tvDeleteContact.setOnClickListener {
            showActivity(DeleteContactActivity::class.java)
        }
        //合并重复联系人
        tvUnionContact.setOnClickListener {
            showActivity(MergeContactActivity::class.java)

        }
    }
}

