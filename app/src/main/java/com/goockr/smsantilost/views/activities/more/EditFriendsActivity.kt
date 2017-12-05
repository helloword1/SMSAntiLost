package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_friends.*
import kotlinx.android.synthetic.main.page1_edit_friends.*
import kotlinx.android.synthetic.main.page2_edit_friends.*

class EditFriendsActivity(override val contentView: Int = R.layout.activity_edit_friends) : BaseActivity() {

    private var mCurrentPage = 1
    private var mName: String? = null
    private var mNum: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMData()
        initMView()
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
        titleOk = titleLayout.findViewById(R.id.titleOk)
        title?.text = "编辑好友"
        ll?.addView(titleLayout)
        tv_FriendsName.text = mName
        tv_FriendsNum.text = mNum
    }

    /**
     * 获取数据
     */
    private fun initMData() {
        mName = intent.getStringExtra("friendsName")
        mNum = intent.getStringExtra("friendsNum")
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 添加备注
        ll_AddRemarks.setOnClickListener {
            showPage2()
        }
        // 监听备注editText
        et_InputFriendsRemark.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    titleOk?.setTextColor(resources.getColor(R.color.appGray))
                    iv_CleanRemark.visibility = View.GONE
                } else {
                    titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
                    iv_CleanRemark.visibility = View.VISIBLE
                }
            }
        })
        // 清空
        iv_CleanRemark.setOnClickListener {
            et_InputFriendsRemark.text = null
        }
        // 保存备注
        titleOk?.setOnClickListener {
            showPage1()
            tv_FriendsRemark.text = "昵称:" + et_InputFriendsRemark.text
            tv_FriendsRemark.visibility = View.VISIBLE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCurrentPage == 1) {
                finish()
            } else if (mCurrentPage == 2) {
                showPage1()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showPage1() {
        ll_page1.visibility = View.VISIBLE
        mCurrentPage = 1
        ll_page2.visibility = View.GONE
        titleOk?.visibility = View.GONE
    }

    private fun showPage2() {
        ll_page1.visibility = View.GONE
        mCurrentPage = 2
        ll_page2.visibility = View.VISIBLE
        titleOk?.visibility = View.VISIBLE
        titleOk?.text = "完成"
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        if (TextUtils.isEmpty(et_InputFriendsRemark.text)) {
            titleOk?.setTextColor(resources.getColor(R.color.appGray))
            iv_CleanRemark.visibility = View.GONE
        } else {
            titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
            iv_CleanRemark.visibility = View.VISIBLE
        }
    }
}
