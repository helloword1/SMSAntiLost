package com.goockr.smsantilost.views.blueteeth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Message
import android.util.Log
import com.goockr.smsantilost.utils.Constant
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * Created by Administrator on 2017/4/4.
 */

class ClientThread(internal var bluetoothAdapter: BluetoothAdapter, internal var device: BluetoothDevice,
                   internal var uiHandler: Handler) : Runnable {

    internal val TAG = "ClientThread"
    internal var writeHandler: Handler? = null

    internal var socket: BluetoothSocket? = null
    internal var out: OutputStream? = null
    internal lateinit var `in`: InputStream
    internal var reader: BufferedReader? = null


    init {

        var tmp: BluetoothSocket? = null
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Constant.UUID))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        socket = tmp
    }

    override fun run() {
        Log.e(TAG, "----------------- do client thread run()")
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        try {
            socket!!.connect()
            out = socket!!.outputStream
            `in` = socket!!.inputStream
            Thread(Runnable {
                Log.e(TAG, "-----------do client read run()")

                val buffer = ByteArray(1024)
                var len: Int
                var content: String
                try {
                    while (`in`.read(buffer) != -1) {
                        len=`in`.read(buffer)
                        content = String(buffer, 0, len)
                        val message = Message()
                        message.what = Constant.MSG_CLIENT_REV_NEW
                        message.obj = content
                        uiHandler.sendMessage(message)
                        Log.e(TAG, "------------- client read data in while ,send msg ui" + content)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()

            //            Looper.prepare();
            //            writeHandler = new Handler() {
            //                @Override
            //                public void handleMessage(Message msg) {
            //                    switch (msg.what) {
            //                        case Params.MSG_CLIENT_WRITE_NEW:
            //                            String data = msg.obj.toString() + "\n";
            //                            try {
            //                                out.write(data.getBytes("utf-8"));
            //                                Log.e(TAG, "-------------client write data " + data);
            //                            } catch (IOException e) {
            //                                e.printStackTrace();
            //                            }
            //                            break;
            //
            //                    }
            //                }
            //            };
            //            Looper.loop();

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "-------------- exception")
        }

    }


    fun write(data: String) {
        //        data = data+"\r\n";
        try {
            if (out != null) {
                out!!.write(data.toByteArray(charset("utf-8")))
            }
            Log.e(TAG, "---------- write data ok " + data)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
