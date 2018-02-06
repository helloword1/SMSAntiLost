package com.goockr.smsantilost.views.activities.more

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.Window
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.entries.ValidateCodeBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.LOGIN_MSM_CODE
import com.goockr.smsantilost.utils.Constant.MULTiPLY_MOBIL_PHONE_NUM
import com.goockr.smsantilost.utils.CountDownButtonHelper
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.login.MultiplyMobilPhoneNumActivity
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_bind_phone_num.*
import kotlinx.android.synthetic.main.multiply_phone_num_layout.*
import kotlinx.android.synthetic.main.page1_bind_phone_num.*
import kotlinx.android.synthetic.main.page2_bind_phone_num.*
import okhttp3.Call
import org.json.JSONObject
import java.lang.Exception

class BindPhoneNumActivity(override val contentView: Int = R.layout.activity_bind_phone_num) : BaseActivity() {

    // 当前页数
    private var mCurrent = 1
    private var dialog: Dialog? = null
    private var cIndex = "86"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initDialog()
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
        titleOk?.isClickable = false
        title?.text = getString(R.string.changePhone)
        ll?.addView(titleLayout)
        rlMulPhone.setOnClickListener {
            showActivityForResult(MultiplyMobilPhoneNumActivity::class.java, MULTiPLY_MOBIL_PHONE_NUM)
        }
        val phone = preferences?.getStringValue(Constant.LOGIN_PHONE)
        if (NotNull.isNotNull(phone)) {
            val replace = phone?.replace(phone.substring(3, 9), "***")
            tv_CodeTips.text = getString(R.string.SentAuth1) + replace
        }
        et_VerificationCode1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!p0?.isEmpty()!!) {
                    titleOk?.setTextColor(ContextCompat.getColor(this@BindPhoneNumActivity, R.color.blue))
                    titleOk?.isClickable = true
                } else {
                    titleOk?.setTextColor(ContextCompat.getColor(this@BindPhoneNumActivity, R.color.appGray))
                    titleOk?.isClickable = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        et_VerificationCode2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!p0?.isEmpty()!!) {
                    titleOk?.setTextColor(ContextCompat.getColor(this@BindPhoneNumActivity, R.color.blue))
                    titleOk?.isClickable = true
                } else {
                    titleOk?.setTextColor(ContextCompat.getColor(this@BindPhoneNumActivity, R.color.appGray))
                    titleOk?.isClickable = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    /**
     * 点击事件
     */
    @SuppressLint("SetTextI18n")
    private fun initClickEvent() {
        // 下一步按钮
        titleOk?.setOnClickListener {
            if (mCurrent == 1) {
                // 先检查有没有输入验证码
                when {
                    TextUtils.isEmpty(et_VerificationCode1.text) -> ToastUtils.showShort(this, R.string.inputCode)
                    TextUtils.equals(et_VerificationCode1.text, preferences?.getStringValue(LOGIN_MSM_CODE)) -> showPage2()
                    else -> ToastUtils.showShort(this, R.string.msmCodeError)
                }
            } else if (mCurrent == 2) {
                if (TextUtils.isEmpty(et_VerificationCode2.text)) {
                    ToastUtils.showShort(this, R.string.inputCode)
                } else {
                    savePhone(et_InputNewPhoneNum.text.toString(),et_VerificationCode2.text.toString())

                }
            }
        }
        // 返回键
        titleBack?.setOnClickListener {
            if (mCurrent == 1) {
                finish()
            } else if (mCurrent == 2) {
                showPage1()
            }
        }
        reSend.setOnClickListener {
            val phone = preferences?.getStringValue(Constant.LOGIN_PHONE)
            val cIndex = preferences?.getStringValue(Constant.PHONE_TYPE)
            OkHttpUtils
                    .post()
                    .url(Constant.BASE_URL + NetApi.GET_CODE)
                    .addParams("mobile", phone)
                    .addParams("national", cIndex)
                    .build()
                    .execute(object : MyStringCallback(this) {
                        override fun onResponse(response: String?, id: Int) {
                            val t = Gson().fromJson(response, ValidateCodeBean::class.java)
                            dismissDialog()
                            if (t.result == 0) {
                                val timer = CountDownButtonHelper(reSend,
                                        getString(R.string.getMsmCode), getString(R.string.ReWantMsmCode), 60, 1)
                                timer.start()
                                preferences?.putValue(Constant.LOGIN_MSM_CODE, t.code.toString())
//                                        tvLoginPassword.setText(t.code.toString())

                                if (NotNull.isNotNull(phone)) {
                                    val replace = phone?.replace(phone.substring(3, 9), "***")
                                    tv_CodeTips.text = getString(R.string.SentAuth) + replace
                                }
                            }
                            MyToast.showToastCustomerStyleText(this@BindPhoneNumActivity, "${t.msg}")
                        }

                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            dismissDialog()
                            MyToast.showToastCustomerStyleText(this@BindPhoneNumActivity, getString(R.string.networkError))
                        }
                    })
        }
        reSend2.setOnClickListener {
            val phone =et_InputNewPhoneNum.text.toString()
            var cIndex = preferences?.getStringValue(Constant.PHONE_TYPE)
            if (!NotNull.isNotNull(cIndex)) {
                cIndex = "86"
            }
            if (!NotNull.isNotNull(phone)){
                MyToast.showToastCustomerStyleText(this@BindPhoneNumActivity,getString(R.string.inputPhoneNumber))
                return@setOnClickListener
            }
            OkHttpUtils
                    .post()
                    .url(Constant.BASE_URL + NetApi.GET_CODE)
                    .addParams("mobile", phone)
                    .addParams("national", cIndex)
                    .build()
                    .execute(object : MyStringCallback(this) {
                        override fun onResponse(response: String?, id: Int) {
                            val t = Gson().fromJson(response, ValidateCodeBean::class.java)
                            dismissDialog()
                            if (t.result == 0) {
                                val timer = CountDownButtonHelper(reSend2,
                                        getString(R.string.getMsmCode), getString(R.string.ReWantMsmCode), 60, 1)
                                timer.start()
                                preferences?.putValue(Constant.LOGIN_MSM_CODE, t.code.toString())
                            }
                            MyToast.showToastCustomerStyleText(this@BindPhoneNumActivity, "${t.msg}")
                        }

                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            dismissDialog()
                            MyToast.showToastCustomerStyleText(this@BindPhoneNumActivity, getString(R.string.networkError))
                        }
                    })
        }


    }
    private fun savePhone(phone: String,vCode:String) {
        showProgressDialog()
        val token = preferences?.getStringValue(Constant.TOKEN)
        val url = Constant.BASE_URL + NetApi.USER_SETTING
        OkHttpUtils.post().url(url).addParams("mobile", phone).addParams("token", token).addParams("vcode", vCode).addParams("national", cIndex)
                .build()
                .execute(object : MyStringCallback(this) {
                    override fun onResponse(response: String?, id: Int) {
                        val jsonOb = JSONObject(response)
                        val result = jsonOb.getString("result")
                        val msg = jsonOb.getString("msg")
                        if (TextUtils.equals(result, "0")) {
                            preferences?.putValue(Constant.LOGIN_PHONE, phone)
                            preferences?.putValue(Constant.LOGIN_MSM_CODE, "")
                            MyToast.showToastCustomerStyleText(this@BindPhoneNumActivity, msg)
                            finish()
                        }
                        LogUtils.mi(response!!)
                        dismissDialog()

                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        LogUtils.mi(e.toString())
                        dismissDialog()
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
        title?.text = getString(R.string.changePhone)
        titleOk?.text = getString(R.string.nextStep)
        mCurrent = 1
        ll_page2.visibility = View.GONE
    }

    private fun showPage2() {
        ll_page1.visibility = View.GONE
        title?.text = getString(R.string.inputNewPhone)
        titleOk?.text = getString(R.string.Done)
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        titleOk?.isClickable = false
        et_VerificationCode2.setText("")
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

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.MULTiPLY_MOBIL_PHONE_NUM && resultCode == Activity.RESULT_OK) {
            cIndex = data?.getStringExtra(Constant.MOBIL_PHONE_NUM)!!
            tvMulPhone.text = "+$cIndex"
        }
    }
}
