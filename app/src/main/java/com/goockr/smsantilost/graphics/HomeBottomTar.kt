package com.goockr.smsantilost.graphics

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.goockr.smsantilost.R
import kotlinx.android.synthetic.main.tabbar.view.*

/**
 * Created by LJN on 2017/11/14.
 */
class HomeBottomTar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var selLinearLayout: LinearLayout? = null
    private var view: View? = null
    private var selectIndex: Int = 0
    lateinit var tabbarCallback: (Int)->Unit
    init {
        view = LayoutInflater.from(context).inflate(R.layout.tabbar, this, true)
        selLinearLayout = llMsm
        llMsm.setOnClickListener(this)
        llAntiLost.setOnClickListener(this)
        llLocation.setOnClickListener(this)
        llMore.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 == selLinearLayout) return else selLinearLayout = p0 as LinearLayout
        restartBotton()
        when (p0.id) {
            R.id.llMsm ->{
                ivMsm.setImageResource(R.mipmap.tab_btn_msg_selected)
                tvMsm.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                selectIndex=0
            }

            R.id.llAntiLost->{
                ivAntiLost.setImageResource(R.mipmap.tab_btn_anti_lost_selected)
                tvAntiLost.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                selectIndex=1
            }
            R.id.llLocation->{
                ivLocation.setImageResource(R.mipmap.tab_btn_location_selected)
                tvLocation.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                selectIndex=2
            }

            R.id.llMore->{
                ivMore.setImageResource(R.mipmap.tab_btn_more_selected)
                tvMore.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                selectIndex=3
            }
        }
        tabbarCallback(selectIndex)
    }

    private fun restartBotton() {
        ivMsm.setImageResource(R.mipmap.tab_btn_msg_normal)
        ivAntiLost.setImageResource(R.mipmap.tab_btn_anti_lost_normal)
        ivMore.setImageResource(R.mipmap.tab_btn_more_normal)
        ivLocation.setImageResource(R.mipmap.tab_btn_location_normal)
        tvMsm.setTextColor(ContextCompat.getColor(context, R.color.textColor))
        tvAntiLost.setTextColor(ContextCompat.getColor(context, R.color.textColor))
        tvMore.setTextColor(ContextCompat.getColor(context, R.color.textColor))
        tvLocation.setTextColor(ContextCompat.getColor(context, R.color.textColor))

    }
    fun setTabbarCallbackListener(listener:(Int)->Unit){
        this.tabbarCallback=listener
    }
}