package com.goockr.smsantilost.utils

import android.os.CountDownTimer
import android.widget.TextView

/**
 * Created by dgy on 15/7/10.
 * 倒计时Button帮助类
 */
class CountDownButtonHelper
/**
 *
 * @param button
 * 需要显示倒计时的Button
 * @param defaultString
 * 默认显示的字符串
 * @param max
 * 需要进行倒计时的最大值,单位是秒
 * @param interval
 * 倒计时的间隔，单位是秒
 */
(private val button: TextView,
 defaultString: String,
 tmpString: String,
 max: Int, interval: Int) {

    // 倒计时timer
    private val countDownTimer: CountDownTimer
    // 计时结束的回调接口
    private lateinit var listener: ()->Unit

    init {

        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        countDownTimer = object : CountDownTimer((max * 1000).toLong(), (interval * 1000 - 10).toLong()) {

            override fun onTick(time: Long) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                button.text = (tmpString + "(" + (time + 15) / 1000
                        + ")")
            }

            override fun onFinish() {
                button.isEnabled = true
                button.text = defaultString
            }
        }
    }

    /**
     * 开始倒计时
     */
    fun start() {
        button.isEnabled = false
        countDownTimer.start()
    }



}

