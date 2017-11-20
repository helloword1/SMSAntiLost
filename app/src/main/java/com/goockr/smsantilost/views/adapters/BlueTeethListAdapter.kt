package com.goockr.smsantilost.views.adapters

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.goockr.smsantilost.R

/**
 * Created by Administrator on 2017/9/30.
 */

class BlueTeethListAdapter(private val context: Context, private val datas: List<BluetoothDevice>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflate: LayoutInflater

    init {
        inflate = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IntenvoryViewHolder(inflate.inflate(R.layout.adapter_blueteeth_item_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder1 = holder as IntenvoryViewHolder
        val bean = datas[position]
        holder1.bluetoothName1.text = bean.name
        holder1.itemClick1.setOnClickListener {
            OnGetAdapterListener.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    private inner class IntenvoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val bluetoothName1: TextView
        val itemClick1: LinearLayout

        init {
            this.bluetoothName1 = view.findViewById(R.id.bluetoothName)
            this.itemClick1 = view.findViewById(R.id.itemClick)
        }
    }
    lateinit var OnGetAdapterListener: (Int) -> Unit

    fun setoOnGetAdapterListener(listener: (Int) -> Unit) {
        this.OnGetAdapterListener = listener
    }
}
