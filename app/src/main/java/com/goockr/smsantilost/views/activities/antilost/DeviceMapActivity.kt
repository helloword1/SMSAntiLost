package com.goockr.smsantilost.views.activities.antilost

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.ADDRESS
import com.goockr.smsantilost.utils.Constant.ADDRESS_TYPE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_ID
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_NAME
import com.goockr.smsantilost.utils.Constant.LATITUDE
import com.goockr.smsantilost.utils.Constant.LONGITUDE
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.more.AddAntiAreaMapActivity
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_device_map.*

/**
 * 钥匙界面
 */
class DeviceMapActivity(override val contentView: Int = R.layout.activity_device_map) : BaseActivity() {
    private var longitude = ""
    private var latitude = ""
    private var address = ""
    private var type = 2
    private var currentRadius = "0"
    private var aMap: AMap? = null
    private var isResume = false
    private var name = ""
    private var addCircle :Circle?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map.onCreate(savedInstanceState)
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    override fun initView() {
        initExtras()
        init()
        initMap()
    }

    private fun initExtras() {
        val extras = intent.extras
        latitude = extras.getString(LONGITUDE)
        longitude = extras.getString(LATITUDE)
        address = extras.getString(ADDRESS)
        type = extras.getInt(ADDRESS_TYPE)
        name = extras.getString(CURRENT_AREA_NAME)
        currentRadius = extras.getString(Constant.CURRENT_AREA_RADUIS)

    }

    private fun init() {
        if (aMap == null) {
            aMap = map.map
        }
        aMap?.myLocationStyle = MyLocationStyle()//设置定位蓝点的Style
        aMap?.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.uiSettings?.isMyLocationButtonEnabled = true
        aMap?.setMyLocationType(AMap.LOCATION_TYPE_LOCATE)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent!!.extras
        latitude = extras.getString(LONGITUDE)
        longitude = extras.getString(LATITUDE)
        address = extras.getString(ADDRESS)
        type = extras.getInt(ADDRESS_TYPE)
        name = extras.getString(CURRENT_AREA_NAME)
        currentRadius = extras.getString(Constant.CURRENT_AREA_RADUIS)
        initMap()
        isResume = false
    }
    private fun initMap() {
        if (type == 5) {
            val id = intent.extras.getString(CURRENT_AREA_ID)
            aMap?.isMyLocationEnabled = false// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
            aMap?.uiSettings?.isMyLocationButtonEnabled = true
            titleRight1?.visibility = View.VISIBLE
            titleRight1?.text = getString(R.string.edit)
            titleRight1?.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Constant.CURRENT_AREA_NAME, name)
                bundle.putString(Constant.CURRENT_AREA_LONGITUDE, longitude)
                bundle.putString(Constant.CURRENT_AREA_LATITUDE, latitude)
                bundle.putString(Constant.CURRENT_AREA_ID, id)
                bundle.putString(Constant.CURRENT_AREA_ADDRESS, address)
                bundle.putString(Constant.CURRENT_AREA_RADUIS, currentRadius)
                showActivity(AddAntiAreaMapActivity::class.java, bundle)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initCenter()
    }

    private fun initCenter() {
        if (isResume) return
        isResume = true
        val latitude = latitude.toDouble()
        val longitude = longitude.toDouble()
        val latLng = LatLng(longitude, latitude)
        if (type == 5) {
            val markerOptions = MarkerOptions()
            markerOptions.anchor(0.5f, 0.5f).title(address)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.defaultcluster)).position(latLng)
            aMap?.addMarker(markerOptions)
            if (NotNull.isNotNull(addCircle)){
                addCircle?.remove()
            }
            addCircle = aMap?.addCircle(getCircleOptions(latLng))
        } else {
            val markerOptions = MarkerOptions()
            markerOptions.anchor(0.5f, 0.5f).title(address)
                    .icon(getBitmapDescriptor()).position(latLng)
            aMap?.addMarker(markerOptions)
        }
        Handler().postDelayed({
            if (latitude != 0.0 && longitude != 0.0) {
                aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0F))
            }
        }, 1500)
    }

    /**
     * tarketLatLng 圆心坐标
     * currentRadius 圆半径
     * 设置圆参数
     */
    private fun getCircleOptions(la: LatLng): CircleOptions {
        return CircleOptions().
                center(la).
                radius(currentRadius.toDouble()).
                fillColor(ContextCompat.getColor(this, R.color.mapBg)).
                strokeColor(ContextCompat.getColor(this, R.color.mapBg)).
                strokeWidth(1f)
    }

    private fun getBitmapDescriptor(): BitmapDescriptor {
        return when (type) {
            0 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_key_colation_normal)
            1 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_wallet_colation_normal)
            2 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_portable_computer_colation_normal)
            3 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_vice_card_phone_colation_normal)
            4 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_other_colation_normal)
            else -> BitmapDescriptorFactory.fromResource(R.mipmap.defaultcluster)
        }
    }
}

