package com.goockr.smsantilost.views.activities.antilost

import android.os.Bundle
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity

class SetSimActivity(override val contentView: Int = R.layout.activity_set_sim) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
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
        title?.text = getString(R.string.SIMNumberSetting)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {

    }
}
