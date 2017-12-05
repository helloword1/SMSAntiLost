package com.goockr.smsantilost.https

import com.zhy.http.okhttp.callback.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Created by LJN on 2017/11/29.
 */
abstract class MyStringCallback : Callback<String>(){
    @Throws(IOException::class)
    override fun parseNetworkResponse(response: Response, id: Int): String {
        return response.body().string()
    }
}