package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.goockr.smsantilost.R
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import kotlinx.android.synthetic.main.adapter_create_name_item.view.*
import kotlinx.android.synthetic.main.adapter_create_phone_item.view.*
import kotlinx.android.synthetic.main.adapter_create_send_msm_item.view.*

/**
 *
 */

open class ContactDetailsAdapter(mContext: Context, var mDatas: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE: Int = 101
    private val NAME: Int = 102
    private val PHONRS: Int = 103
    private val PHONE_ADD: Int = 104
    private var isedit = false
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val editList = ArrayList<EditText>()
    fun getEditList()=editList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TITLE -> TitleViewHolder(mInflater.inflate(R.layout.adapter_create_title_item, parent, false))
                NAME -> NameViewHolder(mInflater.inflate(R.layout.adapter_create_name_item, parent, false))
                PHONRS -> PhoneViewHolder(mInflater.inflate(R.layout.adapter_create_phone_item, parent, false))
                else -> SendMsmViewHolder(mInflater.inflate(R.layout.adapter_create_send_msm_item, parent, false))
            }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> TITLE
        1 -> NAME
        mDatas.size - 1 -> PHONE_ADD
        else -> PHONRS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (position) {
            0 -> {
            }
            1 -> {
                val nameViewHolder = holder as NameViewHolder
                nameViewHolder.etContactName.setText(mDatas[position])
                //隐藏删除按钮与点击事件
                nameViewHolder.etContactName.isEnabled = isedit
                nameViewHolder.etContactName.setClearDrawableVisible(isedit)
            }
            mDatas.size - 1 -> {
                val sendMsmViewHolder = holder as SendMsmViewHolder
                if (isedit) {
                    sendMsmViewHolder.llAddPhone.visibility = View.VISIBLE
                    sendMsmViewHolder.tvSendMsm.visibility = View.GONE
                    sendMsmViewHolder.llAddPhone.setOnClickListener {
                        //添加电话号码
                        mDatas.add("")
                        notifyDataSetChanged()
                    }
                } else {
                    sendMsmViewHolder.llAddPhone.visibility = View.GONE
                    sendMsmViewHolder.tvSendMsm.visibility = View.VISIBLE
                    sendMsmViewHolder.tvSendMsm.isEnabled = isedit
                    sendMsmViewHolder.tvSendMsm.setOnClickListener {
                        //发送短信

                    }
                }
            }
            else -> {
                val phoneViewHolder = holder as PhoneViewHolder
                phoneViewHolder.etPhoneName.setText(mDatas[position])
                if (!editList.contains(phoneViewHolder.etPhoneName))
                    editList.add(phoneViewHolder.etPhoneName)
                (phoneViewHolder.itemView as SwipeMenuLayout).isSwipeEnable = isedit
                phoneViewHolder.etPhoneName.isEnabled = isedit
                phoneViewHolder.etPhoneName.setClearDrawableVisible(isedit)
                phoneViewHolder.btnDel.setOnClickListener {
                    mDatas.removeAt(position)
                    if (editList.contains(phoneViewHolder.etPhoneName))
                        editList.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int = mDatas.size

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var etContactName = itemView.etContactName
    }

    class SendMsmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSendMsm = itemView.tvSendMsm
        var llAddPhone = itemView.llAddPhone
    }

    class PhoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var etPhoneName = itemView.etPhoneName
        var btnDel = itemView.btnDel
    }

    fun setEdit(b: Boolean) {
        isedit = b
    }
}
