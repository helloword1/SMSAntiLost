package com.goockr.smsantilost.utils


import com.goockr.smsantilost.entries.LoginApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 */
object NetWorkUtil {
    private var okHttpClient: OkHttpClient? = null
    private val gsonConverterFactory = GsonConverterFactory.create()
    private val rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create()
    private var netCacheInterceptor1: Interceptor? = null
    private var loginApi: LoginApi? = null
//        private static GetUserInfoApi getUserInfoApi;
    //    private static UpLoadImgApi upLoadImgApi;
    //    private static AddGoodsApi addGoodsApi;
    //    private static GetInventoryApi getInventoryApi;
    /**
     * 初始化okhttp
     */
    fun initOkhttp() {
        if (okHttpClient == null) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient = OkHttpClient.Builder().addInterceptor(netCacheInterceptor1).addInterceptor(httpLoggingInterceptor).build()
        }

    }
    /**
     * 获取登录token
     *
     * @return
     */
    fun LoginApi(netCacheInterceptor: Interceptor): LoginApi? {
        netCacheInterceptor1 = netCacheInterceptor
        initOkhttp()
        if (loginApi == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build()
            loginApi = retrofit.create(LoginApi::class.java)
        }
        return loginApi
    }
    /**
     * 获取登录用户信息
     *
     * @return
     *//*
    public static GetUserInfoApi getUserInfoApi(Interceptor netCacheInterceptor) {
       netCacheInterceptor1 =netCacheInterceptor;
        initOkhttp();
        if (getUserInfoApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            getUserInfoApi = retrofit.create(GetUserInfoApi.class);
        }
        return getUserInfoApi;
    }
    */
    /**
     * 上传图片
     *
     * @return
     *//*
    public static UpLoadImgApi getUpLoadImageApi(Interceptor netCacheInterceptor) {
        netCacheInterceptor1 =netCacheInterceptor;
        initOkhttp();
        if (upLoadImgApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_IMG_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            upLoadImgApi = retrofit.create(UpLoadImgApi.class);
        }
        return upLoadImgApi;
    }
    */
    /**
     * 添加编辑商品
     *
     * @return
     *//*
    public static AddGoodsApi addGoodsApi(Interceptor netCacheInterceptor) {
        netCacheInterceptor1 =netCacheInterceptor;
        initOkhttp();
        if (addGoodsApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            addGoodsApi = retrofit.create(AddGoodsApi.class);
        }
        return addGoodsApi;
    }
    */
    /**
     * 获取库存商品
     *
     * @return
     *//*
    public static GetInventoryApi getInventoryApi(Interceptor netCacheInterceptor) {
        netCacheInterceptor1 =netCacheInterceptor;
        initOkhttp();
        if (getInventoryApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
            getInventoryApi = retrofit.create(GetInventoryApi.class);
        }
        return getInventoryApi;
    }
*/
}
