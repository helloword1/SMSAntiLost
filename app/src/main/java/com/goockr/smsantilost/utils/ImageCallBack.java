package com.goockr.smsantilost.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by lijinning on 2018/1/11.
 */

public abstract class ImageCallBack extends Callback<Bitmap> {
    @Override
    public Bitmap parseNetworkResponse(Response response, int id) throws Exception {
        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
        return bitmap;
    }
}
