package com.goockr.smsantilost.views.activities.msm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContentBean
import com.goockr.smsantilost.entries.PhoneBean
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.CHOICE_CONTACT_RESULT_ID
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.SendMsmAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.SystemInput
import kotlinx.android.synthetic.main.activity_new_msm.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by LJN on 2017/11/14.
 */

class NewMSMActivity(override val contentView: Int = R.layout.activity_new_msm) : BaseActivity() {
    private val lists = ArrayList<PhoneBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun initView() {
        title?.text = getString(R.string.newSms)
        recycleView.layoutManager = LinearLayoutManager(this)
        val mDatas = ArrayList<ContentBean>()
        val data = SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(System.currentTimeMillis())
        val sendMsmAdapter = SendMsmAdapter(this, mDatas)
        recycleView.adapter = sendMsmAdapter
        ivSend.setOnClickListener {
            MyToast.showToastCustomerStyleText(this@NewMSMActivity,getString(R.string.deviceDeveloping))
            return@setOnClickListener
            val content = smsSend.text.toString()
            if (TextUtils.equals(content, "")) return@setOnClickListener
//            mDatas.add(ContentBean(data, content, false))
            sendMsmAdapter.notifyDataSetChanged()
            smsSend.setText("")
            SystemInput.closeInput(this)
        }
        ibReceiveAdd.setOnClickListener {

            val extras = Bundle()
            extras.putSerializable("PHONE_DATA", lists)
            showActivityForResult(ChoiceContactActivity::class.java, extras, Constant.CHOICE_CONTACT_RESULT_ID)
        }
        etReceiveName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.toString()?.length == 0) {
                    etReceiveName.visibility= VISIBLE
                    tvName.visibility= GONE
                    ibReceiveAdd.visibility= VISIBLE
                }else{
                    ibReceiveAdd.visibility= GONE
                }
            }

        })
        smsSend.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.toString()?.length == 0) {
                    ivSend.setImageResource(R.mipmap.btn_sent_out_disabled)
                }else{
                    ivSend.setImageResource(R.mipmap.btn_sent_out_normal)
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CHOICE_CONTACT_RESULT_ID) {
            val extras = data?.extras
            val serializable = extras?.getSerializable("CHOICE_RETURN") as ArrayList<*>
            lists.clear()
            lists.addAll(serializable as ArrayList<PhoneBean>)
            var str = ""
            serializable
                    .map { it }
                    .forEach { str += it.getMPhone() + "、" }
            if (TextUtils.equals(str,"")){
                tvName.visibility= GONE
                etReceiveName.visibility= VISIBLE
            }else{
                tvName.text=str
                tvName.visibility= VISIBLE
                etReceiveName.visibility=GONE
            }

        }
    }
}

