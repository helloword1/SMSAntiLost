package com.goockr.smsantilost.views.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.LoadingDialog
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.blueteeth.ClientThread
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import cxx.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.base_title_view.view.*
import java.io.Serializable

/**
 * Created by Administrator on 2017/9/26.
 */

abstract class BaseActivity : AppCompatActivity() {
    protected var ll: FrameLayout? = null
    private var flbase: android.widget.FrameLayout? = null
    private var inflation: LayoutInflater? = null
    protected var titleBack: Button? = null
    protected var title: TextView? = null
    protected var titleOk: TextView? = null
    protected var progressDialog: LoadingDialog? = null// 加载对话框
    protected var titleRight: ImageView? = null
    protected var titleAdd: ImageView? = null
    protected var titleRight1: TextView? = null
    var preferences: SharedPreferencesUtils? = null// 配置文件
    protected var baseLine: View? = null
    protected var status_bar: View? = null
    protected var goockrApplication: GoockrApplication? = null
    protected var instance: ClientThread? = null
    protected var mediaPlayer: MediaPlayer? = null
    //蓝牙通信
    protected val myHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            handleMyMessage(msg)
        }
    }

    //处理蓝牙模块返回信息
    protected open fun handleMyMessage(msg: Message?) {}

    abstract val contentView: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = ContextCompat.getColor(this,R.color.statueBarColor)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }

        setContentView(R.layout.base_content_view)
        preferences = SharedPreferencesUtils.getInstance(this)
        this.flbase = findViewById<View>(R.id.fl_base_content_view) as FrameLayout
        this.ll = findViewById<View>(R.id.ll_title) as FrameLayout
        status_bar = findViewById(R.id.status_bar)
        baseLine = findViewById(R.id.baseLine)
        inflation = LayoutInflater.from(this)
        goockrApplication = application as GoockrApplication
        instance = goockrApplication?.instance
        goockrApplication?.addActivity(this)
        //滑动删除库初始化
        SwipeBackHelper.onCreate(this)
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(true)
                .setSwipeRelateOffset(300)
        initTitleView()
        initContentView()
        initView()
    }

    protected open fun initView() {}

    private fun initContentView() {
        flbase?.addView(inflation!!.inflate(contentView, null))
    }

    private fun initTitleView() {
        val titleView = inflation!!.inflate(R.layout.base_title_view, null)
        this.titleRight = titleView.titleRight
        this.titleRight1 = titleView.titleRight1
        this.title = titleView.title
        titleAdd = titleView.titleAdd
        this.titleBack = titleView.titleBack
        titleOk = titleView.titleOk
        ll?.addView(titleView)
        titleBack?.setOnClickListener { finish() }
    }

    /**
     * 跳转到对应的Activity
     */
    fun <T> showActivity(activityCls: Class<T>) {
        showActivity(activityCls, null)
    }

    fun <T> showActivity(activityCls: Class<T>, extras: Bundle?) {
        val intent = Intent(this, activityCls)
        if (null == extras) {
            startActivity(intent)
        } else {
            intent.putExtras(extras)
            startActivity(intent)
        }
    }

    fun <T> showActivity(activityCls: Class<T>, key: String, obj: Serializable) {
        val intent = Intent(this, activityCls)
        intent.putExtra(key, obj)
        startActivity(intent)
    }

    fun <T> showActivityByLoginFilter(activityCls: Class<T>,
                                      extras: Bundle?) {
        val intent = Intent(this, activityCls)
        if (null != extras) {
            intent.putExtras(extras)
        }
        showActivity(activityCls, extras)
    }

    /**
     * 跳转到对应的Activity返回的时候可以接受到结果
     * @param requestCode 请求码
     */
    fun <T> showActivityForResult(activityCls: Class<T>,
                                  requestCode: Int) {
        showActivityForResult(activityCls, null, requestCode)
    }

    fun <T> showActivityForResult(activityCls: Class<T>,
                                  extras: Bundle?, requestCode: Int) {
        val intent = Intent(this, activityCls)
        if (null == extras) {
            startActivityForResult(intent, requestCode)
        } else {
            intent.putExtras(extras)
            startActivityForResult(intent, requestCode)
        }
    }

    /**
     * 跳转到对应的Activity by Flags的过滤
     *
     * @param activityCls
     * @param flags
     */
    fun <T> showActivityByFlags(activityCls: Class<T>, flags: Int) {
        val intent = Intent(this, activityCls)
        intent.flags = flags
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    fun <T> showActivityByFlags(activityCls: Class<T>, extras: Bundle, flags: Int) {
        val intent = Intent(this, activityCls)
        intent.flags = flags
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtras(extras)
        startActivity(intent)
    }

    var thread: Thread? = null
    /**
     * 显示加载对话框
     */
    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = LoadingDialog(this)
        }
        progressDialog?.show()
        if (thread == null) {
            thread = Thread {
                kotlin.run {
                    SystemClock.sleep(8000)
                    if (thread!!.isInterrupted) return@Thread
                    runOnUiThread {
                        thread = null
                        progressDialog?.hide()
                    }
                }
            }
        }
    }

    /**
     * 隐藏对话框
     */
    fun dismissDialog() {
        if (NotNull.isNotNull(progressDialog) && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            if (thread != null) {
                thread?.interrupt()
                thread = null
            }
            progressDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        goockrApplication?.removeActivity(this)
        dismissDialog()
    }

    /**
     * 显示加载对话框
     *
     * @param message 需要加载的信息
     */
    fun showProgressDialog(message: String) {
        if (progressDialog == null) {
            progressDialog = LoadingDialog(this, message)
        }
        progressDialog!!.show()
    }

    fun showProgressDialog(message: String, cancelable: Boolean) {
        if (progressDialog == null) {
            progressDialog = LoadingDialog(this, message, cancelable)
        }
        progressDialog!!.show()
    }

    protected open fun receive(intent: Intent) {}
    /**
     * 广播接受器
     */
    protected inner class MyBtReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            receive(intent)
        }
    }
    //越界提醒
    protected fun overAlert() {
        if (NotNull.isNotNull(mediaPlayer)){
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }else{
            mediaPlayer = MediaPlayer()
        }
        val value = preferences?.getStringValue(Constant.SELECT_PHONE_SOUND)
        val descriptor = resources.assets
                .openFd("alert.mp3")
        mediaPlayer?.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }
}
