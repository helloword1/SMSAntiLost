package com.goockr.smsantilost.views.activities.antilost

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import com.bigkoo.pickerview.OptionsPickerView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.VIBRATE
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_two_way_anti.*

class TwoWayAntiActivity(override val contentView: Int = R.layout.activity_two_way_anti) : BaseActivity(), View.OnClickListener {

    private val PHONE_SOUND_REQUEST_CODE = 1
    private val PHONE_SOUND_RESULT_CODE = 2
    private var mPvOptions: OptionsPickerView<*>? = null
    private var vibrator: Vibrator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initLoopView()
        initClickEvent()
    }

    /**
     * 初始化title
     */
    private fun initMView() {
        ll?.removeAllViews()
        val titleLayout = layoutInflater.inflate(R.layout.base_title_view, null)
        title = titleLayout.findViewById(R.id.title)
        titleBack = titleLayout.findViewById(R.id.titleBack)
        title?.text = getString(R.string.Bi_directionalAntiLoss)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        val value = preferences!!.getStringValue(Constant.VIBRATE)
        if (NotNull.isNotNull(value)){
            btn_OpenVibration.isChecked = value.toBoolean()
        }else{
            btn_OpenVibration.isChecked =false
        }

    }

    private fun initLoopView() {
        val lists = ArrayList<String>()
        lists.add("5s")
        lists.add("10s")
        lists.add("15s")
        lists.add("30s")
        // 滑轮监听
        mPvOptions = OptionsPickerView.Builder(this, OptionsPickerView.OnOptionsSelectListener { options1, option2, options3, v ->
            tv_SoundTotal1.text = lists.get(options1)
        })
                .setTitleBgColor(resources.getColor(R.color.colorPrimary))
                .setSubmitColor(Color.parseColor("#ffffff"))
                .setCancelColor(Color.parseColor("#ffffff"))
                .build()
        mPvOptions?.setPicker(lists)
    }

    /**
     * 各个控件点击事件
     */
    private fun initClickEvent() {
        ll_PhoneSound1.setOnClickListener(this)
        ll_SoundTotal1.setOnClickListener(this)
        // 双向防丢滑块按钮监听
        btn_OpenAnti.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            }
        }
        // 震动滑块按钮监听
        btn_OpenVibration.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                preferences?.putValue(VIBRATE,"true")
                vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator!!.vibrate(200)
            }else{
                preferences?.putValue(VIBRATE,"false")
                if (NotNull.isNotNull(vibrator) && vibrator!!.hasVibrator()) {
                    vibrator?.cancel()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (NotNull.isNotNull(vibrator) && vibrator!!.hasVibrator()) {
            vibrator?.cancel()
        }
    }
    /**
     * 点击事件具体实现
     */
    override fun onClick(v: View?) {
        when (v?.id) {
        // 设置提醒声
            R.id.ll_PhoneSound1 -> {
                var intent = Intent()
                intent.setClass(this,SelectPhoneSoundActivity::class.java)
                startActivityForResult(intent,PHONE_SOUND_REQUEST_CODE)
            }
        // 设置提醒声时长
            R.id.ll_SoundTotal1 -> {
                mPvOptions?.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PHONE_SOUND_REQUEST_CODE && resultCode == PHONE_SOUND_RESULT_CODE){
            val phoneSoundName = data?.getStringExtra("phoneSound")
            tv_PhoneSound1.text = phoneSoundName
        }
    }
}
