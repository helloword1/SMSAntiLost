package com.goockr.smsantilost

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.amap.api.location.AMapLocationClient
import com.goockr.smsantilost.entries.DaoMaster
import com.goockr.smsantilost.entries.DaoSession
import com.goockr.smsantilost.utils.CrashUtil
import com.goockr.smsantilost.utils.LocaleUtil
import com.goockr.smsantilost.views.activities.HomeActivity
import com.goockr.smsantilost.views.blueteeth.ClientThread
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2017/10/15.
 */

class GoockrApplication : Application() {
    private val activityList = LinkedList<Activity>()
    private val encrypted = false
    var mDaoSession: DaoSession? = null
    var instance: ClientThread? = null
    /**
     * 声明AMapLocationClient类对象
     */
    var mLocationClient: AMapLocationClient? = null
    /**
     * 全局debug
     */
    override fun onCreate() {
        super.onCreate()
        //改变语言
        LocaleUtil.changeAppLanguage(this)
        //异常捕捉
        val crashUtil = CrashUtil.instance
        crashUtil?.init(this)
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build()

        OkHttpUtils.initClient(okHttpClient)
        //数据库
        initDao()
        //全局蓝牙读写线程runable
        instance = ClientThread()
        //定位
        mLocationClient = AMapLocationClient(this)
    }

    private fun initDao() {
        val name = "antilost-db" // 数据库名称
        val helper = DaoMaster.DevOpenHelper(this, name) // helper
        val db = helper.writableDb
        mDaoSession = DaoMaster(db).newSession()
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

    fun exitToHome() {
        for (activity in activityList) {
            if (activity is HomeActivity) {
                activity.sendMac()
            } else {
                activity.finish()
            }
        }
    }
    fun getContext(): Context {
        return this
    }

    companion object {
        var isDebug = true//全局log 关闭或打开
    }

    //用于当系统设置语言变化时进行语言设置
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtil.setLanguage(this, newConfig)
    }
}
