package com.goockr.smsantilost.views.activities.antilost

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.DeviceBean
import com.goockr.smsantilost.entries.DeviceBeanDao
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.*
import com.goockr.smsantilost.utils.Constant.MAC
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.DeviceAdapter
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_add_device.*
import kotlinx.android.synthetic.main.page1_add_device.*
import kotlinx.android.synthetic.main.page2_add_device.*
import kotlinx.android.synthetic.main.page3_add_device.*
import java.lang.Exception
import java.util.*


/**
 * 添加设备页面
 */
class AddDeviceActivity(override val contentView: Int = R.layout.activity_add_device) : BaseActivity() {

    private var mDataList = ArrayList<BluetoothDevice>()
    private var shortList = ArrayList<Short>()
    // 三个页面模式
    private val PAGE1 = 1
    private val PAGE2 = 2
    private val PAGE3 = 3
    private var isConnect = false
    private var mCurrentPage = 1
    private var mLocationClient: AMapLocationClient? = null
    private var longitude = "0.0"
    private var latitude = "0.0"
    private var address = ""
    //蓝牙
    var mBluetoothAdapter: BluetoothAdapter? = null
//    private var btReceiver: MyBtReceiver? = null
    // 动画
    var mRotationAnimation: RotateAnimation? = null
    private var adapter: DeviceAdapter? = null
    private var bluetoothDevice: BluetoothDevice? = null
    private var currentPotion = -1
    override fun initView() {
        initMView()
        initAnimation()
        initMData()
        initClickEvent()
    }

    private fun initAnimation() {
        mRotationAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        mRotationAnimation?.duration = 2000
        mRotationAnimation?.repeatCount = -1
        mRotationAnimation?.interpolator = LinearInterpolator()
        iv_BindingWait.animation = mRotationAnimation
    }


