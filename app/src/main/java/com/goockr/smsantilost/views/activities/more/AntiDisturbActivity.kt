package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AntiAddressBean
import com.goockr.smsantilost.entries.AntiAddressBeanDao
import com.goockr.smsantilost.graphics.LikeAppleComfirDialog
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.AreaAdapter
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_anti_disturb.*

/**
 * 防打扰区域
 */
class AntiDisturbActivity(override val contentView: Int = R.layout.activity_anti_disturb) : BaseActivity() {

    private var mDatas = ArrayList<AntiAddressBean>()
    private var mAdapter: AreaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initClickEvent()
    }

    override fun onResume() {
        super.onResume()
        initMData()
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
        title?.text = getString(R.string.antiDisturb)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        mAdapter = AreaAdapter(this, mDatas)
        lv_AreaList.adapter = mAdapter
    }

    /**
     * 初始化区域数据
     */
    private fun initMData() {
        mDatas.clear()
        val antiAddressBeanDao = goockrApplication?.mDaoSession?.antiAddressBeanDao
        val list = antiAddressBeanDao?.queryBuilder()?.list()
        if (NotNull.isNotNull(list)) {
            mDatas.addAll(list!!)
        }
        val element = AntiAddressBean()
        element.name = getString(R.string.addAntiLostArea)
        mDatas.add(element)
        mAdapter?.notifyDataSetChanged()
        mAdapter?.setOnSwipeDeleteListener { position, swipeMenuLayout ->
            val dialog = LikeAppleComfirDialog(this@AntiDisturbActivity, getString(R.string.deleteAntititle), getString(R.string.comfir), getString(R.string.cancel), 2)
            dialog.show()
            dialog.setDialogClicklistener(object : LikeAppleComfirDialog.DialogAppleClickListener {
                override fun doConfirm() {
                    val unit = antiAddressBeanDao?.queryBuilder()?.where(AntiAddressBeanDao.Properties.Id.eq(mDatas[position].id))?.unique()
                    if (NotNull.isNotNull(unit)) {
                        antiAddressBeanDao?.delete(unit)
                        mDatas.removeAt(position)
                        swipeMenuLayout.quickClose()
                        mAdapter?.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                }

                override fun doCancel() {
                    dialog.dismiss()
                    swipeMenuLayout.quickClose()
                }

            })

        }
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
    }
}
