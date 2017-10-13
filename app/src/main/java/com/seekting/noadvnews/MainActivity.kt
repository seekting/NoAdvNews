package com.seekting.noadvnews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager


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

        recycleView.layoutManager = object : LinearLayoutManager(this) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                val params = super.generateDefaultLayoutParams()
                params.width = ViewGroup.LayoutParams.MATCH_PARENT

                return params
            }

        }

        newsAdapter = NewsAdapter(null, this, requestManager)
        recycleView.adapter = newsAdapter

        var thread = Thread({
            val response = NoAdvRequest(NewsListParam(fileAbsName = filePath, useCache = true)).performRequest()

            runOnUiThread({
                newsAdapter.list = response.showapi_res_body.pagebean.contentlist
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

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager) : super(itemView) {
        this.requestManager = requestManager
        itemView.setOnClickListener({ onItemClickListener.onItemClick(null, itemView, position, 0) })
        titleView = itemView.findViewById(R.id.title) as TextView
        this.onItemClickListener = onItemClickListener
    }

    open fun setData(contentList: Contentlist) {
        titleView?.text = contentList.title
    }


}

class NewsViewHolder1 : NewsViewHolder {

    lateinit var image1: ImageView

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager) : super(itemView, onItemClickListener, requestManager) {
        image1 = itemView.findViewById(R.id.image1) as ImageView
    }

    override fun setData(contentList: Contentlist) {
        super.setData(contentList)
        requestManager.load(contentList.imageurls[0].url).into(image1)

    }


}

class NewsViewHolder2 : NewsViewHolder {
    lateinit var image2: ImageView

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager) : super(itemView, onItemClickListener, requestManager) {
        image2 = itemView.findViewById(R.id.image2) as ImageView
    }

    override fun setData(contentList: Contentlist) {
        super.setData(contentList)
        requestManager.load(contentList.imageurls[1].url).into(image2)
    }


}

class NewsViewHolder3 : NewsViewHolder {
    lateinit var image3: ImageView

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager) : super(itemView, onItemClickListener, requestManager) {
        image3 = itemView.findViewById(R.id.image2) as ImageView
    }

    override fun setData(contentList: Contentlist) {
        super.setData(contentList)
        requestManager.load(contentList.imageurls[2].url).into(image3)
    }


}


class NewsAdapter(var list: List<Contentlist>?, val context: Context, val requestManager: RequestManager) : RecyclerView.Adapter<NewsViewHolder>(), AdapterView.OnItemClickListener {
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
            1 -> R.layout.news_item1
            2 -> R.layout.news_item2
            3 -> R.layout.news_item3
            else -> R.layout.news_item
        }
        var item = LayoutInflater.from(context).inflate(resId, null)
        val result = when (viewType) {
            1 -> NewsViewHolder1(item, this, requestManager)
            2 -> NewsViewHolder2(item, this, requestManager)
            3 -> NewsViewHolder3(item, this, requestManager)
            else -> NewsViewHolder(item, this, requestManager)
        }
        return result
    }

    override fun getItemViewType(position: Int): Int {
        val size: Int = list?.get(position)?.imageurls?.size!!

        return size
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

