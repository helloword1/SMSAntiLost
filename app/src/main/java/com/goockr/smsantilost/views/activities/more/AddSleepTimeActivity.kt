package com.goockr.smsantilost.views.activities.more

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bigkoo.pickerview.TimePickerView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.SleepTimeBean
import com.goockr.smsantilost.utils.ToastUtils
import com.goockr.smsantilost.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_add_sleep_time.*
import java.text.SimpleDateFormat
import java.util.*

class AddSleepTimeActivity(override val contentView: Int = R.layout.activity_add_sleep_time) : BaseActivity() {

    private val REQUEST_NAME = 0
    private val REQUEST_REPEAT = 1
    private val RESULT_BEAN = 2
    private var mStartTime: TimePickerView? = null
    private var mStopTime: TimePickerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMView()
        initStartLoop()
        initEndLoop()
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
        titleOk = titleLayout.findViewById(R.id.titleOk)
        title?.text = getString(R.string.addSleepTime)
        titleOk?.text = getString(R.string.Done)
        titleOk?.visibility = View.VISIBLE
        titleBack?.setOnClickListener { finish() }
        ll?.addView(titleLayout)
    }

    /**
     * 点击事件
     */
    private fun initClickEvent() {
        // 设置名称
        ll_SleepTimeName.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, SetSleepTimeNameActivity::class.java)
            startActivityForResult(intent, REQUEST_NAME)
        }
        // 开始时间
        ll_SleepTimeStart.setOnClickListener {
            mStartTime?.show()
        }
        // 结束时间
        ll_SleepTimeStop.setOnClickListener {
            mStopTime?.show()
        }
        // 重复选择
        ll_SleepTimeRepeat.setOnClickListener {
            val i = Intent()
            i.setClass(this,RepeatActivity::class.java)
            startActivityForResult(i,REQUEST_REPEAT)
        }
        // 完成按钮
        titleOk?.setOnClickListener {
            if (!TextUtils.isEmpty(tv_SleepTimeName.text) && !TextUtils.isEmpty(tv_SleepTimeStart.text) && !TextUtils.isEmpty(tv_SleepTimeStop.text) && !TextUtils.isEmpty(tv_SleepTimeRepeat.text)) {
                // 保存在本地

                // 返回数据
                val bean = SleepTimeBean(tv_SleepTimeName.text.toString(),tv_SleepTimeStart.text.toString() + " - " + tv_SleepTimeStop.text.toString(),tv_SleepTimeRepeat.text.toString())
                val i = Intent()
                val bundle = Bundle()
                bundle.putSerializable("bean",bean)
                i.putExtras(bundle)
                setResult(RESULT_BEAN,i)
                finish()
            }else {
                ToastUtils.showShort(this,getString(R.string.infoIsEmpty))
            }
        }
    }

    /**
     * 开始时间滑轮
     */
    private fun initStartLoop() {
        val selectedDate = Calendar.getInstance()//系统当前时间
        val startDate = Calendar.getInstance()
        startDate.set(2014, 1, 23)
        val endDate = Calendar.getInstance()
        endDate.set(2027, 2, 28)
        // 滑轮监听
        mStartTime = TimePickerView.Builder(this,TimePickerView.OnTimeSelectListener { date, v ->
                tv_SleepTimeStart.text = getTime(date)
                tv_SleepTimeStart.visibility = View.VISIBLE
        })
                .setDate(selectedDate)
                .setTitleText(getString(R.string.beginTime))
                .setSubmitText(getString(R.string.button_ok))
                .setCancelText(getString(R.string.cancel))
                .setTitleBgColor(resources.getColor(R.color.colorPrimary))
                .setSubmitColor(Color.parseColor("#ffffff"))
                .setCancelColor(Color.parseColor("#ffffff"))
                .setTitleColor(Color.parseColor("#ffffff"))
                .setRangDate(startDate, endDate)
                .setContentSize(18)
                .setType(booleanArrayOf(false, false, false, true, true, false))
                .setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day), getString(R.string.hour), getString(R.string.minute), getString(R.string.second))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build()
    }

    /**
     * 开始时间滑轮
     */
    private fun initEndLoop() {
        val selectedDate = Calendar.getInstance()//系统当前时间
        val startDate = Calendar.getInstance()
        startDate.set(2014, 1, 23)
        val endDate = Calendar.getInstance()
        endDate.set(2027, 2, 28)
        // 滑轮监听
        mStopTime = TimePickerView.Builder(this,TimePickerView.OnTimeSelectListener { date, v ->
                tv_SleepTimeStop.text = getTime(date)
                tv_SleepTimeStop.visibility = View.VISIBLE
        })
                .setDate(selectedDate)
                .setTitleText(getString(R.string.end))
                .setTitleBgColor(resources.getColor(R.color.colorPrimary))
                .setSubmitColor(Color.parseColor("#ffffff"))
                .setCancelColor(Color.parseColor("#ffffff"))
                .setTitleColor(Color.parseColor("#ffffff"))
                .setRangDate(startDate, endDate)
                .setContentSize(18)
                .setType(booleanArrayOf(false, false, false, true, true, false))
                .setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day), getString(R.string.hour), getString(R.string.minute), getString(R.string.second))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build()
    }

    private fun getTime(date: Date): String {//可根据需要自行截取数据显示
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_NAME && resultCode == REQUEST_NAME) {
            tv_SleepTimeName.text = data?.getStringExtra("sleepTimeName")
            tv_SleepTimeName.visibility = View.VISIBLE
        }
        if (requestCode == REQUEST_REPEAT && resultCode == REQUEST_REPEAT) {
            tv_SleepTimeRepeat.text = data?.getStringExtra("repeat")
            tv_SleepTimeRepeat.visibility = View.VISIBLE
        }
    }
}
