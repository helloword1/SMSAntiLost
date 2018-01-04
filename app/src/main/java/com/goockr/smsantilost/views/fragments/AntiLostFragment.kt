package com.goockr.smsantilost.views.fragments

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AntilostBean
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.DateUtils
import com.goockr.smsantilost.views.activities.antilost.KeyActivity
import com.goockr.smsantilost.views.adapters.AntilostAdapter
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.fragment_antilost.*


/**
 * Created by ning.wen on 2016/11/1.
 *防丢页面
 */
class AntiLostFragment : BaseFragment() {
    private var lists: ArrayList<AntilostBean>? = ArrayList()
    private var listsAdapter: AntilostAdapter? = null
    private var device: BluetoothDevice? = null
    private var isConnect = false
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //占位置用
        return setContentView(R.layout.fragment_antilost)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emptyView = layoutInflater.inflate(R.layout.anti_empty_view, null)
        emptyView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)//设置LayoutParams
        (antilost_list_view.parent as ViewGroup).addView(emptyView)//添加到当前的View hierarchy
        antilost_list_view.emptyView = emptyView
    }

    override fun onResume() {
        super.onResume()
        lists?.clear()
        initData()
        initMView()
        initClickEvent()
    }

    /**
     * 初始化listView数据
     */
    private fun initData() {
        val goockrApplication = activity.application as GoockrApplication
        val deviceBeanDao = goockrApplication.mDaoSession?.deviceBeanDao
        val list = deviceBeanDao?.queryBuilder()?.build()?.list()
        if (NotNull.isNotNull(list))
            for (c in list!!) {
                var id = 0
                var now = ""
                val key = c.deviceType
                val mac = c.mac
                val distance = c.distance
                val date = c.date
                val deviceName = c.name
                when (key) {
                    getString(R.string.key) -> id = 0
                    getString(R.string.wallet) -> id = 1
                    getString(R.string.computor) -> id = 2
                    getString(R.string.secondCard) -> id = 3
                    getString(R.string.other) -> id = 4
                }
                if (TextUtils.equals(DateUtils.getDate(DateUtils.parsePatterns[2]), date)) {
                    now = "刚刚"
                    isConnect = true
                } else {
                    now = date
                }
                val antilostBean = AntilostBean(id, key, now, isConnect, getString(R.string.hadConnect))
                antilostBean.mac = mac
                antilostBean.deviceName = deviceName
                antilostBean.distance = distance
                antilostBean.longitude = c.longitude
                antilostBean.latitude = c.latitude
                antilostBean.address = c.address
                if (TextUtils.equals(mac, device?.address)) {
                    for (c in 0 until lists!!.size) {
                        lists!![c].isConnectState = false
                    }
                    antilostBean.isConnectState = true
                }
                lists?.add(antilostBean)
            }

    }

    /**
     * 初始化View
     */
    private fun initMView() {
        listsAdapter = AntilostAdapter(lists, context)
        antilost_list_view.adapter = listsAdapter
    }

    /**
     * listView点击事件
     */
    private fun initClickEvent() {
        antilost_list_view.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            //icon_vice_card_phone_divice_details
            val bundle = Bundle()
            val antilostBean = lists!![position]
            bundle.putSerializable("device", antilostBean)
            bundle.putInt(Constant.ADDRESS_TYPE, antilostBean.drawableId)
            showActivity(KeyActivity::class.java, bundle)
        }
    }



    fun setDevice(device: BluetoothDevice?) {
        this.device = device
        if (!lists!!.isEmpty()) {
            for (i in 0 until lists!!.size) {
                if (TextUtils.equals(lists!![i].mac, device?.address)) {
                    lists!![i].isConnectState = true
                    listsAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    fun disConnect() {
        for (c in 0 until lists!!.size) {
            lists!![c].isConnectState = false
        }
        listsAdapter?.notifyDataSetChanged()
    }
}

