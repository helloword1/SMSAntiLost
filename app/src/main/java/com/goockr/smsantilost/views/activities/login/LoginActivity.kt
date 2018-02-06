package com.goockr.smsantilost.views.activities.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.LoginCodeBean
import com.goockr.smsantilost.entries.NetApi.LOGIN_PWD
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.LOGIN_PHONE
import com.goockr.smsantilost.utils.Constant.MOBIL_PHONE_NUM
import com.goockr.smsantilost.utils.Constant.MULTiPLY_MOBIL_PHONE_NUM
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.HomeActivity
import com.google.gson.Gson
import com.jude.swipbackhelper.SwipeBackHelper
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.multiply_phone_num_layout.*
import okhttp3.Call
import java.lang.Exception

class LoginActivity(override val contentView: Int = R.layout.activity_login) : BaseActivity(), View.OnClickListener {
    var isShowPed = true
    private var cIndex="86"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    override fun initView() {
        ll?.visibility = View.GONE
        tvRegister.setOnClickListener(this)
        tvCodeLogin.setOnClickListener(this)
        btnConfir.setOnClickListener(this)
        rlMulPhone.setOnClickListener(this)
        tvLoginPasswordDelete.setOnClickListener {
            if (isShowPed) {
                isShowPed = false
                tvLoginPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                tvLoginPasswordDelete.setImageResource(R.mipmap.icon_so)
            } else {
                isShowPed = true
                tvLoginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                tvLoginPasswordDelete.setImageResource(R.mipmap.icon_invisible)
            }
        }
        val phone = preferences?.getStringValue(LOGIN_PHONE)
        if (NotNull.isNotNull(phone)){
            tvLoginUser.setText(phone)
        }
//        tvLoginUser.setText("13666666666")
//        tvLoginPassword.setText("123456")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvRegister -> showActivity(RegisterActivity::class.java)
            R.id.tvCodeLogin -> showActivity(CodeLoginActivity::class.java)
            R.id.rlMulPhone -> showActivityForResult(MultiplyMobilPhoneNumActivity::class.java, MULTiPLY_MOBIL_PHONE_NUM)
            R.id.btnConfir -> {
                if (isVail()) {
                    showProgressDialog()
//                    if (TextUtils.equals(tvLoginUser.text.toString(), "13666666666") && TextUtils.equals(tvLoginPassword.text.toString(), "123456")) {
//                        kotlin.concurrent.thread {
//                            SystemClock.sleep(4000)
//                            runOnUiThread {
//                                dismissDialog()
//                                showActivity(HomeActivity::class.java)
//                                preferences?.putValue(Constant.HAD_LOGIN, "true")
//                                preferences?.putValue(Constant.LOGIN_PHONE, tvLoginUser.text.toString())
//                                finish()
//                            }
//                        }
//                       return
//                    }

                    OkHttpUtils
                            .post()
                            .url(Constant.BASE_URL + LOGIN_PWD)
                            .addParams("mobile", tvLoginUser.text.toString())
                            .addParams("mobileIndex", cIndex)
                            .addParams("pwd", tvLoginPassword.text.toString())
                            .build()
                            .execute(object : MyStringCallback(this) {
                                override fun onResponse(response: String?, id: Int) {
                                    val t = Gson().fromJson(response, LoginCodeBean::class.java)
                                    dismissDialog()
                                    if (t.result == 0) {
                                        showActivity(HomeActivity::class.java)
                                        preferences?.putValue(Constant.TOKEN, t.token)
                                        preferences?.putValue(Constant.HAD_LOGIN, "true")
                                        preferences?.putValue(Constant.LOGIN_PHONE, tvLoginUser.text.toString())
                                        preferences?.putValue(Constant.PHONE_TYPE, cIndex)
                                        preferences?.putValue(Constant.USER_NAME, t.loginname)
                                        finish()
                                    }
                                    MyToast.showToastCustomerStyleText(this@LoginActivity, "${t.msg}")

                                }

                                override fun onError(call: Call?, e: Exception?, id: Int) {
                                    dismissDialog()
                                    MyToast.showToastCustomerStyleText(this@LoginActivity, getString(R.string.networkError))
                                }
                            })
                }
            }
        }
    }

    private fun isVail(): Boolean {
        if (!NotNull.isNotNull(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.EnterNumber))
            return false
        }
        if (!NotNull.isNotNull(tvLoginPassword.text.toString())) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.enterPwd)/*,getString(R.string.EnterNumber)*/)
            return false
        }
       /* if (!StringUtils.isPhone(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.InvalidMobileNumber))
            return false
        }*/
        return true
    }

    private var exitTime: Double = 0.toDouble()
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, getString(R.string.exitNext), Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis().toDouble()
            } else {
                // TODO 退出客户端
                // 退出
                (application as GoockrApplication).exit()
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MULTiPLY_MOBIL_PHONE_NUM && resultCode == Activity.RESULT_OK) {
            cIndex = data?.getStringExtra(MOBIL_PHONE_NUM)!!
            tvMulPhone.text = "+$cIndex"
        }
    }
}

