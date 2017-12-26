package com.goockr.smsantilost.views.activities.antilost

import android.os.Bundle
import android.os.Handler
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant.ADDRESS
import com.goockr.smsantilost.utils.Constant.LATITUDE
import com.goockr.smsantilost.utils.Constant.LONGITUDE
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_device_map.*

/**
 * 钥匙界面
 */
class DeviceMapActivity(override val contentView: Int = R.layout.activity_device_map) : BaseActivity() {

    /**
     * 声明AMapLocationClientOption对象
     */
    private var mLocationOption: AMapLocationClientOption? = null
    private var longitude = ""
    private var latitude = ""
    private var address = ""
    private var type = 2
    private var aMap: AMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map.onCreate(savedInstanceState)
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
    }

    private fun init() {
        if (aMap == null) {
            aMap = map.map
        }
    }

    private fun parseData(it: AMapLocation) {

    }

    private fun initMap() {
        aMap?.myLocationStyle = MyLocationStyle()//设置定位蓝点的Style
        aMap?.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.uiSettings?.isMyLocationButtonEnabled = true
        aMap?.setMyLocationType(AMap.LOCATION_TYPE_LOCATE)
        Handler().postDelayed({
            val latitude = latitude.toDouble()
            val longitude = longitude.toDouble()
            if (latitude != 0.0 && longitude != 0.0) {
                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                markerOptions.anchor(0.5f, 0.5f).title(address)
                        .icon(getBitmapDescriptor()).position(latLng)
                aMap?.addMarker(markerOptions)
                aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19.0F))
            }
        }, 500)
    }

    private fun getBitmapDescriptor(): BitmapDescriptor {
        return when (type) {
            0 -> BitmapDescriptorFactory.fromResource(R.drawable.btn_key_colation_selector)
            1 -> BitmapDescriptorFactory.fromResource(R.drawable.btn_wallet_colation_selector)
            2 -> {
                val fromResource = BitmapDescriptorFactory.fromResource(R.mipmap.btn_vice_card_phone_colation_normal)
                fromResource
            }
            3 -> BitmapDescriptorFactory.fromResource(R.drawable.btn_portable_computer_colation_selector)
            4 -> BitmapDescriptorFactory.fromResource(R.drawable.btn_location_record_point_selector)
            else -> BitmapDescriptorFactory.fromResource(R.drawable.btn_other_colation_selector)
        }
    }
}

