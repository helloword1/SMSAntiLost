package com.goockr.smsantilost.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.login.LoginActivity



/**
 * 程序入口界面
 *
 * 修改时间:
 * 修改内容:
 */
class StartActivity(override val contentView:Int=R.layout.activity_start) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this.isTaskRoot) {
            //如果你就放在launcher Activity中话，这里可以直接return了

            val mainIntent = intent

            val action = mainIntent.action

            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                //finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
                finish()
                return
            }
        }
    }

    public override fun initView() {
        ll!!.visibility = View.GONE
        status_bar!!.visibility = View.GONE
        Thread(Runnable {
            SystemClock.sleep(2500)
            runOnUiThread { jumpNextPage() }
        }).start()
    }

    private fun jumpNextPage() {
        var isLogin = preferences?.getStringValue(Constant.HAD_LOGIN)
        if (TextUtils.equals(isLogin,"true")) {
            showActivity(HomeActivity::class.java)
//            showActivity(TestActivity::class.java)
            finish()
            return
        } else {
            showActivity(LoginActivity::class.java)
            finish()
        }
    }
}
