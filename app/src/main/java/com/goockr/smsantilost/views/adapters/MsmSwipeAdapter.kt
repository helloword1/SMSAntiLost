package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.MsmBean
import com.goockr.smsantilost.graphics.CleanableEditText


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

open class MsmSwipeAdapter(protected var mContext: Context, protected var mDatas: ArrayList<MsmBean>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var isSearch = false
    private var SEARCH = 1

    fun isSearch(isSearch: Boolean) {
        this.isSearch = isSearch
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && isSearch) {
            SEARCH
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SEARCH) {
            return SearchViewHolder(mInflater.inflate(R.layout.item_msm_search, parent, false))
        } else {
            return ViewHolder(mInflater.inflate(R.layout.item_msm_swipe, parent, false))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && isSearch) {

        } else {
            holder as ViewHolder
            val msmBean = mDatas!![position]
            holder.tvTime.text = msmBean.mTime
            holder.tvTitle.text = msmBean.mTitl
            holder.tvContent.text = msmBean.Content
            if (msmBean.isShow) {
                holder.ivAvatar.visibility = View.VISIBLE
            } else {
                holder.ivAvatar.visibility = View.INVISIBLE
            }

        }

    }

    override fun getItemCount(): Int = if (mDatas != null) mDatas!!.size else 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.findViewById<View>(R.id.tvTime) as TextView
        var tvContent: TextView = itemView.findViewById<View>(R.id.tvContent) as TextView
        var tvTitle: TextView = itemView.findViewById<View>(R.id.tvTitle) as TextView
        var ivAvatar: ImageView = itemView.findViewById<View>(R.id.ivAvatar) as ImageView

    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var smsSearch: CleanableEditText = itemView.findViewById<View>(R.id.smsSearch) as CleanableEditText
    }
}
