package com.goockr.smsantilost.views.activities.antilost

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.PhoneSoundBean
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.PhoneSoundAdapter
import kotlinx.android.synthetic.main.activity_select_phone_sound.*


/**
 * 越界提醒声音选择界面
 */
class SelectPhoneSoundActivity(override val contentView: Int = R.layout.activity_select_phone_sound) : BaseActivity() {

    private var mDataList: ArrayList<PhoneSoundBean>? = null
    private var mAdapter: PhoneSoundAdapter? = null
    private var mCurrentPosition = 0
    private val PHONE_SOUND_RESULT_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        initMView()
        initMData()
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
        title?.text = getString(R.string.cutRemindSound)
        ll?.addView(titleLayout)
    }

    /**
     * 初始化数据
     */
    private fun initMData() {
        mDataList = ArrayList()
        var bean1 = PhoneSoundBean(getString(R.string.defaultSound))
        var bean2 = PhoneSoundBean(getString(R.string.sweetSound))
        var bean3 = PhoneSoundBean(getString(R.string.sharpSound))
        var bean4 = PhoneSoundBean(getString(R.string.alarmSound))
        mDataList?.add(bean1)
        mDataList?.add(bean2)
        mDataList?.add(bean3)
        mDataList?.add(bean4)
        mAdapter = PhoneSoundAdapter(this, mDataList)
        lv_PhoneSound.adapter = mAdapter
    }


    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // listView的
        lv_PhoneSound.setOnItemClickListener { parent, view, position, id ->
            mAdapter?.setCurrentPosition(position)
            mAdapter?.notifyDataSetChanged()
            mCurrentPosition = position
        }
        // 返回键的
        titleBack?.setOnClickListener {
            sendResult()
            finish()
        }
    }

    /**
     * 监听返回键，发送选择的提醒声
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sendResult()
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 返回选择的提醒声
     */
    private fun sendResult() {
        var intent = Intent()
        intent.putExtra("phoneSound", mDataList?.get(mCurrentPosition)?.name)
        setResult(PHONE_SOUND_RESULT_CODE, intent)
    }
}
