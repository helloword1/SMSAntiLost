package com.goockr.smsantilost.views.activities

import android.annotation.SuppressLint
import com.goockr.smsantilost.R

/**
 * Created by LJN on 2017/11/14.
 */

class TestActivity(override val contentView: Int = R.layout.activity_text_layout) : BaseActivity() {
    private val hasCapture: Boolean = false
    @SuppressLint("MissingSuperCall")
    override fun initView() {
    }
}

