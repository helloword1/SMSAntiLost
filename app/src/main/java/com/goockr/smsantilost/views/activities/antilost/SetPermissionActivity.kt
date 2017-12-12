package com.goockr.smsantilost.views.activities.antilost

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.PermissionBean
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.PermissionAdapter
import kotlinx.android.synthetic.main.activity_set_permission.*


class SetPermissionActivity(override val contentView: Int = R.layout.activity_set_permission) : BaseActivity() {

    private var mDataList: ArrayList<PermissionBean>? = null
    private var mAdapter: PermissionAdapter? = null
    private var mCurrentPosition = 0
    private var dialog:AlertDialog? = null
    private var tv_Cancel: TextView? = null
    private var tv_Ensure: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initMData()
        initDialog()
        initListView()
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

        titleOk?.text = getString(R.string.concert)
        titleOk?.setTextColor(resources.getColor(R.color.appGray))
        titleOk?.isClickable = false
        titleOk?.visibility = View.VISIBLE
        titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))

        title?.text = getString(R.string.ChangeRights)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 初始化好友
     */
    private fun initMData() {
        mDataList = ArrayList()
        var bean1 = PermissionBean("李小白")
        var bean2 = PermissionBean("张小黑")
        var bean3 = PermissionBean("王小绿")
        mDataList?.add(bean1)
        mDataList?.add(bean2)
        mDataList?.add(bean3)
    }

    /**
     * 初始化listView
     */
    private fun initListView() {
        mAdapter = PermissionAdapter(this,mDataList)
        lv_Permission.adapter = mAdapter
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 确定按钮
        titleOk?.setOnClickListener {
            dialog?.show()
        }
        // listView点击
        lv_Permission.setOnItemClickListener { parent, view, position, id ->
            mCurrentPosition = position
            mAdapter?.setCurrentPosition(position)
            mAdapter?.notifyDataSetChanged()
        }
        // 取消和确定两个按钮点击
        tv_Cancel?.setOnClickListener {
            dialog?.hide()
        }
        tv_Ensure?.setOnClickListener {
            ToastUtils.showShort(this,mDataList?.get(mCurrentPosition)?.name)
        }
    }

    private fun initDialog() {
        var builder = AlertDialog.Builder(this)
        var customView = layoutInflater.inflate(R.layout.dialog_permission, null)
        builder.setView(customView)
        builder.setIcon(R.mipmap.ic_launcher)
        dialog = builder.create()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        tv_Cancel = customView.findViewById(R.id.tv_Cancel)
        tv_Ensure = customView.findViewById(R.id.tv_Ensure)
    }
}
