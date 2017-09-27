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
import android.widget.TextView


class MainActivity : AppCompatActivity() {


    var filePath: String = ""
    var density: Float = 0f
    lateinit var recycleView: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        newsAdapter = NewsAdapter(null, this)
        recycleView.adapter = newsAdapter

        var thread = Thread({
            val r = NoAdvRequest(NewsListParam(fileAbsName = filePath, useCache = true)).performRequest()

            runOnUiThread({
                newsAdapter.list = r.showapi_res_body.pagebean.contentlist
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

class NewsViewHolder : RecyclerView.ViewHolder {
    var titleView: TextView
    var onItemClickListener: AdapterView.OnItemClickListener

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener) : super(itemView) {
        itemView.setOnClickListener({ onItemClickListener.onItemClick(null, itemView, position, 0) })
        titleView = itemView.findViewById(R.id.title) as TextView
        this.onItemClickListener = onItemClickListener
    }


}

class NewsAdapter(var list: List<Contentlist>?, val context: Context) : RecyclerView.Adapter<NewsViewHolder>(), AdapterView.OnItemClickListener {
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
            else -> holder?.titleView?.text = list!![position].title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NewsViewHolder {
        var item = LayoutInflater.from(context).inflate(R.layout.news_item, null)
        val res = NewsViewHolder(item, this)
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

