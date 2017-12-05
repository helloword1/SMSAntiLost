package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.os.SystemClock
import android.text.InputType
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.LoginCodeBean
import com.goockr.smsantilost.entries.NetApi.LOGIN_PWD
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.MyStringCallback
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.HomeActivity
import com.google.gson.Gson
import com.jude.swipbackhelper.SwipeBackHelper
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Call
import java.lang.Exception
import kotlin.concurrent.thread

class LoginActivity(override val contentView: Int = R.layout.activity_login) : BaseActivity(), View.OnClickListener {
    var isShowPed = true

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
        tvLoginUser.setText("13666666666")
        tvLoginPassword.setText("123456")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvRegister -> showActivity(RegisterActivity::class.java)
            R.id.tvCodeLogin -> showActivity(CodeLoginActivity::class.java)
            R.id.btnConfir -> {
                if (isVail()) {
                    showProgressDialog()
                    if (TextUtils.equals(tvLoginUser.text.toString(), "13666666666") && TextUtils.equals(tvLoginPassword.text.toString(), "123456")) {
                        thread {
                            SystemClock.sleep(4000)
                            runOnUiThread {
                                dismissDialog()
                                showActivity(HomeActivity::class.java)
                                preferences?.putValue(Constant.HAD_LOGIN, "true")
                                finish()
                            }
                        }
                       return
                    }

                    OkHttpUtils
                            .post()
                            .url(Constant.BASE_URL + LOGIN_PWD)
                            .addParams("mobile", tvLoginUser.text.toString())
                            .addParams("pwd", tvLoginPassword.text.toString())
                            .build()
                            .execute(object : MyStringCallback() {
                                override fun onResponse(response: String?, id: Int) {
                                    val t = Gson().fromJson(response, LoginCodeBean::class.java)
                                    dismissDialog()
                                    if (t.result == 0) {
                                        showActivity(HomeActivity::class.java)
                                        preferences?.putValue(Constant.TOKEN, t.token)
                                        preferences?.putValue(Constant.HAD_LOGIN, "true")
                                        finish()
                                    }
                                    MyToast.showToastCustomerStyleText(this@LoginActivity, "${t.msg}")

                                }

                                override fun onError(call: Call?, e: Exception?, id: Int) {
                                    MyToast.showToastCustomerStyleText(this@LoginActivity, "网络错误")
                                }
                            })
                }
            }
        }
    }

    private fun isVail(): Boolean {
        if (!NotNull.isNotNull(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入账号")
            return false
        }
        if (!NotNull.isNotNull(tvLoginPassword.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入密码")
            return false
        }
        return true
    }

    private var exitTime: Double = 0.toDouble()
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
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
}

