package com.goockr.smsantilost.views.activities.antilost

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.DeviceBeanDao
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_set_name.*

class SetNameActivity(override val contentView: Int = R.layout.activity_set_name) : BaseActivity() {
    private var name = ""
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

        val extras = intent.extras
        name = extras.getString("DEVICE_NAME")
        et_InputName.setText(name)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 保存SIM卡号码的逻辑
        titleOk?.setOnClickListener {
            if (et_InputName.text.isNotEmpty()) {
                val deviceBeanDao = goockrApplication?.mDaoSession?.deviceBeanDao
                val unique = deviceBeanDao?.queryBuilder()?.where(DeviceBeanDao.Properties.Name.eq(name))?.unique()
                if (NotNull.isNotNull(unique)){
                    unique?.name=et_InputName.text.toString()
                    deviceBeanDao?.update(unique)
                    MyToast.showToastCustomerStyleText(this,getString(R.string.changeSucceed))
                    finish()
                }
            }
        }
        // editText监听
        et_InputName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s)) {
                    titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
                    titleOk?.isClickable = true
                } else {
                    titleOk?.setTextColor(resources.getColor(R.color.appGray))
                    titleOk?.isClickable = false
                }
            }

        })

    }
}
