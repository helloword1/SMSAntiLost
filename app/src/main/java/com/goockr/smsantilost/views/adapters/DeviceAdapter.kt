package com.goockr.smsantilost.views.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.goockr.smsantilost.R

import cxx.utils.NotNull

/**
 * Created by tanzhihao on 2017/11/20.
 */

class DeviceAdapter(private val mList: List<BluetoothDevice>, private val mContext: Context) : BaseAdapter() {
    private val mInflater: LayoutInflater
    init {
        mInflater = LayoutInflater.from(mContext)
    }
    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: AntilostHolder? = null
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_device, parent, false)
            holder = AntilostHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as AntilostHolder
        }
        var name = mList[position].name
        if (!NotNull.isNotNull(name)) {
            name = mContext.getString(R.string.disName)
        }
        holder.mDeviceName.text = name
        return convertView
    }

    internal inner class AntilostHolder(view: View) {
        // 图标
        var mDeviceIcon: ImageView
        // 钥匙
        var mDeviceName: TextView
        // 型号强弱
        var mDeviceSignal: ImageView

        init {
            mDeviceIcon = view.findViewById(R.id.iv_DeviceIcon)
            mDeviceName = view.findViewById(R.id.tv_DeviceName)
            mDeviceSignal = view.findViewById(R.id.iv_DeviceSignal)
        }
    }
}
