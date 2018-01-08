package com.goockr.smsantilost.views.activities.more

import android.support.v7.widget.LinearLayoutManager
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.MesBean
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.MesAdapter
import kotlinx.android.synthetic.main.activity_mes.*


/**
 * 消息中心
 */
class MesActivity(override val contentView: Int = R.layout.activity_mes) : BaseActivity() {
    private val mDatas = ArrayList<MesBean>()
    private var mesAdapter: MesAdapter? = null
    override fun initView() {
        title?.text = getString(R.string.mes)
        initDate()
    }

    private fun initDate() {
        recycleView.layoutManager = LinearLayoutManager(this)
        mesAdapter = MesAdapter(this, mDatas)
        recycleView.adapter = mesAdapter
        for (i in 1..10) {
            val mesBean = MesBean()
            mesBean.mesTitle = "系统通知"
            mesBean.mesContent = "系统更新升级！！"
            mesBean.mesDetailText = "于2018年01月12日更新！！"
            mesBean.mesDate = "8天前"
            mDatas.add(mesBean)
        }
        mesAdapter?.notifyDataSetChanged()
    }
}
