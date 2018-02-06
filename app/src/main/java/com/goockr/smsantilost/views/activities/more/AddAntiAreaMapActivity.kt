package com.goockr.smsantilost.views.activities.more

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.TranslateAnimation
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_add_anti_area_map.*
import java.util.*

open class AddAntiAreaMapActivity(override val contentView: Int = R.layout.activity_add_anti_area_map) : BaseActivity(), LocationSource,
        AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {
    private var aMap: AMap? = null
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private var locationMarker: Marker? = null
    private var geocoderSearch: GeocodeSearch? = null
    private var currentPage = 0// 当前页面，从0开始计数
    private var query: PoiSearch.Query? = null// Poi查询条件类
    private var poiSearch: PoiSearch? = null
    private var poiItems: List<PoiItem>? = null// poi数据

    private var searchType = "住宅区"
    private val searchKey = ""
    private var searchLatlonPoint: LatLonPoint? = null
    private var resultData: MutableList<PoiItem>? = null
    private var isItemClickAction: Boolean = false

    private var autoTips: List<Tip>? = null
    private var isfirstinput = true
    private var firstItem: PoiItem? = null
    private var addCircle: Circle? = null
    private var tarketLatLng: LatLng? = null
    private var currentRadius = "0"
    private var searchAddress = ""
    private var searchName = ""
    private var longitude = "0"
    private var currentId = "0"
    private var latitude = "0"
    private var isResume = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView?.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    override fun initView() {
        initIntent()
        init()
        resultData = ArrayList()
        titleRight1?.visibility = View.VISIBLE
        titleRight1?.text = getString(R.string.nextStep)
        etAddSearch?.setText(searchAddress)
        etAddSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val newText = s.toString().trim { it <= ' ' }
                if (newText.isNotEmpty()) {
                    val inputquery = InputtipsQuery(newText, "佛山")
                    val inputTips = Inputtips(this@AddAntiAreaMapActivity, inputquery)
                    inputquery.cityLimit = true
                    inputTips.setInputtipsListener(inputtipsListener)
                    inputTips.requestInputtipsAsyn()
                }
//                if (!s.isEmpty()) {
//                    etAddSearch.setCompoundDrawables(null, ContextCompat.getDrawable(this@AddAntiAreaMapActivity, R.mipmap.btn_del), null, null)
//                } else {
//                    etAddSearch.setCompoundDrawables(null, null, null, null)
//                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        //下一步
        titleRight1?.setOnClickListener {
            if (TextUtils.equals(currentRadius, "0")) {
                MyToast.showToastCustomerStyleText(this@AddAntiAreaMapActivity, getString(R.string.aeraRadius))
                return@setOnClickListener
            }
            searchAddress = etAddSearch.text.toString()
            val bundle = Bundle()
            bundle.putString(Constant.CURRENT_AREA_NAME, searchName)
            bundle.putString(Constant.CURRENT_AREA_ID, currentId)
            bundle.putString(Constant.CURRENT_AREA_LATITUDE, tarketLatLng?.latitude.toString())
            bundle.putString(Constant.CURRENT_AREA_LONGITUDE, tarketLatLng?.longitude.toString())
            bundle.putString(Constant.CURRENT_AREA_ADDRESS, searchAddress)
            bundle.putString(Constant.CURRENT_AREA_RADUIS, currentRadius)
            showActivity(SetAntiDisturbNameActivity::class.java, bundle)
        }
        etAddSearch.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Log.i("MY", "setOnItemClickListener")
            if (autoTips != null && autoTips!!.size > position) {
                val tip = autoTips!![position]
                searchPoi(tip)
            }
        }

        geocoderSearch = GeocodeSearch(this)
        geocoderSearch?.setOnGeocodeSearchListener(this)

        hideSoftKey(etAddSearch)

        // 监听seekBar
        sb_Radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val string = "" + (progress + 1) + "m"
                tv_LeftRadius.text = string
                tv_CurrentRadius.text = getString(R.string.settingM) + string
                if (NotNull.isNotNull(addCircle)) {
                    addCircle?.remove()
                }
                if (NotNull.isNotNull(tarketLatLng)) {
                    currentRadius = (progress + 1.toDouble()).toString()
                    addCircle = aMap?.addCircle(getCircleOptions())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    /**
     * tarketLatLng 圆心坐标
     * currentRadius 圆半径
     * 设置圆参数
     */
    fun getCircleOptions(): CircleOptions {
        return CircleOptions().
                center(tarketLatLng).
                radius(currentRadius.toDouble()).
                fillColor(ContextCompat.getColor(this@AddAntiAreaMapActivity, R.color.mapBg)).
                strokeColor(ContextCompat.getColor(this@AddAntiAreaMapActivity, R.color.mapBg)).
                strokeWidth(1f)
    }

    private fun initIntent() {
        val extras = intent.extras
        if (NotNull.isNotNull(extras)) {
            searchName = extras.getString(Constant.CURRENT_AREA_NAME)
            currentId = extras.getString(Constant.CURRENT_AREA_ID)
            latitude = extras.getString(Constant.CURRENT_AREA_LATITUDE)
            longitude = extras.getString(Constant.CURRENT_AREA_LONGITUDE)
            searchAddress = extras.getString(Constant.CURRENT_AREA_ADDRESS)
            currentRadius = extras.getString(Constant.CURRENT_AREA_RADUIS)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        if (isResume) return
        isResume = true

        Thread({
            SystemClock.sleep(2000)
            runOnUiThread {
                aMap?.moveCamera(CameraUpdateFactory.zoomTo((16).toFloat()))
            }
        }).start()
    }

    /**
     * 初始化
     */
    private fun init() {
        if (aMap == null) {
            aMap = mapView?.map
            setUpMap()
        }

        aMap?.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition) {

            }

            override fun onCameraChangeFinish(cameraPosition: CameraPosition) {
                if (!isItemClickAction && !isInputKeySearch) {
                    geoAddress()
                    startJumpAnimation()
                }
                searchLatlonPoint = LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude)
                isInputKeySearch = false
                isItemClickAction = false
            }
        })

        aMap?.setOnMapLoadedListener { addMarkerInScreenCenter(null) }
    }

    /**
     * 设置一些amap的属性
     */
    private fun setUpMap() {
        val myLocationStyle = MyLocationStyle()
        //初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        aMap?.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        aMap?.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.uiSettings?.isMyLocationButtonEnabled = true
        aMap?.uiSettings?.isZoomControlsEnabled = false

        if (!TextUtils.equals(latitude, "0") && !TextUtils.equals(longitude, "0")) {
            val latLng = LatLng(longitude.toDouble(), latitude.toDouble())
            aMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        mlocationClient = goockrApplication?.mLocationClient
        mlocationClient?.setLocationListener(this)
        //开始定位
        mlocationClient?.startLocation()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
        deactivate()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        if (null != mlocationClient) {
            mlocationClient?.stopLocation()
        }
    }

    /**
     * 定位成功后回调函数
     */
    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                if (mListener != null) {
                    mListener?.onLocationChanged(amapLocation)
                }

                val curLatlng = LatLng(amapLocation.latitude, amapLocation.longitude)

                searchLatlonPoint = LatLonPoint(curLatlng.latitude, curLatlng.longitude)

                aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 17f))

                isInputKeySearch = false

