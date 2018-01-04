package com.goockr.smsantilost.views.activities.antilost

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.header.MaterialHeader
import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Message
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AntilostBean
import com.goockr.smsantilost.entries.DeviceBean
import com.goockr.smsantilost.entries.DeviceBeanDao
import com.goockr.smsantilost.utils.*
import com.goockr.smsantilost.utils.Constant.ADDRESS_TYPE
import com.goockr.smsantilost.utils.Constant.BUZZER
import com.goockr.smsantilost.utils.Constant.INIT
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_key.*

/**
 * 钥匙界面
 */
class KeyActivity(override val contentView: Int = R.layout.activity_key) : BaseActivity() {
    private var name: AntilostBean? = null
    private var mThread: Thread? = null
    private var mLocationClient: AMapLocationClient? = null
    private var latitude = ""
    private var longitude = ""
    private var address = ""
    private var type = 0
    private var isInsert=false
    private var deviceBean: DeviceBean? = null
    private var deviceBeanDao: DeviceBeanDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun initView() {
        initMData()
        initMTitle()
        initMPullToRefresh()
        initClickEvent()
    }

    override fun onResume() {
        super.onResume()
        initMView()
    }


    /**
     * 初始化数据
     */
    private fun initMData() {
        type = intent.extras.getInt(ADDRESS_TYPE)
        name = intent.extras.getSerializable("device") as AntilostBean?

    }

