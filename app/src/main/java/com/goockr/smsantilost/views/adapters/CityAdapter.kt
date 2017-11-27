package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.CityBean
import com.goockr.smsantilost.utils.Constant.CONTACT_ID
import com.goockr.smsantilost.utils.Constant.CONTACT_NAME
import com.goockr.smsantilost.utils.Constant.CONTACT_PHONE
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.msm.ContactDetailsActivity
import kotlinx.android.synthetic.main.item_city_swipe.view.*


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

open class CityAdapter(private var mContext: Context, protected var mDatas: ArrayList<CityBean>?) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)

    fun getDatas(): List<CityBean>? {
        return mDatas
    }

    fun setDatas(datas: ArrayList<CityBean>): CityAdapter {
        mDatas = datas
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_city_swipe, parent, false))
    }

    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
        val cityBean = mDatas!![position]
        holder.tvCity.text = cityBean.getCity()
        if (cityBean.phone.contains(",")) {
            val split = cityBean.phone.split(",")
            holder.tvPhoneNum.text = split[0]
        } else {
            holder.tvPhoneNum.text = cityBean.phone
        }
        holder.content.setOnClickListener {
            val baseActivity = mContext as BaseActivity
            val bundle = Bundle()
            bundle.putString(CONTACT_NAME, cityBean.getCity())
            bundle.putString(CONTACT_PHONE, cityBean.phone)
            bundle.putString(CONTACT_ID, cityBean.id)
            baseActivity.showActivity(ContactDetailsActivity::class.java, bundle)
            return@setOnClickListener
            Toast.makeText(mContext, "pos:" + position, Toast.LENGTH_SHORT).show()
        }
        holder.avatar.setImageResource(R.drawable.ic_launcher_background)
    }

    override fun getItemCount(): Int = if (mDatas != null) mDatas!!.size else 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvCity: TextView = itemView.tvCity
        internal var tvPhoneNum: TextView = itemView.tvPhoneNum
        internal var avatar: ImageView = itemView.ivAvatar
        internal var content: View = itemView.content
    }
}
