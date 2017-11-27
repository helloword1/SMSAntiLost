package com.goockr.smsantilost.views.fragments

import android.content.Context
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
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.MsmBean
import com.goockr.smsantilost.graphics.SwipeMenuLayout
import com.goockr.smsantilost.views.adapters.MsmSwipeAdapter
import kotlinx.android.synthetic.main.fragment_msm.*
import kotlinx.android.synthetic.main.item_city_swipe.view.*
import kotlin.concurrent.thread


/**
 * Created by ning.wen on 2016/11/1.
 */

class MSMFragment : BaseFragment() {
    var mDatas: ArrayList<MsmBean> = ArrayList()
    var manager: LinearLayoutManager? = null
    var isSearch: Boolean = false
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
        recycleView.adapter = msmSwipeDelMenuAdapter
        initRefresh()
        for (mData in 1..10) {
            var element = MsmBean()
            element.Content = "你好，这是短信内容"
            element.mTitl = "10086"
            element.mTime = "24:00"
            element.isShow = mData == 0 || mData == 1 || mData == 2
            mDatas.add(element)
        }
        msmSwipeDelMenuAdapter?.notifyDataSetChanged()
        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                        floatSetting.visibility = View.VISIBLE
                        floatSend.visibility = View.VISIBLE
                    }
                    else -> {
                        floatSetting.visibility = View.GONE
                        floatSend.visibility = View.GONE
                    }
                }
            }
        })

    }

    private inner class MsmSwipeDelMenuAdapter(mContext: Context, mDatas: ArrayList<MsmBean>) : MsmSwipeAdapter(mContext, mDatas) {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            if (position == 0 && isSearch) return
            (holder.itemView as SwipeMenuLayout).isOpen {
                if (it) {
                    floatSetting.visibility = View.GONE
                    floatSend.visibility = View.GONE

                } else {
                    floatSetting.visibility = View.VISIBLE
                    floatSend.visibility = View.VISIBLE
                }
            }
            holder.itemView.btnDel.setOnClickListener {
                (holder.itemView as SwipeMenuLayout).quickClose()
                mDatas?.removeAt(holder.adapterPosition)
                notifyDataSetChanged()
            }
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

}
