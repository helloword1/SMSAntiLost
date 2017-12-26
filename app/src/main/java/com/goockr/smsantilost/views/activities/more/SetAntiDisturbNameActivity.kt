package com.goockr.smsantilost.views.activities.more

import android.text.TextUtils
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_ADDRESS
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_ID
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_LATITUDE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_LONGITUDE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_NAME
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_RADUIS
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_set_anti_disturb_name.*

class SetAntiDisturbNameActivity(override val contentView: Int = R.layout.activity_set_anti_disturb_name) : BaseActivity() {
    private var currentId = ""
    private var latitude = ""
    private var longitude = ""
    private var searchName = ""
    private var searchAddress = ""
    private var currentRadius = ""
    override fun initView() {
        initIntent()
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        titleOk = titleLayout.findViewById(R.id.titleOk)
        title?.text = getString(R.string.settingAntiAreaName)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        titleOk?.text = getString(R.string.complete)
        etDisturbName.setText(searchName)
        //完成
        titleOk?.setOnClickListener {

        }
    }

    private fun initIntent() {
        val extras = intent.extras
        searchName = extras.getString(CURRENT_AREA_NAME)
        currentId = extras.getString(CURRENT_AREA_ID)
        latitude = extras.getString(CURRENT_AREA_LATITUDE)
        longitude = extras.getString(CURRENT_AREA_LONGITUDE)
        searchAddress = extras.getString(CURRENT_AREA_ADDRESS)
        currentRadius = extras.getString(CURRENT_AREA_RADUIS)
    }

    private fun isVail(): Boolean {

        if (!NotNull.isNotNull(etDisturbName.text.toString()) || TextUtils.equals(etDisturbName.text.toString(), "")) {
            MyToast.showToastCustomerStyleText(this, getString(R.string.pleaseInputName))
            return false
        }
        return true
    }

}
