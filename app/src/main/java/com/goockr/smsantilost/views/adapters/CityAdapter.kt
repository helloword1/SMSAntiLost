package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.CityBean
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.activities.BlueTeethSearchActivity


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

open class CityAdapter(protected var mContext: Context, protected var mDatas: ArrayList<CityBean>?) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    protected var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(mContext)
    }

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
        holder.tvPhoneNum.text = cityBean.phone
        holder.content.setOnClickListener {
            if (position==0){
                var baseActivity = mContext as BaseActivity
                baseActivity.showActivity(BlueTeethSearchActivity::class.java)
                return@setOnClickListener
            }
            Toast.makeText(mContext, "pos:" + position, Toast.LENGTH_SHORT).show() }
        holder.avatar.setImageResource(R.drawable.ic_launcher_background)
    }

    override fun getItemCount(): Int {
        return if (mDatas != null) mDatas!!.size else 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvCity: TextView
        internal var tvPhoneNum: TextView
        internal var avatar: ImageView
        internal var content: View

        init {
            tvCity = itemView.findViewById<View>(R.id.tvCity) as TextView
            tvPhoneNum = itemView.findViewById<View>(R.id.tvPhoneNum) as TextView
            avatar = itemView.findViewById<View>(R.id.ivAvatar) as ImageView
            content = itemView.findViewById(R.id.content)
        }
    }
}
