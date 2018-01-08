package com.goockr.smsantilost.views.activities.msm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContactsBean
import com.goockr.smsantilost.entries.PhoneBean
import com.goockr.smsantilost.graphics.SuspensionDecoration
import com.goockr.smsantilost.utils.ContactUtils
import com.goockr.smsantilost.utils.LocaleUtil
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.ChoiceContactAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
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
    private var bDatas: MutableList<PhoneBean> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun initView() {
        title?.text = getString(R.string.comtactList)
        titleRight1?.visibility = View.VISIBLE
        titleRight1?.setTextColor(ContextCompat.getColor(this, R.color.blue))
        titleRight1?.text = getString(R.string.concert)
        val extras = intent.extras
        serializable.addAll(extras.getSerializable("PHONE_DATA") as ArrayList<PhoneBean>)
        mManager = LinearLayoutManager(this)
        recycleView.layoutManager = mManager
        mAdapter = ChoiceContactAdapter(this, mDatas)
        recycleView.adapter = mAdapter
        mDecoration = SuspensionDecoration(this, mDatas)
        mDecoration?.setIsChoice(1, serializable.size)
        recycleView.addItemDecoration(mDecoration)

        thread {
            kotlin.run {
                val systemContactInfos = ContactUtils.getSystemContactInfos(applicationContext)
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
            mDecoration?.setIsChoice(1, mAdapter?.getLists()?.size!!)
        }
        titleBack?.setOnClickListener {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putSerializable("CHOICE_RETURN", ArrayList<PhoneBean>())
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        contactSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(tv: Editable?) {
                formatPhoneState(tv)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun formatPhoneState(tv: Editable?) {
        mDatas.clear()
        if (NotNull.isNotNull(tv) && !TextUtils.equals("", tv)) {
            for (mData in bDatas) {
                if (mData.getMPhone()!!.contains(tv!!.toString()) && !mDatas.contains(mData)) {
                    mDatas.add(mData)
                } else if (mData.phone.contains(tv.toString()) && !mDatas.contains(mData)) {
                    mDatas.add(mData)
                }else if (LocaleUtil.getFirstChar(mData.getMPhone()!!.toUpperCase()).contains(tv.toString().toUpperCase())){
                    mDatas.add(mData)
                }else if (LocaleUtil.getPingYin(mData.getMPhone()!!).contains(tv.toString().toUpperCase())){
                    mDatas.add(mData)
                }
            }
            mDecoration?.setIsChoice(3, mDatas.size)
        } else {
            mDatas.addAll(bDatas)
            mDecoration?.setIsChoice(0, mDatas.size)
        }
        mAdapter?.notifyDataSetChanged()
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
            bDatas.clear()
            bDatas.addAll(mDatas)
            mAdapter?.notifyDataSetChanged()
            indexBar.setNeedRealIndex(true)//设置需要真实的索引
                    .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                    .setmSourceDatas(mDatas)//设置数据
                    .invalidate()
            mDecoration?.setmDatas(mDatas)
        }, 500)
    }
}

