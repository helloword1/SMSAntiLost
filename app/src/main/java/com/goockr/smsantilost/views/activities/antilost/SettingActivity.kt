package com.goockr.smsantilost.views.activities.antilost

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 每个钥匙的设置页面（解绑，提醒声选择，提醒声时长等）
 */
class SettingActivity(override val contentView: Int = R.layout.activity_setting) : BaseActivity(), View.OnClickListener {

    private var dialog: AlertDialog? = null
    private var tv_Cancel: TextView? = null
    private var tv_Ensure: TextView? = null
    private var tv_Text1: TextView? = null
    private var tv_Text2: TextView? = null
    private var tv_Text3: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initDialog()
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
        title?.text = "设置"
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 各个点击事件
     */
    private fun initClickEvent() {
        ll_SetIndexOut.setOnClickListener(this)
        ll_SetAntiLost.setOnClickListener(this)
        ll_SetSim.setOnClickListener(this)
        ll_SetName.setOnClickListener(this)
        ll_SetPermission.setOnClickListener(this)
        ll_Unbundling.setOnClickListener(this)
        tv_Cancel?.setOnClickListener(this)
        tv_Ensure?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var intent = Intent()
        when (v?.id) {
            R.id.ll_SetIndexOut -> {
                intent.setClass(this, SetIndexOutActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_SetAntiLost -> {
                intent.setClass(this, TwoWayAntiActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_SetSim -> {
                intent.setClass(this, SetSimActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_SetName -> {
                intent.setClass(this, SetNameActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_SetPermission -> {
                intent.setClass(this, SetPermissionActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_Unbundling -> {
                dialog?.show()
            }
            R.id.tv_Cancel -> {
                dialog?.hide()
            }
            R.id.tv_Ensure -> {
                // 解绑逻辑
                ToastUtils.showShort(this,"解绑逻辑")
            }
        }
    }

    private fun initDialog() {
        var builder = AlertDialog.Builder(this)
        var customView = layoutInflater.inflate(R.layout.dialog_permission, null)
        builder.setView(customView)
        builder.setIcon(R.mipmap.ic_launcher)
        dialog = builder.create()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        tv_Cancel = customView.findViewById(R.id.tv_Cancel)
        tv_Ensure = customView.findViewById(R.id.tv_Ensure)
        tv_Text1 = customView.findViewById(R.id.tv_Text1)
        tv_Text2 = customView.findViewById(R.id.tv_Text2)
        tv_Text3 = customView.findViewById(R.id.tv_Text3)
        tv_Text1?.text = "确定解除设备绑定吗？"
        tv_Text2?.text = "解除绑定后，你将无法操作该设备"
        tv_Text3?.visibility = View.GONE
    }
}
