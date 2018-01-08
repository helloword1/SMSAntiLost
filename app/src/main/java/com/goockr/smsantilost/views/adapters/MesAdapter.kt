package com.goockr.smsantilost.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.MesBean
import kotlinx.android.synthetic.main.item_mes.view.*


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */
class MesAdapter(private var mContext: Context, private var mDatas: ArrayList<MesBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  ViewHolder(mInflater.inflate(R.layout.item_mes, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val mesBean = mDatas!![position]
        holder.mesTitle.text = mesBean.mesTitle
        holder.mesContent.text = mesBean.mesContent
        holder.mesDetailText.text = mesBean.mesDetailText
        holder.mesDateStr.text = mesBean.mesDate
    }

    override fun getItemCount(): Int = if (mDatas != null) mDatas!!.size else 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mesDetailText: TextView = itemView.mesDetailText
        var mesContent: TextView = itemView.mesContent
        var mesTitle: TextView = itemView.mesTitle
        var mesDateStr: TextView = itemView.mesDateStr
        var ivMes: ImageView = itemView.ivMes
    }


    var OnGetMesAdapterListener: ((ArrayList<MesBean>) -> Unit)? = null

    fun setOnGetAdapterListener(listener: (ArrayList<MesBean>) -> Unit) {
        this.OnGetMesAdapterListener = listener
    }
}
