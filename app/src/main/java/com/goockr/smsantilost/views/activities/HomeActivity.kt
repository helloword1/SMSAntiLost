package com.goockr.smsantilost.views.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.*
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.MAC
import com.goockr.smsantilost.utils.DateUtils.stringToLong
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.activities.antilost.AddActivity
import com.goockr.smsantilost.views.fragments.*
import com.jude.swipbackhelper.SwipeBackHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.msm_title_view.*
import kotlinx.android.synthetic.main.msm_title_view.view.*
import org.json.JSONObject
import java.util.*

class HomeActivity(override val contentView: Int = R.layout.activity_home) : BaseActivity()/*, BottomTabBar.OnSelectListener */ {
    private var homeFragment: MoreFragment? = null
    private var locationFragment: LocationFragment? = null
    private var antiLostFragment: AntiLostFragment? = null
    private var contactFragment: ContactFragment? = null
    private var msmFragment: MSMFragment? = null
    private var current: Fragment? = null
    private var isMsmAndContact = true
    private var isConnect = false
    private var deviceBeanDao: DeviceBeanDao? = null
    private var list: List<DeviceBean>? = null
    private var mDataList = ArrayList<BluetoothDevice>()
    private var mDevice: BluetoothDevice? = null
    //蓝牙
    var mBluetoothAdapter: BluetoothAdapter? = null
    private var btReceiver: MyBtReceiver? = null
    var mDatas: ArrayList<MsmBean> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE
                    ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    initMView()
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {

                }

            }).check()
        } else {
            initMView()
        }

    }

    fun initMView() {
        //初始化titlebar
        ll?.removeAllViews()
        val inflate1 = layoutInflater.inflate(R.layout.msm_title_view, null)
        inflate1.llMsm.setOnClickListener {
            if (msmFragment == null) {
                msmFragment = MSMFragment()
            }
            isMsmAndContact = false
            switchContent(msmFragment!!)
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.textColor))
            tvContact.setTextColor(ContextCompat.getColor(this, R.color.msmTextColorGray))
            tvMessageLine.visibility = View.VISIBLE
            tvContactLine.visibility = View.GONE
            current = msmFragment
        }
        inflate1.llContact.setOnClickListener {
            if (contactFragment == null) {
                contactFragment = ContactFragment()
            }
            isMsmAndContact = true
            switchContent(contactFragment!!)
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.msmTextColorGray))
            tvContact.setTextColor(ContextCompat.getColor(this, R.color.textColor))
            tvMessageLine.visibility = View.GONE
            tvContactLine.visibility = View.VISIBLE
            current = contactFragment
        }
        ll?.addView(inflate1)
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (msmFragment == null) {
            msmFragment = MSMFragment()
            beginTransaction.add(R.id.homeFrameLayout, msmFragment).commit()
            current = msmFragment
            isMsmAndContact = false
        }


        homeBottom.setTabbarCallbackListener {
            val inflate = layoutInflater.inflate(R.layout.base_title_view, null)
            title = inflate.findViewById(R.id.title)
            titleBack = inflate.findViewById(R.id.titleBack)
            titleAdd = inflate.findViewById(R.id.titleAdd)
            titleRight = inflate.findViewById(R.id.titleRight)
            when (it) {
                0 -> {
                    if (!isMsmAndContact) {
                        if (msmFragment == null) {
                            msmFragment = MSMFragment()
                        }
                        switchContent(msmFragment!!)
                    } else {
                        if (contactFragment == null) {
                            contactFragment = ContactFragment()
                        }
                        switchContent(contactFragment!!)
                    }

                    ll?.removeAllViews()
                    ll?.addView(inflate1)
                }
                1 -> {
                    ll?.removeAllViews()
                    ll?.addView(inflate)
                    titleBack?.visibility = View.GONE
                    titleAdd?.visibility = View.VISIBLE
                    titleRight?.visibility = View.GONE
                    if (antiLostFragment == null) {
                        antiLostFragment = AntiLostFragment()
                    }
                    title?.text = getString(R.string.natilost)
                    switchContent(antiLostFragment!!)
                    titleAdd?.setOnClickListener {
                        val intent = Intent()
                        intent.setClass(this, AddActivity::class.java)
                        startActivity(intent)
                    }
                }
                2 -> {
                    if (locationFragment == null) {
                        locationFragment = LocationFragment()
                    }
                    ll?.removeAllViews()
                    ll?.addView(inflate)
                    titleBack?.visibility = View.GONE
                    titleAdd?.visibility = View.GONE
                    titleRight?.visibility = View.GONE
                    title?.text = getString(R.string.location)
                    switchContent(locationFragment!!)
                }
                3 -> {
                    if (homeFragment == null) {
                        homeFragment = MoreFragment()
                    }
                    ll?.removeAllViews()
                    ll?.addView(inflate)
                    titleBack?.visibility = View.GONE
                    titleAdd?.visibility = View.GONE
                    title?.text = getString(R.string.more)
                    titleRight?.visibility = View.VISIBLE
                    switchContent(homeFragment!!)
                }
            }
        }
        startRecover()
    }

    override fun receive(intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (NotNull.isNotNull(list)) {
                list!!
                        .filter {
                            val mac = it.mac
                            TextUtils.equals(device.address, mac)
                        }
                        .forEach {
                            if (mDataList.isEmpty()) {
                                mDataList.add(device)
                                if (!instance!!.isConnect) {
                                    instance?.init(0, device, myHandler, this)
                                    Thread(instance).start()
                                }
                                this.mDevice = device
                            }
                        }
            }

        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
            //搜索完成
            if (NotNull.isNotNull(mBluetoothAdapter)) {
                mBluetoothAdapter?.startDiscovery()
            }
        }
    }


    override fun handleMyMessage(msg: Message?) {
        super.handleMyMessage(msg)
        when (msg?.what) {
            Constant.MSG_CONNECT_SUCCEED -> {
                LogUtils.d("", "" + msg.what)
                //重连提醒
                val value = preferences?.getStringValue(Constant.RECONNECT)
                if (NotNull.isNotNull(value)) {
                    if (value?.toBoolean()!!) {
                        overAlert()
                    }
                }
                instance?.write(Constant.MAC)
                //添加连接按钮到列表
                if (antiLostFragment == null) {
                    antiLostFragment = AntiLostFragment()
                }
                antiLostFragment?.setDevice(mDevice)
            }
            Constant.MSG_CLIENT_REV_NEW -> {
                val obj = msg.obj.toString()
                val potion = msg.arg1
                getBlueMsg(potion, obj)
            }
            Constant.MSG_CONNECT_DISCONNECT -> {
                //断开连接，刷新界面
                antiLostFragment?.disConnect()
                //越界提醒
                overAlert()
            }
        }
    }

    //处理短信
    private fun getBlueMsg(potion: Int, obj: String) {
        if (obj.startsWith("{\"ID\"") && obj.contains("Phone")) {
            instance?.write("ID_0")
            if (!obj.contains("}")) return
            val jsonObject = JSONObject(obj)
            val id = jsonObject.getString("ID")
            val name = jsonObject.getString("Phone")
            val date = jsonObject.getString("Data")
            val content = jsonObject.getString("Content")
//            instance?.write(id)

            val goockrApplication = application as GoockrApplication
            val contentBeanDao = goockrApplication.mDaoSession?.contentBeanDao
            val msmBeanDao = goockrApplication.mDaoSession?.msmBeanDao
            val same = msmBeanDao?.queryBuilder()?.where(MsmBeanDao.Properties.SmsTitle.eq(name))?.build()?.unique()
            //更新数据库
            if (NotNull.isNotNull(same)) {
                val list = contentBeanDao?.queryBuilder()?.where(ContentBeanDao.Properties.Date.eq(date))?.build()?.list()
                //根据时间过滤相同
                if (list!!.isEmpty()) {
                    val contentBean = ContentBean(null, date, content, true, true, 1, "", same?.id)
                    contentBean.setIsShowDate(stringEqDate(same?.contentBeans?.get((same.contentBeans?.size)!! - 1)?.date!!, date))
                    same.contentBeans?.add(contentBean)
                    contentBeanDao.insert(contentBean)
                    same.smsStr = content
                    same.isShow = true
                    msmBeanDao.update(same)
                }
            } else {
                //添加数据
                val msmBean = MsmBean(null, name, date, content, true, false)
                msmBeanDao?.insert(msmBean)
                val contentBean = ContentBean(null, date, content, true, true, 1, "", msmBean.id)
                contentBeanDao?.insert(contentBean)
            }

            //添加连接按钮到列表
            msmFragment?.setDates()
        } else if (obj.startsWith("MA_")) {
            instance?.write(Constant.DAT)
            //表示已经连接
            isConnect = true
        }
    }


    override fun onStop() {
        super.onStop()
        if (NotNull.isNotNull(btReceiver)) {
            unregisterReceiver(btReceiver)
            mBluetoothAdapter?.cancelDiscovery()
        }
        //停止音频
        if (NotNull.isNotNull(mediaPlayer)) {
            mediaPlayer?.stop()
        }
    }

    /**
     * 切换当前显示的fragment
     */
    private fun switchContent(to: Fragment) {
        if (current !== to) {
            val transaction = supportFragmentManager.beginTransaction()

            if (current != null) {
                transaction.hide(current)
            }
            if (!to.isAdded) { // 先判断是否被add过
                transaction.add(R.id.homeFrameLayout, to).commit()
            } else {
                transaction.show(to).commit() // 隐藏当前的fragment，显示下一个
            }
            current = to
        }
    }

    private var exitTime: Double = 0.toDouble()
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, getString(R.string.exitNext), Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis().toDouble()
            } else {
                // TODO 退出客户端
                // 退出
                (application as GoockrApplication).exit()
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    //第一次新建进来重连加载短信
    fun initThread() {
        startRecover()
    }

    /**
     * 开启搜索广播
     */
    private fun startRecover() {
        deviceBeanDao = goockrApplication?.mDaoSession?.deviceBeanDao
        list = deviceBeanDao?.queryBuilder()?.build()?.list()
        if (!NotNull.isNotNull(list) || list!!.isEmpty() || isConnect) return
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        //蓝牙已开启
        if (mBluetoothAdapter?.isEnabled!!) {
            val turnOnBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOnBtIntent, Constant.REQUEST_ENABLE_BT)
            val intentFilter = IntentFilter()
            btReceiver = MyBtReceiver()
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND)

            intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            registerReceiver(btReceiver, intentFilter)
            mBluetoothAdapter?.startDiscovery()
        }
    }

    private fun stringEqDate(oldeDate: String, newDate: String): Boolean {
        val oldLong = stringToLong(oldeDate, "yyyy-MM-dd_HH-mm-ss")
        val newLong = stringToLong(newDate, "yyyy-MM-dd_HH-mm-ss")
        return newLong - oldLong > 5000
    }

    //新增设备读取短信
    fun sendMac() {
        val connect = instance!!.isConnect
        if (connect) {
            instance?.setUiHandler(myHandler)
            instance?.write(MAC)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //结束音频
        if (NotNull.isNotNull(mediaPlayer)) {
            mediaPlayer?.stop()
        }
    }
}
