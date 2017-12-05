package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.entries.ValidateCodeBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.MyStringCallback
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.LOGIN_MSM_CODE
import com.goockr.smsantilost.utils.CountDownButtonHelper
import com.goockr.smsantilost.views.activities.BaseActivity
import com.google.gson.Gson
import com.jude.swipbackhelper.SwipeBackHelper
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import cxx.utils.StringUtils
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.Call
import java.lang.Exception

class RegisterActivity(override val contentView: Int = R.layout.activity_register) : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun initView() {
        ll?.visibility = View.GONE
        getCode.setOnClickListener(this)
        tvCodeLogin.setOnClickListener(this)
        btn_confir.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
        //获取验证码
            R.id.getCode -> {

                if (isValidPhone()) {
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
                                        val timer = CountDownButtonHelper(getCode,
                                                "获取验证码", "重新获取", 60, 1)
                                        timer.start()
                                        preferences?.putValue(LOGIN_MSM_CODE, t.code.toString())
//                                        tvLoginPassword.setText(t.code.toString())
                                    }
                                    MyToast.showToastCustomerStyleText(this@RegisterActivity, "${t.msg}")
                                }

                                override fun onError(call: Call?, e: Exception?, id: Int) {
                                    dismissDialog()
                                    MyToast.showToastCustomerStyleText(this@RegisterActivity, "网络错误")
                                }
                            })
                }
            }
            R.id.tvCodeLogin -> showActivity(LoginActivity::class.java)
            R.id.btn_confir -> {
                if (isValid()) {
                    val bundle = Bundle()
                    bundle.putString(Constant.LOGIN_PHONE, tvLoginUser.text.toString())
                    bundle.putString(Constant.LOGIN_MSM_CODE, tvLoginPassword.text.toString())
                    showActivity(RegisterNextActivity::class.java, bundle)
                }
            }
        }
    }

    private fun isValid(): Boolean {
        if (isValidPhone()) {
            if (!NotNull.isNotNull(tvLoginPassword.text.toString())) {
                MyToast.showToastCustomerStyleText(this, "请输入的短信验证码")
                return false
            }
            if (!TextUtils.equals(preferences?.getStringValue(LOGIN_MSM_CODE), tvLoginPassword.text.toString())) {
                MyToast.showToastCustomerStyleText(this, "验证码错误")
                return false
            }
        } else {
            return false
        }
        return true
    }

    private fun isValidPhone(): Boolean {

        if (!NotNull.isNotNull(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入手机号码")
            return false
        }
        var phone = StringUtils.isPhone(tvLoginUser.text.toString())
        if (!phone) {
            MyToast.showToastCustomerStyleText(this, "请输入正确的手机号码")
            return false
        }

        return true
    }
}
