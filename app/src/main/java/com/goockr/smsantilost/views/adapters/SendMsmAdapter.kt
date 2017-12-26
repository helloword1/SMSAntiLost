package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.ContentBean
import com.goockr.smsantilost.utils.LocaleUtil.dealWith
import kotlinx.android.synthetic.main.adapter_msm_left_item.view.*
import kotlinx.android.synthetic.main.adapter_msm_right_item.view.*
import java.text.SimpleDateFormat

/**
 * Created by  .
 * “”
 */

open class SendMsmAdapter(mContext: Context, private val mDatas: ArrayList<ContentBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val LEFT: Int = 101
    private val RIGHRT: Int = 102
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    val mdata = SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(System.currentTimeMillis())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                LEFT -> LeftViewHolder(mInflater.inflate(R.layout.adapter_msm_left_item, parent, false))
                else -> RightViewHolder(mInflater.inflate(R.layout.adapter_msm_right_item, parent, false))
            }

    override fun getItemViewType(position: Int): Int = when (mDatas[position].isLeft) {
        true -> LEFT
        else -> RIGHRT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contentBean = mDatas[position]
        return when (contentBean.isLeft) {
            true -> {
                val leftViewHolder = holder as LeftViewHolder
                leftViewHolder.tvLeftContent.text = contentBean.msmStr

                val date = contentBean.date
                val dealWith = dealWith(date)
                if (contentBean.isShowDate) {
                    leftViewHolder.tvLeftDate.text = dealWith
                    leftViewHolder.tvLeftDate.visibility = View.VISIBLE
                } else {
                    leftViewHolder.tvLeftDate.visibility = View.GONE
                }

            }
            false -> {
                val rightViewHolder = holder as RightViewHolder
                rightViewHolder.tvRightContent.text = contentBean.msmStr
                val date = contentBean.date
                if (contentBean.isShowDate) {
                    rightViewHolder.tvRightDate.visibility = View.VISIBLE
                    rightViewHolder.tvRightDate.text = dealWith(date)
                } else {
                    rightViewHolder.tvRightDate.visibility = View.GONE
                    rightViewHolder.tvRightDate.text = ""
                }
                if (contentBean.isSucceed) {
                    rightViewHolder.ivRightSendIcon.visibility = View.GONE

                } else {
                    rightViewHolder.ivRightSendIcon.visibility = View.VISIBLE
                    rightViewHolder.ivRightSendIcon.setOnClickListener {
                        OnGetAdapterListener.invoke(position)
                    }
                }
                if (contentBean.isSending){
                    rightViewHolder.ivRightProgress.visibility= View.VISIBLE
                }else{
                    rightViewHolder.ivRightProgress.visibility= View.GONE
                }

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
        var ivRightProgress = itemView.ivRightProgress
    }
    private lateinit var OnGetAdapterListener: (Int) -> Unit

    fun setOnGetAdapterListener(listener: (Int) -> Unit) {
        this.OnGetAdapterListener = listener
    }

}
