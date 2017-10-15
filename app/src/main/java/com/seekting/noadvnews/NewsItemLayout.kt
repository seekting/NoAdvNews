package com.seekting.noadvnews

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Administrator on 2017/10/15.
 */
class NewsItemLayout(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = resources.getDimensionPixelSize(R.dimen.item_height)
        setMeasuredDimension(width, height)
    }

}