package com.goockr.smsantilost.views.activities.antilost

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.zxing.android.CaptureActivity
import kotlinx.android.synthetic.main.activity_add.*


/**
 * 添加设备，好友，扫一扫页面
 */
class AddActivity(override val contentView: Int = R.layout.activity_add) : BaseActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
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
        titleRight = titleLayout.findViewById(R.id.titleRight)
        titleBack = titleLayout.findViewById(R.id.titleBack)

        title?.text = getString(R.string.add)
        titleBack?.setOnClickListener { finish() }

        ll?.addView(titleLayout)
    }
    /**
     * 点击事件
     */
    private fun initClickEvent() {
        ll_AddDevice.setOnClickListener(this)
        ll_AddFriend.setOnClickListener(this)
        ll_SaoYiSao.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var intent = Intent()
        when (v?.id) {
            R.id.ll_AddDevice -> {
                intent.setClass(this,AddDeviceActivity::class.java)
            }
            R.id.ll_AddFriend -> {
                intent.setClass(this,AddFriendActivity::class.java)
            }
            R.id.ll_SaoYiSao -> {
                intent.setClass(this,CaptureActivity::class.java)
            }
        }
        startActivity(intent)
    }
}
