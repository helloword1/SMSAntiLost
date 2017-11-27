package com.goockr.smsantilost.views.activities.login

import android.text.TextUtils
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.LoginCodeBean
import com.goockr.smsantilost.entries.RequestParam
import com.goockr.smsantilost.entries.ValidateCodeBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.DefaultObserver
import com.goockr.smsantilost.utils.*
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.HomeActivity
import cxx.utils.NotNull
import cxx.utils.StringUtils
import kotlinx.android.synthetic.main.activity_code_login.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

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
                    MyToast.showToastCustomerStyleText(this, "请输入手机号码")
                    return
                }
                showProgressDialog()
                val param = RequestParam()
                param.put("mobile", tvLoginUser.text.toString())
                NetWorkUtil.LoginApi(SysInterceptor(this))?.getCode(param)?.
                        subscribeOn(Schedulers.io())?.
                        observeOn(AndroidSchedulers.
                                mainThread())?.
                        subscribe(object : DefaultObserver<ValidateCodeBean>(this) {
                            override fun onNext(t: ValidateCodeBean) {
                                super.onNext(t)
                                if (t.result == 0) {
                                    val timer = CountDownButtonHelper(tvLoginPasswordDelete,
                                            "获取验证码", "重新获取", 60, 1)
                                    timer.start()
                                    preferences?.putValue(Constant.LOGIN_MSM_CODE, t.code.toString())
                                    tvLoginPassword.setText(t.code.toString())
                                }
                                MyToast.showToastCustomerStyleText(this@CodeLoginActivity, "${t.msg}")
                            }

                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                LogUtils.i("", e.toString())
                            }

                        })
            }
            R.id.btn_confir -> {
                if (isValid()) {
                    showProgressDialog()
                    val param = RequestParam()
                    param.put("mobile", tvLoginUser.text.toString())
                    param.put("vcode", tvLoginPassword.text.toString())
                    NetWorkUtil.LoginApi(SysInterceptor(this))?.LoginCode(param)?.
                            subscribeOn(Schedulers.io())?.
                            observeOn(AndroidSchedulers.
                                    mainThread())?.
                            subscribe(object : DefaultObserver<LoginCodeBean>(this) {
                                override fun onNext(t: LoginCodeBean) {
                                    super.onNext(t)
                                    if (t.result == 0) {
                                        showActivity(HomeActivity::class.java)
                                        preferences?.putValue(Constant.TOKEN, t.token)
                                        preferences?.putValue(Constant.HAD_LOGIN, "true")
                                        finish()
                                    }
                                    MyToast.showToastCustomerStyleText(this@CodeLoginActivity, "${t.msg}")
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
        }
    }

    private fun isValid(): Boolean {

        if (!StringUtils.isPhone(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入正确的手机号码")
            return false
        }
        if (!NotNull.isNotNull(tvLoginPassword.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入的短信验证码")
            return false
        }
        if (!TextUtils.equals(preferences?.getStringValue(Constant.LOGIN_MSM_CODE), tvLoginPassword.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入正确的短信验证码")
            return false
        }
        return true
    }
}
