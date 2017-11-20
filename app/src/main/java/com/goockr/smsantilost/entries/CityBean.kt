package com.goockr.smsantilost.entries

/**
 * Created by LJN on 2017/11/13.
 */
class CityBean : com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean {

    private var city: String? = null//城市名字
    private var isTop: Boolean = false//是否是最上面的 不需要被转化成拼音的
    var phone: String = ""

    constructor() {}

    constructor(city: String) {
        this.city = city
    }

    fun getCity(): String? = city

    fun setCity(city: String): CityBean {
        this.city = city
        return this
    }

    fun isTop(): Boolean {
        return isTop
    }

    fun setTop(top: Boolean): CityBean {
        isTop = top
        return this
    }

    override fun getTarget(): String? = city
    override fun isNeedToPinyin(): Boolean = !isTop
    override fun isShowSuspension(): Boolean = !isTop

}
