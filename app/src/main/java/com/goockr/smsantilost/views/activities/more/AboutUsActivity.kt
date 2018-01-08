package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.net.Uri
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyAlertDialog
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_about_us.*


/**
 * 关于我们
 */
class AboutUsActivity(override val contentView: Int = R.layout.activity_about_us) : BaseActivity() {
    override fun initView() {
        title?.text = getString(R.string.aboutMe)
        initDate()
    }

    private fun initDate() {
        llServicePhone.setOnClickListener {
            val dialog = MyAlertDialog(this).setTitle(getString(R.string.ifCall)).setConfirm(getString(R.string.call)).
                    setContent(Constant.ABOUT_US_PHONE)
            dialog.show()
            dialog.setOnDialogListener(object : MyAlertDialog.OnDialogListener {
                override fun onConfirmListener() {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Constant.ABOUT_US_PHONE}"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                override fun onCancelListener() {
                    dialog.dismiss()
                }

            })
        }
    }

}
