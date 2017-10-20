package com.seekting.noadvnews

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import org.jetbrains.anko.textColor


/**
 * Created by Administrator on 2017/9/24.
 */
class NewsDetailActivity1 : AppCompatActivity() {

    companion object Consts {
        const private val TAG = "NewsDetailActivity1"
        const private val ALL_LIST = "Contentlist"

    }

    lateinit var requestManager: RequestManager
    lateinit var mRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentList = intent.getSerializableExtra("Contentlist") as Contentlist
        mRecyclerView = RecyclerView(this)
        requestManager = Glide.with(this)
        setContentView(mRecyclerView)
        mRecyclerView.layoutManager = object : LinearLayoutManager(this) {

            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                params.rightMargin = resources.getDimensionPixelSize(R.dimen.margin_8dp)
                params.leftMargin = params.rightMargin
                return params
            }
        }
        mRecyclerView.adapter = MAdapter(resources, contentList.allList, requestManager)


    }
}

abstract open class AbsViewHolder(val imageWidth: Int, view: View, val requestManager: RequestManager) : RecyclerView.ViewHolder(view) {


    abstract fun setData(any: Any)
}

class ImgViewHolder(imageWidth: Int, view: View, val mImageView: ImageView, requestManager: RequestManager)
    : AbsViewHolder(imageWidth, view, requestManager) {
    override fun setData(any: Any) {
        if (any is LinkedHashMap<*, *>) {
            requestManager.load(NoUrlEncodeUrl(any["url"].toString()))
                    .fitCenter().override(imageWidth, imageWidth).thumbnail(0.1f).fitCenter().
                    into(mImageView)
        }
    }

}

class TextViewHolder(width: Int, view: View, val mTextView: TextView, requestManager: RequestManager)
    : AbsViewHolder(width, view, requestManager) {
    override fun setData(any: Any) {
        mTextView.text = any.toString()
    }

}

class MAdapter(val resources: Resources, val list: Array<Any>, val requestManager: RequestManager) : RecyclerView.Adapter<AbsViewHolder>() {

    val imageWidth: Int

    companion object {
        val TYPE_TEXT = 1
        val TYPE_IMG = 2
    }

    init {
        imageWidth = resources.displayMetrics.widthPixels - resources.getDimensionPixelSize(R.dimen.margin_8dp) * 2
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AbsViewHolder {
        return when (viewType) {

            TYPE_IMG -> {
                val imageView = ImageView(App.app)
                ImgViewHolder(imageWidth, imageView, imageView, requestManager)
            }
            else -> {
                val textView = TextView(App.app)
                textView.textColor = App.app.resources.getColor(R.color.desc_text_color)
                textView.textSize = App.app.resources.getDimension(R.dimen.desc_text_size)
                TextViewHolder(imageWidth, textView, textView, requestManager)
            }
        }
    }

    override fun onBindViewHolder(holder: AbsViewHolder, position: Int) {
        holder.setData(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = list.get(position)
        when {
            item is String -> {
                return TYPE_TEXT
            }
            else -> return TYPE_IMG
        }
    }

}