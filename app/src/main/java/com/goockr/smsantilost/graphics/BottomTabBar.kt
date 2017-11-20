package com.goockr.smsantilost.graphics

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.goockr.smsantilost.R
import com.goockr.smsantilost.entries.BarEntity

/**
 * Created by zhihao.wen on 2016/11/4.
 */

class BottomTabBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var manager: FragmentManager? = null
    /**
     * 设置文本颜色
     */
    private val textColor: Int
    /**
     * 设置文本选中颜色
     */
    private val textSelectColor: Int
    /**
     * 设置整体背景颜色
     */
    private val backgroundColor: Int

    private var current: Fragment? = null

    private var onSelectListener: OnSelectListener? = null

    fun setManager(manager: FragmentManager) {
        this.manager = manager
    }

    fun setBars(bars: List<BarEntity>) {
        init(bars)
    }

    init {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomTabBar)
        textColor = typedArray.getColor(R.styleable.BottomTabBar_textColor, Color.parseColor("#666666"))
        textSelectColor = typedArray.getColor(R.styleable.BottomTabBar_textSelectColor, Color.parseColor("#666666"))
        backgroundColor = typedArray.getColor(R.styleable.BottomTabBar_backgroundColor, Color.parseColor("#e9e9e9"))
        typedArray.recycle()
    }

    /**
     * 初始化
     *
     * @param bars
     */
    private fun init(bars: List<BarEntity>?) {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(backgroundColor)
        if (bars == null || bars.size <= 0) {
            return
        }
        /**
         * 最上面添加一个Fragment
         */
        val fl = FrameLayout(context)
        fl.id = R.id.fl_base_content_view
        fl.setBackgroundColor(Color.parseColor("#ffffff"))
        val flp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        addView(fl, flp)
        /**
         * 添加一根华丽的分割线
         */
        val line = View(context)
        line.setBackgroundColor(Color.parseColor("#cccccc"))
        val llp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
        addView(line, llp)
        /**
         * 添加底部导航栏菜单
         */
        val bottom = LinearLayout(context)
        bottom.orientation = LinearLayout.HORIZONTAL
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        for (i in bars.indices) {
            val bar = LinearLayout(this.context)
            bar.orientation = LinearLayout.VERTICAL
            val blp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            bar.gravity = Gravity.CENTER
            bar.setPadding(10, 16, 10, 16)
            val image = ImageView(context)
            val ilp = LinearLayout.LayoutParams(resources.getDimension(R.dimen.image_height).toInt(), resources.getDimension(R.dimen.image_height).toInt())
            image.setImageResource(bars[i].tabUnSelectedResId)
            bar.addView(image, ilp)

            val text = TextView(context)
            text.text = bars[i].tabText
            val tlp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text.setTextColor(textColor)
            bar.addView(text, tlp)

            bar.setOnClickListener { select(i, bars) }
            bottom.addView(bar, blp)
        }
        addView(bottom, lp)
        /**
         * 默认选中第一个菜单栏
         */
        select(0, bars)
    }

    /**
     * 选择了第几个tab
     *
     * @param position
     */
    fun select(position: Int, bars: List<BarEntity>?) {
        if (getChildAt(1) == null) {
            return
        }
        var image: ImageView
        var text: TextView
        val bottom = getChildAt(2) as LinearLayout
        for (i in 0 until bottom.childCount) {
            val bar = bottom.getChildAt(i) as LinearLayout
            image = bar.getChildAt(0) as ImageView
            text = bar.getChildAt(1) as TextView
            if (position == i) {
                image.setImageResource(bars!![i].tabSelectedResId)
                text.setTextColor(textSelectColor)
            } else {
                image.setImageResource(bars!![i].tabUnSelectedResId)
                text.setTextColor(textColor)
            }
        }
        switchByPosition(position)
    }

    private fun switchByPosition(position: Int) {
        if (onSelectListener != null) {
            onSelectListener!!.onSelect(position)
        }
    }

    /**
     * 切换当前显示的fragment
     */
    fun switchContent(to: Fragment) {
        if (current !== to) {
            val transaction = manager!!.beginTransaction()

            if (current != null) {
                transaction.hide(current)
            }
            if (!to.isAdded) { // 先判断是否被add过
                transaction.add(R.id.fl_base_content_view, to).commit()
            } else {

                transaction.show(to).commit() // 隐藏当前的fragment，显示下一个
            }
            current = to
        }
    }

    /**
     * 选择切换的监听，在这里处理切换fragment,防止重复创建
     */
    interface OnSelectListener {
        fun onSelect(position: Int)
    }

    fun setOnSelectListener(onSelectListener: OnSelectListener) {
        this.onSelectListener = onSelectListener
    }
}
