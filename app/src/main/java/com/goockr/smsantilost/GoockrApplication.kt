package com.goockr.smsantilost

import android.app.Activity
import android.app.Application
import com.goockr.smsantilost.utils.CrashUtil
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2017/10/15.
 */

class GoockrApplication : Application() {
    private val activityList = LinkedList<Activity>()

    /**
     * 全局debug
     */
    override fun onCreate() {
        super.onCreate()
        val crashUtil = CrashUtil.instance
        crashUtil!!.init(this)
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build()

        OkHttpUtils.initClient(okHttpClient)
    }

    //添加Activity到容器中
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }
    //遍历所有Activity并finish

    fun exit() {
        for (activity in activityList) {
            activity.finish()
        }
    }

    companion object {
        var isDebug = true//全局log 关闭或打开
    }
}
