package com.goockr.smsantilost.views.activities.antilost

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.ClsUtils
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.DeviceAdapter
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_add_device.*
import kotlinx.android.synthetic.main.page1_add_device.*
import kotlinx.android.synthetic.main.page2_add_device.*
import kotlinx.android.synthetic.main.page3_add_device.*


/**
 * 添加设备页面
 */
class AddDeviceActivity(override val contentView: Int = R.layout.activity_add_device) : BaseActivity() {

    private var mHandler = Handler()
    private var mDataList = ArrayList<BluetoothDevice>()
    // 三个页面模式
    private val PAGE1 = 1
    private val PAGE2 = 2
    private val PAGE3 = 3
    private var mCurrentPage = 1
    // 动画
    var mScaleAnimation: ScaleAnimation? = null
    var mRotationAnimation: RotateAnimation? = null
    var mBluetoothAdapter: BluetoothAdapter? = null
    private var btReceiver: MyBtReceiver? = null
    private var adapter: DeviceAdapter? = null
    private var bluetoothDevice: BluetoothDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initAnimation()
        initMData()
        initClickEvent()
    }

    private fun initAnimation() {
        mScaleAnimation = ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        mScaleAnimation?.duration = 1000
        mScaleAnimation?.repeatMode = Animation.REVERSE
        mScaleAnimation?.repeatCount = -1
        iv_InSearch.animation = mScaleAnimation


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

        title?.text = "添加设备"
        ll?.addView(titleLayout)
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
            val turnOnBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOnBtIntent, Constant.REQUEST_ENABLE_BT)
            val intentFilter = IntentFilter()
            btReceiver = MyBtReceiver()
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
            intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            registerReceiver(btReceiver, intentFilter)
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

    /**
     * 用户打开蓝牙后，显示已绑定的设备列表
     */
    private fun showBondDevice() {
        mDataList.clear()
        val tmp = mBluetoothAdapter?.bondedDevices
        for (d in tmp!!) {
            mDataList.add(d)
        }
        adapter?.notifyDataSetChanged()
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // listView
        lv_Device.setOnItemClickListener { parent, view, position, id ->

            ll_page1.visibility = View.GONE
            ll_page2.visibility = View.VISIBLE
            mCurrentPage = 2
            mRotationAnimation?.start()
            // 用RxAndroid模拟绑定（返回值到时再修改）
            bluetoothDevice = mDataList[position]
            ClsUtils.createBond(bluetoothDevice!!::class.java, mDataList[position])

        }
        // 返回键
        titleBack?.setOnClickListener {
            when (mCurrentPage) {
                PAGE1 -> {
                    finish()
                }
                PAGE2 -> {
                    showPage1()
                    mHandler.removeCallbacksAndMessages(null)
                }
                PAGE3 -> {
                    showPage2()
                }
            }
        }
        // 选择绑定的类型
        ll_Key.setOnClickListener {
            finish()
        }
        ll_Wallet.setOnClickListener {
            finish()
        }
        ll_Pc.setOnClickListener {
            finish()
        }
        ll_ViceCard.setOnClickListener {
            finish()
        }
        ll_Other.setOnClickListener {
            finish()
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
    }

    override fun onDestroy() {
        super.onDestroy()
        if (NotNull.isNotNull(btReceiver)) {
            unregisterReceiver(btReceiver)
            mBluetoothAdapter?.cancelDiscovery()
        }
    }

    /**
     * 广播接受器
     */
    private inner class MyBtReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                //开始搜索
                mScaleAnimation?.start()
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                //toast("搜索结束")
                mScaleAnimation?.cancel()
                tvBlueTooth.text = "搜索完成"
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (isNewDevice(device)) {
                    mDataList.add(device)
                    adapter?.notifyDataSetChanged()
                }
            } else if (action == BluetoothDevice.ACTION_PAIRING_REQUEST)
            //再次得到的action，会等于PAIRING_REQUEST
            {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device !== bluetoothDevice) return//如果不是当前蓝牙 退出
                try {
                    //1.确认配对
//                    ClsUtils.setPairingConfirmation(bluetoothDevice!!::class.java, bluetoothDevice!!, true)
//                    //2.终止有序广播
                    abortBroadcast()//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    val pin = "0000"
                    ClsUtils.setPin(bluetoothDevice!!::class.java, bluetoothDevice!!, pin)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when (device.bondState) {
                    BluetoothDevice.BOND_BONDING ->
                        Log.d("BlueToothTestActivity", "正在配对......")
                    BluetoothDevice.BOND_BONDED -> {
                        showPage3()
                        Log.d("BlueToothTestActivity", "完成配对")
                    }
                    BluetoothDevice.BOND_NONE -> {
                        showPage1()
                    }

                }
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
        return device.bondState != BluetoothDevice.BOND_BONDED && !repeatFlag
    }
}
