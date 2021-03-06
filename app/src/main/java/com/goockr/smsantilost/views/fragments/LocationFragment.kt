package com.goockr.smsantilost.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.ScaleAnimation
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.DeviceBean
import com.goockr.smsantilost.graphics.LocationDeviceInfoView
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.graphics.ZoomControlsView
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.MyAMapLocationListener
import com.goockr.smsantilost.views.activities.HomeActivity
import com.goockr.smsantilost.views.activities.antilost.PositionRecordMapActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.fragment_location.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


/**
 * Created by zhihao.wen on 2016/11/1.
 */

class LocationFragment : BaseFragment() {

    private var aMap: AMap? = null
    private var currentZoom = 17
    private var goockrApplication: GoockrApplication? = null
    private var deviceBean: DeviceBean? = null
    @SuppressLint("SimpleDateFormat")
    private fun parseData(amapLocation: AMapLocation?) {
        val locationType = amapLocation!!.locationType//
        val latitude = amapLocation.latitude//获取纬度
        val longitude = amapLocation.longitude//获取经度
        val accuracy = amapLocation.accuracy//获取精度信息
        val address = amapLocation.address//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        val country = amapLocation.country//国家信息
        val province = amapLocation.province//省信息
        val city = amapLocation.city//城市信息
        val district = amapLocation.district//城区信息
        val street = amapLocation.street//街道信息
        val streetNum = amapLocation.streetNum//街道门牌号信息
        val cityCode = amapLocation.cityCode//城市编码
        val adCode = amapLocation.adCode//地区编码
        val aoiName = amapLocation.aoiName//获取当前定位点的AOI信息
        val buildingId = amapLocation.buildingId//获取当前室内定位的建筑物Id
        val floor = amapLocation.floor//获取当前室内定位的楼层
        //获取定位时间
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(amapLocation.time)
        val format = df.format(date)
        //        addMarkersToMap(latitude,longitude);// 往地图上添加marker
        val format1 = String.format("获取当前定位结果来源，如网络定位结果，详见定位类型表:%s\n获取纬度:%s\n获取经度:" +
                "%s\n获取精度信息:%s\n地址:%s\n国家信息:%s\n省信息:%s\n城市信息：%s\n城区信息：%s\n街道信息：%s\n" +
                "街道门牌号信息：%s\n城市编码：%s\n" +
                "地区编码：%s\n获取当前定位点的AOI信息：%s" +
                "\n获取当前室内定位的建筑物Id：%s\n获取当前室内定位的楼层：%s\n时间：%s",
                locationType, latitude,
                longitude, accuracy, address, country, province, city, district, street, streetNum,
                cityCode, adCode, aoiName, buildingId, floor, format)
        LogUtils.i("", "" + format1)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return setContentView(R.layout.fragment_location)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        initView()
        init()
        initMap()
    }

    private fun initView() {
        locationDeviceInfoView.setOnLocationControlsListenerListener(object :
                LocationDeviceInfoView.OnLocationControlsListener {
            override fun locationRecord() {
                showActivity(PositionRecordMapActivity::class.java)
//                MyToast.showToastCustomerStyleText(activity, getString(R.string.deviceDeveloping))
            }

            override fun locationGuide() {
                if (NotNull.isNotNull(deviceBean)) {
                    val homeActivity = activity as HomeActivity
                    homeActivity.showMapDialog(deviceBean!!)
                } else {
                    MyToast.showToastCustomerStyleText(activity, getString(R.string.pleaseChoiceDevice))
                }
//
            }

        })
        zoomControlsView.setOnZoomControlsListener(object : ZoomControlsView.OnZoomControlsListener {
            override fun zoomAdd() {
                if (currentZoom <= 18) {
                    ++currentZoom
                    aMap?.moveCamera(CameraUpdateFactory.zoomTo((currentZoom).toFloat()))
                }
            }

            override fun zoomMinus() {
                if (currentZoom >= 4) {
                    --currentZoom
                    aMap?.moveCamera(CameraUpdateFactory.zoomTo((currentZoom).toFloat()))
                }
            }
        })

    }

    private fun init() {
        if (aMap == null) {
            aMap = map.map

        }
    }

    private fun initMap() {
        val myLocationStyle = MyLocationStyle()
        //初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        aMap?.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        aMap?.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.uiSettings?.isMyLocationButtonEnabled = true
        aMap?.uiSettings?.isZoomControlsEnabled = false

        //初始化定位
        val mLocationListener = MyAMapLocationListener()
        mLocationListener.setLocationListener {
            if (it != null) {
                if (it.errorCode == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    activity.runOnUiThread { parseData(it) }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtils.e("AmapError", "location Error, ErrCode:"
                            + it.errorCode + ", errInfo:"
                            + it.errorInfo)
                }
            }
        }
        thread {
            activity.runOnUiThread {
                SystemClock.sleep(2200)
                aMap?.moveCamera(CameraUpdateFactory.zoomTo((currentZoom).toFloat()))
            }
        }
        initDeviceState()
    }

    private fun initDeviceState() {
        val goockrApplication = activity.application as GoockrApplication
        val deviceBeanDao = goockrApplication.mDaoSession?.deviceBeanDao
        val list = deviceBeanDao?.queryBuilder()?.build()?.list()
        if (NotNull.isNotNull(list) && !list!!.isEmpty()) {
            val device = list[list.size - 1]
            deviceBean = device
            for (c in list) {
                val latLng = LatLng(c.latitude, c.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.anchor(0.5f, 0.5f)/*.title(c.address)*/
                        .icon(getBitmapDescriptor(c.deviceType)).position(latLng)
                aMap?.addMarker(markerOptions)

            }
        }
        aMap?.setOnMarkerClickListener {
            val scaleAnimation = ScaleAnimation(1f, 1.2f, 1f, 1.2f)
            scaleAnimation.setDuration(100)
            it.setAnimation(scaleAnimation)
            it.startAnimation()

            list!!.filter { c -> TextUtils.equals(c?.latitude.toString(), it.position.latitude.toString()) && TextUtils.equals(c?.longitude.toString(), it.position.longitude.toString()) }
                    .forEach {
                        deviceBean = it
                        locationDeviceInfoView.visibility = View.VISIBLE
                        locationDeviceInfoView.setDeviceBean(it)
                        aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), currentZoom.toFloat()))
                    }

            false
        }

    }

    private fun getBitmapDescriptor(type: String): BitmapDescriptor {
        return when (type) {
            "钥匙" -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_key_location_normal)
            "钱包" -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_wallet_colation_normal)
            "笔记本" -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_portable_computer_colation_normal)
            "手机副卡" -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_vice_card_phone_colation_normal)
            "更多" -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_other_colation_normal)
            else -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_key_location_normal)
        }
    }

    override fun onPause() {
        super.onPause()
        goockrApplication?.mLocationClient?.stopLocation()
    }

    override fun onResume() {
        super.onResume()
        goockrApplication?.mLocationClient?.stopLocation()
    }
}
