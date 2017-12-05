package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContentBean
import kotlinx.android.synthetic.main.adapter_msm_left_item.view.*
import kotlinx.android.synthetic.main.adapter_msm_right_item.view.*
/**
 * Created by  .
 * “”
 */

open class SendMsmAdapter(mContext: Context, private val mDatas: ArrayList<ContentBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val LEFT: Int = 101
    private val RIGHRT: Int = 102
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                LEFT -> LeftViewHolder(mInflater.inflate(R.layout.adapter_msm_left_item, parent, false))
                else -> RightViewHolder(mInflater.inflate(R.layout.adapter_msm_right_item, parent, false))
            }

    override fun getItemViewType(position: Int): Int = when (mDatas[position].isLeft) {
        true -> LEFT
        else -> RIGHRT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            when (mDatas[position].isLeft) {
                true -> {
                    val leftViewHolder = holder as LeftViewHolder
                    leftViewHolder.tvLeftContent.text = mDatas[position].msm
                    leftViewHolder.tvLeftDate.text = mDatas[position].data
                }
                false -> {
                    val rightViewHolder = holder as RightViewHolder
                    rightViewHolder.tvRightContent.text = mDatas[position].msm
                    rightViewHolder.tvRightDate.text = mDatas[position].data
                    if (mDatas[position].isSucceed) {
                        rightViewHolder.ivRightSendIcon.visibility = View.GONE
                    } else {
                        rightViewHolder.ivRightSendIcon.visibility = View.VISIBLE
                    }
                }
            }

    override fun getItemCount(): Int = mDatas.size

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLeftDate = itemView.tvLeftDate
        var tvLeftContent = itemView.tvLeftContent
    }

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRightDate = itemView.tvRightDate
        var tvRightContent = itemView.tvRightContent
        var ivRightSendIcon = itemView.ivRightSendIcon
    }

}
