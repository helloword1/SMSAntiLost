package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.MsmBean
import com.goockr.smsantilost.graphics.CleanableEditText
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.LocaleUtil.dealWithForSms
import com.goockr.smsantilost.views.activities.msm.MSMControlActivity
import com.goockr.smsantilost.views.fragments.MSMFragment
import kotlinx.android.synthetic.main.item_msm_search.view.*
import kotlinx.android.synthetic.main.item_msm_swipe.view.*


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

open class MsmSwipeAdapter(protected var mContext: Context, private var fragment: MSMFragment, protected var mDatas: ArrayList<MsmBean>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            holder as SearchViewHolder
        } else {
            holder as ViewHolder
            val msmBean = mDatas!![position]
            holder.tvTime.text = dealWithForSms(msmBean.smsTime)
            holder.tvTitle.text = msmBean.smsTitle
            if (msmBean.contentBeans.size>0){
                holder.tvContent.text = msmBean.smsStr
            }
            if (msmBean.isShow) {
                holder.ivAvatar.visibility = View.VISIBLE
            } else {
                holder.ivAvatar.visibility = View.INVISIBLE
            }
            holder.content.setOnClickListener {
                msmBean.isShow=false
                notifyDataSetChanged()
                val bundle = Bundle()
                bundle.putString(Constant.MSM_NAME, msmBean.smsTitle)
                val intent = Intent(mContext, MSMControlActivity::class.java)
                intent.putExtras(bundle)
                fragment.startActivityForResult(intent, Constant.MSM_RESULT_ID)
            }
        }

    }

    override fun getItemCount(): Int = if (mDatas != null) mDatas!!.size else 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.tvTime
        var tvContent: TextView = itemView.tvContent
        var tvTitle: TextView = itemView.tvTitle
        var ivAvatar: ImageView = itemView.ivAvatar
        var content: RelativeLayout = itemView.content

    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var smsSearch: CleanableEditText = itemView.smsSearch
    }

    lateinit var OnGetAdapterListener: (ArrayList<MsmBean>) -> Unit

    fun setoOnGetAdapterListener(listener: (ArrayList<MsmBean>) -> Unit) {
        this.OnGetAdapterListener = listener
    }
}
