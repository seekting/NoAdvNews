//package com.seekting.noadvnews
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.os.SystemClock
//import android.support.v4.view.ViewCompat
//import android.support.v4.widget.SwipeRefreshLayout
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.RequestManager
//import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
//
//
//class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
//
//
//    var filePath: String = ""
//    var density: Float = 0f
//    lateinit var newsAdapter: NewsAdapter
//    lateinit var requestManager: RequestManager
//    lateinit var linearLayoutManager: LinearLayoutManager
//    lateinit var swipeRefreshLayout: SwipeRefreshLayout
//    lateinit var recyclerView: RecyclerView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        requestManager = Glide.with(this)
//        density = resources.displayMetrics.density
//        filePath = "${filesDir.absolutePath}/news/home.json"
//
//        setContentView(R.layout.activity_main)
//        recyclerView = findViewById(R.id.recyclerview) as RecyclerView
//        swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout) as SwipeRefreshLayout
//        val build = HorizontalDividerItemDecoration.Builder(this)
//        build.size(1)
//        build.color(resources.getColor(R.color.item_divider))
//        recyclerView.addItemDecoration(build.build())
//        linearLayoutManager = object : LinearLayoutManager(this) {
//            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
//                val params = super.generateDefaultLayoutParams()
//                params.width = ViewGroup.LayoutParams.MATCH_PARENT
//
//                return params
//            }
//
//        }
//
//        recyclerView.layoutManager = linearLayoutManager
//        val listener = object : RecyclerView.OnScrollListener() {
//            var state: Int = RecyclerView.SCROLL_STATE_IDLE
//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                state = newState
//                if (state == RecyclerView.SCROLL_STATE_IDLE) {
//
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val can = ViewCompat.canScrollVertically(recyclerView, 1)
//                if (!can) {
//                    loadMore()
//                }
//                Log.d("seekting", "onScrolled.()$can:" + dy)
//            }
//
//        }
//        recyclerView.addOnScrollListener(listener)
//        newsAdapter = NewsAdapter(context = this, requestManager = requestManager)
//        recyclerView.adapter = newsAdapter
//        swipeRefreshLayout.isRefreshing = true
//
//        swipeRefreshLayout.setOnRefreshListener(this)
//        swipeRefreshLayout.isEnabled = true
//        asyncLoadData(true)
//
//    }
//
//    fun asyncLoadData(useCache: Boolean) {
//        var thread = Thread({
//            val response = NoAdvRequest(NewsListParam(fileAbsName = filePath, useCache = useCache)).performRequest()
//
//            runOnUiThread({
//                Log.d("seekting", "notifyDataSetChanged")
//                newsAdapter.list = response
//                newsAdapter.notifyDataSetChanged()
//                swipeRefreshLayout.isRefreshing = false
//
//
//            })
//
//        })
//        thread.start()
//    }
//
//    fun loadMoreData(useCache: Boolean) {
//        var thread = Thread({
//
//            SystemClock.sleep(1000)
//            runOnUiThread({
//                Log.d("seekting", "notifyDataSetChanged")
//                val t = newsAdapter.list as ArrayList<Contentlist>
//                t.add(t[0])
//                newsAdapter.notifyDataSetChanged()
////                swipeRefreshLayout.isRefreshing = false
//
//
//            })
//
//        })
//        thread.start()
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onRefresh() {
//        asyncLoadData(false)
//        Log.d("seekting", "onRefresh")
//        setTitle("下拉")
//
//    }
//
//    fun loadMore() {
//
//        loadMoreData(false)
//    }
//
//}
//
//
//open class NewsViewHolder : RecyclerView.ViewHolder {
//    var titleView: TextView
//    var onItemClickListener: AdapterView.OnItemClickListener?
//    var requestManager: RequestManager?
//    val dividerDrawable: Drawable?
//
//    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener?, requestManager: RequestManager?, dividerDrawable: Drawable?) :
//            super(itemView) {
//        this.requestManager = requestManager
//        this.dividerDrawable = dividerDrawable
//        itemView.setOnClickListener({ onItemClickListener?.onItemClick(null, itemView, position, 0) })
//        titleView = itemView.findViewById(R.id.title) as TextView
//        this.onItemClickListener = onItemClickListener
//    }
//
//    open fun setData(contentList: Contentlist) {
//        titleView?.text = contentList.title + ":" + contentList.imageurls.size
//    }
//
//
//}
//
//class LoadMoreHolder : NewsViewHolder {
//    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable) :
//            super(itemView, onItemClickListener, requestManager, dividerDrawable) {
//
//    }
//}
//
//open class NewsViewHolder1 : NewsViewHolder {
//
//    var image1: ImageView
//
//    val imgLayout: LinearLayout
//
//    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable) :
//            super(itemView, onItemClickListener, requestManager, dividerDrawable) {
//        image1 = itemView.findViewById(R.id.image1) as ImageView
//        imgLayout = itemView.findViewById(R.id.img_layout) as LinearLayout
//        imgLayout.dividerDrawable = dividerDrawable
//
//    }
//
//    override fun setData(contentList: Contentlist) {
//        super.setData(contentList)
//        loadImg(contentList.imageurls[0].url, image1)
//    }
//
//    protected fun loadImg(url: String, imageView: ImageView) {
//        requestManager?.load(url)?.placeholder(R.drawable.default_img)?.centerCrop()?.into(imageView)
//    }
//}
//
//open class NewsViewHolder2 : NewsViewHolder1 {
//    var image2: ImageView
//
//    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable)
//            : super(itemView, onItemClickListener, requestManager, dividerDrawable) {
//        image2 = itemView.findViewById(R.id.image2) as ImageView
//    }
//
//    override fun setData(contentList: Contentlist) {
//        super.setData(contentList)
//        loadImg(contentList.imageurls[1].url, image2)
//    }
//
//
//}
//
//class NewsViewHolder3 : NewsViewHolder2 {
//    var image3: ImageView
//
//    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener, requestManager: RequestManager, dividerDrawable: Drawable) :
//            super(itemView, onItemClickListener, requestManager, dividerDrawable) {
//        image3 = itemView.findViewById(R.id.image3) as ImageView
//    }
//
//    override fun setData(contentList: Contentlist) {
//        super.setData(contentList)
//        loadImg(contentList.imageurls[2].url, image3)
//    }
//
//
//}
//
//
//class NewsAdapter(var list: List<Contentlist>? = null, val context: Context, val requestManager: RequestManager, var dividerDrawable: Drawable? = null)
//    : RecyclerView.Adapter<NewsViewHolder>(), AdapterView.OnItemClickListener {
//
//    override fun getItemCount(): Int {
//        return 1 + getListSize()
//    }
//
//    fun getListSize(): Int {
//        return if (list == null) 0 else list!!.size
//    }
//
//    init {
//        dividerDrawable = object : ColorDrawable(Color.TRANSPARENT) {
//            val gap = Math.round(1 * context.resources.displayMetrics.density)
//            override fun getIntrinsicHeight(): Int {
//                return gap
//            }
//
//            override fun getIntrinsicWidth(): Int {
//                return gap
//            }
//
//        }
//    }
//
//    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        val content: Contentlist = list!![position]
//        val intent = Intent(context, NewsDetailActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.putExtra("Contentlist", content)
//        context.startActivity(intent)
//    }
//
//
//    override fun onBindViewHolder(holder: NewsViewHolder?, position: Int) {
//        when {
//            list == null -> return
//            position >= list!!.size -> return
//            else -> {
//
//                if (holder != null) {
//                    holder.setData(list!![position])
//                }
//            }
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NewsViewHolder {
//        val resId = when (viewType) {
//            ITEM0 -> R.layout.news_item
//            ITEM1 -> R.layout.news_item1
//            ITEM2 -> R.layout.news_item2
//            ITEM3 -> R.layout.news_item3
//            LOAD_MORE -> R.layout.custom_bottom_progressbar
//            else -> R.layout.news_item3
//        }
//        var item = LayoutInflater.from(context).inflate(resId, null)
//        val result = when (viewType) {
//            ITEM0 -> NewsViewHolder(item, this, requestManager, dividerDrawable!!)
//            ITEM1 -> NewsViewHolder1(item, this, requestManager, dividerDrawable!!)
//            ITEM2 -> NewsViewHolder2(item, this, requestManager, dividerDrawable!!)
//            ITEM3 -> NewsViewHolder3(item, this, requestManager, dividerDrawable!!)
//            LOAD_MORE -> LoadMoreHolder(item, this, requestManager, dividerDrawable!!)
//            else -> NewsViewHolder3(item, this, requestManager, dividerDrawable!!)
//        }
//        return result
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        val listSize = getListSize()
//        if (position == listSize) {
//            return LOAD_MORE
//        }
//        val size: Int = list?.get(position)?.imageurls?.size!!
//        val res = when (size) {
//            0 -> ITEM0
//            1 -> ITEM1
//            2 -> ITEM2
//            else -> ITEM3
//        }
//
//        return res
//    }
//}
//
//
//const val LOAD_MORE = 0
//const val ITEM0 = 1
//const val ITEM1 = 2
//const val ITEM2 = 3
//const val ITEM3 = 4
//fun main(args: Array<String>) {
//
////    val param = NoAdvRequestParam(fileAbsName = "", useCache = false)
////    val request = NoAdvRequest(param)
////    val response = request.performRequest()
////    for (v in response.showapi_res_body.pagebean.contentlist) {
////        println(v.title)
////    }
//
//
//}
//
