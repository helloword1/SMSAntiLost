package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AntiAddressBean
import com.goockr.smsantilost.entries.AntiAddressBeanDao
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_ADDRESS
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_ID
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_LATITUDE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_LONGITUDE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_NAME
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_RADUIS
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.antilost.DeviceMapActivity
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
        titleOk?.visibility = View.VISIBLE
        etDisturbName.setText(searchName)
        val antiAddressBeanDao = goockrApplication?.mDaoSession?.antiAddressBeanDao
        //完成
        titleOk?.setOnClickListener {
            if (isVail()) {
                var addressBean: AntiAddressBean? = null
                if (TextUtils.equals(currentId, "0")) {
                    addressBean = AntiAddressBean(null, longitude, latitude, currentRadius, etDisturbName.text.toString(), searchAddress)
                    antiAddressBeanDao?.insert(addressBean)
                } else {
                    addressBean = antiAddressBeanDao?.queryBuilder()?.where(AntiAddressBeanDao.Properties.Id.eq(currentId))?.unique()
                    addressBean?.name = etDisturbName.text.toString()
                    addressBean?.radius = currentRadius
                    addressBean?.longitude = longitude
                    addressBean?.latitude = latitude
                    addressBean?.remark = searchAddress
                    antiAddressBeanDao?.update(addressBean)
                }
                val bundle = Bundle()
                bundle.putString(Constant.LONGITUDE, addressBean?.longitude)
                bundle.putString(Constant.LATITUDE, addressBean?.latitude)
                bundle.putString(Constant.ADDRESS, addressBean?.remark)
                bundle.putInt(Constant.ADDRESS_TYPE, 5)
                bundle.putString(Constant.CURRENT_AREA_ID, addressBean?.id!!.toString())
                bundle.putString(Constant.CURRENT_AREA_RADUIS, addressBean.radius)
                bundle.putString(Constant.CURRENT_AREA_NAME, addressBean.name)
                showActivity(DeviceMapActivity::class.java, bundle)
                val activityLists = goockrApplication?.getActivityLists()
                activityLists!!
                        .filterIsInstance<AddAntiAreaMapActivity>()
                        .forEach { it.finish() }
                finish()
            }
        }
        etDisturbName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isEmpty()) {
                    titleOk?.setTextColor(ContextCompat.getColor(this@SetAntiDisturbNameActivity, R.color.msmTextColorGray))
                } else {
                    titleOk?.setTextColor(ContextCompat.getColor(this@SetAntiDisturbNameActivity, R.color.blue))
                }
            }

        })
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
