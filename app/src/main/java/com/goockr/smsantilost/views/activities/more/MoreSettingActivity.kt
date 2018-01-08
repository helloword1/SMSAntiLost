package com.goockr.smsantilost.views.activities.more

import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity

/**
 * 防打扰区域
 */
class MoreSettingActivity(override val contentView: Int = R.layout.activity_more_setting) : BaseActivity() {

    override fun initView() {
        title?.text=getString(R.string.setting)

    }
}
