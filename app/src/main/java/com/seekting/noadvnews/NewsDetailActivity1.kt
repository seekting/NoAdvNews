package com.seekting.noadvnews

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
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = MAdapter(contentList.allList, requestManager)


    }
}

abstract open class AbsViewHolder(view: View, val requestManager: RequestManager) : RecyclerView.ViewHolder(view) {


    abstract fun setData(any: Any)
}

class ImgViewHolder(view: View, val mImageView: ImageView, requestManager: RequestManager) : AbsViewHolder(view, requestManager) {
    override fun setData(any: Any) {
        if (any is LinkedHashMap<*, *>) {
            requestManager.load(NoUrlEncodeUrl(any["url"].toString())).fitCenter().into(mImageView)
        }
    }

}

class TextViewHolder(view: View, val mTextView: TextView, requestManager: RequestManager) : AbsViewHolder(view, requestManager) {
    override fun setData(any: Any) {
        mTextView.text = any.toString()
    }

}

class MAdapter(val list: Array<Any>, val requestManager: RequestManager) : RecyclerView.Adapter<AbsViewHolder>() {
    companion object {
        val TYPE_TEXT = 1
        val TYPE_IMG = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AbsViewHolder {
        return when (viewType) {

            TYPE_IMG -> {
                val imageView = ImageView(App.app)
                ImgViewHolder(imageView, imageView, requestManager)
            }
            else -> {
                val textView = TextView(App.app)
                textView.textColor = App.app.resources.getColor(R.color.desc_text_color)
                textView.textSize = App.app.resources.getDimension(R.dimen.desc_text_size)
                TextViewHolder(textView, textView, requestManager)
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