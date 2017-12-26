package com.goockr.smsantilost.views.activities.more

import android.content.Context
import android.os.Bundle
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
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_area_map.*

class AreaMapActivity(override val contentView: Int = R.layout.activity_area_map) : BaseActivity(), LocationSource, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {

    /**
     * 声明AMapLocationClient类对象
     */
    var mLocationClient: AMapLocationClient? = null
    /**
     * 声明定位回调监听器
     */
    private var isfirstinput = true
    private var isInputKeySearch = false
    private var inputSearchKey = ""
    private var searchLatlonPoint: LatLonPoint? = null
    private var autoTips: List<Tip>? = null
    private var firstItem: PoiItem? = null
    private var resultData: MutableList<PoiItem>? = null
    private var currentPage = 0// 当前页面，从0开始计数
    private var query: PoiSearch.Query? = null// Poi查询条件类
    private var poiSearch: PoiSearch? = null
    private var searchType = "住宅区"
    private val searchKey = ""
    private var searchAddress = ""
    private var searchName = ""
    private var longitude = ""
    private var latitude = ""
    private var tarketLatLng: LatLng? = null
    private var currentRadius = -1.0
    private var currentId = -1
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private var addCircle: Circle? = null

    /**
     * 声明AMapLocationClientOption对象
     */
    var mLocationOption: AMapLocationClientOption? = null

