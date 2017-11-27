package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.LoginCodeBean
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
import kotlinx.android.synthetic.main.activity_login.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

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
                tvLoginPassword.inputType=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                isShowPed = true
                tvLoginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvRegister -> showActivity(RegisterActivity::class.java)
            R.id.tvCodeLogin -> showActivity(CodeLoginActivity::class.java)
            R.id.btnConfir -> {
                if (isVail()) {
                    showProgressDialog()
                    val param = RequestParam()
                    param.put("mobile", tvLoginUser.text.toString())
                    param.put("pwd", tvLoginPassword.text.toString())
                    NetWorkUtil.LoginApi(SysInterceptor(this))?.LoginPwd(param)?.
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
                                    MyToast.showToastCustomerStyleText(this@LoginActivity, "${t.msg}")
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

