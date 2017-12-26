package com.goockr.smsantilost.views.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.goockr.smsantilost.GoockrApplication
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContentBeanDao
import com.goockr.smsantilost.entries.MsmBean
import com.goockr.smsantilost.entries.MsmBeanDao
import com.goockr.smsantilost.graphics.SwipeMenuLayout
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.MSM_MANAGER_RESULT_ID
import com.goockr.smsantilost.utils.DateUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.msm.MSMManagerActivity
import com.goockr.smsantilost.views.activities.msm.NewMSMActivity
import com.goockr.smsantilost.views.adapters.MsmSwipeAdapter
import kotlinx.android.synthetic.main.empty_view.view.*
import kotlinx.android.synthetic.main.fragment_msm.*
import kotlinx.android.synthetic.main.item_msm_swipe.view.*
import java.util.*
import kotlin.concurrent.thread


/**
 * Created by ning
 */

class MSMFragment : BaseFragment() {
    var mDatas: ArrayList<MsmBean> = ArrayList()
    var manager: LinearLayoutManager? = null
    var isSearch: Boolean = false
    var ischeck: Boolean = false
    private var msmBeanDao: MsmBeanDao? = null
    private var contentBeanDao: ContentBeanDao? = null

    private var msmSwipeDelMenuAdapter: MsmSwipeDelMenuAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return setContentView(R.layout.fragment_msm)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        manager = LinearLayoutManager(activity)
        recycleView.layoutManager = manager
        msmSwipeDelMenuAdapter = MsmSwipeDelMenuAdapter(activity, mDatas)
        emptyView.ivEmptyView.setImageResource(R.mipmap.no_msg_figure)
        emptyView.tvEmptyView.text = getString(R.string.emptySms)
        recycleView.adapter = msmSwipeDelMenuAdapter


        initRefresh()
        val goockrApplication = activity.application as GoockrApplication
        msmBeanDao = goockrApplication.mDaoSession?.msmBeanDao
        contentBeanDao = goockrApplication.mDaoSession?.contentBeanDao
        val list = msmBeanDao?.queryBuilder()?.build()?.list()
        if (list != null) {
            //排序
            Collections.sort(list) { arg0, arg1 ->
                DateUtils.stringToLong(arg0.smsTime, "yyyy-MM-dd_HH-mm-ss")
                        .compareTo(DateUtils.stringToLong(arg1.smsTime, "yyyy-MM-dd_HH-mm-ss"))
            }
            // 倒序排列
            Collections.reverse(list)
            mDatas.addAll(list)
            setEmptyView()
            msmSwipeDelMenuAdapter?.notifyDataSetChanged()
        }
        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                        if (!ischeck) {
                            floatSetting.visibility = View.VISIBLE
                            floatSend.visibility = View.VISIBLE
                        }
                    }
                    else -> {
                        floatSetting.visibility = View.GONE
                        floatSend.visibility = View.GONE
                    }
                }
            }
        })
        floatSend.setOnClickListener {
            val baseActivity = activity as BaseActivity
            baseActivity.showActivity(NewMSMActivity::class.java)
        }
        floatSetting.setOnClickListener {
            val extras = Bundle()
            extras.putSerializable("DATA", mDatas)
            extras.putBoolean("IS_SEARCH", isSearch)
            showActivityForResult(MSMManagerActivity::class.java, extras, MSM_MANAGER_RESULT_ID)
        }

    }

    private inner class MsmSwipeDelMenuAdapter(mContext: Context, mDatas: ArrayList<MsmBean>) : MsmSwipeAdapter(mContext, this, mDatas) {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            if (position == 0 && isSearch) return
            (holder.itemView as SwipeMenuLayout).isOpen {
                if (it) {
                    floatSetting.visibility = View.GONE
                    floatSend.visibility = View.GONE

                } else {
                    if (!ischeck) {
                        floatSetting.visibility = View.VISIBLE
                        floatSend.visibility = View.VISIBLE
                    }
                }
            }
            //删除短信
            holder.itemView.btnDel.setOnClickListener {
                val bean = mDatas?.get(holder.adapterPosition)
                msmBeanDao?.delete(bean)
                val lists = contentBeanDao?.queryBuilder()?.where(ContentBeanDao.Properties.Mid.eq(bean?.id))?.list()
                for (c in 0 until lists!!.count()) {
                    contentBeanDao?.delete(lists[c])
                }
                (holder.itemView as SwipeMenuLayout).quickClose()
                mDatas?.removeAt(holder.adapterPosition)
                notifyDataSetChanged()
                setEmptyView()
            }

        }
    }

    private fun setEmptyView() {
        if (isSearch && mDatas.size == 1) {
            emptyView.visibility = View.VISIBLE
        } else if (mDatas.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
    }

    private fun initRefresh() {
        //改变加载显示的颜色
        refresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.parseColor("#4285f4"))
        //设置初始时的大小
        refresh.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL)
        //设置监听
        refresh.setOnRefreshListener {
            thread {
                SystemClock.sleep(1000)
                activity.runOnUiThread {
                    refresh.isRefreshing = false
                    val element = MsmBean()
                    element.smsTitle = "10086"
                    element.smsTime = "24:00"
                    mDatas.add(0, element)
                    msmSwipeDelMenuAdapter?.isSearch(true)
                    msmSwipeDelMenuAdapter?.notifyDataSetChanged()
                    isSearch = true
                    refresh.isEnabled = false
                }

            }
            //设置向下拉多少出现刷新
            refresh.setDistanceToTriggerSync(100)
            //设置刷新出现的位置
            refresh.setProgressViewEndTarget(false, 200)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.MSM_MANAGER_RESULT_ID && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val serializable = extras?.getSerializable("MSM_MANAGER")
            val arrayList = serializable as ArrayList<MsmBean>
            mDatas.clear()
            if (isSearch) {
                mDatas.add(MsmBean())
            }
            mDatas.addAll(arrayList)
            msmSwipeDelMenuAdapter?.notifyDataSetChanged()
        }
    }

    fun setDates() {
        mDatas.clear()
        if (isSearch) {
            mDatas.add(MsmBean())
        }
        val goockrApplication = activity.application as GoockrApplication
        val msmBeanDao = goockrApplication.mDaoSession?.msmBeanDao
        val list = msmBeanDao?.queryBuilder()?.build()?.list()
        if (list != null) {
            //排序
            Collections.sort(list) { arg0, arg1 ->
                DateUtils.stringToLong(arg0.smsTime, "yyyy-MM-dd_HH-mm-ss")
                        .compareTo(DateUtils.stringToLong(arg1.smsTime, "yyyy-MM-dd_HH-mm-ss"))
            }
            // 倒序排列
            Collections.reverse(list)
            mDatas.addAll(list)
        }
        setEmptyView()
        msmSwipeDelMenuAdapter?.notifyDataSetChanged()
    }

    private fun stringEqDate(oldeDate: String, newDate: String): Boolean {
        val oldLong = DateUtils.stringToLong(oldeDate, "yyyy-MM-dd_HH-mm-ss")
        val newLong = DateUtils.stringToLong(newDate, "yyyy-MM-dd_HH-mm-ss")
        return newLong - oldLong > 5000
    }
}
