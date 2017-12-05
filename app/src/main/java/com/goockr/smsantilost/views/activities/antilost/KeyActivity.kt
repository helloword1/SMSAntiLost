package com.goockr.smsantilost.views.activities.antilost

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.header.MaterialHeader
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.CalculateUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.jude.swipbackhelper.SwipeBackHelper
import kotlinx.android.synthetic.main.activity_key.*

/**
 * 钥匙界面
 */
class KeyActivity(override val contentView: Int = R.layout.activity_key) : BaseActivity() {

    private var iconId: Int = 0
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(false)
    }

    override fun onResume() {
        super.onResume()
        initMData()
        initMView()
        initClickEvent()
    }

    /**
     * 初始化数据
     */
    private fun initMData() {
        iconId = intent.getIntExtra("icon",0)
        name = intent.getStringExtra("name")
    }

    private fun initMView() {
        initMTitle()
        initMPullToRefresh()
    }

    /**
     * 初始化下拉刷新布局
     */
    private fun initMPullToRefresh() {
        val header = MaterialHeader(this)
        // 改变刷新图标颜色
        val colors = resources.getIntArray(R.array.google_colors)
        header.setColorSchemeColors(colors)
        // 调整刷新图标位置
        val calculateUtils = CalculateUtils(this)
        header.layoutParams = PtrFrameLayout.LayoutParams(-1, -2)
        header.setPadding(0, calculateUtils.dp2px(15), 0, calculateUtils.dp2px(10))
        header.setPtrFrameLayout(ptrFrameLayout)
        // 设置刷新完毕时关闭的时间
        ptrFrameLayout.setDurationToCloseHeader(500)
        // 改变刷新图标风格
        ptrFrameLayout.headerView = header
        ptrFrameLayout.addPtrUIHandler(header)
        ptrFrameLayout.setLastUpdateTimeRelateObject(this)
        ptrFrameLayout.setPtrHandler(object : PtrDefaultHandler() {
            override fun onRefreshBegin(frame: PtrFrameLayout?) {
                frame!!.postDelayed({
                    ptrFrameLayout.refreshComplete()
                }, 1500)
            }

            override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
                return true
            }
        })
    }

    /**
     * 初始化头部
     */
    private fun initMTitle() {
        ll?.removeAllViews()
        mainIcon.setImageResource(iconId)
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleRight = titleLayout.findViewById(R.id.titleRight)
        titleBack = titleLayout.findViewById(R.id.titleBack)

        title?.text = name
        titleRight?.visibility = View.VISIBLE
        titleRight?.setImageResource(R.mipmap.btn_nav_set_up)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }


    private fun initClickEvent() {
        llBell.setOnClickListener {
            mainBack.setImageDrawable(resources.getDrawable(R.drawable.animation_search))
            var anim = mainBack.drawable as AnimationDrawable
            anim.start()
        }
        // 设置按钮点击跳转
        titleRight?.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

}

