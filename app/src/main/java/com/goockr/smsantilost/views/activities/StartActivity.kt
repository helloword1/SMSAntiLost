package com.goockr.smsantilost.views.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.login.LoginActivity


/**
 * 程序入口界面
 *
 * 修改时间:
 * 修改内容:
 */
class StartActivity(override val contentView: Int = R.layout.activity_start) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this.isTaskRoot) {
            //如果你就放在launcher Activity中话，这里可以直接return了
            val mainIntent = intent

            val action = mainIntent.action

            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                //finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
                finish()
                return
            }
        }
    }

    public override fun initView() {
        ll!!.visibility = View.GONE
        status_bar!!.visibility = View.GONE
        jumpNextPage()

    }


    private fun jumpNextPage() {
        val isLogin = preferences?.getStringValue(Constant.HAD_LOGIN)
        if (TextUtils.equals(isLogin, "true")) {
            showActivity(HomeActivity::class.java)
//            showActivity(TestActivity::class.java)
            finish()
            return
        } else {
            Thread(Runnable {
                SystemClock.sleep(2000)
                runOnUiThread {
                    showActivity(LoginActivity::class.java)
                    finish()
                }
            }).start()
//            isPermissionVail()
        }
    }

    //申请权限
    private fun isPermissionVail() {
        val sdCardWritePermission = packageManager.checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, packageName) == PackageManager.PERMISSION_GRANTED
        val phoneSatePermission = packageManager.checkPermission(
                Manifest.permission.READ_PHONE_STATE, packageName) == PackageManager.PERMISSION_GRANTED
        val writeContactsPermission = packageManager.checkPermission(
                Manifest.permission.WRITE_CONTACTS, packageName) == PackageManager.PERMISSION_GRANTED
        val cameraPermission = packageManager.checkPermission(
                Manifest.permission.CAMERA, packageName) == PackageManager.PERMISSION_GRANTED
        val locationPermission = packageManager.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION, packageName) == PackageManager.PERMISSION_GRANTED
        val smsPermission = packageManager.checkPermission(
                Manifest.permission.READ_SMS, packageName) == PackageManager.PERMISSION_GRANTED
        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission || !writeContactsPermission
                || !cameraPermission || !locationPermission || !smsPermission) {
            requestPermission()
        }
    }

    //申请权限
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}
