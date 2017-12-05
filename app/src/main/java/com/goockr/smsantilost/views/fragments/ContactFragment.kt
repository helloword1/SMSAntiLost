package com.goockr.smsantilost.views.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContactsBean
import com.goockr.smsantilost.entries.PhoneBean
import com.goockr.smsantilost.graphics.SuspensionDecoration
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.CONTACT_RESULT_ID
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.activities.msm.CreateContactActivity
import com.goockr.smsantilost.views.activities.msm.SettingContactActivity
import com.goockr.smsantilost.views.adapters.CityAdapter
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlin.concurrent.thread

/**
 * 通讯录页
 */

class ContactFragment : BaseFragment() {
    private var mAdapter: CityAdapter? = null
    private var mManager: LinearLayoutManager? = null
    private var mDatas: MutableList<PhoneBean> = ArrayList()
    private var mDecoration: SuspensionDecoration? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return setContentView(R.layout.fragment_contact)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mManager = LinearLayoutManager(activity)
        rv.layoutManager = mManager
        mAdapter = CityAdapter(this,activity, mDatas as ArrayList<PhoneBean>)
        rv.adapter = mAdapter
        mDecoration = SuspensionDecoration(activity, mDatas)
        rv.addItemDecoration(mDecoration)
        //分割线
//        rv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST))

        thread {
            kotlin.run {
                val systemContactInfos = getSystemContactInfos()
                activity.runOnUiThread { initDatas(systemContactInfos) }
            }
        }
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                        floatSetting.visibility = View.VISIBLE
                        floatSend.visibility = View.VISIBLE
                    }
                    else -> {
                        floatSetting.visibility = View.GONE
                        floatSend.visibility = View.GONE
                    }
                }
            }
        })
        //新建联系人
        floatSetting.setOnClickListener {
            showActivityForResult(SettingContactActivity::class.java,CONTACT_RESULT_ID)
        }
        //设置
        floatSend.setOnClickListener {
            showActivityForResult(CreateContactActivity::class.java,CONTACT_RESULT_ID)
        }
    }

    private fun initDatas(data: List<ContactsBean>) {
        //延迟两秒 模拟加载数据中....
        activity.window.decorView.postDelayed({
            mDatas.clear()
            for (i in data.indices) {
                val cityBean = PhoneBean()
                cityBean.setMPhone(data[i].name)//设置名称
                cityBean.phone = (data[i].phone)//设置电话
                cityBean.id = (data[i].id)//设置id
                mDatas.add(cityBean)
            }
            mAdapter?.notifyDataSetChanged()
            indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                    .setNeedRealIndex(true)//设置需要真实的索引
                    .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                    .setmSourceDatas(mDatas)//设置数据
                    .invalidate()
            mDecoration?.setmDatas(mDatas)
        }, 500)
    }


    private var PHONES_PROJECTION = arrayOf(Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID)
    /**
     * 获取系统联系人信息
     *
     * @return
     */

    private fun getSystemContactInfos(): List<ContactsBean> {
        val infos = ArrayList<ContactsBean>()
        val cursor = activity.contentResolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== Constant.CONTACT_RESULT_ID){
            thread {
                kotlin.run {
                    val systemContactInfos = getSystemContactInfos()
                    activity.runOnUiThread { initDatas(systemContactInfos) }
                }
            }
        }
    }
}


