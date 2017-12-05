package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_area_map.*

class AreaMapActivity(override val contentView: Int = R.layout.activity_area_map) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
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
        titleOk = titleLayout.findViewById(R.id.titleOk)
        title?.text = "防打扰区域"
        titleOk?.text = "下一步"
        titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
        titleOk?.visibility = View.VISIBLE
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 监听editText
        et_Search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    ll_SearchIcon.visibility = View.VISIBLE
                } else {
                    ll_SearchIcon.visibility = View.GONE
                }
            }
        })

        // 监听seekBar
        sb_Radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var string = "" + (progress + 1) + "m"
                tv_LeftRadius.text = string
                tv_CurrentRadius.text = "半径设置：当前半径" + string
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}
