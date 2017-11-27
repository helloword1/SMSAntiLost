package com.goockr.smsantilost.views.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.LoadingDialog
import com.jude.swipbackhelper.SwipeBackHelper
import cxx.utils.NotNull
import cxx.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.base_title_view.view.*
import java.io.Serializable
import kotlin.concurrent.thread

/**
 * Created by Administrator on 2017/9/26.
 */

abstract class BaseActivity : AppCompatActivity() {
    protected var ll: FrameLayout? = null
    private var flbase: android.widget.FrameLayout? = null
    private var inflation: LayoutInflater? = null
    protected var titleBack: Button? = null
    protected var title: TextView? = null
    protected var progressDialog: LoadingDialog? = null// 加载对话框
    protected var titleRight: TextView? = null
    var preferences: SharedPreferencesUtils? = null// 配置文件
    protected var baseLine: View? = null
    protected var fileImagePath: String? = null
    protected var status_bar: View? = null
    private val bitmap: Bitmap? = null
    private var goockrApplication: GoockrApplication? = null

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
        initData()
    }

    protected open fun initView() {

    }

    protected fun initData() {

    }

    private fun initContentView() {
        flbase?.addView(inflation!!.inflate(contentView, null))
    }

    private fun initTitleView() {
        val titleView = inflation!!.inflate(R.layout.base_title_view, null)
        this.titleRight = titleView.titleRight
        this.title = titleView.title
        this.titleBack = titleView.titleBack
        ll?.addView(titleView)
        titleBack!!.setOnClickListener { finish() }
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
     *
     * @param activityCls 对应的Activity.class
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

    /**
     * 显示加载对话框
     */
    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = LoadingDialog(this)
        }
        progressDialog?.show()

        thread {
            kotlin.run {
                Thread.sleep(8000)
                runOnUiThread {
                    progressDialog?.hide()
                }
            }
        }

    }

    /**
     * 隐藏对话框
     */
    fun dismissDialog() {
        if (NotNull.isNotNull(progressDialog) && progressDialog?.isShowing!!) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        goockrApplication!!.removeActivity(this)
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

    companion object {
        private val TAG = "BaseActivity"
    }


}
