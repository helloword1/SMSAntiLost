package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.location.LocationManager
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.utils.Constant.SHOW_CONTACTS_ENABLE
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_more_setting.*

/**
 * 防打扰区域
 */
class MoreSettingActivity(override val contentView: Int = R.layout.activity_more_setting) : BaseActivity() {

    override fun initView() {
        title?.text = getString(R.string.setting)
        val contactsEnable = preferences?.getStringValue(SHOW_CONTACTS_ENABLE)
        btnShowContacts.isChecked = !(NotNull.isNotNull(contactsEnable) && !contactsEnable!!.toBoolean())


    }

    override fun onResume() {
        super.onResume()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        btnMyLocation.isChecked = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        btnMyLocation.setShadowEffect(true)
        btnMyLocation.setEnableEffect(true)
        btnMyLocation.setOnCheckedChangeListener { view, isChecked ->
            initDialog()
        }
    }
    private fun initDialog() {
        val myAlertDialog = MyAlertDialog(this).setTitle(getString(R.string.locationSetting)).setContent(getString(R.string.lsettingc))
                .setConfirm(getString(R.string.comfir))
        myAlertDialog.setOnDialogListener(object : MyAlertDialog.OnDialogListener {
            override fun onConfirmListener() {
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                myAlertDialog.hide()
            }

            override fun onCancelListener() {
                myAlertDialog.hide()
            }

        })
        myAlertDialog.show()
    }
}
