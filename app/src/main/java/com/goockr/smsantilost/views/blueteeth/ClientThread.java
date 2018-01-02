package com.goockr.smsantilost.views.blueteeth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.goockr.smsantilost.utils.Constant;
import com.goockr.smsantilost.views.activities.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import cxx.utils.NotNull;

/**
 * @author Administrator
 * @date 2017/4/4
 */

public class ClientThread implements Runnable {
    final String TAG = "ClientThread";
    private BaseActivity activity;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    Handler uiHandler;
    BluetoothSocket socket;
    OutputStream out;
    InputStream in;
    private int potion;

    public ClientThread() {
    }

    public void init(int potion, BluetoothDevice device,
                     Handler handler, BaseActivity activity) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.activity = activity;
        this.device = device;
        this.potion = potion;
        this.uiHandler = handler;
        BluetoothSocket tmp = null;
        try {
            String address = device.getAddress();
            BluetoothDevice remoteDevice = bluetoothAdapter.getRemoteDevice(address);
            tmp = remoteDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = tmp;
    }

    @Override
    public void run() {
        Log.e(TAG, "----------------- do client thread run()");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        try {
            socket.connect();
            out = socket.getOutputStream();
            in = socket.getInputStream();
            if (socket.isConnected()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 通知 ui 连接的服务器端设备
                        Message message = new Message();
                        message.what = Constant.INSTANCE.getMSG_CONNECT_SUCCEED();
                        message.obj = device;
                        uiHandler.sendMessage(message);
                    }
                });
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "-----------do client read run()");
                    byte[] buffer = new byte[1024];
                    int len;
                    String content;
                    try {
                        while ((len = in.read(buffer)) != -1) {
                            content = new String(buffer, 0, len, "utf-8");
                            Message message = new Message();
                            message.what = Constant.INSTANCE.getMSG_CLIENT_REV_NEW();
                            message.arg1 = potion;
                            message.obj = content;
                            uiHandler.sendMessage(message);
                        }

                    } catch (IOException e) {
                        //断开设备
                        Message message = new Message();
                        message.what = Constant.INSTANCE.getMSG_CONNECT_DISCONNECT();
                        uiHandler.sendMessage(message);
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            //断开设备
            Message message = new Message();
            message.what = Constant.INSTANCE.getMSG_CONNECT_FAIL();
            uiHandler.sendMessage(message);
            e.printStackTrace();
        }
    }


    public void write(String data) {
        try {
            if (out != null) {
                out.write(data.getBytes("utf-8"));
                Log.e(TAG, "---------- write date ok " + data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //关闭通道
    public void disConnect() {
        if (NotNull.isNotNull(out) && NotNull.isNotNull(in)) {
            try {
                socket.close();
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isConnect() {
        return NotNull.isNotNull(socket) && socket.isConnected();
    }

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public BluetoothDevice getCurrent() {
        return device;
    }
}
