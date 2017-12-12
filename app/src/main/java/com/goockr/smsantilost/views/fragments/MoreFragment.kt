package com.goockr.smsantilost.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.more.*
import kotlinx.android.synthetic.main.fragment_more.*


/**
 * Created by ning.wen on 2016/11/1.
 */

class MoreFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return setContentView(R.layout.fragment_more)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickEvent()
    }

    private fun initView() {

    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        iv_ProfilePic.setOnClickListener(this)
        ll_MyFriends.setOnClickListener(this)
        ll_AntiDisturb.setOnClickListener(this)
        ll_SleepTime.setOnClickListener(this)
        ll_UserSetting.setOnClickListener(this)
        ll_AboutUs.setOnClickListener(this)
        llMultilingualLanguage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_ProfilePic -> {
                showActivity(UserSettingActivity::class.java)
            }
            R.id.ll_MyFriends -> {
                showActivity(MyFriendsActivity::class.java)
            }
            R.id.ll_AntiDisturb -> {
                showActivity(AntiDisturbActivity::class.java)
            }
            R.id.ll_SleepTime -> {
                showActivity(SleepTimeActivity::class.java)
            }
            //多国语言
            R.id.llMultilingualLanguage -> {
                showActivity(MultilingualLanguageActivity::class.java)
            }
            R.id.ll_UserSetting -> {

            }
            R.id.ll_AboutUs -> {

            }
        }
    }
}