    /**
     * 初始化title
     */
    @SuppressLint("InflateParams")
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleRight = titleLayout.findViewById(R.id.titleRight)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        title?.text = getString(R.string.addDiveceTitle)
        ll?.addView(titleLayout)

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
        mLocationClient?.startLocation()
    }

    private fun parseData(it: AMapLocation) {
        latitude = it.latitude.toString()
        longitude = it.longitude.toString()
        address = it.address
        mLocationClient?.stopLocation()
    }

    override fun handleMyMessage(msg: Message?) {
        when (msg?.what) {
            Constant.MSG_CONNECT_SUCCEED -> {
                LogUtils.d("", "" + msg.what)
                instance?.write(MAC)
            }

            Constant.MSG_CLIENT_REV_NEW -> {
                val obj = msg.obj.toString()
                getBlueMsg(msg.arg1, obj)
            }
            Constant.MSG_CONNECT_FAIL -> {
                if (isConnect) return
                ToastUtils.showShort(this, "连接失败")
                showPage1()
                mRotationAnimation?.cancel()
            }
        }
    }

    /**
     * 初始化数据
     */
    private fun initMData() {
        adapter = DeviceAdapter(mDataList, this@AddDeviceActivity)
        lv_Device.adapter = adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        // 蓝牙已开启
        if (mBluetoothAdapter?.isEnabled!!) {
            /*val turnOnBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOnBtIntent, Constant.REQUEST_ENABLE_BT)
            val intentFilter = IntentFilter()
            btReceiver = MyBtReceiver()
//            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
//            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
            intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            registerReceiver(btReceiver, intentFilter)*/
            mBluetoothAdapter?.startDiscovery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constant.REQUEST_ENABLE_BT -> {
                if (resultCode == RESULT_OK) {
//                    showBondDevice()
                }
            }
        //设置蓝牙可见
            Constant.REQUEST_ENABLE_VISIBILITY -> {
                if (resultCode == 600) {//蓝牙已设置可见
                } else if (resultCode == RESULT_CANCELED) {//蓝牙设置可见失败,请重试
                }
            }
        }
    }

    private fun getBlueMsg(potion: Int, msg: String) {
        currentPotion = potion
        when {
            msg.startsWith("MA_") -> {
                showPage3()
                isConnect = true
            }

        }

    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        lv_Device.setOnItemClickListener { parent, view, position, id ->
            //            showProgressDialog(getString(R.string.binding))
            showPage2()
            mRotationAnimation?.start()
            // 用RxAndroid模拟绑定（返回值到时再修改）
            bluetoothDevice = mDataList[position]
            val remoteDevice = BluetoothAdapter.getDefaultAdapter()?.getRemoteDevice(bluetoothDevice?.address)
            if (NotNull.isNotNull(remoteDevice)) {
//                instance = ClientThread(position, remoteDevice!!,myHandler, this)
//                instance = ClientThread()
                if (NotNull.isNotNull(instance) && instance!!.isConnect) {
                    instance?.disConnect()
                }
                instance?.init(position, remoteDevice!!, myHandler, this)
                Thread(instance).start()
            }
        }
        // 返回键
        titleBack?.setOnClickListener {
            when (mCurrentPage) {
                PAGE1 -> {
                    finish()
                }
                PAGE2 -> {
                    showPage1()
                }
                PAGE3 -> {
                    showPage2()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient?.stopLocation()
        mBluetoothAdapter?.cancelDiscovery()
        if (btReceiver!=null){
            try {
                unregisterReceiver(btReceiver)
            }catch (e : Exception){}
        }
    }

    /**
     * 重写返回键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            when (mCurrentPage) {
                3 -> showPage2()
                2 -> showPage1()
                1 -> finish()
            }
        }
        return true
    }

    private fun showPage1() {
        ll_page3.visibility = View.GONE
        ll_page2.visibility = View.GONE
        ll_page1.visibility = View.VISIBLE
        mCurrentPage = 1
    }

    private fun showPage2() {
        ll_page3.visibility = View.GONE
        ll_page2.visibility = View.VISIBLE
        ll_page1.visibility = View.GONE
        mCurrentPage = 2
    }

    private fun showPage3() {
        ll_page3.visibility = View.VISIBLE
        ll_page2.visibility = View.GONE
        ll_page1.visibility = View.GONE
        mCurrentPage = 3
        //钥匙
        ll_Key.setOnClickListener {
            saveData("钥匙")
        }
        //钱包
        ll_Wallet.setOnClickListener {
            saveData("钱包")

        }//笔记本
        ll_Pc.setOnClickListener {
            saveData("笔记本")

        }//ll_ViceCard
        ll_ViceCard.setOnClickListener {
            saveData("手机副卡")

        }//其他
        ll_Other.setOnClickListener {
            saveData("其他")
        }
    }


    private fun saveData(key: String) {
        if (currentPotion != -1) {
            val deviceBeanDao = goockrApplication?.mDaoSession?.deviceBeanDao
            val deviceBean = deviceBeanDao?.queryBuilder()?.where(DeviceBeanDao.Properties.
                    Mac.eq(mDataList[currentPotion].address))?.unique()
            if (!NotNull.isNotNull(deviceBean)) {
                deviceBeanDao?.insert(DeviceBean(null, key, mDataList[currentPotion].name,
                        mDataList[currentPotion].name, mDataList[currentPotion].address
                        , shortList[currentPotion].toString(), DateUtils.getDate(DateUtils.parsePatterns[2]),longitude.toDouble(),latitude.toDouble(),address))
            }else{
                deviceBean?.name=mDataList[currentPotion].name
                deviceBean?.date=DateUtils.getDate(DateUtils.parsePatterns[2])
                deviceBean?.distance=shortList[currentPotion].toString()
                deviceBean?.deviceType=key
                deviceBean?.latitude=latitude.toDouble()
                deviceBean?.longitude=longitude.toDouble()
                deviceBean?.address=address
                deviceBeanDao?.update(deviceBean)
                MyToast.showToastCustomerStyleText(this,getString(R.string.deviceHasAdd))
            }
            goockrApplication?.exitToHome()
        }
    }

    override fun receive(intent: Intent) {
        super.receive(intent)
        val action = intent.action
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
            //开始搜索
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
            //toast("搜索结束")
            tvBlueTooth.text = getString(R.string.searchComplete)
        } else if (BluetoothDevice.ACTION_FOUND == action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (isNewDevice(device)) {
                shortList.add(intent.extras.getShort(BluetoothDevice.EXTRA_RSSI))
                mDataList.add(device)
                adapter?.notifyDataSetChanged()
            }
        }

    }

    /**
     * 判断搜索的设备是新蓝牙设备，且不重复
     * @param device
     * @return
     */
    private fun isNewDevice(device: BluetoothDevice): Boolean {
        val repeatFlag = mDataList.any { it.address == device.address }
        //不是已绑定状态，且列表中不重复
        return !repeatFlag
    }

    override fun onStop() {
        super.onStop()
        mLocationClient?.stopLocation()
        iv_InSearch.stopAnimator()
    }
    override fun onPause() {
        super.onPause()
        iv_InSearch.stopAnimator()
    }

    override fun onResume() {
        super.onResume()
        iv_InSearch.startAnimator()
    }
}
