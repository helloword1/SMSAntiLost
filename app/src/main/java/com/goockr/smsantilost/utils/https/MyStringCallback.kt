package com.goockr.smsantilost.utils.https

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.text.TextUtils
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.login.LoginActivity
import com.zhy.http.okhttp.callback.Callback
import cxx.utils.NotNull
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Created by LJN on 2017/11/29.
 */
abstract class MyStringCallback(var context: Context) : Callback<String>() {
    @Throws(IOException::class)
    override fun parseNetworkResponse(response: Response, id: Int): String {
        val string = response.body().string()
        val jsonObject = JSONObject(string)
        val result = jsonObject.getString("result")
        val application = context.applicationContext as GoockrApplication
        if (TextUtils.equals(result, "4")) {
            if (context is BaseActivity) {
                val baseActivity = context as BaseActivity
                baseActivity.runOnUiThread { MyToast.showToastCustomerStyleText(baseActivity,baseActivity.getString(R.string.loginOutDate)) }
                baseActivity.preferences?.clearPreferences()
                baseActivity.showActivity(LoginActivity::class.java)
                application.exitToLogin()
                SystemClock.sleep(500)
                return string
            }else{
                val mIntent=Intent(context,LoginActivity::class.java)
                mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                application.exit()
                context.startActivity(mIntent)
            }
        }
        return if (NotNull.isNotNull(string)){
            string
        }else{
            ""
        }

    }
}