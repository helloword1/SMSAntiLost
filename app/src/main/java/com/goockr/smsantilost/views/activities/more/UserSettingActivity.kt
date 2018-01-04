package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.graphics.PhotoAlertDialog
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.login.LoginActivity
import kotlinx.android.synthetic.main.activity_user_setting.*


/**
 * 用户设置页面，头像，密码，手机登
 */
class UserSettingActivity(override val contentView: Int = R.layout.activity_user_setting) : BaseActivity(), View.OnClickListener {

    private var bottomDialog: PhotoAlertDialog? = null
    private var myAlertDialog: MyAlertDialog? = null

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
        title?.text = getString(R.string.accountSetting)
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
        val intent = Intent()
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
                myAlertDialog?.show()
            }
        }
    }

    private fun initBottomDialog() {
        bottomDialog = PhotoAlertDialog(this)
        bottomDialog?.setOnDialogListener(object :PhotoAlertDialog.OnDialogListener{
            override fun onTakePhotoListener() {
                //拍照

            }

            override fun onFromPicListener() {
                //照片

            }

            override fun onPhotoCancelListener() {
                bottomDialog?.hide()
            }

        })
    }

    private fun initDialog() {
        myAlertDialog = MyAlertDialog(this).setTitle(getString(R.string.exitLogin)).setContent("")
        myAlertDialog?.setOnDialogListener(object :MyAlertDialog.OnDialogListener{
            override fun onConfirmListener() {
                preferences?.clearPreferences()
                val goockrApplication = application as GoockrApplication
                goockrApplication.exit()//清空preference
                showActivity(LoginActivity::class.java)

            }
            override fun onCancelListener() {
                myAlertDialog?.hide()
            }

        })
    }
}
