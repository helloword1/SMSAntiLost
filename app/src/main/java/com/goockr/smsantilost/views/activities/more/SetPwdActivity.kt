package com.goockr.smsantilost.views.activities.more

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.entries.ValidateCodeBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.https.MyStringCallback
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.LOGIN_MSM_CODE
import com.goockr.smsantilost.utils.Constant.LOGIN_PHONE
import com.goockr.smsantilost.utils.CountDownButtonHelper
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_code_login.*
import kotlinx.android.synthetic.main.activity_set_pwd.*
import kotlinx.android.synthetic.main.page1_set_pwd.*
import kotlinx.android.synthetic.main.page2_set_pwd.*
import okhttp3.Call
import java.lang.Exception

class SetPwdActivity(override val contentView: Int = R.layout.activity_set_pwd) : BaseActivity() {

    // 当前页数
    private var mCurrent = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initClickEvent()
    }

    /**
     * 初始化title
     */
    @SuppressLint("SetTextI18n")
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        titleOk = titleLayout.findViewById(R.id.titleOk)
        titleOk?.text = getString(R.string.nextStep)
        titleOk?.visibility = View.VISIBLE
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        title?.text = getString(R.string.changePwd)
        ll?.addView(titleLayout)
        val phone = preferences?.getStringValue(LOGIN_PHONE)
        if (NotNull.isNotNull(phone)){
            val replace = phone?.replace(phone.substring(3, 9), "***")
            tv_CodeTips.text = getString(R.string.SentAuth) + replace
        }


    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        iv_CleanPwd1.setOnClickListener {
            et_InputNewPWD.text = null
        }
        iv_CleanPwd2.setOnClickListener {
            et_InputNewPWDAgain.text = null
        }
        // 下一步
        titleOk?.setOnClickListener {
            if (mCurrent == 1) {
                val code = preferences?.getStringValue(LOGIN_MSM_CODE)
                // 检查是否输入验证码
                if (TextUtils.isEmpty(et_VerificationCode1.text)) {
                    ToastUtils.showShort(this,getString(R.string.inputCode))
                }else if (TextUtils.equals(code,et_VerificationCode1.text)){
                    title?.text = getString(R.string.EnterNewPassword)
                    titleOk?.text = getString(R.string.Done)
                    showPage2()
                }else{
                    ToastUtils.showShort(this,getString(R.string.msmCodeError))
                }
            }else if (mCurrent == 2) {
                // 检查两次新密码输入是否一致
                if (et_InputNewPWD.text.equals(et_InputNewPWDAgain.text)) {
                    ToastUtils.showShort(this,getString(R.string.changeSucceed))
                    titleOk?.text = getString(R.string.nextStep)
                    finish()
                }else {
                    ToastUtils.showShort(this,getString(R.string.pwdNotSame))
                }
            }
        }
        // 返回键
        titleBack?.setOnClickListener {
            if (mCurrent == 1) {
                finish()
            }else if (mCurrent == 2) {
                title?.text = getString(R.string.changePwd)
                showPage1()
            }
        }
        // 监听editText
        et_InputNewPWD.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    iv_CleanPwd1.visibility = View.GONE
                }else {
                    iv_CleanPwd1.visibility = View.VISIBLE
                }
            }
        })// 监听editText
        et_VerificationCode1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    titleOk?.setTextColor(ContextCompat.getColor(this@SetPwdActivity,R.color.msmTextColorGray))
                }else {
                    titleOk?.setTextColor(ContextCompat.getColor(this@SetPwdActivity,R.color.blue))
                }
            }
        })
        et_InputNewPWDAgain.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    iv_CleanPwd2.visibility = View.GONE
                }else {
                    iv_CleanPwd2.visibility = View.VISIBLE
                }
            }
        })
        getPwdCode.setOnClickListener {
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
                                ToastUtils.showShort(this@SetPwdActivity,"验证码已发送，请注意查收")
                                val timer = CountDownButtonHelper(getPwdCode,
                                        getString(R.string.getMsmCode), getString(R.string.ReWantMsmCode), 60, 1)
                                timer.start()
                                preferences?.putValue(Constant.LOGIN_MSM_CODE, t.code.toString())
                                tvLoginPassword.setText(t.code.toString())
                            }
                            MyToast.showToastCustomerStyleText(this@SetPwdActivity, "${t.msg}")

                        }

                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            dismissDialog()
                            MyToast.showToastCustomerStyleText(this@SetPwdActivity, getString(R.string.networkError))
                        }
                    })



        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCurrent == 1) {
                finish()
            } else {
                showPage1()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showPage1() {
        ll_page1.visibility = View.VISIBLE
        mCurrent = 1
        title?.text = getString(R.string.changePwd)
        ll_page2.visibility = View.GONE
    }

    private fun showPage2() {
        ll_page1.visibility = View.GONE
        mCurrent = 2
        title?.text = getString(R.string.inputNewPwd)
        ll_page2.visibility = View.VISIBLE
    }
}
