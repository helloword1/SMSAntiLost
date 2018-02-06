package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.FriendsListBean
import com.goockr.smsantilost.utils.MyComparator
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.antilost.AddFriendActivity
import com.goockr.smsantilost.views.adapters.FriendsListAdapter
import kotlinx.android.synthetic.main.activity_my_friends.*
import kotlinx.android.synthetic.main.empty_view.view.*
import java.util.*

class MyFriendsActivity(override val contentView: Int = R.layout.activity_my_friends) : BaseActivity() {

    private var mDatas: ArrayList<FriendsListBean>? = null
    // 假网址
    private var pic = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511779025679&di=65dece40d00461b66922866f8bb8e448&imgtype=0&src=http%3A%2F%2Fpic4.58cdn.com.cn%2Fmobile%2Fbig%2Fn_v1bkujjdzdro3fox3lcidq.jpg"
    private var mAdapter: FriendsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
    }

    override fun onResume() {
        super.onResume()
        initFriendsList()
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
        title?.text = getString(R.string.myFiends)
        titleOk?.text = getString(R.string.add)
        titleOk?.setTextColor(resources.getColor(R.color.colorPrimary))
        titleOk?.visibility = View.VISIBLE
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 初始化RecyclerView
     */
    private fun initFriendsList() {
        initMData()
        mAdapter = FriendsListAdapter(this,mDatas)
        lv_FriendsList.adapter = mAdapter
    }

    /**
     * 假数据
     */
    private fun initMData() {
        mDatas = ArrayList()
        /*val bean1 = FriendsListBean(pic,"阿凡达","123")
        val bean2 = FriendsListBean(pic,"布吉岛","123")
        val bean3 = FriendsListBean(pic,"次元","123")
        val bean4 = FriendsListBean(pic,"滴滴","123")
        val bean5 = FriendsListBean(pic,"峨眉派","123")
        val bean6 = FriendsListBean(pic,"佛跳墙","123")
        val bean7 = FriendsListBean(pic,"狗剩","123")
        val bean8 = FriendsListBean(pic,"核算","123")
        mDatas?.add(bean1)
        mDatas?.add(bean2)
        mDatas?.add(bean3)
        mDatas?.add(bean4)
        mDatas?.add(bean5)
        mDatas?.add(bean6)
        mDatas?.add(bean7)
        mDatas?.add(bean8)*/
        if (mDatas!!.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            emptyView.tvEmptyView.text = getString(R.string.noFriend)
        } else {
            emptyView.visibility = View.GONE
        }
        val comparator = MyComparator()
        Collections.sort(mDatas, comparator)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 新的好友
        ll_NewFriends.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MyFriendsActivity,NewFriendsActivity::class.java)
            startActivity(intent)
        }
        // listView
        lv_FriendsList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent()
            intent.setClass(this@MyFriendsActivity,EditFriendsActivity::class.java)
            intent.putExtra("friendsName", mDatas!!.get(position).name)
            intent.putExtra("friendsNum", mDatas!!.get(position).num)
            startActivity(intent)
        }
        titleOk?.setOnClickListener {
            showActivity(AddFriendActivity::class.java)
        }
    }
}
