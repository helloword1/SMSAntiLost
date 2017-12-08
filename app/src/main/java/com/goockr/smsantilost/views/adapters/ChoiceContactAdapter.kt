package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.PhoneBean
import kotlinx.android.synthetic.main.item_choice_contact.view.*

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

open class ChoiceContactAdapter(private var mContext: Context, protected var mDatas: ArrayList<PhoneBean>?) : RecyclerView.Adapter<ChoiceContactAdapter.ViewHolder>() {
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val lists = ArrayList<Int>()

    fun getLists():ArrayList<Int> {
        return lists
    }

    fun setDatas(datas: ArrayList<PhoneBean>): ChoiceContactAdapter {
        mDatas = datas
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceContactAdapter.ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_choice_contact, parent, false))
    }

    override fun onBindViewHolder(holder: ChoiceContactAdapter.ViewHolder, position: Int) {
        val cityBean = mDatas!![position]
        holder.tvCity.text = cityBean.getMPhone()
        if (cityBean.phone.contains(",")) {
            val split = cityBean.phone.split(",")
            holder.tvPhoneNum.text = split[0]
        } else {
            holder.tvPhoneNum.text = cityBean.phone
        }
        if (cityBean.isshowRight) {
            holder.avatar.visibility = View.VISIBLE
            if (!lists.contains(position))
                lists.add(position)
        } else {
            holder.avatar.visibility = View.GONE
            if (!lists.isEmpty() && lists.contains(position))
                lists.remove(position)
        }
        holder.content.setOnClickListener {
            if (mDatas!![position].isshowRight) {
                mDatas!![position].isshowRight = false
                if (!lists.isEmpty() && lists.contains(position))
                    lists.remove(position)

            } else {
                mDatas!![position].isshowRight = true
                if (!lists.contains(position))
                    lists.add(position)
            }
            OnGetAdapterListener.invoke(lists)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = if (mDatas != null) mDatas!!.size else 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvCity: TextView = itemView.tvCity
        internal var tvPhoneNum: TextView = itemView.tvPhoneNum
        internal var avatar: ImageView = itemView.ivRightAvatar
        internal var content: View = itemView.content
    }

    lateinit var OnGetAdapterListener: (ArrayList<Int>) -> Unit

    fun setoOnGetAdapterListener(listener: (ArrayList<Int>) -> Unit) {
        this.OnGetAdapterListener = listener
    }

}
