package com.goockr.smsantilost.views.activities.more

import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_add_friends.*
import kotlinx.android.synthetic.main.logo_layout.*

class AddFriendsActivity(override val contentView: Int = R.layout.activity_add_friends) : BaseActivity() {
    override fun initView() {
        title?.text = getString(R.string.addFriends)
        titleRight1?.visibility= View.VISIBLE
        titleRight1?.text=getString(R.string.send)
        ivLoginIcon.setImageResource(R.mipmap.icon_add_friends_2)
        initDate()
    }

    private fun initDate() {
        shareApp.setOnClickListener {
            MyToast.showToastCustomerStyleText(this,getString(R.string.deviceDeveloping))
        }
    }

}
