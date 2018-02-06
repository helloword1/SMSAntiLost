package com.goockr.smsantilost.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.goockr.smsantilost.utils.ContactUtils.getSystemContactInfos
import com.goockr.smsantilost.utils.LocaleUtil
import com.goockr.smsantilost.views.activities.msm.CreateContactActivity
import com.goockr.smsantilost.views.activities.msm.SettingContactActivity
import com.goockr.smsantilost.views.adapters.CityAdapter
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlin.concurrent.thread

/**
 * 通讯录页
 */

class ContactFragment : BaseFragment() {
    private var mAdapter: CityAdapter? = null
    private var mManager: LinearLayoutManager? = null
    private var mDatas: MutableList<PhoneBean> = ArrayList()
    private var bDatas: MutableList<PhoneBean> = ArrayList()
    private var mDecoration: SuspensionDecoration? = null
    private val REQUEST_PERMISSION = 111
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return setContentView(R.layout.fragment_contact)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        mManager = LinearLayoutManager(activity)
        rv.layoutManager = mManager
        mAdapter = CityAdapter(this, activity, mDatas as ArrayList<PhoneBean>)
//        rv.setEmptyView(layoutInflater.inflate(R.layout.empty_view, null))
        rv.adapter = mAdapter
        mDecoration = SuspensionDecoration(activity, mDatas)
        rv.addItemDecoration(mDecoration)
        //分割线
//        rv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST))

        thread {
            kotlin.run {
                val systemContactInfos = getSystemContactInfos(activity)
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
        //判断权限
        val readContacts = activity.packageManager.checkPermission(
                Manifest.permission.READ_CONTACTS, activity.packageName) == PackageManager.PERMISSION_GRANTED
        if (!readContacts) {
            tvEmptyView.text = getString(R.string.noContacts)
            btnOpenRight.visibility = View.VISIBLE
            btnOpenRight.setOnClickListener {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_PERMISSION)
            }
        } else {
            tvEmptyView.text = getString(R.string.noRight)
            btnOpenRight.visibility = View.GONE
        }
        //新建联系人
        floatSetting.setOnClickListener {
            showActivityForResult(SettingContactActivity::class.java, CONTACT_RESULT_ID)
        }
        //设置
        floatSend.setOnClickListener {
            showActivityForResult(CreateContactActivity::class.java, CONTACT_RESULT_ID)
        }
        smsSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(tv: Editable?) {
                formatPhoneState(tv)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) {
            thread {
                kotlin.run {
                    val systemContactInfos = getSystemContactInfos(activity)
                    activity.runOnUiThread { initDatas(systemContactInfos) }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults)
        }
    }

    private fun formatPhoneState(tv: Editable?) {
        mDatas.clear()
        if (NotNull.isNotNull(tv) && !TextUtils.equals("", tv)) {
            for (mData in bDatas) {
                if (mData.getMPhone()!!.contains(tv!!.toString()) && !mDatas.contains(mData)) {
                    mDatas.add(mData)
                } else if (mData.phone.contains(tv.toString()) && !mDatas.contains(mData)) {
                    mDatas.add(mData)
                } else if (LocaleUtil.getFirstChar(mData.getMPhone()!!.toUpperCase()).contains(tv.toString().toUpperCase())) {
                    mDatas.add(mData)
                }
            }
            mDecoration?.setIsChoice(3, mDatas.size)
        } else {
            mDatas.addAll(bDatas)
            mDecoration?.setIsChoice(0, mDatas.size)
        }
        notifyDatas()
    }

    private fun initDatas(data: List<ContactsBean>) {
        //延迟两秒 模拟加载数据中....
        activity.window.decorView.postDelayed({
            mDatas.clear()
            bDatas.clear()
            for (i in data.indices) {
                val cityBean = PhoneBean()
                cityBean.setMPhone(data[i].name)//设置名称
                cityBean.phone = (data[i].phone)//设置电话
                cityBean.id = (data[i].id)//设置id
                mDatas.add(cityBean)
                bDatas.add(cityBean)
            }
            if (mDatas.isEmpty()) {
                emptyView.visibility = View.VISIBLE
            } else {
                emptyView.visibility = View.GONE
            }
            notifyDatas()
        }, 500)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.CONTACT_RESULT_ID) {
            thread {
                kotlin.run {
                    val systemContactInfos = getSystemContactInfos(activity)
                    activity.runOnUiThread { initDatas(systemContactInfos) }
                }
            }
        }
    }

    /**
     * 刷新并重新筛选
     */
    private fun notifyDatas() {
        mAdapter?.notifyDataSetChanged()
        indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mDatas)//设置数据
                .invalidate()
        mDecoration?.setmDatas(mDatas)
    }
}


