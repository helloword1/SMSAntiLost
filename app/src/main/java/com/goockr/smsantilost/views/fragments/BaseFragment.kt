package com.goockr.smsantilost.views.fragments

import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup


/**
 * Created by ning.wen on 2016/4/7.
 */
abstract class BaseFragment : Fragment() {
    private var container: ViewGroup? = null
    fun setContentView(resourceId: Int): View {
        return layoutInflater.inflate(resourceId, container, false)
    }
}
