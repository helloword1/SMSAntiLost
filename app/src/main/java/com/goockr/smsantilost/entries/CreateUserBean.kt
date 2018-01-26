package com.goockr.smsantilost.entries

/**
 * Created by LJN on 2017/11/17.
 */
class CreateUserBean {
    var result: Int = 0
    var msg: String? = null
    var acct: String? = null
    var name: String? = null
    var token: String? = null
    var mobil  = ""
}
class LoginCodeBean: BaseEntity() {
    var loginname:String?=""
    var username:String?=""
    var mobile:String?=""
    var token:String?=""
}