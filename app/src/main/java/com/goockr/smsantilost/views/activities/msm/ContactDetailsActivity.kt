package com.goockr.smsantilost.views.activities.msm

import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant.CONTACT_ID
import com.goockr.smsantilost.utils.Constant.CONTACT_NAME
import com.goockr.smsantilost.utils.Constant.CONTACT_PHONE
import com.goockr.smsantilost.utils.ContactUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.adapter_create_name_item.view.*
import kotlinx.android.synthetic.main.adapter_create_phone_item.view.*
import kotlinx.android.synthetic.main.adapter_create_send_msm_item.*

/**
 * Created by LJN on 2017/11/14.
 * 联系人详情
 */

class ContactDetailsActivity(override val contentView: Int = R.layout.activity_create_contact) : BaseActivity() {
    private val lists = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    private var isEdit: Boolean = true

    override fun initView() {
        title?.text = getString(R.string.contactDetails)
        titleRight1?.visibility = View.VISIBLE
        titleRight1?.text = getString(R.string.edit)
        val extras = intent.extras
        val contactName = extras.getString(CONTACT_NAME)
        val contactPhone = extras.getString(CONTACT_PHONE)
        val id = extras.getString(CONTACT_ID)
        if (contactPhone.contains(",")) {
            val split = contactPhone.split(",")
            lists += split
        } else {
            lists.add(contactPhone)
        }
        addAction(false)
        llAddPhone.setOnClickListener {
            lists.add("")
            addAction(true)
            if (lists.size >= 5) {
                llAddPhone.visibility = View.GONE
            }
        }
        contactMName.etContactName.isEnabled = false
        contactMName.etContactName.setText(contactName)

        titleRight1?.setOnClickListener {
            if (isEdit) {
                //编辑
                titleRight1?.text = getString(R.string.complete)
                llAddPhone.visibility = View.VISIBLE
                tvSendMsm.visibility = View.GONE
                for (i in lists.indices) {
                    val swipe = llPhone.getChildAt(i) as SwipeMenuLayout
                    swipe.isSwipeEnable = true
                    swipe.etPhoneName.isEnabled = true
                }
                contactMName.etContactName.isEnabled = true
                isEdit = false
            } else {
                //完成
                titleRight1?.text = getString(R.string.edit)
                llAddPhone.visibility = View.GONE
                tvSendMsm.visibility = View.VISIBLE
                val Phone = ArrayList<String>()
                val types = ArrayList<Int>()
                for (i in lists.indices) {
                    val swipe = llPhone.getChildAt(i) as SwipeMenuLayout
                    swipe.isSwipeEnable = false
                    swipe.etPhoneName.isEnabled = false
                    Phone.add(llPhone.getChildAt(i).etPhoneName.text.toString())
                    types.add(getPhoneType("Mobile"))
                }
                contactMName.etContactName.isEnabled = false
                isEdit = true

                ContactUtils.updateContact(applicationContext, id, contactMName.etContactName.text.toString(), Phone, types)
                MyToast.showToastCustomerStyleText(this, getString(R.string.editSucceed))
            }
        }
    }


    private fun getPhoneType(type: String): Int {
        val phoneType: Int = when {
            "Mobile".equals(type, ignoreCase = true) -> ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
            "Home".equals(type, ignoreCase = true) -> ContactsContract.CommonDataKinds.Phone.TYPE_HOME
            "Work".equals(type, ignoreCase = true) -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK
            else -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
        }
        return phoneType
    }

    private fun addAction(isSwipe: Boolean) {
        goneAction()
        for (i in lists.indices) {
            llPhone.getChildAt(i).visibility = View.VISIBLE
            val swipe = llPhone.getChildAt(i) as SwipeMenuLayout
            swipe.isSwipeEnable = isSwipe
            swipe.etPhoneName.isEnabled = isSwipe
            swipe.etPhoneName.setText(lists[i])
            swipe.btnDel.setOnClickListener {
                llAddPhone.visibility = View.VISIBLE
                val toString = llPhone.getChildAt(i).etPhoneName.text.toString()
                if (lists.contains(toString)) {
                    lists.remove(toString)
                } else {
                    lists.remove("")
                }
                llPhone.getChildAt(i).visibility = View.GONE
            }
        }
    }

    private fun goneAction() {
        for (i in lists.indices) {
            val childAt = llPhone.getChildAt(i)
            childAt.visibility = View.GONE
            val swipe = childAt as SwipeMenuLayout
            swipe.quickClose()
        }
    }
}


