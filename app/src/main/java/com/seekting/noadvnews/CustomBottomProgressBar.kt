package com.seekting.noadvnews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView

/**
 * Created by Administrator on 2017/10/15.
 */
class CustomBottomProgressBar(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {


    lateinit var mLoadMoreListener: SwipeMenuRecyclerView.LoadMoreListener
    lateinit var clickLoad: View
    lateinit var progress: View
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = resources.getDimensionPixelSize(R.dimen.load_more_height)
//        visibility = View.GONE
        setMeasuredDimension(width, height)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        clickLoad = findViewById(R.id.click_load)
        progress = findViewById(R.id.bottom_progress_bar)
        clickLoad.setOnClickListener(this)
    }

    fun hideProgress() {
        progress.visibility = View.GONE
        clickLoad.visibility = View.VISIBLE
    }

    fun showProgress() {
        progress.visibility = View.VISIBLE
        clickLoad.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        if (mLoadMoreListener != null) {
            progress.visibility = View.VISIBLE
            clickLoad.visibility = View.GONE
            mLoadMoreListener.onLoadMore()
        }
    }

}