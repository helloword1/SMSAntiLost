package com.goockr.smsantilost.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.HomeActivity
import cxx.utils.NotNull
import cxx.utils.SharedPreferencesUtils
import net.sourceforge.pinyin4j.PinyinHelper
import java.util.*


object LocaleUtil {
    /**
     * 获取用户设置的Locale
     *
     * @return Locale
     */
    fun getUserLocale(context: Context): Locale {
        val current = SharedPreferencesUtils.getInstance(context).getValue("currentLanguage", String::class.java, true) as String
        var currentLanguage = 0
        if (NotNull.isNotNull(current)) {
            currentLanguage = Integer.valueOf(current)!!
        }
        var myLocale = Locale.SIMPLIFIED_CHINESE
        when (currentLanguage) {
            0 -> myLocale = Locale.SIMPLIFIED_CHINESE
            1 -> myLocale = Locale.TRADITIONAL_CHINESE
            2 -> myLocale = Locale.ENGLISH
        }
        return myLocale
    }

    /**
     * 设置语言：如果之前有设置就遵循设置如果没设置过就跟随系统语言
     */
    fun changeAppLanguage(context: Context?) {
        if (context == null) {
            return
        }
        val appContext = context.applicationContext
        val current = SharedPreferencesUtils.getInstance(appContext).getStringValue("currentLanguage")
        var currentLanguage = getCurrentLocale(getCurrentLocale(context))
        if (NotNull.isNotNull(current)) {
            currentLanguage = current.toInt()
        }
        val myLocale: Locale
        // 0 简体中文 1 繁体中文 2 English
        myLocale = when (currentLanguage) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.TRADITIONAL_CHINESE
            2 -> Locale.ENGLISH
            else -> {
                Locale.ENGLISH
            }
        }
        when (myLocale) {
            Locale.SIMPLIFIED_CHINESE -> SharedPreferencesUtils.instance.putValue("currentLanguage", "0")
            Locale.TRADITIONAL_CHINESE -> SharedPreferencesUtils.instance.putValue("currentLanguage", "1")
            Locale.ENGLISH -> SharedPreferencesUtils.instance.putValue("currentLanguage", "2")
            else->SharedPreferencesUtils.instance.putValue("currentLanguage", "2")
        }
        // 本地语言设置
        if (needUpdateLocale(appContext, myLocale)) {
            updateLocale(appContext, myLocale)
        }
    }

    private fun getCurrentLocale(locale: Locale): Int {
        return when (locale) {
            Locale.SIMPLIFIED_CHINESE -> 0
            Locale.TRADITIONAL_CHINESE -> 1
            Locale.ENGLISH -> 2
            else -> {
                2
            }
        }
    }

    /**
     * 保存设置的语言
     *
     * @param currentLanguage index
     */
    fun changeAppLanguage(context: Context?, currentLanguage: Int) {
        if (context == null) {
            return
        }
        val appContext = context.applicationContext
        SharedPreferencesUtils.getInstance(appContext).putValue("currentLanguage", currentLanguage.toString())
        var myLocale = Locale.SIMPLIFIED_CHINESE
        // 0 简体中文 1 繁体中文 2 English
        when (currentLanguage) {
            0 -> myLocale = Locale.SIMPLIFIED_CHINESE
            1 -> myLocale = Locale.TRADITIONAL_CHINESE
            2 -> myLocale = Locale.ENGLISH
        }
        // 本地语言设置
        if (LocaleUtil.needUpdateLocale(appContext, myLocale)) {
            LocaleUtil.updateLocale(appContext, myLocale)
        }

        Toast.makeText(appContext, appContext.getString(R.string.settingSuscceed), Toast.LENGTH_SHORT).show()
        restartApp(appContext)
    }

    /**
     * 重启app生效
     *
     * @param context
     */
    private fun restartApp(context: Context) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * 获取当前的Locale
     *
     * @param context Context
     * @return Locale
     */
    private fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }

    /**
     * 更新Locale
     *
     * @param context Context
     * @param locale  New User Locale
     */
    private fun updateLocale(context: Context, locale: Locale) {
        if (needUpdateLocale(context, locale)) {
            val configuration = context.resources.configuration
            if (Build.VERSION.SDK_INT >= 19) {
                configuration.setLocale(locale)
            } else {
                configuration.locale = locale
            }
            val displayMetrics = context.resources.displayMetrics
            context.resources.updateConfiguration(configuration, displayMetrics)
        }
    }

    /**
     * 判断需不需要更新
     *
     * @param context Context
     * @param locale  New User Locale
     * @return true / false
     */
    private fun needUpdateLocale(context: Context, locale: Locale?): Boolean {
        return locale != null && getCurrentLocale(context) != locale
    }

    /**
     * 当系统语言发生改变的时候还是继续遵循用户设置的语言
     *
     * @param context
     * @param newConfig
     */
    fun setLanguage(context: Context?, newConfig: Configuration) {
        if (context == null) {
            return
        }
        val appContext = context.applicationContext
        val current = SharedPreferencesUtils.getInstance(appContext).getStringValue("currentLanguage")
        var currentLanguage = 0
        if (NotNull.isNotNull(current)) {
            currentLanguage = current.toInt()
        }
        val locale: Locale?
        // 0 简体中文 1 繁体中文 2 English
        locale = when (currentLanguage) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.TRADITIONAL_CHINESE
            2 -> Locale.ENGLISH
            else -> appContext.resources.configuration.locale
        }
        // 系统语言改变了应用保持之前设置的语言
        if (locale != null) {
            Locale.setDefault(locale)
            val configuration = Configuration(newConfig)
            if (Build.VERSION.SDK_INT >= 19) {
                configuration.setLocale(locale)
            } else {
                configuration.locale = locale
            }
            appContext.resources.updateConfiguration(configuration, appContext.resources.displayMetrics)
        }
    }

    fun dealWith(date: String): String {
        if (date.contains("_")) {
            val dates = date.split("_")
            val date0 = dates[0]
            val date1 = dates[1]
            return "${date0.split("-")[0]}年${date0.split("-")[1]}" +
                    "月${date0.split("-")[2]}日 ${date1.split("-")[0]}:${date1.split("-")[1]}"
        }
        return ""
    }

    fun dealWithForSms(date: String): String {
        if (date.contains("_")) {
            val dates = date.split("_")
            val date0 = dates[0]
            return "${date0.split("-")[0]}/${date0.split("-")[1]}" +
                    "/${date0.split("-")[2]}"
        }
        return ""
    }

    //验证各种导航地图是否安装
    fun isAvilible(context: Context, packageName: String): Boolean {
        //获取所有已安装程序的包信息
        val packageInfos = context.packageManager.getInstalledPackages(0)
        //用于存储所有已安装程序的包名
        val packageNames = ArrayList<String>()
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (i in packageInfos.indices) {
                val packName = packageInfos[i].packageName
                packageNames.add(packName)
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName)
    }

    // 获得字符串的首字母 首字符 转汉语拼音
    fun getFirstChar(value: String): String {
        // 首字符
        var firstChar = value[0]
        // 首字母分类
        var first: String? = null
        // 是否是非汉字
        val print = PinyinHelper.toHanyuPinyinStringArray(firstChar)

        if (print == null) {
            // 将小写字母改成大写
            if (firstChar.toInt() in 97..122) {
                firstChar -= 32
            }
            first = if (firstChar.toInt() in 65..90) {
                firstChar.toString()
            } else {
                // 认为首字符为数字或者特殊字符
                "#"
            }
        } else {
            // 如果是中文 分类大写字母
            // 这里对多音字“长”做一些处理
            first = if ("" + firstChar == "长") {
                (print[1][0].toInt() - 32).toChar().toString()
            } else {
                (print[0][0].toInt() - 32).toChar().toString()
            }
        }
        if (first == null) {
            first = "?"
        }
        return first
    }

    fun getPingYin(firstChar: String): String {
        val builder = StringBuilder()
        for (s in firstChar) {
            val print = PinyinHelper.toHanyuPinyinStringArray(s)
            builder.append(print)
        }
        val string = builder.toString()
        return string.toUpperCase()
    }
}