//                etAddSearch?.setText("")

            } else {
                val errText = "定位失败," + amapLocation.errorCode + ": " + amapLocation.errorInfo
                Log.e("AmapErr", errText)
            }
        }
    }

    /**
     * 激活定位
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        mListener = listener
        if (mlocationClient == null) {
            mlocationClient = goockrApplication?.mLocationClient
            mlocationClient?.setLocationListener(this)
            //开始定位
            mlocationClient?.startLocation()
        }
    }

    /**
     * 停止定位
     */
    override fun deactivate() {
        mListener = null
        if (mlocationClient != null) {
            mlocationClient?.stopLocation()
        }
        mlocationClient = null
    }


    /**
     * 响应逆地理编码
     */
    fun geoAddress() {
        etAddSearch.setText("")
        if (searchLatlonPoint != null) {
            val query = RegeocodeQuery(searchLatlonPoint, 200f, GeocodeSearch.AMAP)// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocoderSearch?.getFromLocationAsyn(query)
        }
    }

    /**
     * 开始进行poi搜索
     */
    private fun doSearchQuery() {
        currentPage = 0
        query = PoiSearch.Query(searchKey, searchType, "")// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query?.cityLimit = true
        query?.pageSize = 20
        query?.pageNum = currentPage

        if (searchLatlonPoint != null) {
            poiSearch = PoiSearch(this, query)
            poiSearch?.setOnPoiSearchListener(this)
            poiSearch?.bound = PoiSearch.SearchBound(searchLatlonPoint, 1000, true)//
            poiSearch?.searchPOIAsyn()
        }
    }

    override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.regeocodeAddress != null
                    && result.regeocodeAddress.formatAddress != null) {
                val address = result.regeocodeAddress.province + result.regeocodeAddress.city + result.regeocodeAddress.district + result.regeocodeAddress.township
                firstItem = PoiItem("regeo", searchLatlonPoint, address, address)
                doSearchQuery()
            }
        } else {
            Toast.makeText(this@AddAntiAreaMapActivity, "error code is " + rCode, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onGeocodeSearched(geocodeResult: GeocodeResult, i: Int) {

    }

    /**
     * POI搜索结果回调
     * @param poiResult 搜索结果
     * @param resultCode 错误码
     */
    override fun onPoiSearched(poiResult: PoiResult?, resultCode: Int) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.query != null) {
                if (poiResult.query == query) {
                    poiItems = poiResult.pois
                    if (poiItems != null && poiItems!!.isNotEmpty()) {
                        updateListview(poiItems!!)
                    } else {
                        Toast.makeText(this@AddAntiAreaMapActivity, "无搜索结果", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@AddAntiAreaMapActivity, "无搜索结果", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 更新列表中的item
     * @param poiItems
     */
    @SuppressLint("SetTextI18n")
    private fun updateListview(poiItems: List<PoiItem>) {
        resultData?.clear()
        resultData?.add(firstItem!!)
        var cityName = poiItems[0].cityName
        if (!NotNull.isNotNull(cityName)) {
            cityName = ""
        }
        var adName = poiItems[0].adName
        if (!NotNull.isNotNull(adName)) {
            adName = ""
        }
        var snippet = poiItems[0].snippet
        if (!NotNull.isNotNull(snippet)) {
            snippet = ""
        }
        var title = poiItems[0].title
        if (!NotNull.isNotNull(title)) {
            title = ""
        }
        etAddSearch?.setText("$cityName$adName$snippet$title")
        resultData?.addAll(poiItems)
    }


    override fun onPoiItemSearched(poiItem: PoiItem, i: Int) {

    }

    private fun addMarkerInScreenCenter(locationLatLng: LatLng?) {
        val latLng = aMap?.cameraPosition?.target

        val screenPosition = aMap?.projection?.toScreenLocation(latLng)
        locationMarker = aMap?.addMarker(MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.defaultcluster)))
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker?.setPositionByPixels(screenPosition!!.x, screenPosition.y)
        locationMarker?.zIndex = 1f

    }

    /**
     * 屏幕中心marker 跳动
     */
    @SuppressLint("SetTextI18n")
    private fun startJumpAnimation() {
        if (locationMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            val latLng = locationMarker?.position
            tarketLatLng = latLng
            if (NotNull.isNotNull(addCircle)) {
                addCircle?.remove()
            }
            currentRadius = "0"
            val string = "" + 0 + "m"
            tv_LeftRadius.text = string
            sb_Radius.progress = 0
            tv_CurrentRadius.text = getString(R.string.settingM) + string
            addCircle = aMap?.addCircle(getCircleOptions())

            val point = aMap?.projection?.toScreenLocation(latLng)
            point!!.y -= dip2px(this, 125f)
            val target = aMap?.projection
                    ?.fromScreenLocation(point)
            //使用TranslateAnimation,填写一个需要移动的目标点
            val animation = TranslateAnimation(target)
            animation.setInterpolator { input ->
                // 模拟重加速度的interpolator
                if (input <= 0.5) {
                    (0.5f - 2.0 * (0.5 - input) * (0.5 - input)).toFloat()
                } else {
                    (0.5f - Math.sqrt(((input - 0.5f) * (1.5f - input)).toDouble())).toFloat()
                }
            }
            //整个移动所需要的时间
            animation.setDuration(600)
            //设置动画
            locationMarker?.setAnimation(animation)
            //开始动画
            locationMarker?.startAnimation()

        } else {
            Log.e("ama", "screenMarker is null")
        }
    }

    //dip和px转换
    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


    internal var inputtipsListener: Inputtips.InputtipsListener = Inputtips.InputtipsListener { list, rCode ->
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            autoTips = list
            val listString = list.indices.map { list[it].name }
            val aAdapter = ArrayAdapter(
                    applicationContext,
                    R.layout.route_inputs, listString)
            etAddSearch.setAdapter(aAdapter)
            aAdapter.notifyDataSetChanged()
            if (isfirstinput) {
                isfirstinput = false
                etAddSearch.showDropDown()
            }
        } else {
            Toast.makeText(this@AddAntiAreaMapActivity, "erroCode " + rCode, Toast.LENGTH_SHORT).show()
        }
    }

    private var isInputKeySearch: Boolean = false
    private var inputSearchKey: String? = null
    private fun searchPoi(result: Tip) {
        isInputKeySearch = true
        inputSearchKey = result.name//getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
        searchLatlonPoint = result.point
        firstItem = PoiItem("tip", searchLatlonPoint, inputSearchKey, result.address)
        firstItem?.cityName = result.district
        firstItem?.adName = ""
        resultData?.clear()
        val latLng = LatLng(searchLatlonPoint!!.latitude, searchLatlonPoint!!.longitude)
        aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        tarketLatLng = latLng
        hideSoftKey(etAddSearch)
        doSearchQuery()
    }

    private fun hideSoftKey(view: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}
