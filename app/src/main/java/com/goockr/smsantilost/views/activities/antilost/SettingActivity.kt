package com.goockr.smsantilost.views.activities.antilost

import android.app.AlertDialog
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
        title?.text =getString(R.string.setting)
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
        when (v?.id) {
            R.id.ll_SetIndexOut -> {
                //越界提醒
                showActivity(SetIndexOutActivity::class.java)
            }
            R.id.ll_SetAntiLost -> {
                //双向防丢
                showActivity(TwoWayAntiActivity::class.java)
            }
            R.id.ll_SetSim -> {
                //SIM设置
                showActivity(SetSimActivity::class.java)
            }
            R.id.ll_SetName -> {
                //修改名称
                showActivity(SetNameActivity::class.java)
            }
            R.id.ll_SetPermission -> {
                //转移权限
                showActivity(SetPermissionActivity::class.java)
            }
            R.id.ll_Unbundling -> {
                dialog?.show()
            }
            R.id.tv_Cancel -> {
                dialog?.hide()
            }
            R.id.tv_Ensure -> {
                // 解绑逻辑
                ToastUtils.showShort(this,getString(R.string.BindingLogic))
            }
        }
    }

    private fun initDialog() {
        val builder = AlertDialog.Builder(this)
        val customView = layoutInflater.inflate(R.layout.dialog_permission, null)
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
        tv_Text1?.text = getString(R.string.UnbindingSure)
        tv_Text2?.text = getString(R.string.UnbindingAndLost)
        tv_Text3?.visibility = View.GONE
    }
}
