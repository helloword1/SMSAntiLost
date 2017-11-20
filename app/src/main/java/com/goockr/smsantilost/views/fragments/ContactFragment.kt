package com.goockr.smsantilost.views.fragments

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.CityBean
import com.goockr.smsantilost.entries.ContactsBean
import com.goockr.smsantilost.graphics.DividerItemDecoration
import com.goockr.smsantilost.graphics.SuspensionDecoration
import com.goockr.smsantilost.views.adapters.CityAdapter
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.item_city_swipe.view.*
import java.util.*
import kotlin.concurrent.thread


/**
 * 通讯录页
 */

class ContactFragment : BaseFragment() {
    private var mAdapter: SwipeDelMenuAdapter? = null
    private var mManager: LinearLayoutManager? = null
    private var mDatas: MutableList<CityBean> = ArrayList()
    private var mDecoration: SuspensionDecoration? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return setContentView(R.layout.fragment_contact)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mManager = LinearLayoutManager(activity)
        rv.setLayoutManager(mManager)
        mAdapter = SwipeDelMenuAdapter(activity, mDatas as ArrayList<CityBean>)
        rv.adapter = mAdapter
        mDecoration = SuspensionDecoration(activity, mDatas)
        rv.addItemDecoration(mDecoration)
        //如果add两个，那么按照先后顺序，依次渲染。
        //mRv.addItemDecoration(new TitleItemDecoration2(this,mDatas));
        rv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST))
        thread {
            kotlin.run {
                var systemContactInfos = getSystemContactInfos()
                activity.runOnUiThread { initDatas(systemContactInfos) }
            }
        }
        //模拟线上加载数据
//        initDatas(resources.getStringArray(R.array.provinces))
    }

    private fun initDatas(data: List<ContactsBean>) {
        //延迟两秒 模拟加载数据中....
        activity.window.decorView.postDelayed({
            mDatas.clear()
            //微信的头部 也是可以右侧IndexBar导航索引的，
            // 但是它不需要被ItemDecoration设一个标题titile
            mDatas.add(CityBean("新的朋友").setTop(true).setBaseIndexTag(INDEX_STRING_TOP) as CityBean)
            mDatas.add(CityBean("群聊").setTop(true).setBaseIndexTag(INDEX_STRING_TOP) as CityBean)
            mDatas.add(CityBean("标签").setTop(true).setBaseIndexTag(INDEX_STRING_TOP) as CityBean)
            mDatas.add(CityBean("公众号").setTop(true).setBaseIndexTag(INDEX_STRING_TOP) as CityBean)
            for (i in data.indices) {
                val cityBean = CityBean()
                cityBean.setCity(data[i].name)//设置城市名称
                cityBean.phone = (data[i].phone)//设置城市名称
                mDatas.add(cityBean)
            }
//            mAdapter?.setDatas(mDatas as ArrayList<CityBean>)
            mAdapter?.notifyDataSetChanged()

            indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                    .setNeedRealIndex(true)//设置需要真实的索引
                    .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                    .setmSourceDatas(mDatas)//设置数据
                    .invalidate()
            mDecoration?.setmDatas(mDatas)
        }, 1000)
    }

    private inner class SwipeDelMenuAdapter(mContext: Context, mDatas: ArrayList<CityBean>) : CityAdapter(mContext, mDatas) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return CityAdapter.ViewHolder(mInflater.inflate(R.layout.item_city_swipe, parent, false))
        }

        override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.btnDel.setOnClickListener {
                (holder.itemView as SwipeMenuLayout).quickClose()
                mDatas?.removeAt(holder.adapterPosition)
                indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                        .setNeedRealIndex(true)//设置需要真实的索引
                        .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                        .setmSourceDatas(mDatas)//设置数据
                        .invalidate()
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        private val TAG = "zxt"
        private val INDEX_STRING_TOP = "↑"
    }

    private var PHONES_PROJECTION = arrayOf(Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID)
    /**
     * 获取系统联系人信息
     *
     * @return
     */
    fun getSystemContactInfos(): List<ContactsBean> {
        val infos = ArrayList<ContactsBean>()
        val cursor = activity.getContentResolver().query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                var info = ContactsBean()
                val contactName = cursor.getString(0)
                val phoneNumber = cursor.getString(1)
                info.name = contactName
                info.phone = phoneNumber
                infos.add(info)
            }
            cursor.close()
        }
        return infos
    }
}


