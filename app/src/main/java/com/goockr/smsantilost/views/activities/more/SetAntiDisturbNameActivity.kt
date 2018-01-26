package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_ADDRESS
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_ID
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_LATITUDE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_LONGITUDE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_NAME
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_RADUIS
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.antilost.DeviceMapActivity
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_set_anti_disturb_name.*
import okhttp3.Call
import org.json.JSONObject
import java.lang.Exception

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
        titleOk?.text = getString(R.string.Done)
        titleOk?.visibility = View.VISIBLE
        etDisturbName.setText(searchName)
        //完成
        titleOk?.setOnClickListener {
            if (isVail()) {
                var message = getString(R.string.changing)
                if (TextUtils.equals(currentId, "0")) {
                    currentId=""
                    message = getString(R.string.adding)
                }

                showProgressDialog(message)
                //添加/修改防打扰区域
                OkHttpUtils
                        .post()
                        .url(Constant.BASE_URL + NetApi.AM_PREVENT_DISTURB)
                        .addParams("token", preferences?.getStringValue(Constant.TOKEN))
                        .addParams("antiname", etDisturbName.text.toString())
                        .addParams("longitude", longitude)
                        .addParams("latitude", latitude )
                        .addParams("address", searchAddress )
                        .addParams("radius", currentRadius )
                        .addParams("id", currentId)
                        .build()
                        .execute(object : MyStringCallback(this) {
                            override fun onResponse(response: String?, id: Int) {
                                LogUtils.mi(response!!)
                                val res = JSONObject(response)
                                if (TextUtils.equals(res.getString("result"), "0")) {
                                    val bundle = Bundle()
                                    bundle.putString(Constant.LONGITUDE, longitude)
                                    bundle.putString(Constant.LATITUDE, latitude)
                                    bundle.putString(Constant.ADDRESS, searchAddress)
                                    bundle.putInt(Constant.ADDRESS_TYPE, 5)
                                    bundle.putString(Constant.CURRENT_AREA_ID, currentId)
                                    bundle.putString(Constant.CURRENT_AREA_RADUIS, currentRadius)
                                    bundle.putString(Constant.CURRENT_AREA_NAME, etDisturbName.text.toString())
                                    showActivity(DeviceMapActivity::class.java, bundle)
                                    val activityLists = goockrApplication?.getActivityLists()
                                    activityLists!!
                                            .filterIsInstance<AddAntiAreaMapActivity>()
                                            .forEach { it.finish() }
                                    finish()
                                }

                            }

                            override fun onError(call: Call?, e: Exception?, id: Int) {
                                dismissDialog()
                                MyToast.showToastCustomerStyleText(this@SetAntiDisturbNameActivity, getString(R.string.networkError))
                            }
                        })

            }
        }
        if (!etDisturbName.text.toString().isEmpty()){
            titleOk?.setTextColor(ContextCompat.getColor(this@SetAntiDisturbNameActivity, R.color.blue))
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
