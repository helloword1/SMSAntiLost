package com.goockr.smsantilost.views.blueteeth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
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

class ServerThread(internal var bluetoothAdapter: BluetoothAdapter, internal var uiHandler: Handler) : Runnable {

    internal val TAG = "ServerThread"
    internal var serverSocket: BluetoothServerSocket? = null
    internal var socket: BluetoothSocket? = null
    internal var writeHandler: Handler? = null

    internal var out: OutputStream? = null
    internal lateinit var `in`: InputStream
    internal var reader: BufferedReader? = null

    internal var acceptFlag = true

    init {
        var tmp: BluetoothServerSocket? = null
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(Constant.NAME, UUID.fromString(Constant.UUID))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        serverSocket = tmp
        Log.e(TAG, "-------------- do new()")
    }

    override fun run() {
        Log.e(TAG, "-------------- do run()")
        try {
            while (acceptFlag) {
                socket = serverSocket!!.accept()

                // 阻塞，直到有客户端连接
                if (socket != null) {
                    Log.e(TAG, "-------------- socket not null, get a client")
                    out = socket!!.outputStream
                    `in` = socket!!.inputStream
                    //reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));

                    val remoteDevice = socket!!.remoteDevice
                    val message = Message()
                    message.what = Constant.MSG_REV_A_CLIENT
                    message.obj = remoteDevice
                    uiHandler.sendMessage(message)

                    // 读取服务器 socket 数据
                    Thread(Runnable {
                        Log.e(TAG, "-----------do server read run()")

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
                                Log.e(TAG, "------------- server read data in while ,send msg ui" + content)
                            }

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }).start()
                    //                    Looper.prepare();
                    //                    writeHandler = new Handler() {
                    //                        @Override
                    //                        public void handleMessage(Message msg) {
                    //                            switch (msg.what) {
                    //                                case Params.MSG_SERVER_WRITE_NEW:
                    //                                    String data = msg.obj.toString() + "\n";
                    //                                    try {
                    //                                        out.write(data.getBytes("utf-8"));
                    //                                        Log.e(TAG, "-------------server write data " + data);
                    //                                    } catch (IOException e) {
                    //                                        e.printStackTrace();
                    //                                    }
                    //                                    break;
                    //                            }
                    //                        }
                    //                    };
                    //                    Looper.loop();
                    break
                }
            }// end while(true)
        } catch (e: IOException) {
            e.printStackTrace()
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

    fun cancel() {
        try {
            acceptFlag = false
            serverSocket!!.close()
            Log.e(TAG, "-------------- do cancel ,flag is " + acceptFlag)

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "----------------- cancel $TAG error")
        }

    }
}