    @SuppressLint("SetTextI18n")
    private fun initMView() {
        val deviceBeanDao = goockrApplication?.mDaoSession?.deviceBeanDao
        val bean = deviceBeanDao?.queryBuilder()?.where(DeviceBeanDao.Properties.Mac.eq(name?.mac))?.build()?.unique()
        if (NotNull.isNotNull(bean)){
            title?.text = bean?.name
        }
        if (mLocationClient!!.isStarted) {
            mLocationClient?.stopLocation()
        }
        val connect = instance!!.isConnect
        if (connect) {
            instance?.setUiHandler(myHandler)
            instance?.write(Constant.INIT)
            //开始定位
            mLocationClient?.startLocation()
        } else {
            tvNotify.visibility = View.VISIBLE
            tvNotify.text = getString(R.string.deviceNoConnect)
        }
        tvDeviceDate.text = DateUtils.getDate(DateUtils.parsePatterns[5])
        tvDeviceAddress.text = name?.address + " >"
        address = name?.address!!
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLocationClient!!.isStarted) {
            mLocationClient?.stopLocation()
        }
    }

    /**
     * 初始化下拉刷新布局
     */
    private fun initMPullToRefresh() {
        val header = MaterialHeader(this)
        // 改变刷新图标颜色
        val colors = resources.getIntArray(R.array.google_colors)
        header.setColorSchemeColors(colors)
        // 调整刷新图标位置
        val calculateUtils = CalculateUtils(this)
        header.layoutParams = PtrFrameLayout.LayoutParams(-1, -2) as ViewGroup.LayoutParams?
        header.setPadding(0, calculateUtils.dp2px(15), 0, calculateUtils.dp2px(10))
        header.setPtrFrameLayout(ptrFrameLayout)
        // 设置刷新完毕时关闭的时间
        ptrFrameLayout.setDurationToCloseHeader(500)
        // 改变刷新图标风格
        ptrFrameLayout.headerView = header
        ptrFrameLayout.addPtrUIHandler(header)
        ptrFrameLayout.setLastUpdateTimeRelateObject(this)
        ptrFrameLayout.setPtrHandler(object : PtrDefaultHandler() {
            override fun onRefreshBegin(frame: PtrFrameLayout?) {
                initMView()
                frame!!.postDelayed({
                    ptrFrameLayout.refreshComplete()
                }, 1500)
            }

            override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
                return true
            }
        })
    }

    /**
     * 初始化头部
     */
    @SuppressLint("InflateParams")
    private fun initMTitle() {
        ll?.removeAllViews()
        mainIcon.setImageResource(getDeviceID(type))
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleRight = titleLayout.findViewById(R.id.titleRight)
        titleBack = titleLayout.findViewById(R.id.titleBack)

        title?.text = name?.deviceName
        titleRight?.visibility = View.VISIBLE
        titleRight?.setImageResource(R.mipmap.btn_nav_set_up)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        if (instance?.isConnect!!) {
            instance?.write(INIT)
        }
        dealWithSignal(name?.distance)
        //定位
        val mLocationListener = MyAMapLocationListener()
        mLocationListener.setLocationListener {
            if (it != null) {
                if (it.errorCode == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    runOnUiThread { parseData(it) }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtils.e("AmapError", "location Error, ErrCode:"
                            + it.errorCode + ", errInfo:"
                            + it.errorInfo)
                }
            }
        }
        mLocationClient = goockrApplication?.mLocationClient
        mLocationClient?.setLocationListener(mLocationListener)
        //开始定位
//        mLocationClient?.startLocation()

        deviceBeanDao = goockrApplication?.mDaoSession?.deviceBeanDao
        deviceBean = deviceBeanDao?.queryBuilder()?.where(DeviceBeanDao.Properties.
                Mac.eq(name?.mac))?.unique()
        latitude = deviceBean?.latitude.toString()
        longitude = deviceBean?.longitude.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun parseData(it: AMapLocation) {
        tvDeviceAddress.text = it.address + " >"
        latitude = it.latitude.toString()
        longitude = it.longitude.toString()

        checkNotNull(deviceBean).address = it.address
        checkNotNull(deviceBean).latitude = it.latitude
        checkNotNull(deviceBean).longitude = it.longitude
        deviceBeanDao?.update(deviceBean)
    }

    override fun handleMyMessage(msg: Message?) {
        super.handleMyMessage(msg)
        val msgStr = msg?.obj?.toString()
        if (!NotNull.isNotNull(msgStr)) return
        if (msgStr!!.startsWith("Init_")) {
            //电量
            val battery = msgStr.split(",")[0].split("_")[1]
            dealWithBattery(battery)
            //是否有SIM卡
            val sim = msgStr.split(",")[1].split("_")[1]
            dealWithSIM(sim)
            //是否在充电中
            val charge = msgStr.split(",")[2].split("_")[1]
            dealWithCharge(charge)
        }

    }

    private fun dealWithCharge(charge: String) {

    }

    private fun dealWithSIM(s: String) {
        if (s.toInt() == 0) {
            tvNotify.visibility = View.VISIBLE
            tvNotify.text = getString(R.string.simNoInsert)
            SimPhone.text = getString(R.string.notInsert)
            isInsert=false
        } else if (s.toInt() == 1) {
            tvNotify.visibility = View.GONE
            SimPhone.text = s
            isInsert=true
        }
    }

    private fun dealWithBattery(s: String) {
        tvBatteryTest.text = "$s%"
        when {
            s.toInt() > 90 -> ivBattery.setImageResource(R.mipmap.icon_equipment_capacity_5)
            s.toInt() > 73 -> ivBattery.setImageResource(R.mipmap.icon_equipment_capacity_4)
            s.toInt() > 51 -> ivBattery.setImageResource(R.mipmap.icon_equipment_capacity_3)
            s.toInt() > 34 -> ivBattery.setImageResource(R.mipmap.icon_equipment_capacity_2)
            s.toInt() > 17 -> ivBattery.setImageResource(R.mipmap.icon_equipment_capacity_1)
            s.toInt() > 0 -> ivBattery.setImageResource(R.mipmap.icon_equipment_capacity_0)
        }

    }

    private fun dealWithSignal(distance: String?) {
        when {
            distance?.toDouble()!! > -50.0 -> ivSignal.setImageResource(R.mipmap.icon_bluetooth_signal_4)
            distance.toDouble() > -70.0 -> ivSignal.setImageResource(R.mipmap.icon_bluetooth_signal_3)
            distance.toDouble() > -80.0 -> ivSignal.setImageResource(R.mipmap.icon_bluetooth_signal_2)
            distance.toDouble() > -90.0 -> ivSignal.setImageResource(R.mipmap.icon_bluetooth_signal_1)
        }
    }


    private fun initClickEvent() {
        llBell.setOnClickListener {
            if (instance!!.isConnect) {
                mainBack.setImageDrawable(resources.getDrawable(R.drawable.animation_search))
                val anim = mainBack.drawable as AnimationDrawable
                if (!NotNull.isNotNull(mThread) || !mThread!!.isAlive) {
                    mThread = Thread({
                        while (!Thread.currentThread().isInterrupted) {
                            SystemClock.sleep(1000)
                            instance?.write(BUZZER)
                        }
                    })
                    mThread?.start()
                    anim.start()
                } else {
                    mThread?.interrupt()
                    mThread = null
                    anim.stop()
                }
            }
        }
        llLocation.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.LONGITUDE, longitude)
            bundle.putString(Constant.LATITUDE, latitude)
            bundle.putString(Constant.ADDRESS, address)
            bundle.putInt(Constant.ADDRESS_TYPE, type)
            bundle.putString(Constant.CURRENT_AREA_RADUIS, "0")
            bundle.putString(Constant.CURRENT_AREA_NAME, "")
            showActivity(DeviceMapActivity::class.java, bundle)
        }
        // 设置按钮点击跳转
        titleRight?.setOnClickListener {
            val extras=Bundle()
            extras.putBoolean("IS_INSERT",isInsert)
            extras.putSerializable("device",name!!)
            showActivity(SettingActivity::class.java,extras)
        }
    }

    private fun getDeviceID(type: Int): Int {
        when (type) {
            0 -> return R.mipmap.icon_key_device_details
            1 -> return R.mipmap.icon_wallet_device_details
            2 -> return R.mipmap.icon_portable_computer_device_details
            3 -> return R.mipmap.icon_vice_card_phone_divice_details
            4 -> return R.mipmap.icon_other_device_details
        }
        return 0
    }
}

