package com.goockr.smsantilost.utils

import android.util.Log

import com.goockr.smsantilost.GoockrApplication

object LogUtils {
    val isDebug = GoockrApplication.isDebug
    private val LOG_LENGTH = 30000 // 分段打印Log
    var LOG_LEVEL = 6
    var ERROR = 1
    var WARN = 2
    var INFO = 3
    var DEBUG = 4
    var VERBOS = 5

    fun d(classes: Class<*>, msg: Any) {
        if (isDebug) {
            if (msg.toString().length < LOG_LENGTH) {
                Log.d(classes.simpleName, msg.toString())
            } else {
                // msg过长
                val str1 = msg.toString().substring(0, LOG_LENGTH)
                Log.d(classes.simpleName, str1)
                d(classes, msg.toString().substring(LOG_LENGTH))
            }
        }
    }

    fun i(classes: Class<*>, msg: Any) {
        if (isDebug) {
            if (msg.toString().length < LOG_LENGTH) {
                Log.i(classes.simpleName, msg.toString())
            } else {
                // msg过长
                val str1 = msg.toString().substring(0, LOG_LENGTH)
                Log.i(classes.simpleName, str1)
                i(classes, msg.toString().substring(LOG_LENGTH))
            }
        }
    }

    fun v(classes: Class<*>, msg: Any) {
        if (isDebug) {
            if (msg.toString().length < LOG_LENGTH) {
                Log.v(classes.simpleName, msg.toString())
            } else {
                // msg过长
                val str1 = msg.toString().substring(0, LOG_LENGTH)
                Log.v(classes.simpleName, str1)
                v(classes, msg.toString().substring(LOG_LENGTH))
            }
        }
    }

    fun e(classes: Class<*>, msg: Any) {
        if (isDebug) {
            if (msg.toString().length < LOG_LENGTH) {
                Log.e(classes.simpleName, msg.toString())
            } else {
                // msg过长
                val str1 = msg.toString().substring(0, LOG_LENGTH)
                Log.e(classes.simpleName, str1)
                e(classes, msg.toString().substring(LOG_LENGTH))
            }
        }
    }

    fun w(classes: Class<*>, msg: Any) {
        if (isDebug) {
            if (msg.toString().length < LOG_LENGTH) {
                Log.w(classes.simpleName, msg.toString())
            } else {
                // msg过长
                val str1 = msg.toString().substring(0, LOG_LENGTH)
                Log.w(classes.simpleName, str1)
                w(classes, msg.toString().substring(LOG_LENGTH))
            }
        }
    }

    fun e(TAG: String, msg: String) {
        if (LOG_LEVEL > ERROR) {
            Log.e(TAG, msg)
        }
    }

    fun e(TAG: String, msg: String, e: Exception) {
        if (LOG_LEVEL > ERROR) {
            Log.e(TAG, msg, e)
        }
    }

    fun w(TAG: String, msg: String) {
        if (LOG_LEVEL > WARN) {
            Log.w(TAG, msg)
        }
    }

    fun i(TAG: String, msg: String) {
        if (LOG_LEVEL > INFO) {
            Log.i(TAG, msg)
        }
    }

    fun l(tag: String, content: String) {
        val p = 2000
        val length = content.length.toLong()
        if (length < p || length == p.toLong()) {
            Log.e(tag, content)
        } else {
            Log.i(tag, content.substring(0, content.length / 2))
            Log.i(tag, content.substring(content.length / 2, content.length))
        }
    }

    fun d(TAG: String, msg: String) {
        if (LOG_LEVEL > DEBUG) {
            Log.d(TAG, msg)
        }
    }

    fun v(TAG: String, msg: String) {
        if (LOG_LEVEL > VERBOS) {
            Log.v(TAG, msg)
        }
    }
}

