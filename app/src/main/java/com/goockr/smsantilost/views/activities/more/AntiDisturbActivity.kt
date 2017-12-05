package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AreaBean
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.AreaAdapter
import kotlinx.android.synthetic.main.activity_anti_disturb.*

class AntiDisturbActivity(override val contentView: Int = R.layout.activity_anti_disturb) : BaseActivity() {

    private var mDatas: ArrayList<AreaBean>? = null
    private var mAdapter: AreaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initMData()
        initListView()
        initClickEvent()
    }

    /**
     * 初始化title
     */
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        titleOk = titleLayout.findViewById(R.id.titleOk)
        title?.text = "防打扰区域"
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 初始化区域数据
     */
    private fun initMData() {
        mDatas = ArrayList()
        mDatas?.add(AreaBean("公司", "天安中心"))
        mDatas?.add(AreaBean("家", "顺德广场"))
        mDatas?.add(AreaBean("添加勿扰区域", "null"))
    }


    private fun initListView() {
        mAdapter = AreaAdapter(this,mDatas)
        lv_AreaList.adapter = mAdapter
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
    }
}
