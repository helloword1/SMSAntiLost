package com.goockr.smsantilost.views.fragments

import android.content.Intent
import android.os.Bundle
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
    /**
     * 跳转到对应的Activity返回的时候可以接受到结果
     *
     * @param activityCls 对应的Activity.class
     * @param requestCode 请求码
     */
    fun <T> showActivityForResult(activityCls: Class<T>,
                                  requestCode: Int) {
        showActivityForResult(activityCls, null, requestCode)
    }

    fun <T> showActivityForResult(activityCls: Class<T>,
                                  extras: Bundle?, requestCode: Int) {
        val intent = Intent(activity, activityCls)
        if (null == extras) {
            startActivityForResult(intent, requestCode)
        } else {
            intent.putExtras(extras)
            startActivityForResult(intent, requestCode)
        }
    }
    /**
     * 跳转到对应的Activity
     */
    fun <T> showActivity(activityCls: Class<T>) {
        showActivity(activityCls, null)
    }

    fun <T> showActivity(activityCls: Class<T>, extras: Bundle?) {
        val intent = Intent(activity, activityCls)
        if (null == extras) {
            startActivity(intent)
        } else {
            intent.putExtras(extras)
            startActivity(intent)
        }
    }
}
