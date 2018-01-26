package com.goockr.smsantilost.views.activities.login

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.CountryBean
import com.goockr.smsantilost.graphics.HidingScrollListener
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.LogUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.MultiplyPhoneAdapter
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_multiply_mobil_phone_num.*
import kotlinx.android.synthetic.main.search_layout.*
import org.json.JSONArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by lijinning on 2018/1/23.
 */
class MultiplyMobilPhoneNumActivity(override val contentView: Int = R.layout.activity_multiply_mobil_phone_num) : BaseActivity() {
    var threadExecutor: ExecutorService? = null
    val mCountries = ArrayList<CountryBean>()
    var allCountries: ArrayList<CountryBean>? = null
    var phoneAdapter: MultiplyPhoneAdapter? = null

    override fun initView() {
        title?.text = getString(R.string.choiceCountry)
        threadExecutor = Executors.newSingleThreadExecutor()
        threadExecutor?.submit {
            allCountries = ArrayList()
            val strings = resources.getStringArray(R.array.multiply_mobil_phone_num)
            val s = strings[0]
            val s1 = strings[1]
            allCountries?.addAll(dealWithString(s))
            val size = allCountries?.size
            allCountries?.addAll(dealWithString(s1))
            runOnUiThread {
                setDates(size!!)
            }
            LogUtils.mi(s)
            LogUtils.mi(s1)
        }

    }

    private fun setDates(size: Int) {
        mulRecycleView.layoutManager = LinearLayoutManager(this)
        mCountries.clear()
        mCountries.addAll(allCountries!!)
        phoneAdapter = MultiplyPhoneAdapter(this, mCountries, size)
        mulRecycleView.adapter = phoneAdapter

        mulRecycleView.addOnScrollListener(object : HidingScrollListener() {
            override fun onHide() {
                val dm = resources.displayMetrics
                val height = dm.heightPixels
                top.animate()
                        .translationY(-height.toFloat())
                        .setDuration(800)
                        .setInterpolator(AccelerateInterpolator(2.toFloat()))
                        .start()
            }

            override fun onShow() {
                top.animate().translationY(0.toFloat()).setInterpolator(DecelerateInterpolator(2.toFloat())).setDuration(200).start()
            }

        })
        smsSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(tv: Editable?) {
                formatPhoneState(tv)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        phoneAdapter?.setAdapterItemClickListener {
            val phone = mCountries[it].phone
            val intent = Intent()
            intent.putExtra(Constant.MOBIL_PHONE_NUM, phone)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun formatPhoneState(tv: Editable?) {
        mCountries.clear()
        val tvStr = tv?.toString()
        if (NotNull.isNotNull(tvStr) && tvStr != "") {
            mCountries.filterTo(mCountries) { it.allStr.contains(tvStr!!) }
        } else {
            mCountries.addAll(allCountries!!)
        }
        phoneAdapter?.notifyDataSetChanged()
    }

    private fun dealWithString(s: String?): ArrayList<CountryBean> {
        val arrayList = ArrayList<CountryBean>()
        val country = JSONArray(s)
        val current = preferences?.getStringValue("currentLanguage")

        if (NotNull.isNotNull(current) && current!!.toInt() == 2) {
            //英文
            (0 until country.length())
                    .map { country[it].toString().split("-") }
                    .mapTo(arrayList) {
                        val countryName = it[0]
                        val phone = it[2]
                        CountryBean(countryName, phone, it.toString())
                    }
        } else {
            //汉语
            (0 until country.length())
                    .map { country[it].toString().split("-") }
                    .mapTo(arrayList) {
                        val countryName = it[1]
                        val phone = it[2]
                        CountryBean(countryName, phone, it.toString())
                    }
        }
        return arrayList
    }

    override fun onDestroy() {
        super.onDestroy()
        threadExecutor?.shutdown()
    }
}