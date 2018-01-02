package com.goockr.smsantilost.utils

/**
 * Created by LJN on 2017/11/10.
 */
object Constant {
    val HAD_LOGIN = "had_login"
    val NAME: String? = "SMSBluetooth"
    val UUID: String? = "00001101-1231-1000-8000-00805F9B34FB"
    val REQUEST_ENABLE_BT = 11
    //蓝牙连接成功
    val MSG_CONNECT_SUCCEED = 32
    //接收蓝牙数据
    val MSG_CLIENT_REV_NEW = 30
    //断开蓝牙连接
    val MSG_CONNECT_DISCONNECT = 31

    val MSG_CONNECT_FAIL = 33


    val REQUEST_ENABLE_VISIBILITY: Int = 22

    val BASE_URL = "http://192.168.1.56:8088/antilost/api/"
    val TOKEN = "token"
    val LOGIN_PHONE = "login_phone"
    val LOGIN_MSM_CODE = "login_msm_code"
    val CONTACT_NAME = "contact_name"
    val CONTACT_PHONE = "contact_phone"
    val CONTACT_ID = "contact_id"
    val CONTACT_RESULT_ID = 55

    val MSM_NAME = "msm_name"
    val MSM_Time = "msm_time"
    val MSM_CONTENT = "msm_content"
    val MSM_RESULT_ID = 66

    val CHOICE_CONTACT_RESULT_ID = 77
    val CREATE_CONTACT_RESULT_ID = 88
    val MSM_MANAGER_RESULT_ID = 99

    val CURRENT_AREA_NAME = "current_area_name"
    val CURRENT_AREA_ID = "current_area_id"
    val CURRENT_AREA_LATITUDE = "current_area_latitude"
    val CURRENT_AREA_LONGITUDE = "current_area_longitude"
    val CURRENT_AREA_ADDRESS = "current_area_address"
    val CURRENT_AREA_RADUIS = "current_area_raduis"
    val LATITUDE = "latitude"
    val LONGITUDE = "longitude"

    //蓝牙操作:
    //获取主机蓝牙物理地址
    val MAC = "Mac"
    //获取主机电池电量
    val BATTERY = "Battery"
    //同步SIM短信
    val DAT = "Dat"
    //关闭蓝牙
    val DOWN = "Down"
    //重启主机
    val RESET = "Reset"
    //发送短信 Sms,13415580890,你好
    val SMS = "Sms"
    //设置系统时间（断电复位）
    val SET_TIME = "SetTime"
    //获取时间
    val TIME = "Time"
    //获取初始化信息
    val INIT = "Init"
    //主机响铃
    val BUZZER = "Buzzer"
    //蓝牙保存标志
    val BLUETEETH_INDEX = "blueteeth_index"

    val SM_OK = "SM_OK"
    val ERROR = "Error"
    val ADDRESS = "address"
    val ADDRESS_TYPE = "address_type"

    //越界提醒
    val OVERSTEP = "overstep"
    //开启振动
    val VIBRATE = "vibrate"
    //重连提醒
    val RECONNECT = "reconnect"
    //选择断开提醒声音
    val SELECT_PHONE_SOUND = "select_phone_sound"
}