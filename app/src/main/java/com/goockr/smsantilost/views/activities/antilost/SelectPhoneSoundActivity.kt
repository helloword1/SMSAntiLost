package com.goockr.smsantilost.views.activities.antilost

import android.content.Intent
import android.media.MediaPlayer
import android.view.KeyEvent
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.PhoneSoundBean
import com.goockr.smsantilost.utils.Constant.SELECT_PHONE_SOUND
import com.goockr.smsantilost.views.activities.BaseActivity
import com.goockr.smsantilost.views.adapters.PhoneSoundAdapter
import cxx.utils.NotNull
import kotlinx.android.synthetic.main.activity_select_phone_sound.*


/**
 * 越界提醒声音选择界面
 */
class SelectPhoneSoundActivity(override val contentView: Int = R.layout.activity_select_phone_sound) : BaseActivity() {

    private var mDataList: ArrayList<PhoneSoundBean>? = null
    private var mAdapter: PhoneSoundAdapter? = null
    private var mCurrentPosition = 0
    private val PHONE_SOUND_RESULT_CODE = 2
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
        val bean1 = PhoneSoundBean(getString(R.string.defaultSound))
        val bean2 = PhoneSoundBean(getString(R.string.sweetSound))
        val bean3 = PhoneSoundBean(getString(R.string.sharpSound))
        val bean4 = PhoneSoundBean(getString(R.string.alarmSound))
        mDataList?.add(bean1)
        mDataList?.add(bean2)
        mDataList?.add(bean3)
        mDataList?.add(bean4)
        mAdapter = PhoneSoundAdapter(this, mDataList)
        lv_PhoneSound.adapter = mAdapter

        val value = preferences?.getStringValue(SELECT_PHONE_SOUND)
        if (NotNull.isNotNull(value)) {
            val position = value?.toInt()
            mAdapter?.setCurrentPosition(position!!)
            mAdapter?.notifyDataSetChanged()
        }
    }


    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // listView的
        lv_PhoneSound.setOnItemClickListener { _, _, position, _ ->
            mAdapter?.setCurrentPosition(position)
            mAdapter?.notifyDataSetChanged()
            mCurrentPosition = position
            preferences?.putValue(SELECT_PHONE_SOUND, position.toString())
            mOverAlert(position)
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
        val intent = Intent()
        intent.putExtra("phoneSound", mDataList?.get(mCurrentPosition)?.name)
        setResult(PHONE_SOUND_RESULT_CODE, intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        //结束音频
        if (NotNull.isNotNull(mediaPlayer)) {
            mediaPlayer?.stop()
        }
    }

    //越界提醒
    private fun mOverAlert(position: Int) {
        if (!NotNull.isNotNull(mediaPlayer)){
            mediaPlayer = MediaPlayer()
        }else{
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }
        val descriptor = when (position) {
            0 -> resources.assets
                    .openFd("alert.mp3")
            1 -> resources.assets
                    .openFd("alert.mp3")
            2 -> resources.assets
                    .openFd("alert.mp3")
            else -> resources.assets
                    .openFd("alert.mp3")
        }
        mediaPlayer?.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

}
