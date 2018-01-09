package com.goockr.smsantilost.views.activities.msm

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.os.SystemClock
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.*
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.graphics.MyToast
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
    private var sum = 12
    private var myThread: Thread? = null
    private val mDatas = ArrayList<ContentBean>()
    private val sendLists = ArrayList<ContentBean>()
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
            setDates(1, obj)
        } else if (obj.contains(ERROR)) {
            setDates(0, obj)
        }
    }

    private fun setDates(isSucceed: Int, obj: String) {
        if (!obj.contains("||")) return
        val split = obj.split("||")
        val tDate = split[1]
        if (!NotNull.isNotNull(tDate)) return
        val unique = contentBeanDao?.queryBuilder()?.where(ContentBeanDao.Properties.TimeIndex.eq(tDate))?.build()?.unique()
        if (NotNull.isNotNull(unique)) {
            unique?.isSucceed = isSucceed
            contentBeanDao?.update(unique)
            //刷新界面
            for (bean in mDatas) {
                if (TextUtils.equals(bean.timeIndex, tDate)) {
                    if (sendLists.contains(bean))
                        sendLists.remove(bean)
                    bean.isSucceed = isSucceed
                    sendMsmAdapter?.notifyDataSetChanged()
                    if (!sendLists.isEmpty()) {
                        val contentBean = sendLists[0]
                        //发送短信
                        instance?.write("$SMS,${contentBean.date},$msmName,${contentBean.msmStr}")
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun initView() {
        val extras = intent.extras
        msmName = extras.getString(MSM_NAME)
        //初始化数据库
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
            val dialog = MyAlertDialog(this).setTitle(getString(R.string.reSend)).setConfirm(getString(R.string.trtAgain))
            dialog.show()
            dialog.setOnDialogListener(object : MyAlertDialog.OnDialogListener {
                override fun onConfirmListener() {
                    val contentBean = mDatas[it]
                    contentBean.isSucceed = -1
                    contentBean.sumTime = sum
                    if (!sendLists.contains(contentBean)) {
                        sendLists.add(contentBean)
                    }
                    val content = contentBean.msmStr
                    val date = contentBean.timeIndex
                    if (sendLists.indexOf(contentBean) == 0 || sendLists[sendLists.indexOf(contentBean) - 1].isSucceed == 0) {
                        //发送短信
                        instance?.write("$SMS,$date,$msmName,$content")
                        instance?.setUiHandler(myHandler)
                        initThread()
                    }
                    sendMsmAdapter?.notifyDataSetChanged()
                }

                override fun onCancelListener() {
                    dialog.dismiss()
                }

            })

        }
        ivSend.setOnClickListener {
            sendSms()
        }
    }

    //发送短信
    private fun sendSms() {
        same = msmBeanDao?.queryBuilder()?.where(MsmBeanDao.Properties.SmsTitle.eq(msmName))?.build()?.unique()
        content = smsSend.text.toString()
        if (TextUtils.equals(content, "") || !instance!!.isConnect) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.pleaseConnect))
            return
        }
        val currentTime = System.currentTimeMillis()
        val date = DateUtils.getDiveceDate("HHmmss", currentTime)
        element = ContentBean()
        val nowDate = DateUtils.getDiveceDate("yyyy-MM-dd_HH-mm-ss", currentTime)
        val nowLong = DateUtils.stringToLong(nowDate, "yyyy-MM-dd_HH-mm-ss")
        val mDateLong = if (mDate.contains("-")) {
            DateUtils.stringToLong(mDate, "yyyy-MM-dd_HH-mm-ss")
        } else {
            0
        }
        element?.isShowDate = nowLong - mDateLong > 60000 * 5
        //插入数据库
        mDate = nowDate
        element?.isLeft = false
        element?.msmStr = content
        element?.date = mDate
        element?.isSucceed = 0
        element?.timeIndex = date
        element?.mid = same?.id
        element?.sumTime = sum
        contentBeanDao?.insert(element)
        //添加队列
        sendLists.add(element!!)
        if (sendLists.indexOf(element!!) == 0 || sendLists[sendLists.indexOf(element!!) - 1].isSucceed == 0) {
            //发送短信
            instance?.write("$SMS,$date,$msmName,$content")
            instance?.setUiHandler(myHandler)
        }

        //保存更新same
        same?.smsTime = mDate
        same?.smsStr = content
        msmBeanDao?.update(same)
        //更新页面
        element?.isSucceed = -1
        mDatas.add(element!!)
        sendMsmAdapter?.notifyDataSetChanged()
        //设置超时
        initThread()

        //移动至底部
        if (!mDatas.isEmpty()) {
            recycleView.smoothScrollToPosition(mDatas.size - 1)
        }
        smsSend.setText("")
    }

    private fun initThread() {
        if (!NotNull.isNotNull(myThread)) {
            myThread = Thread({
                while (true) {
                    runOnUiThread {
                        val iterator = sendLists.iterator()
                        while (iterator.hasNext()) {
                            val c = iterator.next()
                            if (c.isSucceed == -1) {
                                val sumTime = c.sumTime
                                if (c.sumTime > 1) {
                                    c.sumTime = sumTime - 1
                                } else if (c.sumTime <= 1) {
                                    iterator.remove()
                                    for (bean in mDatas) {
                                        if (bean == c) {
                                            bean.isSucceed = 0
                                            sendMsmAdapter?.notifyDataSetChanged()
                                            contentBeanDao?.update(bean)
                                        }
                                    }
                                }
                            }
                        }

                    }
                    SystemClock.sleep(1000)
                }
            })
            myThread?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (NotNull.isNotNull(myThread)) {
            myThread?.interrupt()
            myThread = null
        }
    }
}

