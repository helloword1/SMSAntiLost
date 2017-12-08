package com.goockr.smsantilost.views.activities.msm

import android.text.Html
import android.text.TextUtils
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContactsBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.ContactUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_merge_contact.*
import kotlin.concurrent.thread

/**
 * Created by LJN on 2017/11/14.
 * 合并重复联系人
 */

class MergeContactActivity(override val contentView: Int = R.layout.activity_merge_contact) : BaseActivity() {
    private val nameAndPhoneList = ArrayList<ArrayList<ContactsBean>>()
    private val nameList = ArrayList<ArrayList<ContactsBean>>()
    private val phoneList = ArrayList<ArrayList<ContactsBean>>()

    override fun initView() {
        title?.text = "合并重复联系人"
        thread {
            val systemContactInfos = ContactUtils.getSystemContactInfos(applicationContext)
            sameByNameAndPhone(systemContactInfos as ArrayList<ContactsBean>)
            sameByName(systemContactInfos)
            phoneList.addAll(sameByPhone(systemContactInfos))
            runOnUiThread {
                tvMergeContact.text = Html.fromHtml("<font color='#4285f4'>${nameAndPhoneList.filterTo(ArrayList()) { it.size > 1 }.size} 组</font>联系人资料完全重复")
                tvMergeName.text = Html.fromHtml("<font color='#4285f4'>${nameList.filterTo(ArrayList()) { it.size > 1 }.size} 组</font>联系人资料姓名重复")
                tvMergeNumber.text = Html.fromHtml("<font color='#4285f4'>${ phoneList.filterTo(ArrayList()) { it.size > 1 }.size} 组</font>联系人资料号码重复")
            }
        }
        //完全重复
        tvMergeContact.setOnClickListener {
            MyToast.showToastCustomerStyleText(this, "功能开发中")
        }
        //姓名重复
        tvMergeName.setOnClickListener {
            MyToast.showToastCustomerStyleText(this, "功能开发中")
        }
        //号码重复
        tvMergeNumber.setOnClickListener {
            MyToast.showToastCustomerStyleText(this, "功能开发中")
        }
    }

    //获取资料中姓名电话重复的用户
    private fun sameByNameAndPhone(lists: ArrayList<ContactsBean>) {
        val sameList = ArrayList<ContactsBean>()
        val diffList = ArrayList<ContactsBean>()
        sameList.add(lists[0])
        var i = 1
        while (i < lists.size) {
            if (lists[i].name == sameList[0].name && lists[i].phone == sameList[0].phone) {
                sameList.add(lists[i])
            } else {
                diffList.add(lists[i])
            }
            i++
        }
        nameAndPhoneList.add(sameList)
        if (!diffList.isEmpty()) {
            sameByNameAndPhone(diffList)
        }
    }

    //获取资料中姓名重复的用户
    private fun sameByName(lists: ArrayList<ContactsBean>) {
        val sameList = ArrayList<ContactsBean>()
        val diffList = ArrayList<ContactsBean>()
        sameList.add(lists[0])
        var i = 1
        while (i < lists.size) {
            if (lists[i].name == sameList[0].name) {
                sameList.add(lists[i])
            } else {
                diffList.add(lists[i])
            }
            i++
        }
        nameList.add(sameList)
        if (!diffList.isEmpty()) {
            sameByName(diffList)
        }
    }

    //获取资料中姓名电话重复的用户
    private fun sameByPhone(lists: ArrayList<ContactsBean>) :ArrayList<ArrayList<ContactsBean>>{
        val phoneList = ArrayList<ArrayList<ContactsBean>>()
        var i = 0
        var j = 1
        while (i < lists.size) {
            val sameList = ArrayList<ContactsBean>()
            while (j < lists.size) {
                when {
                    lists[i].phone.contains(",") && !lists[j].phone.contains(",") && lists[i].phone.contains(lists[j].phone) -> sameList.add(lists[j])
                    lists[i].phone.contains(",") && lists[j].phone.contains(",") -> {
                        val split = lists[i].phone.split(",")
                        val split1 = lists[j].phone.split(",")
                        for (s in split) {
                            split1
                                    .filter { TextUtils.equals(s, it) }
                                    .forEach {
                                        if (!sameList.contains(lists[j]))
                                            sameList.add(lists[j])

                                    }
                        }

                    }
                    !lists[i].phone.contains(",") && lists[j].phone.contains(",") && lists[j].phone.contains(lists[i].phone) -> sameList.add(lists[j])
                    !lists[i].phone.contains(",") && !lists[j].phone.contains(",") && TextUtils.equals(lists[i].phone, lists[j].phone) -> sameList.add(lists[j])
                    else -> {
                    }
                }
                j++
            }
            i++
            phoneList.add(sameList)
        }
        return phoneList
    }
}

