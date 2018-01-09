package com.goockr.smsantilost.views.activities.msm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContactsBean
import com.goockr.smsantilost.entries.PhoneBean
import com.goockr.smsantilost.graphics.DeleteDecoration
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.ContactUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.DeleteContactAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_delete_contact.*
import kotlin.concurrent.thread

/**
 * Created by LJN on 2017/11/14.
 * 批量删除联系人
 */

class DeleteContactActivity(override val contentView: Int = R.layout.activity_delete_contact) : BaseActivity() {
    private var mDatas = ArrayList<PhoneBean>()
    private var mAdapter: DeleteContactAdapter? = null
    private var mDecoration: DeleteDecoration? = null
    private var mManager: LinearLayoutManager? = null
    private var isAllchoice = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        title?.text = getString(R.string.BatchDeletingContacts)
        titleRight1?.visibility = View.VISIBLE
        titleRight1?.setTextColor(ContextCompat.getColor(this, R.color.blue))
        titleRight1?.text = getString(R.string.delete)
        mManager = LinearLayoutManager(this)
        recycleView.layoutManager = mManager
        mAdapter = DeleteContactAdapter(this, mDatas)
        recycleView.adapter = mAdapter
        mDecoration = DeleteDecoration(this, mDatas)
        mDecoration?.setIsChoice(false)
        recycleView.addItemDecoration(mDecoration)

        thread {
            kotlin.run {
                val systemContactInfos = ContactUtils.getSystemContactInfos(applicationContext)
                runOnUiThread { initDatas(systemContactInfos) }
            }
        }
        //删除
        titleRight1?.setOnClickListener {
            val beans = ArrayList<PhoneBean>()
            mAdapter?.getLists()?.filterTo(beans) { it.isshowRight }
            ContactUtils.BatchDeleteContact(applicationContext,beans)
            mDatas.removeAll(beans)
            mAdapter?.notifyDataSetChanged()
            MyToast.showToastCustomerStyleText(this,getString(R.string.DeleteSuccess))
            haveChoice.text = "已选中0人"
        }
        allChoice.setOnClickListener {
            val beans = ArrayList<PhoneBean>()
            mAdapter?.getLists()?.filterTo(beans) { it.isshowRight }
            if (beans.size!=mDatas.size) {
                allChoice.setTextColor(ContextCompat.getColor(this, R.color.blue))
                mDatas.map { it.isshowRight = true }
                haveChoice.text = "已选中${mDatas.size}人"

            } else {
                allChoice.setTextColor(ContextCompat.getColor(this, R.color.statueBarColor))
                mDatas.map { it.isshowRight = false }
                haveChoice.text = "已选中0人"
            }
            mAdapter?.notifyDataSetChanged()
        }
        mAdapter?.setoOnGetAdapterListener {
            val beans = ArrayList<PhoneBean>()
            mAdapter?.getLists()!!.filterTo(beans) { it.isshowRight }
            haveChoice.text = "已选中${beans.size}人"
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
}

