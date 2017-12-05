package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.MsmBean
import kotlinx.android.synthetic.main.item_msm_manager.view.*

class MsmManagerAdapter(var mContext: Context,var mDatas: ArrayList<MsmBean>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var lists = ArrayList<MsmBean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_msm_manager, parent, false))
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val msmBean = mDatas!![position]
        holder.tvTime.text = msmBean.mTime
        holder.tvTitle.text = msmBean.mTitl
        holder.tvContent.text = msmBean.Content
        if (msmBean.isCheck) {
            holder.ivCheck.setImageResource(R.mipmap.btn_check_box_selected)
        } else {
            holder.ivCheck.setImageResource(R.mipmap.btn_check_box_normal)
        }
        holder.ivCheck.setOnClickListener {
            if (msmBean.isCheck) {
                msmBean.isCheck = false
                holder.ivCheck.setImageResource(R.mipmap.btn_check_box_normal)
                if (!lists.isEmpty() && lists.contains(msmBean))
                    lists.remove(msmBean)
            } else {
                msmBean.isCheck = true
                holder.ivCheck.setImageResource(R.mipmap.btn_check_box_selected)
                if (!lists.contains(msmBean)) lists.add(msmBean)
            }
            OnGetAdapterListener.invoke(lists)
        }

    }

    override fun getItemCount(): Int = if (mDatas != null) mDatas!!.size else 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.tvTime
        var tvContent: TextView = itemView.tvContent
        var tvTitle: TextView = itemView.tvTitle
        var content: RelativeLayout = itemView.content
        var ivCheck: ImageView = itemView.ivCheck
    }

    lateinit var OnGetAdapterListener: (ArrayList<MsmBean>) -> Unit

    fun setoOnGetAdapterListener(listener: (ArrayList<MsmBean>) -> Unit) {
        this.OnGetAdapterListener = listener
    }
}
