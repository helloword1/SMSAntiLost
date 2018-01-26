package com.goockr.smsantilost.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.CountryBean
import kotlinx.android.synthetic.main.item_multiply_phone_item.view.*

/**
 * Date: 16/08/28
 */

open class MultiplyPhoneAdapter(protected var mContext: Context, protected var mDatas: ArrayList<CountryBean>, val size: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var SEARCH = 1
    private var SEARCH1 = 2

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> SEARCH
            1 -> SEARCH1
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SEARCH -> SearchViewHolder(mInflater.inflate(R.layout.item_msm_search, parent, false))
            SEARCH1 -> {
                val itemView = TextView(mContext)
                itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.some_white_et))
                val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mContext.resources.getDimension(R.dimen.x10).toInt())
                itemView.layoutParams = params
                SearchViewHolder(itemView)
            }
            else -> ViewHolder(mInflater.inflate(R.layout.item_multiply_phone_item, parent, false))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> {
            }
            1 -> {
            }
            else -> {
                holder as ViewHolder
                val countryBean = mDatas[position - 2]
                if (position == size + 2) {
                    holder.space.visibility = View.VISIBLE
                } else {
                    holder.space.visibility = View.GONE
                }
                holder.country.text = countryBean.countryName
                holder.countryPhone.text = "+${countryBean.phone}"
                holder.itemMultiplyPhone.setOnClickListener {
                    onAdapterItemClickListener.invoke(position - 2)
                }
            }
        }

    }

    override fun getItemCount(): Int = mDatas.size + 2

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val country = itemView.country!!
        val countryPhone = itemView.countryPhone!!
        val space = itemView.space!!
        val itemMultiplyPhone = itemView.itemMultiplyPhone!!
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var onAdapterItemClickListener: (Int) -> Unit

    fun setAdapterItemClickListener(listener: (Int) -> Unit) {
        this.onAdapterItemClickListener = listener
    }
}

