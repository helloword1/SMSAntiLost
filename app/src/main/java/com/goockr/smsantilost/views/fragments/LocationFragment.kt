package com.goockr.smsantilost.views.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.MyLocationStyle
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_location.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zhihao.wen on 2016/11/1.
 */

class LocationFragment : BaseFragment() {
    /**
     * 声明AMapLocationClient类对象
     */
    var mLocationClient: AMapLocationClient? = null
    /**
     * 声明定位回调监听器
     */
    var mLocationListener: AMapLocationListener = AMapLocationListener { aMapLocation ->
        if (aMapLocation != null) {
            if (aMapLocation.errorCode == 0) {
                //可在其中解析amapLocation获取相应内容。

                activity.runOnUiThread { parseData(aMapLocation) }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtils.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.errorCode + ", errInfo:"
                        + aMapLocation.errorInfo)
            }
        }
    }
    private var aMap: AMap? = null
    private var flBottom: FrameLayout? = null

    /**
     * 声明AMapLocationClientOption对象
     */
    var mLocationOption: AMapLocationClientOption? = null

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
    }
    private fun initView() {
        init()
        initMap()

    }

    private fun init() {
        if (aMap == null) {
            aMap = map.map
        }
    }

    private fun initMap() {
        val myLocationStyle: MyLocationStyle
        myLocationStyle = MyLocationStyle()//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        aMap!!.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap!!.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap!!.moveCamera(CameraUpdateFactory.zoomTo((1 + 20).toFloat()))
        //初始化定位
        mLocationClient = AMapLocationClient(context)
        //设置定位回调监听
        mLocationClient!!.setLocationListener(mLocationListener)

        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        mLocationOption!!.isOnceLocation = true
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        //        mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption!!.isNeedAddress = true
        //给定位客户端对象设置定位参数
        mLocationClient!!.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient!!.startLocation()
    }

    private fun initbottom() {
        val bar = LinearLayout(this.context)
        bar.orientation = LinearLayout.VERTICAL
        val blp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        bar.gravity = Gravity.CENTER
        bar.setPadding(10, 16, 10, 16)
        val image = ImageView(context)
        val ilp = LinearLayout.LayoutParams(resources.getDimension(R.dimen.image_height).toInt(), resources.getDimension(R.dimen.image_height).toInt())
        image.setImageResource(R.mipmap.ic_launcher)
        bar.addView(image, ilp)

        val text = TextView(context)
        text.text = "12"
        val tlp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        bar.addView(text, tlp)
        flBottom!!.addView(bar)
        flBottom!!.visibility = View.INVISIBLE
    }

    /* @Override
    public void onPause() {
        super.onPause();
        mLocationClient.stopLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationClient.startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }*/
}
