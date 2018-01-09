package com.goockr.smsantilost.views.activities.msm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.MsmBean
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.MsmManagerAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_msm_manager.*
import kotlinx.android.synthetic.main.custom_title_view.*

/**
 * Created by LJN on 2017/11/14.
 * 短信管理
 */

class MSMManagerActivity(override val contentView: Int = R.layout.activity_msm_manager) : BaseActivity() {
    private val lists = ArrayList<MsmBean>()
    private val mDatas = ArrayList<MsmBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)

    }

    override fun initView() {
        ll?.removeAllViews()
        ll?.addView(layoutInflater.inflate(R.layout.custom_title_view, null))
        cTitle.text = getString(R.string.SMSManager)
        cTitleBack.text = getString(R.string.cancel)
        val extras = intent.extras
        val boolean = extras.getBoolean("IS_SEARCH")

        val arrayList = extras.getSerializable("DATA") as ArrayList<*>
        recycleView.layoutManager = LinearLayoutManager(this)
        if (boolean) {
            arrayList.removeAt(0)
        }
        mDatas.addAll(arrayList as ArrayList<MsmBean>)
        if (mDatas.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
        val msmManagerAdapter = MsmManagerAdapter(this, mDatas)
        recycleView.adapter = msmManagerAdapter
        //删除
        tvDelete.setOnClickListener {
            lists.filter { mDatas.contains(it) }
                    .forEach {
                        it.isCheck = false
                        mDatas.remove(it)
                    }
            msmManagerAdapter.notifyDataSetChanged()
            setDatasBack()
        }
        //已读
        tvHadRead.setOnClickListener {
            lists.filter { mDatas.contains(it) }
                    .forEach {
                        it.isCheck = false
                        it.isShow = false
                    }
            msmManagerAdapter.notifyDataSetChanged()
            setDatasBack()
        }

        msmManagerAdapter.setoOnGetAdapterListener {
            lists.clear()
            lists.addAll(it)
            if (!lists.isEmpty()) {
                tvHadRead.text = "已读"
                tvHadRead.setTextColor(ContextCompat.getColor(this, R.color.blue))
                tvHadRead.isEnabled = true
                tvDelete.setTextColor(ContextCompat.getColor(this, R.color.blue))
                tvDelete.isEnabled = true
            } else {
                tvHadRead.text = "标志为已读"
                tvHadRead.setTextColor(ContextCompat.getColor(this, R.color.some_black))
                tvHadRead.isEnabled = false
                tvDelete.setTextColor(ContextCompat.getColor(this, R.color.some_black))
                tvDelete.isEnabled = false
            }
        }
        cTitleBack.setOnClickListener {
            finish()
        }
    }

    private fun setDatasBack() {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putSerializable("MSM_MANAGER", mDatas)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

