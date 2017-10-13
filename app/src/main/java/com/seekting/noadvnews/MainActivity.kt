package com.seekting.noadvnews

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration


class MainActivity : AppCompatActivity() {


    var filePath: String = ""
    var density: Float = 0f
    lateinit var recycleView: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    lateinit var requestManager: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestManager = Glide.with(this)
        density = resources.displayMetrics.density
        filePath = "${filesDir.absolutePath}/news/home.json"

        setContentView(R.layout.activity_main)
        recycleView = findViewById(R.id.recycle) as RecyclerView
        val build = HorizontalDividerItemDecoration.Builder(this)
        build.size(1)
        build.color(getColor(R.color.item_divider))
        recycleView.addItemDecoration(build.build())
        recycleView.layoutManager = object : LinearLayoutManager(this) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                val params = super.generateDefaultLayoutParams()
                params.width = ViewGroup.LayoutParams.MATCH_PARENT

                return params
            }

        }

        newsAdapter = NewsAdapter(context = this, requestManager = requestManager)
        recycleView.adapter = newsAdapter

        var thread = Thread({
            val response = NoAdvRequest(NewsListParam(fileAbsName = filePath, useCache = true)).performRequest()

            runOnUiThread({
                newsAdapter.list = response
                newsAdapter.notifyDataSetChanged()
            })

        })
        thread.start()

        newsAdapter

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()
    }


}

open class NewsViewHolder : RecyclerView.ViewHolder {
    var titleView: TextView
    var onItemClickListener: AdapterView.OnItemClickListener
    var requestManager: RequestManager
    val dividerDrawable: Drawable

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable) :
            super(itemView) {
        this.requestManager = requestManager
        this.dividerDrawable = dividerDrawable
        itemView.setOnClickListener({ onItemClickListener.onItemClick(null, itemView, position, 0) })
        titleView = itemView.findViewById(R.id.title) as TextView
        this.onItemClickListener = onItemClickListener
    }

    open fun setData(contentList: Contentlist) {
        titleView?.text = contentList.title + ":" + contentList.imageurls.size
    }


}

open class NewsViewHolder1 : NewsViewHolder {

    var image1: ImageView

    val imgLayout: LinearLayout

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable) :
            super(itemView, onItemClickListener, requestManager, dividerDrawable) {
        image1 = itemView.findViewById(R.id.image1) as ImageView
        imgLayout = itemView.findViewById(R.id.img_layout) as LinearLayout
        imgLayout.dividerDrawable = dividerDrawable

    }

    override fun setData(contentList: Contentlist) {
        super.setData(contentList)
        loadImg(contentList.imageurls[0].url, image1)
    }

    protected fun loadImg(url: String, imageView: ImageView) {
        requestManager.load(url).placeholder(R.drawable.default_img).centerCrop().into(imageView)
    }
}

open class NewsViewHolder2 : NewsViewHolder1 {
    var image2: ImageView

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable)
            : super(itemView, onItemClickListener, requestManager, dividerDrawable) {
        image2 = itemView.findViewById(R.id.image2) as ImageView
    }

    override fun setData(contentList: Contentlist) {
        super.setData(contentList)
        loadImg(contentList.imageurls[1].url, image2)
    }


}

class NewsViewHolder3 : NewsViewHolder2 {
    var image3: ImageView

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable) :
            super(itemView, onItemClickListener, requestManager, dividerDrawable) {
        image3 = itemView.findViewById(R.id.image3) as ImageView
    }

    override fun setData(contentList: Contentlist) {
        super.setData(contentList)
        loadImg(contentList.imageurls[2].url, image3)
    }


}


class NewsAdapter(var list: List<Contentlist>? = null, val context: Context, val requestManager: RequestManager, var dividerDrawable: Drawable? = null)
    : RecyclerView.Adapter<NewsViewHolder>(), AdapterView.OnItemClickListener {
    init {
        dividerDrawable = object : ColorDrawable(Color.TRANSPARENT) {
            val gap = Math.round(1 * context.resources.displayMetrics.density)
            override fun getIntrinsicHeight(): Int {
                return gap
            }

            override fun getIntrinsicWidth(): Int {
                return gap
            }

        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val content: Contentlist = list!![position]
        val intent = Intent(context, NewsDetailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("Contentlist", content)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return if (list != null) list!!.size else 0
    }

    override fun onBindViewHolder(holder: NewsViewHolder?, position: Int) {
        when {
            list == null -> return
            position >= list!!.size -> return
            else -> {

                if (holder != null) {
                    holder.setData(list!![position])
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NewsViewHolder {
        val resId = when (viewType) {
            0 -> R.layout.news_item
            1 -> R.layout.news_item1
            2 -> R.layout.news_item2
            3 -> R.layout.news_item3
            else -> R.layout.news_item3
        }
        var item = LayoutInflater.from(context).inflate(resId, null)
        val result = when (viewType) {
            1 -> NewsViewHolder1(item, this, requestManager, dividerDrawable!!)
            2 -> NewsViewHolder2(item, this, requestManager, dividerDrawable!!)
            3 -> NewsViewHolder3(item, this, requestManager, dividerDrawable!!)
            else -> NewsViewHolder(item, this, requestManager, dividerDrawable!!)
        }
        return result
    }

    override fun getItemViewType(position: Int): Int {
        val size: Int = list?.get(position)?.imageurls?.size!!
        val res = when (size) {
            0 -> 0
            1 -> 1
            2 -> 2
            else -> 3
        }

        return res
    }
}

fun main(args: Array<String>) {

//    val param = NoAdvRequestParam(fileAbsName = "", useCache = false)
//    val request = NoAdvRequest(param)
//    val response = request.performRequest()
//    for (v in response.showapi_res_body.pagebean.contentlist) {
//        println(v.title)
//    }


}

