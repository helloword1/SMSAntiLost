package com.goockr.smsantilost.views.activities.more

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.FriendsListBean
import com.goockr.smsantilost.utils.MyComparator
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.NewFriendsAdapter
import kotlinx.android.synthetic.main.activity_new_friends.*
import kotlinx.android.synthetic.main.empty_view.view.*
import java.util.*

class NewFriendsActivity(override val contentView: Int = R.layout.activity_new_friends) : BaseActivity() {

    private var mDatas: ArrayList<FriendsListBean>? = null
    // 假网址
    private var pic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511779025679&di=65dece40d00461b66922866f8bb8e448&imgtype=0&src=http%3A%2F%2Fpic4.58cdn.com.cn%2Fmobile%2Fbig%2Fn_v1bkujjdzdro3fox3lcidq.jpg"
    private var mAdapter: NewFriendsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initListView()
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
        title?.text = getString(R.string.newFriends)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    private fun initMData() {
        mDatas = ArrayList()
        var bean1 = FriendsListBean(pic,"阿凡达","123")
        var bean2 = FriendsListBean(pic,"布吉岛","123")
        var bean3 = FriendsListBean(pic,"次元","123")
        var bean4 = FriendsListBean(pic,"滴滴","123")
        var bean5 = FriendsListBean(pic,"峨眉派","123")
        var bean6 = FriendsListBean(pic,"佛跳墙","123")
        var bean7 = FriendsListBean(pic,"狗剩","123")
        var bean8 = FriendsListBean(pic,"核算","123")
        mDatas?.add(bean1)
        mDatas?.add(bean2)
        mDatas?.add(bean3)
        mDatas?.add(bean4)
        mDatas?.add(bean5)
        mDatas?.add(bean6)
        mDatas?.add(bean7)
        mDatas?.add(bean8)
        val comparator = MyComparator()
        Collections.sort(mDatas, comparator)
    }

    @SuppressLint("InflateParams")
    private fun initListView() {
        initMData()
        mAdapter = NewFriendsAdapter(this,mDatas)
        val emptyView = layoutInflater.inflate(R.layout.empty_view, null)
        emptyView.ivEmptyView.setImageResource(R.mipmap.no_mail_list_figure)
        emptyView.tvEmptyView.text=getString(R.string.notNewsFriends)
        emptyView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)//设置LayoutParams
        (lv_NewFriends.parent as ViewGroup).addView(emptyView)//添加到当前的View hierarchy
        lv_NewFriends.emptyView= emptyView
        lv_NewFriends.adapter = mAdapter
    }
}
