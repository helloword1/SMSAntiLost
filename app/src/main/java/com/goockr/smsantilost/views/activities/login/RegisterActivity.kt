package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.RequestParam
import com.goockr.smsantilost.entries.ValidateCodeBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.DefaultObserver
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.NetWorkUtil
import com.goockr.smsantilost.utils.SysInterceptor
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import cxx.utils.StringUtils
import kotlinx.android.synthetic.main.activity_register.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class RegisterActivity(override val contentView: Int = R.layout.activity_register) : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
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
                    val param = RequestParam()
                    param.put("mobile", tvLoginUser.text.toString())
                    NetWorkUtil.LoginApi(SysInterceptor(this))?.getCode(param)?.
                            subscribeOn(Schedulers.io())?.
                            observeOn(AndroidSchedulers.
                                    mainThread())?.
                            subscribe(object : DefaultObserver<ValidateCodeBean>(this) {
                                override fun onNext(t: ValidateCodeBean) {
                                    LogUtils.i("这是验证码：", t.code.toString())
                                }

                                override fun onCompleted() {

                                }

                                override fun onError(e: Throwable?) {
                                    LogUtils.i("", e.toString())
                                }

                            })
                }

            }
            R.id.tvCodeLogin -> showActivity(LoginActivity::class.java)
            R.id.btn_confir -> {
                if (isValid()) {
                    showActivity(RegisterNextActivity::class.java)
                }
            }
        }
    }

    private fun isValid(): Boolean {
        if (isValidPhone()) {
            if (!StringUtils.isPhone(tvLoginPassword.text.toString())) {
                MyToast.showToastCustomerStyleText(this, "请输入的短信验证码")
                return false
            }
            if ("12345" != tvLoginPassword.text.toString()) {
                MyToast.showToastCustomerStyleText(this, "请输入正确的短信验证码")
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
