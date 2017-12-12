package com.goockr.smsantilost.views.activities.login

import android.text.TextUtils
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.LoginCodeBean
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.entries.ValidateCodeBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.MyStringCallback
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.CountDownButtonHelper
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.HomeActivity
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import cxx.utils.StringUtils
import kotlinx.android.synthetic.main.activity_code_login.*
import okhttp3.Call
import java.lang.Exception

class CodeLoginActivity(override val contentView: Int = R.layout.activity_code_login) : BaseActivity(),View.OnClickListener {
    override fun initView() {
        ll?.visibility = View.GONE
        tvLoginPasswordDelete.setOnClickListener(this)
        btn_confir.setOnClickListener(this)
        tvRegister.setOnClickListener(this)
        tvCodeLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0?.id) {
            R.id.tvRegister -> showActivity(RegisterActivity::class.java)
            R.id.tvCodeLogin -> showActivity(LoginActivity::class.java)
            R.id.tvLoginPasswordDelete -> {
                if (!NotNull.isNotNull(tvLoginUser.text.toString())) {
                    MyToast.showToastCustomerStyleText(this, getString(R.string.inputRightNumber))
                    return
                }
                showProgressDialog()
                OkHttpUtils
                        .post()
                        .url(Constant.BASE_URL + NetApi.GET_CODE)
                        .addParams("mobile", tvLoginUser.text.toString())
                        .build()
                        .execute(object : MyStringCallback() {
                            override fun onResponse(response: String?, id: Int) {
                                val t = Gson().fromJson(response, ValidateCodeBean::class.java)
                                dismissDialog()
                                if (t.result == 0) {
                                    val timer = CountDownButtonHelper(tvLoginPasswordDelete,
                                            getString(R.string.getMsmCode), getString(R.string.ReWantMsmCode), 60, 1)
                                    timer.start()
                                    preferences?.putValue(Constant.LOGIN_MSM_CODE, t.code.toString())
                                    tvLoginPassword.setText(t.code.toString())
                                }
                                MyToast.showToastCustomerStyleText(this@CodeLoginActivity, "${t.msg}")

                            }

                            override fun onError(call: Call?, e: Exception?, id: Int) {
                                dismissDialog()
                                MyToast.showToastCustomerStyleText(this@CodeLoginActivity, getString(R.string.networkError))
                            }
                        })


            }
            R.id.btn_confir -> {
                if (isValid()) {
                    showProgressDialog()
                    OkHttpUtils
                            .post()
                            .url(Constant.BASE_URL + NetApi.LOGIN_CODE)
                            .addParams("mobile", tvLoginUser.text.toString())
                            .addParams("vcode", tvLoginPassword.text.toString())
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
                                    MyToast.showToastCustomerStyleText(this@CodeLoginActivity, "${t.msg}")

                                }

                                override fun onError(call: Call?, e: Exception?, id: Int) {
                                    dismissDialog()
                                    MyToast.showToastCustomerStyleText(this@CodeLoginActivity, getString(R.string.NetError))
                                }
                            })


                }
            }
        }
    }

    private fun isValid(): Boolean {

        if (!StringUtils.isPhone(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.inputRightNumber))
            return false
        }
        if (!NotNull.isNotNull(tvLoginPassword.text.toString())) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.inputRightMsmCode))
            return false
        }
        if (!TextUtils.equals(preferences?.getStringValue(Constant.LOGIN_MSM_CODE), tvLoginPassword.text.toString())) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.inputRightMsmCode))
            return false
        }
        return true
    }
}
