package com.goockr.smsantilost.views.activities.antilost

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_set_name.*

class SetNameActivity(override val contentView: Int = R.layout.activity_set_name) : BaseActivity() {

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

        titleOk?.text = getString(R.string.complete)
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        titleOk?.isClickable = false
        titleOk?.visibility = View.VISIBLE

        title?.text = getString(R.string.ModifyName)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 保存SIM卡号码的逻辑
        titleOk?.setOnClickListener {
            ToastUtils.showShort(this,getString(R.string.CanSave))
        }
        // editText监听
        et_InputName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s)) {
                    iv_CleanText.visibility = View.VISIBLE
                    titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
                    titleOk?.isClickable = true
                }else {
                    iv_CleanText.visibility = View.GONE
                    titleOk?.setTextColor(resources.getColor(R.color.appGray))
                    titleOk?.isClickable = false
                }
            }

        })

        // 清除文字按钮
        iv_CleanText.setOnClickListener {
            et_InputName.text = null
        }
    }
}
