package com.goockr.smsantilost.entries

import retrofit2.http.*
import rx.Observable

/**
 * Created by LJN on 2017/11/17.
 */
interface LoginApi {
    //获取验证码
    @GET("validateCode")
    fun getCode(@QueryMap param: RequestParam): Observable<ValidateCodeBean>

    //注册
    @FormUrlEncoded
    @POST("createUser")
    fun getLoginToken(@FieldMap requestParam: RequestParam): Observable<CreateUserBean>

    //密码登陆
    @FormUrlEncoded
    @POST("appPwdLogin")
    fun LoginPwd(@FieldMap requestParam: RequestParam): Observable<LoginCodeBean>

    //验证码登陆
    @FormUrlEncoded
    @POST("appVcodeLogin")
    fun LoginCode(@FieldMap requestParam: RequestParam): Observable<LoginCodeBean>
}