package com.goockr.smsantilost.views.activities.antilost

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.ZoomControlsView
import com.goockr.smsantilost.graphics.calendarview.CalendarUtil
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.ADDRESS
import com.goockr.smsantilost.utils.Constant.ADDRESS_TYPE
import com.goockr.smsantilost.utils.Constant.CURRENT_AREA_NAME
import com.goockr.smsantilost.utils.Constant.LATITUDE
import com.goockr.smsantilost.utils.Constant.LONGITUDE
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_position_record_map.*
import kotlinx.android.synthetic.main.calendar_layout.*
import kotlinx.android.synthetic.main.item_calendar.view.*
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
/**
 * 位置记录页
 */
class PositionRecordMapActivity(override val contentView: Int = R.layout.activity_position_record_map) : BaseActivity() {
    private var longitude = "0"
    private var latitude = "0"
    private var address = ""
    private var type = 2
    private var currentRadius = "0"
    private var aMap: AMap? = null
    private var isResume = false
    private var currentZoom = 17
    private var name = ""
    private var addCircle: Circle? = null
    private var addMarker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map.onCreate(savedInstanceState)
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
    }

    override fun initView() {
        title?.text = getString(R.string.positionRecord)
        titleRight?.setImageResource(R.mipmap.icon_calendar)
        titleRight?.visibility = View.VISIBLE
//        initExtras()
        init()
        setCalendarView()
//        initMap()
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendarView() {
        calendarDateView.setAdapter { convertView, parentView, bean ->
            var convertView = convertView
            val view: TextView
            if (convertView == null) {
                convertView = LayoutInflater.from(parentView.context).inflate(R.layout.item_calendar, null)
                val params = ViewGroup.LayoutParams(px(32f), px(32f))
                convertView!!.layoutParams = params
            }

            view = convertView.text
            view.text = "" + bean.day
            if (0 !== bean.mothFlag) {
                view.setTextColor(-0x6d665f)
            } else {
                view.setTextColor(-0xd6d6d7)
            }

            val ivCircle = convertView.ivCircle
            if (bean.isHavePosition) {
                ivCircle.visibility = View.VISIBLE
            } else {
                ivCircle.visibility = View.GONE
            }
            convertView
        }

        calendarDateView.setOnItemClickListener { view, _, bean ->
                val disPlayNumber = getDisPlayNumber(bean.day)
                if (NotNull.isNotNull(view)) {
                    cTitle.text = bean.year.toString() + "/" + getDisPlayNumber(bean.moth) + "/" + disPlayNumber
                } else {
                    cTitle.text = bean.year.toString() + "/" + getDisPlayNumber(bean.moth)
                }
        }

        val data = CalendarUtil.getYMD(Date())
        cTitle.text = data[0].toString() + "/" + data[1] + "/" + data[2]
        cBack.setOnClickListener { calendarDateView.currentItem = calendarDateView.currentItem - 1 }
        cBack1.setOnClickListener { calendarDateView.currentItem = calendarDateView.currentItem + 1 }
    }

    private fun getDisPlayNumber(num: Int): String {
        return if (num < 10) "0" + num else "" + num
    }

    private fun px(dipValue: Float): Int {
        val r = Resources.getSystem()
        val scale = r.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()

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
        aMap?.uiSettings?.isZoomControlsEnabled = false
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
        //日历
        titleRight?.setOnClickListener {
            if (CalendarView.visibility==View.GONE){
                CalendarView.visibility=View.VISIBLE
            }else{
                CalendarView.visibility=View.GONE
            }

        }
    }

    /* override fun onNewIntent(intent: Intent?) {
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
     }*/

    /* private fun initMap() {
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
     }*/

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
            if (NotNull.isNotNull(addMarker)) {
                addMarker?.remove()
            }
            addMarker = aMap?.addMarker(markerOptions)

            if (NotNull.isNotNull(addCircle)) {
                addCircle?.remove()
            }
            addCircle = aMap?.addCircle(getCircleOptions(latLng))
        } else {
            val markerOptions = MarkerOptions()
            markerOptions.anchor(0.5f, 0.5f).title(address)
                    .icon(getBitmapDescriptor()).position(latLng)
            aMap?.addMarker(markerOptions)
        }
        if (latitude != 0.0 && longitude != 0.0) {
            aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0F))
        }
        Handler().postDelayed({
            aMap?.moveCamera(CameraUpdateFactory.zoomTo(16.0F))
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
            0 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_key_location_normal)
            1 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_wallet_colation_normal)
            2 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_portable_computer_colation_normal)
            3 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_vice_card_phone_colation_normal)
            4 -> BitmapDescriptorFactory.fromResource(R.mipmap.btn_other_colation_normal)
            else -> BitmapDescriptorFactory.fromResource(R.mipmap.defaultcluster)
        }
    }
}

