package com.goockr.smsantilost.views.activities

import android.annotation.SuppressLint
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.ConnectDialogView
import kotlinx.android.synthetic.main.activity_text_layout.*

/**
 * Created by LJN on 2017/11/14.
 */

class TestActivity(override val contentView: Int = R.layout.activity_text_layout): BaseActivity() {
    override fun initView() {
        TestBtn.setOnClickListener {
            ConnectDialogView(this,R.mipmap.icon_connection_success_1).show()
        }
    }
}

