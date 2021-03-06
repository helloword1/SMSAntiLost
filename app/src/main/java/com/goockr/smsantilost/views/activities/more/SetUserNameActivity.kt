package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.TOKEN
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.views.activities.BaseActivity
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_set_user_name.*
import okhttp3.Call
import org.json.JSONObject
import java.lang.Exception

class SetUserNameActivity(override val contentView: Int = R.layout.activity_set_user_name) : BaseActivity() {

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
        titleOk?.text = getString(R.string.save)
        titleOk?.visibility = View.VISIBLE
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        title?.text = getString(R.string.editUserName)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        val name = preferences?.getStringValue(Constant.USER_NAME)
        if (NotNull.isNotNull(name)) {
            et_InputUserName.setText(name)
        }
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        et_InputUserName.addTextChangedListener(object : TextWatcher {
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

        titleOk?.setOnClickListener {
            val name = et_InputUserName.text.toString()
            if (NotNull.isNotNull(name) || name.isNotEmpty()) {
                saveName(name)

            } else {
                MyToast.showToastCustomerStyleText(this, getString(R.string.pleaseInputUserName))
            }
        }
    }

    private fun saveName(name: String) {
        showProgressDialog()
        val token = preferences?.getStringValue(TOKEN)
        val url = Constant.BASE_URL + NetApi.USER_SETTING
        OkHttpUtils.post().url(url).addParams("name",name).addParams("token",token)
                .build()
                .execute(object : MyStringCallback(this) {
                    override fun onResponse(response: String?, id: Int) {
                        val jsonOb = JSONObject(response)
                        val result = jsonOb.getString("result")
                        val msg = jsonOb.getString("msg")
                        if (TextUtils.equals(result, "0")) {
                            preferences?.putValue(Constant.USER_NAME, name)
                            MyToast.showToastCustomerStyleText(this@SetUserNameActivity, getString(R.string.saveSucceed))
                            finish()
                        }
                        LogUtils.mi(response!!)
                        dismissDialog()

                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        LogUtils.mi(e.toString())
                        dismissDialog()
                    }

                });

    }

    private fun parJson(name: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        val token = preferences?.getStringValue(TOKEN)
        jsonObject.put("token", token)
        return jsonObject.toString()
    }
}
