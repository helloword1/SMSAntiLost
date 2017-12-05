package com.goockr.smsantilost.views.activities.more

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.login.LoginActivity
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.dialog_permission.view.*


/**
 * 用户设置页面，头像，密码，手机登
 */
class UserSettingActivity(override val contentView: Int = R.layout.activity_user_setting) : BaseActivity(), View.OnClickListener {

    private var bottomDialog: Dialog? = null
    private var dialog: AlertDialog? = null
    private var tv_Cancel: TextView? = null
    private var tv_Ensure: TextView? = null
    private var tv_Text1: TextView? = null
    private var tv_Text2: TextView? = null
    private var tv_Text3: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initBottomDialog()
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
        title?.text = "账号设置"
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        ll_ChangeProfile.setOnClickListener(this)
        ll_UserName.setOnClickListener(this)
        ll_PhoneBind.setOnClickListener(this)
        ll_WeChatBind.setOnClickListener(this)
        ll_ChangePWD.setOnClickListener(this)
        tv_SignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var intent = Intent()
        when (v?.id) {
            R.id.ll_ChangeProfile -> {
                bottomDialog?.show()
            }
            R.id.ll_UserName -> {
                intent.setClass(this@UserSettingActivity, SetUserNameActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_PhoneBind -> {
                intent.setClass(this@UserSettingActivity, BindPhoneNumActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_WeChatBind -> {

            }
            R.id.ll_ChangePWD -> {
                intent.setClass(this@UserSettingActivity, SetPwdActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_SignUp -> {
                dialog?.show()
            }
        }
    }

    private fun initBottomDialog() {
        bottomDialog = Dialog(this, R.style.BottomDialog)
        val contentView = LayoutInflater.from(this).inflate(R.layout.dialog_profile, null)
        bottomDialog?.setContentView(contentView)
        val layoutParams = contentView.layoutParams
        layoutParams.width = resources.displayMetrics.widthPixels
        contentView.layoutParams = layoutParams
        bottomDialog?.window?.setGravity(Gravity.BOTTOM)
        bottomDialog?.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog?.setCancelable(true)
        bottomDialog?.setCanceledOnTouchOutside(true)
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
        tv_Cancel = customView.tv_Cancel
        tv_Ensure = customView.tv_Ensure
        tv_Text1 = customView.tv_Text1
        tv_Text2 = customView.tv_Text2
        tv_Text3 = customView.tv_Text3
        tv_Text1?.text = "是否确定退出登录？"
        tv_Text2?.visibility = View.GONE
        tv_Text3?.visibility = View.GONE
        tv_Cancel?.text = "取消"
        tv_Ensure?.text = "退出"
        tv_Cancel?.setOnClickListener {
            dialog?.dismiss()
        }
        //退出
        tv_Ensure?.setOnClickListener {
            preferences?.clearPreferences()
            val goockrApplication = application as GoockrApplication
            goockrApplication.exit()//清空preference
            showActivity(LoginActivity::class.java)
        }
    }
}
