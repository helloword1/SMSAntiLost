package com.goockr.smsantilost.utils

import android.content.Context
import com.goockr.smsantilost.views.activities.BaseActivity
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Administrator on 2017/10/11.
 */

class SysInterceptor : Interceptor {
    private var context: Context? = null

    constructor(context: Context?) {
        this.context = context
        (context as BaseActivity).showProgressDialog()
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = request.url().newBuilder() // add common parameter
                .build()
        val build = request.newBuilder().url(httpUrl).build()
        return chain.proceed(build)
    }
}
