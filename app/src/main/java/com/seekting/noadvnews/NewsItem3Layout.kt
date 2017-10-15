package com.seekting.noadvnews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by Administrator on 2017/10/15.
 */
class NewsItem3Layout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = resources.getDimensionPixelSize(R.dimen.item3_height)
        setMeasuredDimension(width, height)
    }

}