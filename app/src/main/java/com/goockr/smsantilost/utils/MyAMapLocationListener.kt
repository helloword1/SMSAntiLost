package com.goockr.smsantilost.utils

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener

/**
 * Created by LJN on 2017/12/25.
 */
class MyAMapLocationListener : AMapLocationListener {
    override fun onLocationChanged(p0: AMapLocation?) {
        onGetLocationListener.invoke(p0)
    }

    private lateinit var onGetLocationListener: (p0: AMapLocation?) -> Unit

    fun setLocationListener(listener: (p0: AMapLocation?) -> Unit) {
        this.onGetLocationListener = listener
    }
}