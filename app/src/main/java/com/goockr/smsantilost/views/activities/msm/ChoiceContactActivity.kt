package com.goockr.smsantilost.views.activities.msm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContactsBean
import com.goockr.smsantilost.entries.PhoneBean
import com.goockr.smsantilost.graphics.SuspensionDecoration
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.ChoiceContactAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_choice_contact.*
import kotlin.concurrent.thread

/**
 * Created by LJN on 2017/11/14.
 */

class ChoiceContactActivity(override val contentView: Int = R.layout.activity_choice_contact) : BaseActivity() {
    private var mDatas = ArrayList<PhoneBean>()
    private var serializable = ArrayList<PhoneBean>()
    private var mAdapter: ChoiceContactAdapter? = null
    private var mDecoration: SuspensionDecoration? = null
    private var mManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun initView() {
        title?.text = "通讯录"
        titleRight1?.visibility = View.VISIBLE
        titleRight1?.setTextColor(ContextCompat.getColor(this, R.color.blue))
        titleRight1?.text = "确定"
        val extras = intent.extras
        serializable.addAll(extras.getSerializable("PHONE_DATA") as ArrayList<PhoneBean>)
        mManager = LinearLayoutManager(this)
        recycleView.layoutManager = mManager
        mAdapter = ChoiceContactAdapter(this, mDatas)
        recycleView.adapter = mAdapter
        mDecoration = SuspensionDecoration(this, mDatas)
        mDecoration?.setIsChoice(false, serializable.size)
        recycleView.addItemDecoration(mDecoration)

        thread {
            kotlin.run {
                val systemContactInfos = getSystemContactInfos()
                runOnUiThread { initDatas(systemContactInfos) }
            }
        }
        titleRight1?.setOnClickListener {
            val intent = Intent()
            val bundle = Bundle()
            val beans = ArrayList<PhoneBean>()
            val elements = mAdapter?.getLists()
            elements?.mapTo(beans) { mDatas[it] }
            bundle.putSerializable("CHOICE_RETURN", beans)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        mAdapter?.setoOnGetAdapterListener {
            mDecoration?.setIsChoice(false, mAdapter?.getLists()?.size!!)
        }
        titleBack?.setOnClickListener {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putSerializable("CHOICE_RETURN", ArrayList<PhoneBean>())
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun initDatas(data: List<ContactsBean>) {
        //延迟两秒 模拟加载数据中....
        window.decorView.postDelayed({
            mDatas.clear()
            for (i in data.indices) {
                val cityBean = PhoneBean()
                cityBean.setMPhone(data[i].name)//设置名字
                cityBean.phone = (data[i].phone)//设置电话
                cityBean.id = (data[i].id)//设置id
                serializable
                        .filter { cityBean.id == it.id }
                        .forEach { cityBean.isshowRight = true }
                mDatas.add(cityBean)
            }
            mAdapter?.notifyDataSetChanged()
            indexBar.setNeedRealIndex(true)//设置需要真实的索引
                    .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                    .setmSourceDatas(mDatas)//设置数据
                    .invalidate()
            mDecoration?.setmDatas(mDatas)
        }, 500)
    }

    private var PHONES_PROJECTION = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
    private fun getSystemContactInfos(): List<ContactsBean> {
        val infos = ArrayList<ContactsBean>()
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                var isAdded = true
                val contactName = cursor.getString(0)
                val phoneNumber = cursor.getString(1)
                val id = cursor.getString(2)
                for (mData in infos) {
                    if (mData.name == contactName) {
                        mData.phone += "," + phoneNumber
                        isAdded = false
                        LogUtils.i("12313123", mData.phone)
                    }
                }
                if (isAdded) {
                    val info = ContactsBean()
                    info.name = contactName
                    info.phone = phoneNumber
                    info.id = id
                    infos.add(info)
                }
            }
            cursor.close()
        }
        return infos
    }

}