    private var aMap: AMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
        map.onCreate(savedInstanceState)
        initIntent()
        init()
        initMView()
        initMap()
        initClickEvent()
    }

    private fun init() {
        if (aMap == null) {
            aMap = map.map
            setUpMap()
        }
        mLocationClient=goockrApplication?.mLocationClient
    }

    /**
     * 初始化title
     */
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        titleOk = titleLayout.findViewById(R.id.titleOk)
        title?.text = getString(R.string.antiDisturb)
        titleOk?.text = getString(R.string.nextStep)
        titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
        titleOk?.visibility = View.VISIBLE
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        et_Search.setText(searchAddress)
        //下一步
        titleOk?.setOnClickListener {
            searchAddress = et_Search.text.toString()
            val bundle = Bundle()
            bundle.putString(Constant.CURRENT_AREA_NAME, searchName)
            bundle.putString(Constant.CURRENT_AREA_ID, currentId.toString())
            bundle.putString(Constant.CURRENT_AREA_LATITUDE, tarketLatLng?.latitude.toString())
            bundle.putString(Constant.CURRENT_AREA_LONGITUDE, tarketLatLng?.longitude.toString())
            bundle.putString(Constant.CURRENT_AREA_ADDRESS, searchAddress)
            bundle.putString(Constant.CURRENT_AREA_RADUIS, currentRadius.toString())
            showActivity(SetAntiDisturbNameActivity::class.java, bundle)
        }
        et_Search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val newText = p0.toString().trim({ it <= ' ' })
                if (newText.isNotEmpty()) {
                    etDelete.visibility = View.VISIBLE
                    val inputquery = InputtipsQuery(newText, getString(R.string.currentArea))
                    val inputTips = Inputtips(this@AreaMapActivity, inputquery)
                    inputquery.cityLimit = true
                    inputTips.setInputtipsListener(inputtipsListener)
                    inputTips.requestInputtipsAsyn()
                } else {
                    etDelete.visibility = View.GONE
                }
            }

        })
        etDelete.setOnClickListener {
            et_Search.setText("")
            etDelete.visibility = View.GONE
        }
        et_Search.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.i("MY", "setOnItemClickListener")
            if (autoTips != null && autoTips?.size!! > position) {
                val tip = autoTips?.get(position)
                searchPoi(tip!!)
            }
        }

        hideSoftKey(et_Search)
    }

    private fun initIntent() {
        val extras = intent.extras
        if (NotNull.isNotNull(extras)) {
            searchName = extras.getString(Constant.CURRENT_AREA_NAME)
            currentId = extras.getString(Constant.CURRENT_AREA_ID).toInt()
            latitude = extras.getString(Constant.CURRENT_AREA_LATITUDE)
            longitude = extras.getString(Constant.CURRENT_AREA_LONGITUDE)
            searchAddress = extras.getString(Constant.CURRENT_AREA_ADDRESS)
            currentRadius = extras.getString(Constant.CURRENT_AREA_RADUIS).toDouble()
        }
    }

    /**
     * 设置一些amap的属性
     */
    private fun setUpMap() {
        aMap?.uiSettings?.isZoomControlsEnabled = false
//        aMap?.setLocationSource(this)// 设置定位监听
        aMap?.moveCamera(CameraUpdateFactory.zoomTo((1 + 17).toFloat()))
        aMap?.uiSettings?.isMyLocationButtonEnabled = true// 设置默认定位按钮是否显示
        aMap?.isMyLocationEnabled = true// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap?.setMyLocationType(AMap.LOCATION_TYPE_LOCATE)
        if (!TextUtils.equals(latitude, "0") && !TextUtils.equals(longitude, "0")) {
            aMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude.toDouble(), longitude.toDouble())))
        }
    }

    internal var inputtipsListener: Inputtips.InputtipsListener = Inputtips.InputtipsListener { list, rCode ->
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            autoTips = list
            val listString = list.indices.map { list[it].name }
            val aAdapter = ArrayAdapter(
                    applicationContext,
                    R.layout.route_inputs, listString)
            et_Search.setAdapter(aAdapter)
            aAdapter.notifyDataSetChanged()
            if (isfirstinput) {
                isfirstinput = false
                et_Search.showDropDown()
            }
        } else {
            Toast.makeText(this@AreaMapActivity, "erroCode " + rCode, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initMap() {
        val myLocationStyle = MyLocationStyle()
        //初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        aMap?.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap?.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

    }

    private fun searchPoi(result: Tip) {
        isInputKeySearch = true
        inputSearchKey = result.name//getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
        searchLatlonPoint = result.point
        firstItem = PoiItem("tip", searchLatlonPoint, inputSearchKey, result.address)
        firstItem?.cityName = result.district
        firstItem?.adName = ""
        resultData?.clear()

        val mapScreenMarkers = aMap?.mapScreenMarkers
        for (marker in mapScreenMarkers!!) {
            val `object` = marker.`object`
            if (`object` is String && TextUtils.equals(`object`.toString(), "搜索结果")) {
                marker.remove()//移除当前Marker
            }
        }
        if (!NotNull.isNotNull(searchLatlonPoint)) {
            return
        }
        val latLng = LatLng(searchLatlonPoint?.latitude!!, searchLatlonPoint?.longitude!!)
        val markerOptions = MarkerOptions()
        markerOptions.anchor(0.5f, 0.5f).title(result.name)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.defaultcluster)).position(latLng)
        val marker = aMap?.addMarker(markerOptions)
        marker?.`object` = getString(R.string.searchResult)
        tarketLatLng = latLng
        addCircle = aMap?.addCircle(getCircleOptions())
        aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        hideSoftKey(et_Search)
        doSearchQuery()
    }

    /**
     * tarketLatLng 圆心坐标
     * currentRadius 圆半径
     * 设置圆参数
     */
    fun getCircleOptions(): CircleOptions {
        return CircleOptions().
                center(tarketLatLng).
                radius(currentRadius).
                fillColor(ContextCompat.getColor(this@AreaMapActivity, R.color.mapBg)).
                strokeColor(ContextCompat.getColor(this@AreaMapActivity, R.color.mapBg)).
                strokeWidth(1f)
    }
    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    protected fun doSearchQuery() {
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

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 监听seekBar
        sb_Radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val string = "" + (progress + 1) + "m"
                tv_LeftRadius.text = string
                tv_CurrentRadius.text = getString(R.string.settingM) + string
                if (NotNull.isNotNull(addCircle)) {
                    addCircle?.remove()
                }
                if (NotNull.isNotNull(tarketLatLng)) {
                    currentRadius = progress + 1.toDouble()
                    addCircle = aMap?.addCircle(getCircleOptions())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    override fun onPause() {
        super.onPause()
        mLocationClient?.stopLocation()
    }

    override fun onResume() {
        super.onResume()
        mLocationClient?.stopLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient?.onDestroy()
    }

    override fun deactivate() {
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        mListener = listener
        if (mlocationClient == null) {
            mlocationClient = AMapLocationClient(this)
            mLocationOption = AMapLocationClientOption()
            //设置定位监听
            mlocationClient?.setLocationListener(this)
            //设置为高精度定位模式
            mLocationOption?.isOnceLocation = true
            mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //设置定位参数
            mlocationClient?.setLocationOption(mLocationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient?.startLocation()
        }
    }

    override fun onLocationChanged(p0: AMapLocation?) {
    }

    override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
    }

    override fun onPoiSearched(p0: PoiResult?, p1: Int) {
    }

    private fun hideSoftKey(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
