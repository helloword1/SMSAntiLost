package com.goockr.smsantilost.views.activities.login

import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import cxx.utils.StringUtils
import kotlinx.android.synthetic.main.activity_code_login.*

class CodeLoginActivity(override val contentView: Int = R.layout.activity_code_login) : BaseActivity() {
    override fun initView() {
        ll?.visibility = View.GONE


    }

    fun click(p0: View) {
        when (p0?.id) {
            R.id.tvRegister -> showActivity(RegisterActivity::class.java)
            R.id.tvCodeLogin -> showActivity(LoginActivity::class.java)
            R.id.btn_confir -> {
                if (isValid()) {
                    /*val param = RequestParam()
                    param.put("mobile", userStr)
                    param.put("vcode", psdStr)
                    NetWorkUtil.LoginApi(SysInterceptor(this))?.getLoginToken(param)?.
                            subscribeOn(Schedulers.io())?.
                            observeOn(AndroidSchedulers.
                                    mainThread())?.
                            subscribe(Observer<CreateUserBean>)*/
//                    showActivity()
                }
            }
        }
    }

    private fun isValid(): Boolean {

        if (!NotNull.isNotNull(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入手机号码")
            return false
        }
        if (!StringUtils.isPhone(tvLoginUser.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入正确的手机号码")
            return false
        }
        if (!StringUtils.isPhone(tvLoginPassword.text.toString())) {
            MyToast.showToastCustomerStyleText(this, "请输入的短信验证码")
            return false
        }
        if ("12345" != tvLoginPassword.text.toString()) {
            MyToast.showToastCustomerStyleText(this, "请输入正确的短信验证码")
            return false
        }
        return true
    }
}
