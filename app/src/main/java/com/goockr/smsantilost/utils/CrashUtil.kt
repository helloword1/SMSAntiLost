package com.goockr.smsantilost.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: LJN
 * Class:   CrashUtil
 * @date:   2017/11/4
 * Description:
 */
class CrashUtil private constructor() : Thread.UncaughtExceptionHandler {
    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null

    /** 存储设备信息和异常信息  */
    private val infos = HashMap<String, String>()

    /** 格式化日期，作为日志文件名的一部分  */
    private val formatter = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())

    private var mContext: Context? = null

    fun init(context: Context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        mContext = context
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        handleException(ex)

        if (mDefaultCrashHandler != null) {
            // 让系统默认的处理器处理
            SystemClock.sleep(500)
            mDefaultCrashHandler!!.uncaughtException(thread, ex)
        }
    }

    /** 自定义错误处理，收集错误信息等  */
    private fun handleException(ex: Throwable?): Boolean {
        // 处理异常，可以提示用户崩溃了，或保存重要信息，尝试恢复现场，或者直接杀死进程
        if (ex == null) {
            return false
        }

        // 收集设备信息
        collectDeviceInfo()
        // 保存日志文件
        saveCrashInfoToFile(ex)
        return true
    }

    /** 收集设备信息  */
    private fun collectDeviceInfo() {
        try {
            val pm = mContext!!.packageManager
            val pi = pm.getPackageInfo(mContext!!.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                // 应用版本信息
                infos.put("App Version", pi.versionName + '_' + pi.versionCode + "\n")
                // Android 系统版本信息
                infos.put("OS Version", Build.VERSION.RELEASE + '_' + Build.VERSION.SDK_INT + "\n")
                // 设备ID
                infos.put("Device ID", Build.ID + "\n")
                // 设备序列号
                infos.put("Device Serial", Build.SERIAL + "\n")
                // 手机制造商
                infos.put("Manufacturer", Build.MANUFACTURER + "\n")
                // 手机型号
                infos.put("Model", Build.MODEL + "\n")
                // CPU架构
                infos.put("CPU ABI", Build.CPU_ABI + "\n")
                // 手机品牌
                infos.put("Brand", Build.BRAND + "\n")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "an error occurred when collect package info")
            e.printStackTrace()
        }

        //        Field[] fields = Build.class.getDeclaredFields();
        //        for (Field field : fields)
        //        {
        //            try
        //            {
        //                field.setAccessible(true);
        //                infos.put(field.getName(), field.get(null).toString());
        //                LogUtil.d(field.getName() + " : " + field.get(null));
        //            }
        //            catch (IllegalAccessException e)
        //            {
        //                LogUtil.e("an error occured when collect crash info");
        //                e.printStackTrace();
        //            }
        //        }
    }

    /** 保存错误信息到文件中  */
    private fun saveCrashInfoToFile(ex: Throwable): String {
        val sb = StringBuffer()
        for ((key, value) in infos) {
            sb.append(key)
            sb.append(" : ")
            sb.append(value)
            sb.append("\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()

        val result = writer.toString()
        sb.append("\n")
        sb.append(result)

        try {
            val currentTime = System.currentTimeMillis()
            val time = formatter.format(Date(currentTime))
            val fileName = "crash_" + time + "_" + currentTime + ".log"

            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val dir = File(SD_CARD_PATH)
                if (!dir.exists()) {
                    val s = dir.mkdirs()
                    println(s)
                }

                val fileOutputStream = FileOutputStream(SD_CARD_PATH + "/" + fileName)
                fileOutputStream.write(sb.toString().toByteArray())
                fileOutputStream.close()
            }

            return fileName
        } catch (e: Exception) {
            LogUtils.e(TAG, "an error occurred while writing file...")
            e.printStackTrace()
        }

        return ""
    }

    companion object {
        private val TAG = "CrashUtil"

        private val SD_CARD_PATH = Environment.getExternalStorageDirectory().absolutePath + "/SimpleDemo/crash"

        /** 类单例  */
        @SuppressLint("StaticFieldLeak")
        private var mInstance: CrashUtil? = null

        val instance: CrashUtil?
            get() {
                if (null == mInstance) {
                    synchronized(CrashUtil::class.java) {
                        if (null == mInstance) {
                            mInstance = CrashUtil()
                        }
                    }
                }
                return mInstance
            }
    }
}
