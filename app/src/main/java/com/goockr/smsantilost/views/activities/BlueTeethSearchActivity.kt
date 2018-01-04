package com.goockr.smsantilost.views.activities


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.adapters.BlueTeethListAdapter
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_blueteeth_search.*
import java.util.*

class BlueTeethSearchActivity(override val contentView: Int = R.layout.activity_blueteeth_search) : BaseActivity() {
    private var mDatas: MutableList<BluetoothDevice> = ArrayList()
    var bluetoothAdapter: BlueTeethListAdapter? = null
    var mBluetoothAdapter: BluetoothAdapter? = null
//    private var btReceiver: MyBtReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        // 蓝牙已开启
        if (mBluetoothAdapter?.isEnabled!!) {
            /*val turnOnBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOnBtIntent, Constant.REQUEST_ENABLE_BT)
            var intentFilter = IntentFilter()
            btReceiver = MyBtReceiver()
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
            registerReceiver(btReceiver, intentFilter)*/
            showBondDevice()
            // 默认开启服务线程监听
//            if (serverThread != null) {
//                serverThread.cancel()
//            }
//            Log.e("", "-------------- new server thread")
//            serverThread = ServerThread(bluetoothAdapter, uiHandler)
//            Thread(serverThread).start()
        }
    }

    override fun initView() {
        initRefresh()
        recycleView.layoutManager = LinearLayoutManager(this)
        bluetoothAdapter = BlueTeethListAdapter(this, mDatas)
        recycleView.adapter = bluetoothAdapter
        bluetoothAdapter?.setoOnGetAdapterListener {
            var get = mDatas[it]
        }
    }

    private fun initRefresh() {
        //改变加载显示的颜色
        refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.parseColor("#4285f4"))
        //设置初始时的大小
        refresh.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL)
        //设置监听
        refresh.setOnRefreshListener {
            if (mBluetoothAdapter?.isDiscovering()!!) {
                mBluetoothAdapter?.cancelDiscovery()
            }
            mDatas.clear()
            mBluetoothAdapter?.startDiscovery()
        }
        //设置向下拉多少出现刷新
        refresh.setDistanceToTriggerSync(100)
        //设置刷新出现的位置
        refresh.setProgressViewEndTarget(false, 200)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (NotNull.isNotNull(btReceiver)) {
            unregisterReceiver(btReceiver)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constant.REQUEST_ENABLE_BT -> {
                if (resultCode == RESULT_OK) {
                    showBondDevice()
                }
            }
        //设置蓝牙可见
            Constant.REQUEST_ENABLE_VISIBILITY -> {
                if (resultCode == 600) {
//                    toast("蓝牙已设置可见")
                } else if (resultCode == RESULT_CANCELED) {
//                    toast("蓝牙设置可见失败,请重试")
                }
            }
        }
    }

    /**
     * 广播接受器
     */
    private inner class MyBtReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
//                toast("开始搜索 ...")
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
//                toast("搜索结束")
                refresh.isRefreshing = false
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (isNewDevice(device)) {
                    mDatas.add(device)
                    bluetoothAdapter?.notifyDataSetChanged()
                    LogUtils.e("", "---------------- " + device.name)
                }
            }
        }
    }

    /**
     * 用户打开蓝牙后，显示已绑定的设备列表
     */
    private fun showBondDevice() {
        mDatas.clear()
        val tmp = mBluetoothAdapter?.bondedDevices
        for (d in tmp!!) {
            mDatas.add(d)
        }
        bluetoothAdapter?.notifyDataSetChanged()
    }

    /**
     * 判断搜索的设备是新蓝牙设备，且不重复
     * @param device
     * @return
     */
    private fun isNewDevice(device: BluetoothDevice): Boolean {
        val repeatFlag = mDatas.any { it.address == device.address }
        //不是已绑定状态，且列表中不重复
        return device.bondState != BluetoothDevice.BOND_BONDED && !repeatFlag
    }
}
