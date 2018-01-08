package com.goockr.smsantilost.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant.USER_NAME
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.more.*
import cxx.utils.NotNull
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
        llUserIcon.setOnClickListener(this)
        ll_MyFriends.setOnClickListener(this)
        ll_AntiDisturb.setOnClickListener(this)
        ll_SleepTime.setOnClickListener(this)
        ll_UserSetting.setOnClickListener(this)
        ll_AboutUs.setOnClickListener(this)
        llMultilingualLanguage.setOnClickListener(this)


    }

    override fun onResume() {
        super.onResume()
        val baseActivity = activity as BaseActivity
        val name = baseActivity.preferences?.getStringValue(USER_NAME)
        if (NotNull.isNotNull(name)) {
            tv_UserName.text = name
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llUserIcon -> {
                //我的头像
                showActivity(UserSettingActivity::class.java)
            }
            R.id.ll_MyFriends -> {
                //我的好友
                showActivity(MyFriendsActivity::class.java)
            }
            R.id.ll_AntiDisturb -> {
                //防打扰区域
                showActivity(AntiDisturbActivity::class.java)
            }
            R.id.ll_SleepTime -> {
                //休眠时间
                showActivity(SleepTimeActivity::class.java)
            }
            R.id.llMultilingualLanguage -> {
                //多国语言
                showActivity(MultilingualLanguageActivity::class.java)
            }
            R.id.ll_UserSetting -> {
                //设置
                showActivity(MoreSettingActivity::class.java)
            }
            R.id.ll_AboutUs -> {
                //关于我们
                showActivity(AboutUsActivity::class.java)
            }
        }
    }
}
