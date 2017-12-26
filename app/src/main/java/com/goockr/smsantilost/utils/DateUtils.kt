/**
 * Copyright  2012-2016 [JeeSite](https://github.com/thinkgem/jeesite) All rights reserved.
 */
package com.goockr.smsantilost.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
object DateUtils {

    val parsePatterns = arrayOf("yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM")

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    val date: String
        get() = getDate("yyyy-MM-dd")

    /**
     * 获取当前月第一天
     * @return
     * @author jk97
     * 2017年10月13日
     */
    val firstDay: String
        get() {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val cale = Calendar.getInstance()
            cale.add(Calendar.MONTH, 0)
            cale.set(Calendar.DAY_OF_MONTH, 1)
            return format.format(cale.time)
        }
    /**
     * 获取当前月最后一天
     * @return
     * @author jk97
     * 2017年10月13日
     */
    val lastDay: String
        get() {
            val format = SimpleDateFormat("yyyy-MM-dd")
            var cale = Calendar.getInstance()
            cale = Calendar.getInstance()
            cale.add(Calendar.MONTH, 1)
            cale.set(Calendar.DAY_OF_MONTH, 0)
            return format.format(cale.time)
        }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    fun getDate(mDate: Date, n: Int): String {
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy-MM-dd")
        val format1 = format.format(mDate)
        return getSpecifiedDayAfter(format1, n)

    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    fun getDate(str: String): String {
        val format = SimpleDateFormat(str)
        val timeMillis = System.currentTimeMillis()
        return format.format(timeMillis)
    }

    /**
     * 获取过去的天数
     * @param date
     * @return
     */
    fun pastDays(date: Date): Long {
        val t = System.currentTimeMillis() - date.time
        return t / (24 * 60 * 60 * 1000)
    }

    /**
     * 获取过去的小时
     * @param date
     * @return
     */
    fun pastHour(date: Date): Long {
        val t = System.currentTimeMillis() - date.time
        return t / (60 * 60 * 1000)
    }

    /**
     * 获取过去的分钟
     * @param date
     * @return
     */
    fun pastMinutes(date: Date): Long {
        val t = System.currentTimeMillis() - date.time
        return t / (60 * 1000)
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * @param timeMillis
     * @return
     */
    fun formatDateTime(timeMillis: Long): String {
        val day = timeMillis / (24 * 60 * 60 * 1000)
        val hour = timeMillis / (60 * 60 * 1000) - day * 24
        val min = timeMillis / (60 * 1000) - day * 24 * 60 - hour * 60
        val s = timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
        val sss = timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000
        return (if (day > 0) day.toString() + "," else "") + hour + ":" + min + ":" + s + "." + sss
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    fun getDistanceOfTwoDate(before: Date, after: Date): Double {
        val beforeTime = before.time
        val afterTime = after.time
        return ((afterTime - beforeTime) / (1000 * 60 * 60 * 24)).toDouble()
    }

    /**
     * 获得指定日期的前n天
     * @param specifiedDay 格式为 yyyy-MM-dd
     * @param n
     * @return yyyy-MM-dd
     * @throws Exception
     */
    fun getSpecifiedDayBefore(specifiedDay: String, n: Int): String {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        c.time = date
        val day = c.get(Calendar.DATE)
        c.set(Calendar.DATE, day - n)

        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得指定日期的后n天
     * @param specifiedDay 格式为 yyyy-MM-dd
     * @param n
     * @return yyyy-MM-dd
     */
    fun getSpecifiedDayAfter(specifiedDay: String, n: Int): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        c.time = date
        val day = c.get(Calendar.DATE)
        c.set(Calendar.DATE, day + n)

        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得当前日期的前n天
     * @param n
     * @return yyyy-MM-dd
     * @throws Exception
     */
    fun getDayBefore(n: Int): String {
        return getSpecifiedDayBefore(date, n)
    }

    /**
     * 获得当前日期的后n天
     * @param n
     * @return yyyy-MM-dd
     */
    fun getDayAfter(n: Int): String {
        return getSpecifiedDayAfter(date, n)
    }

    /**
     * 获得指定日期的前n个月
     * @param specifiedDay 格式为 yyyy-MM-dd
     * @param n
     * @return yyyy-MM
     * @throws Exception
     */
    fun getMonthBefore(specifiedDay: String, n: Int): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        c.time = date
        c.add(Calendar.MONTH, -n)

        return SimpleDateFormat("yyyy-MM").format(c.time)
    }

    /**
     * 获得指定日期的后n个月
     * @param specifiedDay yyyy-MM-dd
     * @param n
     * @return yyyy-MM
     */
    fun getMonthAfter(specifiedDay: String, n: Int): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        c.time = date
        c.add(Calendar.MONTH, n)

        return SimpleDateFormat("yyyy-MM").format(c.time)
    }

    /**
     * 获得当前日期的前n个月
     * @param n
     * @return yyyy-MM
     * @throws Exception
     */
    fun getMonthBefore(n: Int): String {
        return getMonthBefore(date, n)
    }

    /**
     * 获得当前日期的后n个月
     * @param n
     * @return yyyy-MM
     */
    fun getMonthAfter(n: Int): String {
        return getMonthAfter(date, n)
    }

    /**
     * 获得该月第一天
     * @param month  //yyyy-MM-dd
     * @return
     */
    fun getFirstDayOfMonth(month: String): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd").parse(month)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        c.time = date
        //获取某月最小天数
        val firstDay = c.getActualMinimum(Calendar.DAY_OF_MONTH)
        //设置日历中月份的最小天数
        c.set(Calendar.DAY_OF_MONTH, firstDay)
        //格式化日期
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(c.time)
    }

    /**
     * 获得该月最后一天
     * @param month
     * @return
     */
    fun getLastDayOfMonth(month: String): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd").parse(month)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        c.time = date
        //获取某月最大天数
        val lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        //设置日历中月份的最大天数
        c.set(Calendar.DAY_OF_MONTH, lastDay)
        //格式化日期
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(c.time)
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    @Throws(ParseException::class)
    fun stringToLong(strTime: String, formatType: String): Long {
        val date = stringToDate(strTime, formatType) // String类型转成date类型
        return if (date == null) {
            0
        } else {
            dateToLong(date)
        }
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    @Throws(ParseException::class)
    fun stringToDate(strTime: String, formatType: String): Date? {
        val formatter = SimpleDateFormat(formatType)
        var date: Date? = null
        date = formatter.parse(strTime)
        return date
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    fun dateToLong(date: Date): Long {
        return date.time
    }

}
