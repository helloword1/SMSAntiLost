package com.goockr.smsantilost.views.activities.antilost

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import cxx.utils.StringUtils
import kotlinx.android.synthetic.main.page1_add_friend.*

class AddFriendActivity(override val contentView: Int = R.layout.activity_add_friend) : BaseActivity() {

    private var dialog: AlertDialog? = null
    private var tv_Cancel: TextView? = null
    private var tv_Ensure: TextView? = null
    private var tv_Text1: TextView? = null
    private var tv_Text2: TextView? = null
    private var tv_Text3: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initDialog()
        initClick()
    }

    /**
     * 初始化title
     */
    private fun initMView() {
        ll?.removeAllViews()

        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleRight = titleLayout.findViewById(R.id.titleRight)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        titleOk = titleLayout.findViewById(R.id.titleOk)

        title?.text = getString(R.string.addFriends)
        titleOk?.text = getString(R.string.send)
        titleOk?.visibility = View.VISIBLE
        titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
        titleBack?.setOnClickListener { finish() }

        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClick() {
        titleOk?.setOnClickListener {
            // 检查好友手机号码
            val flag = true
            if (NotNull.isNotNull(etNewPhone.text.toString()) && StringUtils.isPhone(etNewPhone.text.toString())) {
                MyToast.showToastCustomerStyleText(this, getString(R.string.deviceDeveloping))
            } else if (!flag) {
                tv_Text1?.text = getString(R.string.notRegist)
                dialog?.show()
            } else {
                tv_Text1?.text = getString(R.string.notEffect)
                tv_Ensure?.text = getString(R.string.trtAgain)
                dialog?.show()
            }

        }
        inviteMyFamily.setOnClickListener {
            MyToast.showToastCustomerStyleText(this, getString(R.string.deviceDeveloping))
        }
        etNewPhone.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isEmpty()){
                    titleOk?.setTextColor(ContextCompat.getColor(this@AddFriendActivity,R.color.msmTextColorGray))
                }else{
                    titleOk?.setTextColor(ContextCompat.getColor(this@AddFriendActivity,R.color.blue))
                }
            }

        })
    }

    private fun initDialog() {
        val builder = AlertDialog.Builder(this)
        val customView = layoutInflater.inflate(R.layout.dialog_permission, null)
        builder.setView(customView)
        builder.setIcon(R.mipmap.ic_launcher)
        dialog = builder.create()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        tv_Cancel = customView.findViewById(R.id.tv_Cancel)
        tv_Cancel?.text = getString(R.string.back)
        tv_Ensure = customView.findViewById(R.id.tv_Ensure)
        tv_Text1 = customView.findViewById(R.id.tv_Text1)
        tv_Text2 = customView.findViewById(R.id.tv_Text2)
        tv_Text3 = customView.findViewById(R.id.tv_Text3)
        tv_Text2?.visibility = View.GONE
        tv_Text3?.visibility = View.GONE
        tv_Ensure?.setOnClickListener {
            dialog?.hide()
        }
        tv_Cancel?.setOnClickListener {
            dialog?.hide()
        }
    }

}
