package com.goockr.smsantilost.utils

import org.json.JSONObject

/**
 * Created by lijinning on 2018/1/26.
 */
object SomethingUtils {
    fun getResult(json: String): JSONObject {
        if (json.contains("result") && json.contains("msg")) {
            val jsonOb = JSONObject(json)
            val result = jsonOb.getString("result")
            val msg = jsonOb.getString("msg")
            return jsonOb
        }else{
            return JSONObject()
        }
    }
}