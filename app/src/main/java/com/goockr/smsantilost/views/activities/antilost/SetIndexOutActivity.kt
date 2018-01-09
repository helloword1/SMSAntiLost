package com.goockr.smsantilost.views.activities.antilost

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.OptionsPickerView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.utils.Constant
import com.goockr.smsantilost.utils.Constant.RECONNECT
import com.goockr.smsantilost.views.activities.BaseActivity
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_set_index_out.*


/**
 * 越界提醒设置页面
 */
class SetIndexOutActivity(override val contentView: Int = R.layout.activity_set_index_out) : BaseActivity(), View.OnClickListener {

    private val PHONE_SOUND_REQUEST_CODE = 1
    private val PHONE_SOUND_RESULT_CODE = 2
    private var mPvOptions: OptionsPickerView<*>? = null



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
        title?.text = getString(R.string.TranundaryReminding)
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
        val value = preferences!!.getStringValue(Constant.RECONNECT)
        if (NotNull.isNotNull(value)) {
            SwitchButton.isChecked = value.toBoolean()
        } else {
            SwitchButton.isChecked = false
        }
    }

    /**
     * 提示时间滑轮控件
     */
    private fun initLoopView() {
        val lists = ArrayList<String>()
        lists.add("5s")
        lists.add("10s")
        lists.add("15s")
        lists.add("30s")
        // 滑轮监听
        mPvOptions = OptionsPickerView.Builder(this, OptionsPickerView.OnOptionsSelectListener { options1, option2, options3, v ->
            tv_SoundTotal.text = lists[options1]
        })
                .setTitleBgColor(resources.getColor(R.color.colorPrimary))
                .setSubmitColor(Color.parseColor("#ffffff"))
                .setCancelColor(Color.parseColor("#ffffff"))
                .setCancelText(getString(R.string.cancel))
                .setSubmitText(getString(R.string.comfir))
                .build()
        mPvOptions?.setPicker(lists)
    }

    /**
     * 各个控件点击事件
     */
    private fun initClickEvent() {
        ll_PhoneSound.setOnClickListener(this)
        ll_SoundTotal.setOnClickListener(this)
        // 重连滑块按钮监听
        SwitchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                preferences?.putValue(RECONNECT, "true")
                overAlert()
            } else {
                preferences?.putValue(RECONNECT, "false")
                if (NotNull.isNotNull(mediaPlayer)&&mediaPlayer!!.isPlaying) {
                    mediaPlayer?.stop()
                }
            }
        }
    }


    /**
     * 点击事件具体实现
     */
    override fun onClick(v: View?) {
        when (v?.id) {
        // 设置提醒声
            R.id.ll_PhoneSound -> {
                showActivityForResult(SelectPhoneSoundActivity::class.java, PHONE_SOUND_REQUEST_CODE)
            }
        // 设置提醒声时长
            R.id.ll_SoundTotal -> {
                mPvOptions?.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PHONE_SOUND_REQUEST_CODE && resultCode == PHONE_SOUND_RESULT_CODE) {
            val phoneSoundName = data?.getStringExtra("phoneSound")
            tv_PhoneSound.text = phoneSoundName
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //结束音频
        if (NotNull.isNotNull(mediaPlayer)) {
            mediaPlayer?.stop()
        }
    }
}
