package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.CreateUserBean
import com.goockr.smsantilost.entries.RequestParam
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.DefaultObserver
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.NetWorkUtil
import com.goockr.smsantilost.utils.SysInterceptor
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.HomeActivity
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_register_next.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class RegisterNextActivity(override val contentView: Int = R.layout.activity_register_next) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    override fun initView() {
        var extras = intent.extras
        var loginPhone = extras.getString(Constant.LOGIN_PHONE)
        var loginMsmCode = extras.getString(Constant.LOGIN_MSM_CODE)

        ll?.visibility = View.GONE

        btnRegister.setOnClickListener {
            if (isVail()) {
                showProgressDialog()
                val param = RequestParam()
                param.put("mobile", loginPhone)
                param.put("vcode", loginMsmCode)
                param.put("pwd", tvPwdNext.text.toString())
                NetWorkUtil.LoginApi(SysInterceptor(this))?.getLoginToken(param)?.
                        subscribeOn(Schedulers.io())?.
                        observeOn(AndroidSchedulers.
                                mainThread())?.
                        subscribe(object : DefaultObserver<CreateUserBean>(this) {
                            override fun onNext(t: CreateUserBean) {
                                super.onNext(t)
                                if (t.result == 0) {
                                    showActivity(HomeActivity::class.java)
                                    preferences?.putValue(Constant.LOGIN_MSM_CODE, "")
                                    preferences?.putValue(Constant.TOKEN, t.token)
                                    preferences?.putValue(Constant.HAD_LOGIN, "true")
                                    finish()
                                }
                                MyToast.showToastCustomerStyleText(this@RegisterNextActivity, "${t.msg}")

                            }

                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                LogUtils.i("", e.toString())
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
            MyToast.showToastCustomerStyleText(this, "请输入密码")
            return false
        }
        if (!NotNull.isNotNull(tvPwdNext.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请再次输入密码")
            return false
        }
        if (!TextUtils.equals(tvPwd.text.toString(), tvPwdNext.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入两次相同的密码")
            return false
        }
        return true
    }
}
