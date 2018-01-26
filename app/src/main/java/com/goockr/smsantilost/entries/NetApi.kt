package com.goockr.smsantilost.entries

/**
 * Created by LJN on 2017/11/29.
 */
object NetApi {
    val GET_CODE ="validateCode"
    val GET_LOGIN_TOKEN="createUser"
    val LOGIN_PWD="appPwdLogin"
    val LOGIN_CODE="appVcodeLogin"
    val UP_LOADHEAD_IMG ="uploadHeadImg"
    val LOADHEAD_IMG ="getHeadImg"
    val FIND_POSITION_RECORD ="findPositionRecord"

    val ADD_POSITION_RECORD ="addPositionRecord"
    //添加/修改防打扰区域
    val AM_PREVENT_DISTURB ="amPreventDisturb"
    //获取防打扰区域列表
    val FIND_PREVENT_DISTURB ="findPreventDisturb"
    val DELETE_PREVENT_DISTURB ="deletePreventDisturb"
    //绑定设备
    val BIND_DEV ="bindDev"
}