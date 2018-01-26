package com.goockr.smsantilost.views.activities.more

import android.os.Bundle
import android.text.TextUtils
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AntiDisturbBean
import com.goockr.smsantilost.entries.NetApi
import com.goockr.smsantilost.graphics.LikeAppleComfirDialog
import com.goockr.smsantilost.graphics.MyToast
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.ADDRESS_RANGE
import com.goockr.smsantilost.utils.Constant.TOKEN
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.utils.https.MyStringCallback
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.AreaAdapter
import com.google.gson.Gson
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import com.zhy.http.okhttp.OkHttpUtils
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_anti_disturb.*
import okhttp3.Call
import org.json.JSONObject
import java.lang.Exception

/**
 * 防打扰区域
 */
class AntiDisturbActivity(override val contentView: Int = R.layout.activity_anti_disturb) : BaseActivity() {

    private var mDatas = ArrayList<AntiDisturbBean.DataBean>()
    private var mAdapter: AreaAdapter? = null
    private var pageNo = 1
    private var pageSize = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()

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
        val value = preferences?.getStringValue(ADDRESS_RANGE)
        if (NotNull.isNotNull(value) && TextUtils.equals(value, "false")) {
            btn_ShowContacts.setEnableEffect(false)
        } else {
            btn_ShowContacts.setEnableEffect(false)
        }
        btn_ShowContacts.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                btn_ShowContacts.setEnableEffect(true)
                preferences?.putValue(ADDRESS_RANGE, "true")
            } else {
                btn_ShowContacts.setEnableEffect(false)
                preferences?.putValue(ADDRESS_RANGE, "false")
            }
        }
    }

    /**
     * 初始化区域数据
     */
    private fun initMData() {
        showProgressDialog()
        OkHttpUtils
                .post()
                .url(Constant.BASE_URL + NetApi.FIND_PREVENT_DISTURB)
                .addParams("token", preferences?.getStringValue(TOKEN))
                .addParams("pageNo", pageNo.toString())
                .addParams("pageSize", pageSize.toString())
                .build()
                .execute(object : MyStringCallback(this) {
                    override fun onResponse(response: String?, id: Int) {
                        val a = Gson().fromJson(response, AntiDisturbBean::class.java)
                        try {
                            if (TextUtils.equals(a.result, "0")) {
                                val data = a.data
                                setDates(data)
                            }
                            LogUtils.mi(response!!)
                        } catch (e: Exception) {

                        }
                        dismissDialog()
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        dismissDialog()
                        MyToast.showToastCustomerStyleText(this@AntiDisturbActivity, getString(R.string.networkError))
                    }
                })
    }

    private fun setDates(data: MutableList<AntiDisturbBean.DataBean>) {
        mDatas.clear()
//        if (!mDatas.isEmpty()) {
//            mDatas.removeAt(mDatas.size - 1)
//         if (!mDatas.isEmpty()) {
//            mDatas.removeAt(mDatas.size - 1)
//        }
        if (NotNull.isNotNull(data)) {
            mDatas.addAll(data)
        }
        val element = AntiDisturbBean.DataBean()
        element.antiname = getString(R.string.addAntiLostArea)
        mDatas.add(element)
        mAdapter?.notifyDataSetChanged()
        mAdapter?.setOnSwipeDeleteListener { position, swipeMenuLayout ->
            val dialog = LikeAppleComfirDialog(this@AntiDisturbActivity, getString(R.string.deleteAntititle), getString(R.string.comfir), getString(R.string.cancel), 2)
            dialog.show()
            dialog.setDialogClicklistener(object : LikeAppleComfirDialog.DialogAppleClickListener {
                //删除
                override fun doConfirm() {
                    deleteAntiDisturb(position, swipeMenuLayout)
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
    private fun deleteAntiDisturb(position: Int, swipeMenuLayout: SwipeMenuLayout) {
        val id = mDatas[position].id
        OkHttpUtils
                .post()
                .url(Constant.BASE_URL + NetApi.DELETE_PREVENT_DISTURB)
                .addParams("token", preferences?.getStringValue(TOKEN))
                .addParams("id", id)
                .build()
                .execute(object : MyStringCallback(this) {
                    override fun onResponse(response: String?, id: Int) {
                        val res = JSONObject(response)
                        if (TextUtils.equals(res.getString("result"), "0")) {
                            mDatas.removeAt(position)
                            mAdapter?.notifyDataSetChanged()
                            swipeMenuLayout.quickClose()
                            MyToast.showToastCustomerStyleText(this@AntiDisturbActivity, getString(R.string.DeleteSuccess))
                        }
                        LogUtils.mi(response!!)
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        dismissDialog()
                        MyToast.showToastCustomerStyleText(this@AntiDisturbActivity, getString(R.string.networkError))
                    }
                })
    }
}
