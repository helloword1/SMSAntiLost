package com.goockr.smsantilost.views.activities.antilost

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity

class AddFriendActivity(override val contentView: Int = R.layout.activity_add_friend) : BaseActivity() {

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
        initClick()
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
        titleOk = titleLayout.findViewById(R.id.titleOk)

        title?.text = "添加好友"
        titleOk?.text = "发送"
        titleOk?.visibility = View.VISIBLE
        titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
        titleBack?.setOnClickListener { finish() }

        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClick() {
        titleOk?.setOnClickListener {
            // 检查好友手机号码
            var flag = true
            if (!flag) { // 未注册
                tv_Text1?.text = "该手机号码未注册"
                dialog?.show()
            } else { // 无效
                tv_Text1?.text = "无效手机号码"
                tv_Ensure?.text = "重新输入"
                dialog?.show()
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
        tv_Cancel?.text = "返回"
        tv_Ensure = customView.findViewById(R.id.tv_Ensure)
        tv_Text1 = customView.findViewById(R.id.tv_Text1)
        tv_Text2 = customView.findViewById(R.id.tv_Text2)
        tv_Text3 = customView.findViewById(R.id.tv_Text3)
        tv_Text2?.visibility = View.GONE
        tv_Text3?.visibility = View.GONE
    }

}
