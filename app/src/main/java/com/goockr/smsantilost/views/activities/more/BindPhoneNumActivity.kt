package com.goockr.smsantilost.views.activities.more

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.Window
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_bind_phone_num.*
import kotlinx.android.synthetic.main.page1_bind_phone_num.*
import kotlinx.android.synthetic.main.page2_bind_phone_num.*

class BindPhoneNumActivity(override val contentView: Int = R.layout.activity_bind_phone_num) : BaseActivity() {

    // 当前页数
    private var mCurrent = 1
    private var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initDialog()
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
        title?.text = getString(R.string.changePhone)
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
        iv_CleanVerificationCode2.setOnClickListener {
            et_VerificationCode2.text = null
        }
        // 下一步按钮
        titleOk?.setOnClickListener {
            if (mCurrent == 1) {
                // 先检查有没有输入验证码
                if (TextUtils.isEmpty(et_VerificationCode1.text)) {
                    ToastUtils.showShort(this, R.string.inputCode)
                } else {
                    showPage2()
                }
            } else if (mCurrent == 2) {
                if (TextUtils.isEmpty(et_VerificationCode2.text)) {
                    ToastUtils.showShort(this, R.string.inputCode)
                } else {
                    dialog?.show()
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
        title?.text = getString(R.string.changePhone)
        mCurrent = 1
        ll_page2.visibility = View.GONE
    }

    private fun showPage2() {
        ll_page1.visibility = View.GONE
        title?.text = getString(R.string.inputNewPhone)
        mCurrent = 2
        ll_page2.visibility = View.VISIBLE
    }

    private fun initDialog() {
        val builder = AlertDialog.Builder(this)
        val customView = layoutInflater.inflate(R.layout.dialog_bind_success, null)
        builder.setView(customView)
        builder.setIcon(R.mipmap.ic_launcher)
        dialog = builder.create()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val tvOk = customView.findViewById<View>(R.id.tv_Ok)
        tvOk.setOnClickListener {
            dialog?.hide()
        }
    }
}
