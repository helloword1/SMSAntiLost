package com.goockr.smsantilost.utils
import com.goockr.smsantilost.BuildConfig

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

    val BASE_URL = BuildConfig.BASE_URL
//    val BASE_URL = "http://120.24.5.252:8022/antilost/api/"
    val TOKEN = "token"
    val LOGIN_PHONE = "login_phone"
    val PHONE_TYPE = "phone_type"
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
    val SHOW_CONTACTS_ENABLE = "show_contacts_enable"
    val MY_LOCATION_ENABLE = "my_location_enable"

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
    val ABOUT_US_PHONE="020-88886666"
    val USER_NAME="user_name"
    val SIM_CARD_NUM="simCardNum"
    val IS_INSERT="is_insert"
    val INSERT_STR="insert_str"
    //多国电环
    val MULTiPLY_MOBIL_PHONE_NUM=12
    val MOBIL_PHONE_NUM="mobil_phone_num"
    val ADDRESS_RANGE="address_range"

    //拍照
    var TAKE_PHOTO=1
    var IMAGE_CHIOCE=2
    val IMAGE_CROP=3

    //定义接收广播的action字符串
    val GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast"
    val GEOFENCE_OUT_CODE="GEOFENCE_OUT_CODE"
}