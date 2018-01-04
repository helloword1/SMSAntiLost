package com.goockr.smsantilost.views.activities.antilost

import android.os.Bundle
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_set_sim.*

class SetSimActivity(override val contentView: Int = R.layout.activity_set_sim) : BaseActivity() {
    private var isInsert = false
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

        val extras = intent.extras
        isInsert = extras.getBoolean("IS_INSERT")
        tv_IsInsertSim.text=if (isInsert){
            getString(R.string.simHadInsert)
        }else{
            getString(R.string.notInsert)
        }
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {

    }
}
