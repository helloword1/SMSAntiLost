package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.view.View

import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity

class RegisterNextActivity(override val contentView: Int = R.layout.activity_register_next) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        ll?.visibility = View.GONE
    }
}
