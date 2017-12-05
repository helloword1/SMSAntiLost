package com.goockr.smsantilost.views.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.antilost.AddActivity
import com.goockr.smsantilost.views.fragments.*
import com.jude.swipbackhelper.SwipeBackHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.msm_title_view.*
import kotlinx.android.synthetic.main.msm_title_view.view.*

class HomeActivity(override val contentView: Int = R.layout.activity_home) : BaseActivity()/*, BottomTabBar.OnSelectListener */ {
    private var homeFragment: MoreFragment? = null
    private var locationFragment: LocationFragment? = null
    private var antiLostFragment: AntiLostFragment? = null
    private var contactFragment: ContactFragment? = null
    private var msmFragment: MSMFragment? = null
    private var current: Fragment? = null
    private var isMsmAndContact = true
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //设置右滑不finsh界面
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE
                    ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    initMView()
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {

                }

            }).check()
        } else {
            initMView()
        }

    }

    fun initMView() {
        //初始化titlebar
        ll?.removeAllViews()
        var inflate1 = layoutInflater.inflate(R.layout.msm_title_view, null)
        inflate1.llMsm.setOnClickListener {
            if (msmFragment == null) {
                msmFragment = MSMFragment()
            }
            isMsmAndContact = false
            switchContent(msmFragment!!)
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.textColor))
            tvContact.setTextColor(ContextCompat.getColor(this, R.color.msmTextColorGray))
            tvMessageLine.visibility = View.VISIBLE
            tvContactLine.visibility = View.GONE
            current = msmFragment
        }
        inflate1.llContact.setOnClickListener {
            if (contactFragment == null) {
                contactFragment = ContactFragment()
            }
            isMsmAndContact = true
            switchContent(contactFragment!!)
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.msmTextColorGray))
            tvContact.setTextColor(ContextCompat.getColor(this, R.color.textColor))
            tvMessageLine.visibility = View.GONE
            tvContactLine.visibility = View.VISIBLE
            current = contactFragment
        }
        ll?.addView(inflate1)
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (msmFragment == null) {
            msmFragment = MSMFragment()
            beginTransaction.add(R.id.homeFrameLayout, msmFragment).commit()
            current = msmFragment
            isMsmAndContact=false
        }

        homeBottom.setTabbarCallbackListener {
            var inflate = layoutInflater.inflate(R.layout.base_title_view, null)
            title = inflate.findViewById(R.id.title)
            titleBack = inflate.findViewById(R.id.titleBack)
            titleAdd = inflate.findViewById(R.id.titleAdd)
            titleRight = inflate.findViewById(R.id.titleRight)
            when (it) {
                0 -> {
                    if (!isMsmAndContact) {
                        if (msmFragment == null) {
                            msmFragment = MSMFragment()
                        }
//                        isMsmAndContact = true
                        switchContent(msmFragment!!)
                    } else {
                        if (contactFragment == null) {
                            contactFragment = ContactFragment()
                        }
//                        isMsmAndContact = false
                        switchContent(contactFragment!!)
                    }

                    ll?.removeAllViews()
                    ll?.addView(inflate1)
                }
                1 -> {
                    ll?.removeAllViews()
                    ll?.addView(inflate)
                    titleBack?.visibility = View.GONE
                    titleAdd?.visibility = View.VISIBLE
                    titleRight?.visibility = View.GONE
                    if (antiLostFragment == null) {
                        antiLostFragment = AntiLostFragment()
                    }
                    title?.text = "防丢"
                    switchContent(antiLostFragment!!)
                    titleAdd?.setOnClickListener {
                        var intent = Intent()
                        intent.setClass(this, AddActivity::class.java)
                        startActivity(intent)
                    }
                }
                2 -> {
                    if (locationFragment == null) {
                        locationFragment = LocationFragment()
                    }
                    ll?.removeAllViews()
                    ll?.addView(inflate)
                    titleBack?.visibility = View.GONE
                    titleAdd?.visibility = View.GONE
                    titleRight?.visibility = View.GONE
                    title?.text = "定位"
                    switchContent(locationFragment!!)
                }
                3 -> {
                    if (homeFragment == null) {
                        homeFragment = MoreFragment()
                    }
                    ll?.removeAllViews()
                    ll?.addView(inflate)
                    titleBack?.visibility = View.GONE
                    titleAdd?.visibility = View.GONE
                    title?.text = "更多"
                    titleRight?.visibility = View.VISIBLE
                    switchContent(homeFragment!!)
                }
            }
        }

    }

    /**
     * 切换当前显示的fragment
     */
    private fun switchContent(to: Fragment) {
        if (current !== to) {
            val transaction = supportFragmentManager.beginTransaction()

            if (current != null) {
                transaction.hide(current)
            }
            if (!to.isAdded) { // 先判断是否被add过
                transaction.add(R.id.homeFrameLayout, to).commit()
            } else {

                transaction.show(to).commit() // 隐藏当前的fragment，显示下一个
            }
            current = to
        }
    }

    private var exitTime: Double = 0.toDouble()
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis().toDouble()
            } else {
                // TODO 退出客户端
                // 退出
                (application as GoockrApplication).exit()
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}
