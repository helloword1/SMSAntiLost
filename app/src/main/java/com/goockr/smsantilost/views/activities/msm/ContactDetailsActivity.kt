package com.goockr.smsantilost.views.activities.msm

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant.CONTACT_ID
import com.goockr.smsantilost.utils.Constant.CONTACT_NAME
import com.goockr.smsantilost.utils.Constant.CONTACT_PHONE
import com.goockr.smsantilost.utils.ContactUtils
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.ContactDetailsAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_create_contact.*

/**
 * Created by LJN on 2017/11/14.
 */

class ContactDetailsActivity(override val contentView: Int = R.layout.activity_create_contact) : BaseActivity() {
    private val editList = ArrayList<EditText>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    private var isEdit: Boolean = true

    override fun initView() {
        title?.text = "联系人详情"
        titleRight?.visibility = View.VISIBLE
        titleRight?.text = "编辑"
        val extras = intent.extras
        val contactName = extras.getString(CONTACT_NAME)
        val contactPhone = extras.getString(CONTACT_PHONE)
        val id = extras.getString(CONTACT_ID)
        val lists = ArrayList<String>()
        if (contactPhone.contains(",")) {
            val split = contactPhone.split(",")
            lists += split
        } else {
            lists.add(contactPhone)
        }
        recycleView.layoutManager = LinearLayoutManager(this)
        val mDatas = ArrayList<String>()
        mDatas.add("")
        mDatas.add(contactName)
        mDatas += lists
        mDatas.add("")
        val detailsAdapter = ContactDetailsAdapter(this, mDatas)
        recycleView.adapter = detailsAdapter
        titleRight?.setOnClickListener {
            if (isEdit) {
                //编辑
                titleRight?.text = "完成"
                detailsAdapter.setEdit(true)
                detailsAdapter.notifyDataSetChanged()
                isEdit = false
            } else {
                //完成
                titleRight?.text = "编辑"
                detailsAdapter.setEdit(false)
                isEdit = true
                editList.clear()
                editList.addAll(detailsAdapter.getEditList())
                val contactUtils = ContactUtils(this)
                mDatas.clear()
                mDatas.add("")
                mDatas.add(contactName)
                var Phone = ""
                for (editText in editList) {
                    Phone += editText.text.toString() + ","
                    mDatas.add(editText.text.toString())
                }
                mDatas.add("")
                LogUtils.i("12313", Phone)
                Phone = Phone.substring(0, Phone.length - 1)
                detailsAdapter.notifyDataSetChanged()
                contactUtils.updataContact(id.toLong(), contactName, Phone)
            }

        }
    }
}


