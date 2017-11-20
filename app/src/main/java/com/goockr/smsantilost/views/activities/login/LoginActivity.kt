package com.goockr.smsantilost.views.activities.login

import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity(override val contentView: Int = R.layout.activity_login) : BaseActivity(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    override fun initView() {
        ll?.visibility = View.GONE
        tvRegister.setOnClickListener(this)
        tvCodeLogin.setOnClickListener(this)

    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tvRegister->showActivity(RegisterActivity::class.java)
            R.id.tvCodeLogin->showActivity(CodeLoginActivity::class.java)
        }
    }
}
