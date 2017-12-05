package com.goockr.smsantilost.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.AntilostBean
import com.goockr.smsantilost.views.activities.antilost.KeyActivity
import com.goockr.smsantilost.views.adapters.AntilostAdapter
import kotlinx.android.synthetic.main.fragment_antilost.*

/**
 * Created by ning.wen on 2016/11/1.
 *
 */

class AntiLostFragment : BaseFragment() {

    private var lists: ArrayList<AntilostBean>? = null
    private var listsAdapter: AntilostAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //占位置用
        return setContentView(R.layout.fragment_antilost)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        initData()
        initMView()
        initClickEvent()
    }

    /**
     * 初始化listView数据
     */
    private fun initData() {
        lists = ArrayList<AntilostBean>()
        val bean1 = AntilostBean(R.mipmap.icon_key, "钥匙", "刚刚", false, "已连接")
        val bean2 = AntilostBean(R.mipmap.icon_key, "钥匙", "2017-03-30", false, "已连接")
        val bean3 = AntilostBean(R.mipmap.icon_portable_computer, "笔记本", "2017-03-30", false, "已连接")
        val bean4 = AntilostBean(R.mipmap.icon_vice_card_phone, "手机副卡", "2017-03-30", false, "已连接")
        val bean5 = AntilostBean(R.mipmap.icon_other, "其他", "2017-03-30", false, "已连接")
        lists!!.add(bean1)
        lists!!.add(bean2)
        lists!!.add(bean3)
        lists!!.add(bean4)
        lists!!.add(bean5)
    }

    /**
     * 初始化View
     */
    private fun initMView() {
        listsAdapter = AntilostAdapter(lists, context)
        antilost_list_view.adapter = listsAdapter
    }

    /**
     * listView点击事件
     */
    private fun initClickEvent() {
        antilost_list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent()
            intent.putExtra("icon", R.mipmap.icon_vice_card_phone_divice_details)
            intent.putExtra("name", lists?.get(position)?.name)
            intent.setClass(activity, KeyActivity::class.java)
            startActivity(intent)
        }
    }
}

