package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goockr.smsantilost.R
import kotlinx.android.synthetic.main.adapter_create_name_item.view.*
import kotlinx.android.synthetic.main.adapter_create_phone_item.view.*


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 * “”
 */

open class CreateContactAdapter(mContext: Context, Datas: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE: Int = 101
    private val NAME: Int = 102
    private val PHONRS: Int = 103
    private val PHONE_ADD: Int = 104
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mDatas: ArrayList<String> = ArrayList()

    init {
        mDatas.addAll(Datas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TITLE -> TitleViewHolder(mInflater.inflate(R.layout.adapter_create_title_item, parent, false))
                NAME -> NameViewHolder(mInflater.inflate(R.layout.adapter_create_name_item, parent, false))
                PHONRS -> PhoneViewHolder(mInflater.inflate(R.layout.adapter_create_phone_item, parent, false))
                else -> AddPhoneViewHolder(mInflater.inflate(R.layout.adapter_create_add_phone_item, parent, false))
            }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> TITLE
        1 -> NAME
        mDatas.size - 1 -> PHONE_ADD
        else -> PHONRS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            when (position) {
                0 -> {
                }
                1 -> {
                    val nameViewHolder = holder as NameViewHolder
                    nameViewHolder.etContactName.setText(mDatas[position])
                }
                mDatas.size - 1 -> {
                    val addPhoneViewHolder = holder as AddPhoneViewHolder
                    addPhoneViewHolder.itemView.setOnClickListener {
                        mDatas.add("")
                        notifyDataSetChanged()
                    }
                }
                else -> {
                    val phoneViewHolder = holder as PhoneViewHolder
                    phoneViewHolder.etPhoneName.hint = "请输入电话号码"
                    phoneViewHolder.btnDel.setOnClickListener {
                        mDatas.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }

    override fun getItemCount(): Int = mDatas.size

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var etContactName = itemView.etContactName
    }

    class AddPhoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class PhoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var etPhoneName = itemView.etPhoneName
        var btnDel = itemView.btnDel
    }
}
