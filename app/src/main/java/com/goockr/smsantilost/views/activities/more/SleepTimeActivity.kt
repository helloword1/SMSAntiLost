package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.SleepTimeBean
import com.goockr.smsantilost.utils.FileCache
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.SleepTimeAdapter
import kotlinx.android.synthetic.main.activity_sleep_time.*

class SleepTimeActivity(override val contentView: Int = R.layout.activity_sleep_time) : BaseActivity() {

    private val REQUEST_SLEEP = 2
    private var mData: ArrayList<SleepTimeBean>? = null
    private var mAdapter: SleepTimeAdapter? = null
    private var mCache: FileCache? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCache = FileCache.get(this)
        initMData()
        initMView()
        initListView()
        initClickEvent()
    }

    private fun initMData() {
//        var date = CacheUtils.getInstance().getSerializable("sleep",null)
//        var date = ObjectSaveUtils.getObject(this, "sleep")
        var data = mCache!!.getAsObject("sleep")
        if (data == null) {
            mData = ArrayList()
        } else {
            mData = data as ArrayList<SleepTimeBean>
            lv_SleepTime.visibility = View.VISIBLE
        }
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
        title?.text = getString(R.string.sleepTime)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 初始化listView
     */
    private fun initListView() {
        mAdapter = SleepTimeAdapter(this, mData,mCache)
        lv_SleepTime.adapter = mAdapter
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        ll_AddSleepTime.setOnClickListener {
            val i = Intent()
            i.setClass(this, AddSleepTimeActivity::class.java)
            startActivityForResult(i, REQUEST_SLEEP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SLEEP && resultCode == REQUEST_SLEEP) {
            val parcelable = data!!.extras.getSerializable("bean")
            mData!!.add(parcelable as SleepTimeBean)
//            CacheUtils.getInstance().put("sleep", mData!!)
//            ObjectSaveUtils.saveObject(this,"sleep",mData)
            mCache!!.put("sleep", mData)
            mAdapter!!.notifyDataSetChanged()
            lv_SleepTime.visibility = View.VISIBLE
        }
    }
}
