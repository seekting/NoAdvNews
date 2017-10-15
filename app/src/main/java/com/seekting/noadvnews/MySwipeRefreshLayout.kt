package com.seekting.noadvnews

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.util.Log

/**
 * Created by Administrator on 2017/10/15.
 */
class MySwipeRefreshLayout(context: Context?, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("seekting", ".()" + measuredHeight)
    }
}