package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.CreateUserBean
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.MyStringCallback
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.HomeActivity
import com.google.gson.Gson
import com.jude.swipbackhelper.SwipeBackHelper
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_register_next.*
import okhttp3.Call
import java.lang.Exception

class RegisterNextActivity(override val contentView: Int = R.layout.activity_register_next) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun initView() {
        var extras = intent.extras
        var loginPhone = extras.getString(Constant.LOGIN_PHONE)
        var loginMsmCode = extras.getString(Constant.LOGIN_MSM_CODE)

        ll?.visibility = View.GONE

        btnRegister.setOnClickListener {
            if (isVail()) {
                showProgressDialog()
                OkHttpUtils
                        .post()
                        .url(Constant.BASE_URL + NetApi.GET_LOGIN_TOKEN)
                        .addParams("mobile", loginPhone)
                        .addParams("vcode", loginMsmCode)
                        .addParams("pwd", tvPwdNext.text.toString())
                        .build()
                        .execute(object : MyStringCallback() {
                            override fun onResponse(response: String?, id: Int) {
                                val t = Gson().fromJson(response, CreateUserBean::class.java)
                                dismissDialog()
                                if (t.result == 0) {
                                    val myAlertDialog = MyAlertDialog(this@RegisterNextActivity)
                                    myAlertDialog.setTitle(getString(R.string.RegistrationSuccess)).
                                            setContent(getString(R.string.registNext)).setConfirm(getString(R.string.goLogin))
                                    myAlertDialog.show()
                                    myAlertDialog.setOnDialogListener(object :MyAlertDialog.OnDialogListener{
                                        override fun onConfirmListener() {
                                            showActivity(HomeActivity::class.java)
                                            preferences?.putValue(Constant.LOGIN_MSM_CODE, "")
                                            preferences?.putValue(Constant.TOKEN, t.token)
                                            preferences?.putValue(Constant.HAD_LOGIN, "true")
                                            finish()
                                        }

                                        override fun onCancelListener() {
                                            myAlertDialog.dismiss()
                                        }

                                    })

                                }
                                MyToast.showToastCustomerStyleText(this@RegisterNextActivity, "${t.msg}")
                            }

                            override fun onError(call: Call?, e: Exception?, id: Int) {
                                dismissDialog()
                                MyToast.showToastCustomerStyleText(this@RegisterNextActivity, getString(R.string.networkError))
                            }
                        })
            }
        }
        tvCodeLogin.setOnClickListener {
            showActivity(LoginActivity::class.java)
        }
    }

    private fun isVail(): Boolean {
        if (!NotNull.isNotNull(tvPwd.text.toString())) {
            MyToast.showLikeAppDialogSingle(this, getString(R.string.PleaseEnterPwd),getString(R.string.cancel))
            return false
        }
        if (!NotNull.isNotNull(tvPwdNext.text.toString())) {
            MyToast.showLikeAppDialogSingle(this, getString(R.string.enterPwdNext),getString(R.string.cancel))
            return false
        }
        if (!TextUtils.equals(tvPwd.text.toString(), tvPwdNext.text.toString())) {
            MyToast.showLikeAppDialogSingle(this, getString(R.string.enterSamePwd),getString(R.string.cancel))
            return false
        }
        return true
    }
}
