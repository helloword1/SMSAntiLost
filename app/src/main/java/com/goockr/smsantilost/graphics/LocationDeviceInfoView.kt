package com.goockr.smsantilost.graphics

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.DeviceBean
import cxx.utils.NotNull

/**
 * Created by LJN on 2018/1/2.
 */

class LocationDeviceInfoView : LinearLayout {
    private var device: DeviceBean? = null
    private var view: View? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    fun setDeviceBean(device: DeviceBean) {
        this.device = device
        if (NotNull.isNotNull(device)&&NotNull.isNotNull(view)) {
            view?.findViewById<TextView>(R.id.locationDeviceName)?.text = device.name
            view?.findViewById<TextView>(R.id.locationAddress)?.text = device.address
            view?.findViewById<TextView>(R.id.ivLocationDeviceTime)?.text = device.date
            view?.findViewById<ImageView>(R.id.ivLocationDevice)?.setImageResource(getBitmapInt(device.deviceType!!))
        }
    }

    private fun init() {
        this.orientation = VERTICAL
        view = LayoutInflater.from(context).inflate(R.layout.location_deivce, null)
        val locationRecord = view?.findViewById<LinearLayout>(R.id.locationRecord)

        locationRecord?.setOnClickListener {
            if (NotNull.isNotNull(listener))
                listener?.locationRecord()
        }
        view?.findViewById<LinearLayout>(R.id.locationGuide)?.setOnClickListener {
            if (NotNull.isNotNull(listener))
                listener?.locationGuide()
        }
        addView(view, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
    }

    private var listener: OnLocationControlsListener? = null

    interface OnLocationControlsListener {
        fun locationRecord()
        fun locationGuide()
    }

    fun setOnLocationControlsListenerListener(listener: OnLocationControlsListener) {
        this.listener = listener
    }

    private fun getBitmapInt(type: String): Int {
        return when (type) {
            "钥匙" -> R.mipmap.icon_key
            "钱包" -> R.mipmap.icon_wallet
            "笔记本" -> R.mipmap.icon_portable_computer
            "手机副卡" -> R.mipmap.icon_vice_card_phone
            "更多" -> R.mipmap.icon_other
            else -> R.mipmap.icon_key
        }
    }
}
