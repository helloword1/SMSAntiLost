package com.goockr.smsantilost.entries

import retrofit2.http.*
import rx.Observable

/**
 * Created by LJN on 2017/11/17.
 */
interface LoginApi {
    @GET("validateCode")
    fun getCode(@QueryMap param: RequestParam): Observable<ValidateCodeBean>

    @FormUrlEncoded
    @POST("createUser")
    fun getLoginToken(@FieldMap requestParam: RequestParam): Observable<CreateUserBean>
}