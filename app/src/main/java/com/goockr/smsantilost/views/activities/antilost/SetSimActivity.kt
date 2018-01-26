package com.goockr.smsantilost.views.activities.antilost

import android.content.Context
import android.os.Bundle
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_set_sim.*
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.goockr.smsantilost.graphics.MyToast
import cxx.utils.StringUtils


class SetSimActivity(override val contentView: Int = R.layout.activity_set_sim) : BaseActivity() {
    private var isInsert = false
    private var insertStr = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
    }

    /**
     * 初始化title
     */
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        title?.text = getString(R.string.SIMNumberSetting)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)

        val extras = intent.extras
        isInsert = extras.getBoolean(Constant.IS_INSERT)
        insertStr = extras.getString(Constant.INSERT_STR)
        tv_IsInsertSim.text = if (isInsert) {
            getString(R.string.insert)
        } else {
            getString(R.string.No)
        }
        et_InputSim.setText(insertStr)
        et_InputSim.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val phoneNum = et_InputSim.text.toString()
                if (!StringUtils.isPhone(phoneNum)) {
                    MyToast.showToastCustomerStyleText(this@SetSimActivity, getString(R.string.inputRightNumber))
                    return@OnEditorActionListener false
                }
                // 先隐藏键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(this@SetSimActivity
                                .currentFocus.windowToken,
                                InputMethodManager.HIDE_NOT_ALWAYS)

                preferences?.putValue(Constant.SIM_CARD_NUM, phoneNum)

                finish()
                // 搜索，进行自己要的操作...
                return@OnEditorActionListener true
            }
            false
        })
    }

}

