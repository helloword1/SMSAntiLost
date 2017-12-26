package com.goockr.smsantilost.views.activities.msm

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.*
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.ERROR
import com.goockr.smsantilost.utils.Constant.MSM_NAME
import com.goockr.smsantilost.utils.Constant.SMS
import com.goockr.smsantilost.utils.Constant.SM_OK
import com.goockr.smsantilost.utils.DateUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.SendMsmAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_msm_control.*
import java.util.*

/**
 * Created by LJN on 2017/11/14.
 * 新建短信
 */

class MSMControlActivity(override val contentView: Int = R.layout.activity_msm_control) : BaseActivity() {
    private var same: MsmBean? = null
    private var element: ContentBean? = null
    private var sendMsmAdapter: SendMsmAdapter? = null
    private var msmBeanDao: MsmBeanDao? = null
    private var contentBeanDao: ContentBeanDao? = null
    private var msmName = ""
    private var content = ""
    private var mDate = ""
    private var canSend = false
    private var currentInt = 0
    private val mDatas = ArrayList<ContentBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun handleMyMessage(msg: Message?) {
        super.handleMyMessage(msg)
        when (msg?.what) {
            Constant.MSG_CLIENT_REV_NEW -> {
                val obj = msg.obj.toString()
                val potion = msg.arg1
                getBlueMsg(potion, obj)
            }
        }
    }

    private fun getBlueMsg(potion: Int, obj: String) {
        if (obj.contains(SM_OK)) {
            setDates(true)
        } else if (obj.contains(ERROR)) {
            setDates(false)
        }
    }

    private fun setDates(isSucceed: Boolean) {
        ivSend.isEnabled = true
        same = msmBeanDao?.queryBuilder()?.where(MsmBeanDao.Properties.SmsTitle.eq(msmName))?.build()?.unique()
        val unique = contentBeanDao?.queryBuilder()?.where(ContentBeanDao.Properties.Date.eq(mDate))?.build()?.unique()
        if (NotNull.isNotNull(unique)) {
            unique?.isSucceed = isSucceed
            contentBeanDao?.update(unique)
            same?.smsTime = mDate
            same?.smsTitle = content
            msmBeanDao?.update(same)
            if (!mDatas.isEmpty()) {
                mDatas[currentInt].isSucceed = isSucceed
                mDatas[currentInt].isSending = false
                sendMsmAdapter?.notifyDataSetChanged()
                recycleView.smoothScrollToPosition(mDatas.size - 1)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun initView() {
        val extras = intent.extras
        msmName = extras.getString(MSM_NAME)
        val name = "antilost-db" // 数据库名称
        val helper = DaoMaster.DevOpenHelper(this, name) // helper
        val db = helper.writableDb
        msmBeanDao = DaoMaster(db).newSession().msmBeanDao
        contentBeanDao = DaoMaster(db).newSession().contentBeanDao
        same = msmBeanDao?.queryBuilder()?.where(MsmBeanDao.Properties.SmsTitle.eq(msmName))?.build()?.unique()
        if (NotNull.isNotNull(same)) {
            same?.isShow = false
            msmBeanDao?.update(same)
            val contentBeans = same?.contentBeans
            mDatas.addAll(contentBeans!!)
        }
        title?.text = msmName
        recycleView.layoutManager = LinearLayoutManager(this)
        sendMsmAdapter = SendMsmAdapter(this, mDatas)
        recycleView.adapter = sendMsmAdapter
        recycleView.smoothScrollToPosition(mDatas.size - 1)
        sendMsmAdapter?.setOnGetAdapterListener {
            currentInt = it
            val content = mDatas[it].msmStr
            mDate = mDatas[it].date
            instance?.setUiHandler(myHandler)
            instance?.write("$SMS,$msmName,$content")
            showProgressDialog()
        }
        ivSend.setOnClickListener {
            ivSend.isEnabled = false
            content = smsSend.text.toString()
            if (TextUtils.equals(content, "")) return@setOnClickListener
            //发送短信
            instance?.write("$SMS,$msmName,$content")
            instance?.setUiHandler(myHandler)
            element = ContentBean()
            val nowDate = DateUtils.getDate("yyyy-MM-dd_HH-mm-ss")
            val nowLong = DateUtils.stringToLong(nowDate, "yyyy-MM-dd_HH-mm-ss")
            val mDateLong = if (mDate.contains("-")) {
                DateUtils.stringToLong(mDate, "yyyy-MM-dd_HH-mm-ss")
            } else {
                0
            }
            element?.isShowDate = nowLong - mDateLong > 60000 * 5
            mDate = nowDate
            element?.isLeft = false
            element?.msmStr = content
            element?.date = mDate
            element?.isSending = true
            element?.mid = same?.id
            contentBeanDao?.insert(element)
            currentInt = mDatas.size - 1
            element?.isSucceed = true
            mDatas.add(element!!)
            sendMsmAdapter?.notifyDataSetChanged()
            if (!mDatas.isEmpty()) {
                recycleView.smoothScrollToPosition(mDatas.size - 1)
            }

            smsSend.setText("")
        }
    }
}

