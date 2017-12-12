package com.goockr.smsantilost.views.activities.more

import android.support.v4.content.ContextCompat
import android.view.View
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.LocaleUtil
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_muti_layout.*

class MultilingualLanguageActivity(override val contentView: Int = R.layout.activity_muti_layout) : BaseActivity(), View.OnClickListener {
    private val rCN = 0
    private val rHK = 1
    private val EN = 2
    private var currentLanguage = 0
    override fun initView() {
        title?.text = getString(R.string.MultilingualLanguage)
        titleRight1?.visibility = View.VISIBLE
        titleRight1?.setTextColor(ContextCompat.getColor(this, R.color.blue))
        titleRight1?.text = getString(R.string.concert)
        rlC.setOnClickListener(this)
        rlH.setOnClickListener(this)
        rlE.setOnClickListener(this)
        val current = preferences?.getStringValue("currentLanguage")
        if (NotNull.isNotNull(current)){
            currentLanguage=current!!.toInt()
        }
        setIvVisible()
        titleRight1?.setOnClickListener {
            LocaleUtil.changeAppLanguage(application, currentLanguage)
            finish()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
        //简体
            R.id.rlC -> {
                currentLanguage = rCN
                setIvVisible()
            }
        //繁体
            R.id.rlH -> {
                currentLanguage = rHK
                setIvVisible()
            }
        //英文
            R.id.rlE -> {
                currentLanguage = EN
                setIvVisible()
            }
        }
    }

    private fun setIvVisible() {
        when (currentLanguage) {
            0 -> {
                ivCLanguage.visibility = View.VISIBLE
                ivHLanguage.visibility = View.GONE
                ivELanguage.visibility = View.GONE
            }
            1 -> {
                ivCLanguage.visibility = View.GONE
                ivHLanguage.visibility = View.VISIBLE
                ivELanguage.visibility = View.GONE
            }
            2 -> {
                ivCLanguage.visibility = View.GONE
                ivHLanguage.visibility = View.GONE
                ivELanguage.visibility = View.VISIBLE
            }
        }
    }
}
