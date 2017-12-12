package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_set_pwd.*
import kotlinx.android.synthetic.main.page1_set_pwd.*
import kotlinx.android.synthetic.main.page2_set_pwd.*

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
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 清空
        iv_CleanVerificationCode1.setOnClickListener {
            et_VerificationCode1.text = null
        }
        iv_CleanPwd1.setOnClickListener {
            et_InputNewPWD.text = null
        }
        iv_CleanPwd2.setOnClickListener {
            et_InputNewPWDAgain.text = null
        }
        // 下一步
        titleOk?.setOnClickListener {
            if (mCurrent == 1) {
                // 检查是否输入验证码
                if (TextUtils.isEmpty(et_VerificationCode1.text)) {
                    ToastUtils.showShort(this,getString(R.string.inputCode))
                }else {
                    showPage2()
                }
            }else if (mCurrent == 2) {
                // 检查两次新密码输入是否一致
                if (et_InputNewPWD.text.equals(et_InputNewPWDAgain.text)) {
                    ToastUtils.showShort(this,getString(R.string.changeSucceed))
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
