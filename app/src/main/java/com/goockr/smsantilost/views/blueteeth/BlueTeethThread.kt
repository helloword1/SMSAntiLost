package com.goockr.smsantilost.views.blueteeth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant.MSG_CLIENT_REV_NEW
import com.goockr.smsantilost.utils.Constant.MSG_CONNECT_SUCCEED
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.concurrent.thread

/**
 * @author Administrator
 * @date 2017/12/4
 */

class BlueTeethThread private constructor() : Runnable {
    private var socket: BluetoothSocket? = null
    private var out: OutputStream? = null
    private var device: BluetoothDevice? = null
    private var uiHandler: Handler? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var activity: BaseActivity? = null
    private var potion= -1

    private fun init() {
        var tmp: BluetoothSocket? = null
        try {
            tmp = device?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        socket = tmp
    }

    companion object {
        @Volatile
        private var instance: BlueTeethThread? = null
        fun getBInstance(): BlueTeethThread {
            if (instance == null) {
                synchronized(BlueTeethThread::class) {
                    if (instance == null) {
                        instance = BlueTeethThread()
                    }
                }
            }
            return instance!!
        }
    }

    fun initBlueTeeth(potion:Int,device: BluetoothDevice, uiHandler: Handler,
                      bluetoothAdapter: BluetoothAdapter, activity: BaseActivity) {
        this.device = device
        this.uiHandler = uiHandler
        this.bluetoothAdapter = bluetoothAdapter
        this.activity = activity
        this.potion = potion
        init()
    }

    override fun run() {
        if (bluetoothAdapter?.isDiscovering!!) {
            bluetoothAdapter?.cancelDiscovery()
        }
        try {
            socket?.connect()
            out = socket?.outputStream
            val `in` = socket?.inputStream
            if (!NotNull.isNotNull(socket)) return
            if (socket!!.isConnected) {
                Handler(Looper.getMainLooper()).post {
                    val message = Message()
                    message.what = MSG_CONNECT_SUCCEED
                    uiHandler?.sendMessage(message)
                }
            }
            thread {
                val bytes = ByteArray(1024)
                var buffer = ""
                var len=0;
                try {
                    while (`in`!!.read()!=-1) {
                        len=`in`.read(bytes)
                        buffer=String(bytes,0,len)
                        val message = Message()
                        message.what = MSG_CLIENT_REV_NEW
                        message.arg1 = potion
                        message.obj = buffer
                        uiHandler?.sendMessage(message)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        } catch (e: IOException) {
            Handler(Looper.getMainLooper()).post {
                MyToast.showToastCustomerStyleText(activity!!, activity?.getString(R.string.connectFail)!!)
            }
            e.printStackTrace()
        }

    }

    fun sendData(data: String) {
        thread {
            write(data)
        }
    }

    private fun write(data: String) {
        try {
            if (out != null) {
                out?.write(data.toByteArray(charset("utf-8")))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
