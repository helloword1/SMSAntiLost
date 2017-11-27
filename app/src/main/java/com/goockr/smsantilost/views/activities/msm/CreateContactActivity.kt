package com.goockr.smsantilost.views.activities.msm

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.CreateContactAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_create_contact.*

/**
 * Created by LJN on 2017/11/14.
 */

class CreateContactActivity(override val contentView: Int = R.layout.activity_create_contact) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    override fun initView() {

        title?.text="新建联系人"
        recycleView.layoutManager= LinearLayoutManager(this)
        var mDatas = ArrayList<String>()
        mDatas.add("")
        mDatas.add("1")
        mDatas.add("2")
        mDatas.add("3")
        mDatas.add("4")
        recycleView.adapter= CreateContactAdapter(this,mDatas)

    }
}

