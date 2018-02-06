package com.goockr.smsantilost

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.text.TextUtils
import com.amap.api.fence.GeoFence
import com.amap.api.fence.GeoFenceClient
import com.amap.api.fence.GeoFenceClient.GEOFENCE_IN
import com.amap.api.fence.GeoFenceListener
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.DPoint
import com.goockr.smsantilost.entries.AntiDisturbBean
import com.goockr.smsantilost.entries.DaoMaster
import com.goockr.smsantilost.entries.DaoSession
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.GEOFENCE_BROADCAST_ACTION
import com.goockr.smsantilost.utils.Constant.GEOFENCE_OUT_CODE
import com.goockr.smsantilost.utils.CrashUtil
import com.goockr.smsantilost.utils.LocaleUtil
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.views.activities.HomeActivity
import com.goockr.smsantilost.views.activities.login.LoginActivity
import com.goockr.smsantilost.views.blueteeth.ClientThread
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.SharedPreferencesUtils
import okhttp3.Call
import okhttp3.OkHttpClient
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


/**
 * Created by Administrator on 2017/10/15.
 */

class GoockrApplication : Application() {
    private val activityList = LinkedList<Activity>()
    private val locationMap = HashMap<String,Boolean>()
    var mDaoSession: DaoSession? = null
    var instance: ClientThread? = null
    var preferences: SharedPreferencesUtils? = null
    /**
     * 声明AMapLocationClient类对象
     */
    var mLocationClient: AMapLocationClient? = null

    //地址围栏客户端
    var mGeoFenceClient: GeoFenceClient? = null

    /**
     * 全局debug
     */
    override fun onCreate() {
        super.onCreate()
        preferences = SharedPreferencesUtils.getInstance(this)
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
        //初始化地址围栏
        startGeoFenceClient()

    }

    //初始化地址围栏
    private fun startGeoFenceClient() {
        mGeoFenceClient = GeoFenceClient(this)
        mGeoFenceClient?.setActivateAction(GEOFENCE_IN)
        initMData()

    }

    private val mGeoFenceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == GEOFENCE_BROADCAST_ACTION) {
                //解析广播内容
                val extras = intent.extras
                val status = extras.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS)
                val fenceId = extras.getString(GeoFence.BUNDLE_KEY_CUSTOMID)
                if (status == GEOFENCE_IN) {
                    preferences?.putValue(GEOFENCE_OUT_CODE, "false")
                } else {
                    preferences?.putValue(GEOFENCE_OUT_CODE, "true")
                }
                /*for (entry in locationMap) {
                    if (TextUtils.equals(entry.key,fenceId)){
                        entry.setValue()
                    }
                }*/
            }
        }
    }
    private fun initDao() {
        val name = "antilost-db" // 数据库名称
        val helper = DaoMaster.DevOpenHelper(this, name)
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

    fun exitToHomeNo() {
        for (activity in activityList) {
            if (activity is HomeActivity) {

            } else {
                activity.finish()
            }
        }
    }

    fun getHomeActivity(): HomeActivity? {
        activityList
                .filterIsInstance<HomeActivity>()
                .forEach { return it }
        return null
    }

    fun exitToLogin() {
        for (activity in activityList) {
            if (activity is LoginActivity) {
                if (instance!!.isConnect) {
                    instance!!.disConnect()
                }
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

    fun getActivityLists(): LinkedList<Activity> {
        return activityList
    }

    //用于当系统设置语言变化时进行语言设置
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtil.setLanguage(this, newConfig)
    }

    /**
     * 初始化区域数据
     */
    private fun initMData() {
        OkHttpUtils
                .post()
                .url(Constant.BASE_URL + NetApi.FIND_PREVENT_DISTURB)
                .addParams("token", preferences?.getStringValue(Constant.TOKEN))
                .addParams("pageNo", "1")
                .addParams("pageSize", "10")
                .build()
                .execute(object : MyStringCallback(this) {
                    override fun onResponse(response: String?, id: Int) {
                        val a = Gson().fromJson(response, AntiDisturbBean::class.java)
                        try {
                            if (TextUtils.equals(a.result, "0")) {
                                val data = a.data
                                setDates(data)
                            }
                            LogUtils.mi(response!!)
                        } catch (e:Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                    }
                })
    }

    private fun setDates(mData: List<AntiDisturbBean.DataBean>) {
        for (data in mData) {
            //创建一个中心点坐标
            val centerPoint = DPoint()
            //设置中心点纬度
            centerPoint.latitude = data.latitude.toDouble()
            //设置中心点经度
            centerPoint.longitude = data.longitude.toDouble()
            mGeoFenceClient?.addGeoFence(centerPoint, data.radius.toFloat(), data.address)
        }
        //创建回调监听
        val fenceListener = GeoFenceListener { p0, errorCode, p2 ->
            if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {//判断围栏是否创建成功
                LogUtils.mi("添加围栏成功!!" + p2)
                locationMap.put(p2,true)
                //geoFenceList就是已经添加的围栏列表，可据此查看创建的围栏
            } else {
                //geoFenceList就是已经添加的围栏列表
                LogUtils.mi(p2)
            }
        }
        //设置回调监听
        mGeoFenceClient?.setGeoFenceListener(fenceListener)
        //创建并设置PendingIntent
        mGeoFenceClient?.createPendingIntent(GEOFENCE_BROADCAST_ACTION)
        val filter = IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION)
        filter.addAction(GEOFENCE_BROADCAST_ACTION)
        registerReceiver(mGeoFenceReceiver, filter)
    }
}
