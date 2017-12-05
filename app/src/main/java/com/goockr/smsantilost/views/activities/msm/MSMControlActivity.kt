package com.goockr.smsantilost.views.activities.msm

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContentBean
import com.goockr.smsantilost.utils.Constant.MSM_CONTENT
import com.goockr.smsantilost.utils.Constant.MSM_NAME
import com.goockr.smsantilost.utils.Constant.MSM_Time
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.SendMsmAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.SystemInput
import kotlinx.android.synthetic.main.activity_msm_control.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by LJN on 2017/11/14.
 */

class MSMControlActivity(override val contentView: Int = R.layout.activity_msm_control) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun initView() {
        val extras = intent.extras
        val msmName = extras.getString(MSM_NAME)
        val msmTime = extras.getString(MSM_Time)
        val msmContent = extras.getString(MSM_CONTENT)
        title?.text = msmName
        recycleView.layoutManager = LinearLayoutManager(this)
        val mDatas = ArrayList<ContentBean>()
        val data = SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(System.currentTimeMillis())
        mDatas.add(ContentBean(msmTime, msmContent, true))
        val sendMsmAdapter = SendMsmAdapter(this, mDatas)
        recycleView.adapter = sendMsmAdapter
        ivSend.setOnClickListener {
            val content = smsSend.text.toString()
            if (TextUtils.equals(content, "")) return@setOnClickListener
            mDatas.add(ContentBean("", content, false))
            sendMsmAdapter.notifyDataSetChanged()
            smsSend.setText("")
            SystemInput.closeInput(this)
        }
    }
}

