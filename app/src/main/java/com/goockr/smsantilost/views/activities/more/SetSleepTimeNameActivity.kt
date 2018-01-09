package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_set_sleep_time_name.*

class SetSleepTimeNameActivity(override val contentView: Int = R.layout.activity_set_sleep_time_name) : BaseActivity() {

    private val RESULT_NAME = 0

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
        title?.text = getString(R.string.sleepTimeName)
        titleOk?.text = getString(R.string.Done)
        titleOk?.visibility = View.VISIBLE
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 监听editText
        et_InputSleepTimeName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    titleOk?.setTextColor(resources.getColor(R.color.appGray))
                }else {
                    titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
                }
            }
        })
        // 完成按钮
        titleOk?.setOnClickListener {
            if (TextUtils.isEmpty(et_InputSleepTimeName.text)) {
                ToastUtils.showShort(this,getString(R.string.inputSleepTimeName))
            }else {
                val intent = Intent()
                intent.putExtra("sleepTimeName", et_InputSleepTimeName.text.toString())
                setResult(RESULT_NAME,intent)
                finish()
            }
        }
    }
}
