package com.goockr.smsantilost.entries

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean
import java.io.Serializable

/**
 * Created by LJN on 2017/11/13.
 */
class PhoneBean : BaseIndexPinyinBean,Serializable{

    private var mPhone: String? = null//城市名字
    private var isTop: Boolean = false//是否是最上面的 不需要被转化成拼音的
    var id: String? = null//是否是最上面的 不需要被转化成拼音的
    var phone: String = ""
    var isshowRight=false

    constructor() {}

    constructor(city: String) {
        this.mPhone = city
    }

    fun getMPhone(): String? = mPhone

    fun setMPhone(city: String): PhoneBean {
        this.mPhone = city
        return this
    }

    fun isTop(): Boolean {
        return isTop
    }

    fun setTop(top: Boolean): PhoneBean {
        isTop = top
        return this
    }

    override fun getTarget(): String? = mPhone
    override fun isNeedToPinyin(): Boolean = !isTop
    override fun isShowSuspension(): Boolean = !isTop

}
