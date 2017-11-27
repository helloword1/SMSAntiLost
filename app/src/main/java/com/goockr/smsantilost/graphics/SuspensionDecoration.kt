package com.goockr.smsantilost.graphics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.mcxtzhang.indexlib.suspension.ISuspensionInterface

/**
 * Created by LJN on 2017/11/14.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

class SuspensionDecoration(context: Context, private var mDatas: List<ISuspensionInterface>?) : RecyclerView.ItemDecoration() {
    private val mPaint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private val mBounds: Rect = Rect()
    private val mInflater: LayoutInflater
    private var mTitleHeight: Int = 0
    private var mHeaderViewCount = 0

    init {
        this.mTitleHeight = TypedValue.applyDimension(1, 28.0f, context.resources.displayMetrics).toInt()
        mTitleFontSize = TypedValue.applyDimension(2, 14.0f, context.resources.displayMetrics).toInt()
        this.mPaint.textSize = mTitleFontSize.toFloat()
        this.mPaint.isAntiAlias = true
        this.mInflater = LayoutInflater.from(context)
    }

    fun setmTitleHeight(mTitleHeight: Int): SuspensionDecoration {
        this.mTitleHeight = mTitleHeight
        return this
    }

    fun setColorTitleBg(colorTitleBg: Int): SuspensionDecoration {
        COLOR_TITLE_BG = colorTitleBg
        return this
    }

    fun setColorTitleFont(colorTitleFont: Int): SuspensionDecoration {
        COLOR_TITLE_FONT = colorTitleFont
        return this
    }

    fun setTitleFontSize(mTitleFontSize: Int): SuspensionDecoration {
        this.mPaint.textSize = mTitleFontSize.toFloat()
        return this
    }

    fun setmDatas(mDatas: List<ISuspensionInterface>): SuspensionDecoration {
        this.mDatas = mDatas
        return this
    }

    fun getHeaderViewCount(): Int {
        return this.mHeaderViewCount
    }

    fun setHeaderViewCount(headerViewCount: Int): SuspensionDecoration {
        this.mHeaderViewCount = headerViewCount
        return this
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            var position = params.viewLayoutPosition
            position -= this.getHeaderViewCount()
            if (this.mDatas != null && !this.mDatas!!.isEmpty() && position <= this.mDatas!!.size - 1 && position >= 0 && this.mDatas!![position].isShowSuspension && position > -1) {
                if (position == 0) {
                    this.drawTitleArea(c, left, right, child, params, position)

                } else if (null != this.mDatas!![position].suspensionTag && this.mDatas!![position].suspensionTag != this.mDatas!![position - 1].suspensionTag) {
                    this.drawTitleArea(c, left, right, child, params, position)
                }
            }
        }

    }

    private fun drawTitleArea(c: Canvas, left: Int, right: Int, child: View, params: RecyclerView.LayoutParams, position: Int) {
        this.mPaint.color = COLOR_TITLE_BG
        c.drawRect(left.toFloat(), (child.top - params.topMargin - this.mTitleHeight).toFloat(), right.toFloat(), (child.top - params.topMargin).toFloat(), this.mPaint)
        this.mPaint.color = COLOR_TITLE_FONT
        this.mPaint.getTextBounds(this.mDatas!![position].suspensionTag, 0, this.mDatas!![position].suspensionTag.length, this.mBounds)
        c.drawText(this.mDatas!![position].suspensionTag, child.paddingLeft.toFloat() + 20, (child.top - params.topMargin - (this.mTitleHeight / 2 - this.mBounds.height() / 2)).toFloat(), this.mPaint)

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        var pos = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        pos -= this.getHeaderViewCount()
        if (this.mDatas != null && !this.mDatas!!.isEmpty() && pos <= this.mDatas!!.size - 1 && pos >= 0 && this.mDatas!![pos].isShowSuspension) {
            val tag = this.mDatas!![pos].suspensionTag
            val child = parent.findViewHolderForLayoutPosition(pos + this.getHeaderViewCount()).itemView
            var flag = false
            if (pos + 1 < this.mDatas!!.size && null != tag && tag != this.mDatas!![pos + 1].suspensionTag) {
                Log.d("zxt", "onDrawOver() called with: c = [" + child.top)
                if (child.height + child.top < this.mTitleHeight) {
                    c.save()
                    flag = true
                    c.translate(0.0f, (child.height + child.top - this.mTitleHeight).toFloat())
                }
            }

            this.mPaint.color = COLOR_TITLE_BG
            c.drawRect(parent.paddingLeft.toFloat(), parent.paddingTop.toFloat(), (parent.right - parent.paddingRight).toFloat(), (parent.paddingTop + this.mTitleHeight).toFloat(), this.mPaint)
            this.mPaint.color = COLOR_TITLE_FONT
            this.mPaint.getTextBounds(tag, 0, tag!!.length, this.mBounds)
            c.drawText(tag, child.paddingLeft.toFloat() + 20, (parent.paddingTop + this.mTitleHeight - (this.mTitleHeight / 2 - this.mBounds.height() / 2)).toFloat(), this.mPaint)
            var s = "当前通讯录有${this.mDatas!!.size}人"
            //右边字体
            this.textPaint.color = COLOR_RIGHT_TEXT_COLOR
            this.textPaint.textSize = 32f
            this.textPaint.isAntiAlias = true
            c.drawText(s, parent.right.toFloat() - 50 - getStringWidth(s), (parent.paddingTop + this.mTitleHeight - (this.mTitleHeight / 2 - this.mBounds.height() / 2)).toFloat(), this.textPaint)
            if (flag) {
                c.restore()
            }
        }
    }

    //获取字体长度
    private fun getStringWidth(str: String): Float = textPaint.measureText(str)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        var position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        position -= this.getHeaderViewCount()
        if (this.mDatas != null && !this.mDatas!!.isEmpty() && position <= this.mDatas!!.size - 1) {
            if (position > -1) {
                val titleCategoryInterface = this.mDatas!![position]
                if (titleCategoryInterface.isShowSuspension) {
                    if (position == 0) {
                        outRect.set(0, this.mTitleHeight, 0, 0)
                    } else if (null != titleCategoryInterface.suspensionTag && titleCategoryInterface.suspensionTag != this.mDatas!![position - 1].suspensionTag) {
                        outRect.set(0, this.mTitleHeight, 0, 0)
                    }
                }
            }

        }
    }

    companion object {
        private var COLOR_TITLE_BG = Color.parseColor("#e8e3e3")
        private var COLOR_TITLE_FONT = Color.parseColor("#1e1e1e")
        private var COLOR_RIGHT_TEXT_COLOR = Color.parseColor("#6d6c6c")
        private var mTitleFontSize: Int = 0
    }
}
