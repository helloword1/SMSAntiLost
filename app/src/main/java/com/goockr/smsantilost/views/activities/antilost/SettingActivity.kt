package com.goockr.smsantilost.views.activities.antilost

import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AntilostBean
import com.goockr.smsantilost.entries.DeviceBeanDao
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 每个钥匙的设置页面（解绑，提醒声选择，提醒声时长等）
 */
class SettingActivity(override val contentView: Int = R.layout.activity_setting) : BaseActivity(), View.OnClickListener {
    private var isInsert = false
    private var device: AntilostBean? = null
    private var myAlertDialog: MyAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initDialog()
        initClickEvent()
    }

    /**
     * 初始化title
     */
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        title?.text = getString(R.string.setting)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        val extras = intent.extras
        isInsert = extras.getBoolean("IS_INSERT")
        device = extras.getSerializable("device") as AntilostBean
    }

    /**
     * 各个点击事件
     */
    private fun initClickEvent() {
        ll_SetIndexOut.setOnClickListener(this)
        ll_SetAntiLost.setOnClickListener(this)
        ll_SetSim.setOnClickListener(this)
        ll_SetName.setOnClickListener(this)
        ll_SetPermission.setOnClickListener(this)
        ll_Unbundling.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_SetIndexOut -> {
                //越界提醒
                showActivity(SetIndexOutActivity::class.java)
            }
            R.id.ll_SetAntiLost -> {
                //双向防丢
                showActivity(TwoWayAntiActivity::class.java)
            }
            R.id.ll_SetSim -> {
                val extras = Bundle()
                extras.putBoolean("IS_INSERT", isInsert)
                //SIM设置
                showActivity(SetSimActivity::class.java, extras)
            }
            R.id.ll_SetName -> {
                val extras = Bundle()
                extras.putString("DEVICE_NAME", device?.deviceName)
                //修改名称
                showActivity(SetNameActivity::class.java, extras)
            }
            R.id.ll_SetPermission -> {
                //转移权限
                showActivity(SetPermissionActivity::class.java)
            }
            //解绑
            R.id.ll_Unbundling -> {
                myAlertDialog?.show()
            }
        }
    }

    private fun initDialog() {
        myAlertDialog = MyAlertDialog(this).setTitle(getString(R.string.setUnBind))
                .setContent(getString(R.string.unbindContent))
        myAlertDialog?.setOnDialogListener(object : MyAlertDialog.OnDialogListener {
            override fun onConfirmListener() {
                // 解绑逻辑
                val deviceBeanDao = goockrApplication?.mDaoSession?.deviceBeanDao
                val bean = deviceBeanDao?.queryBuilder()?.where(DeviceBeanDao.Properties.Mac.eq(device?.mac))?.unique()
                if (NotNull.isNotNull(bean)) {
                    deviceBeanDao?.delete(bean)
                    goockrApplication?.exitToHomeNo()
                }else{
                    MyToast.showToastCustomerStyleText(this@SettingActivity,getString(R.string.binddingSucceed))
                }
                if (NotNull.isNotNull(instance)) {
                    instance?.disConnect()
                }
                MyToast.showToastCustomerStyleText(this@SettingActivity, getString(R.string.unbindSucceed))
            }

            override fun onCancelListener() {
                myAlertDialog?.hide()
            }

        })
    }
}
