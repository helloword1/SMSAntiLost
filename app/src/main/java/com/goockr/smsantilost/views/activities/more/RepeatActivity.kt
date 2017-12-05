package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.RepeatBean
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.RepeatAdapter
import kotlinx.android.synthetic.main.activity_repeat.*

class RepeatActivity(override val contentView: Int = R.layout.activity_repeat) : BaseActivity() {

    private var mData: ArrayList<RepeatBean>? = null
    private val RESULT_REPEAT = 1
    private var canClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
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
        title?.text = "重复"
        titleOk?.text = "完成"
        titleOk?.visibility = View.VISIBLE
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    private fun initListView() {
        mData = ArrayList<RepeatBean>()
        mData?.add(RepeatBean("星期日", false))
        mData?.add(RepeatBean("星期一", false))
        mData?.add(RepeatBean("星期二", false))
        mData?.add(RepeatBean("星期三", false))
        mData?.add(RepeatBean("星期四", false))
        mData?.add(RepeatBean("星期五", false))
        mData?.add(RepeatBean("星期六", false))
        val adapter = RepeatAdapter(mData, this)
        lv_Repeat.adapter = adapter
        lv_Repeat.setOnItemClickListener { parent, view, position, id ->
            mData!![position].isSelected = !mData!![position].isSelected
            adapter.notifyDataSetChanged()
            for (m in mData!!) {
                if (m.isSelected) {
                    titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
                    canClick = true
                    return@setOnItemClickListener
                }
            }
            titleOk?.setTextColor(resources.getColor(R.color.appGray))
            canClick = false
        }
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        titleOk?.setOnClickListener {
            if (canClick) {
                val repeatDate = checkDate(mData!!)
                val i = Intent()
                i.putExtra("repeat", repeatDate)
                setResult(RESULT_REPEAT, i)
                finish()
            } else {
                ToastUtils.showShort(this, "请选择重复日期")
            }
        }
    }

    private fun checkDate(list: ArrayList<RepeatBean>): String {
        if (list[0].isSelected && list[6].isSelected && !list[1].isSelected && !list[2].isSelected && !list[3].isSelected && !list[4].isSelected && !list[5].isSelected) {
            return "周末"
        } else if (!list[0].isSelected && !list[6].isSelected) {
            return "工作日"
        } else {
            var str = StringBuffer()
            for (i in list) {
                if (i.isSelected) {
                    str.append(i.date + " ")
                }
            }
            return str.toString()
        }
    }
}
