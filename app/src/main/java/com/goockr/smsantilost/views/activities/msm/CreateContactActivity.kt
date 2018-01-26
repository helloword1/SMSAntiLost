package com.goockr.smsantilost.views.activities.msm

import android.app.Activity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.ContactUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.adapter_create_name_item.view.*
import kotlinx.android.synthetic.main.adapter_create_phone_item.view.*
import kotlinx.android.synthetic.main.adapter_create_send_msm_item.*

/**
 * Created by LJN on 2017/11/14.
 * 新建联系人
 */

class CreateContactActivity(override val contentView: Int = R.layout.activity_create_contact) : BaseActivity() {
    private val lists = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun initView() {
        title?.text = getString(R.string.createContact)
        titleRight1?.visibility = View.VISIBLE
        titleRight1?.text = getString(R.string.Done)
        llAddPhone.visibility = View.VISIBLE
        tvSendMsm.visibility = View.GONE
        lists.add("")
        addAction(true)
        llAddPhone.setOnClickListener {
            lists.add("")
            addAction(true)
            if (lists.size >= 5) {
                llAddPhone.visibility = View.GONE
            }
        }
        titleRight1?.setOnClickListener {
            //完成
            val Phone = ArrayList<String>()
            val types = ArrayList<Int>()
            for (i in lists.indices) {
                val element = llPhone.getChildAt(i).etPhoneName.text.toString()
                if (NotNull.isNotNull(element)) {
                    Phone.add(element)
                    types.add(getPhoneType("Mobile"))
                }
            }
            if (!isVail(Phone, types)) return@setOnClickListener
            ContactUtils.addContact(applicationContext, contactMName.etContactName.text.toString(), Phone, types)
            MyToast.showToastCustomerStyleText(this, getString(R.string.NewSuccess))
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun isVail(phone: ArrayList<String>, types: ArrayList<Int>): Boolean {
        if (!NotNull.isNotNull(contactMName.etContactName.text.toString())) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.inputContactName))
            return false
        }
        if (phone.isEmpty() || types.isEmpty()) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.inputContactPhone))
            return false
        }
        return true
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

