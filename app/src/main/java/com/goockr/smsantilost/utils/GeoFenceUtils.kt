package com.goockr.smsantilost.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.amap.api.fence.GeoFence
import com.amap.api.fence.GeoFenceClient
import com.amap.api.fence.GeoFenceListener
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.amap.api.location.DPoint
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.LatLng
import com.goockr.smsantilost.GoockrApplication
import java.util.*

/**
 * Created by LJN on 2017/12/29.
 */
class GeoFenceUtils private constructor(private val context: GoockrApplication) : GeoFenceListener,
          AMapLocationListener {
    // 多边形围栏的边界点
    private var polygonPoints: MutableList<LatLng>? = ArrayList()
    // 当前的坐标点集合，主要用于进行地图的可视区域的缩放
    private var mListener: LocationSource.OnLocationChangedListener? = null
    // 地理围栏客户端
    private var fenceClient: GeoFenceClient? = null
    // 触发地理围栏的行为，默认为进入提醒
    private var activatesAction = GeoFenceClient.GEOFENCE_IN
    // 地理围栏的广播action
    private val GEOFENCE_BROADCAST_ACTION = "com.example.geofence.polygon"
    // 记录已经添加成功的围栏
    private val fenceMap = HashMap<String, GeoFence>()
    internal var fenceList: List<GeoFence>? = ArrayList()
    private var handler: Handler? = null
    override fun onGeoFenceCreateFinished(geoFenceList: MutableList<GeoFence>?, errorCode: Int, customId: String?) {
        val msg = Message.obtain()
        if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
            fenceList = geoFenceList
            msg.obj = customId
            msg.what = 0
        } else {
            msg.arg1 = errorCode
            msg.what = 1
        }
        handler?.sendMessage(msg)
    }

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                mListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
            } else {
                val errText = ("定位失败," + amapLocation.errorCode + ": "
                        + amapLocation.errorInfo)
                Log.e("AmapErr", errText)
            }
        }
    }
    fun getIsINGeoFence(customId:String,latLng:LatLng): Boolean {
        if (null == polygonPoints) {
            polygonPoints = ArrayList()
        }
        polygonPoints?.add(latLng)
        val fliter = IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION)
        fliter.addAction(GEOFENCE_BROADCAST_ACTION)
        context.registerReceiver(mGeoFenceReceiver, fliter)
        /**
         * 创建pendingIntent
         */
        fenceClient?.createPendingIntent(GEOFENCE_BROADCAST_ACTION)
        fenceClient?.setGeoFenceListener(this)
        /**
         * 设置地理围栏的触发行为,默认为进入
         */
        fenceClient?.setActivateAction(activatesAction)

        if (null == polygonPoints || polygonPoints!!.size < 3) {
            Toast.makeText(context, "参数不全", Toast.LENGTH_SHORT)
                    .show()
            return false
        }
        val pointList = polygonPoints!!.map { DPoint(it.latitude, it.longitude) }
        //添加多边形围栏
        fenceClient?.addGeoFence(pointList, customId)
        return true
    }
    init {
        fenceClient = GeoFenceClient(context)
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                when (msg?.what) {
                    0 -> {
                        val sb = StringBuffer()
                        sb.append("添加围栏成功")
                        val customId = msg.obj as String
                        if (!TextUtils.isEmpty(customId)) {
                            sb.append("customId: ").append(customId)
                        }
                        Toast.makeText(context, sb.toString(),
                                Toast.LENGTH_SHORT).show()
                        drawFence2Map()
                    }
                    1 -> {
                        val errorCode = msg.arg1
                        Toast.makeText(context,
                                "添加围栏失败 " + errorCode, Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        val statusStr = msg.obj as String
                    }
                    else -> {
                    }
                }
            }
        }
    }


    /**
     * 接收触发围栏后的广播,当添加围栏成功之后，会立即对所有围栏状态进行一次侦测，如果当前状态与用户设置的触发行为相符将会立即触发一次围栏广播；
     * 只有当触发围栏之后才会收到广播,对于同一触发行为只会发送一次广播不会重复发送，除非位置和围栏的关系再次发生了改变。
     */
    private val mGeoFenceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 接收广播
            if (intent.action == GEOFENCE_BROADCAST_ACTION) {
                val bundle = intent.extras
                val customId = bundle!!
                        .getString(GeoFence.BUNDLE_KEY_CUSTOMID)
                val fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID)
                //status标识的是当前的围栏状态，不是围栏行为
                val status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS)
                val sb = StringBuffer()
                when (status) {
                    GeoFence.STATUS_LOCFAIL -> sb.append("定位失败")
                    GeoFence.STATUS_IN -> sb.append("进入围栏 ")
                    GeoFence.STATUS_OUT -> sb.append("离开围栏 ")
                    GeoFence.STATUS_STAYED -> sb.append("停留在围栏内 ")
                    else -> {
                    }
                }
                if (status != GeoFence.STATUS_LOCFAIL) {
                    if (!TextUtils.isEmpty(customId)) {
                        sb.append(" customId: " + customId!!)
                    }
                    sb.append(" fenceId: " + fenceId!!)
                }
                val str = sb.toString()
                val msg = Message.obtain()
                msg.obj = str
                msg.what = 2
                handler?.sendMessage(msg)
            }
        }
    }
    internal var lock = Any()
    internal fun drawFence2Map() {
        object : Thread() {
            override fun run() {
                try {
                    synchronized(lock) {
                        if (null == fenceList || fenceList!!.isEmpty()) {
                            return
                        }
                        for (fence in fenceList!!) {
                            if (fenceMap.containsKey(fence.getFenceId())) {
                                continue
                            }
                            fenceMap.put(fence.fenceId, fence)
                        }
                    }
                } catch (e: Throwable) {

                }

            }
        }.start()
    }


     fun onDestroy() {
        try {
            context.unregisterReceiver(mGeoFenceReceiver)
        } catch (e: Throwable) {
        }
        if (null != fenceClient) {
            fenceClient?.removeGeoFence()
        }
    }
}