package com.seekting.noadvnews

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration


//public val footer = Contentlist(
//        pubDate = "",
//        channelName = "",
//        title = "",
//        allList = arrayOf(0),
//        channelId = "",
//        content = "",
//        desc = "",
//        havePic = false,
//        html = "",
//        id = "",
//        imageurls = arrayOf(),
//        link = "",
//        nid = "",
//        source = ""
//)

class MainActivity1 : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, SwipeMenuRecyclerView.LoadMoreListener {


    var mFootIndex = -1
    val recyclerview_divider_height = 1

    var filePath: String = ""
    var density: Float = 0f
    lateinit var newsAdapter: NewsAdapter
    lateinit var requestManager: RequestManager
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: SwipeMenuRecyclerView
    var state: Int = -1
    val dimens = SparseArray<Int>()
    var recyclerViewHeight = 0
    lateinit var onPreDrawListener: OnPreDrawListener

    lateinit var defineLoadMoreView: MyDefaultLoadMoreView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        recyclerViewHeight = (resources.displayMetrics.heightPixels - 24 * resources.displayMetrics.density).toInt()

        Log.d("seekting", ".()" + resources.displayMetrics.density + "," + recyclerViewHeight)
        dimens.put(ITEM0, resources.getDimensionPixelSize(R.dimen.item_height))
        dimens.put(ITEM1, resources.getDimensionPixelSize(R.dimen.item1_height))
        dimens.put(ITEM2, resources.getDimensionPixelSize(R.dimen.item2_height))
        dimens.put(ITEM3, resources.getDimensionPixelSize(R.dimen.item3_height))

        requestManager = Glide.with(this)
        density = resources.displayMetrics.density
        filePath = "${filesDir.absolutePath}/news/home.json"

        setContentView(R.layout.activity_main1)
        recyclerView = findViewById(R.id.recyclerview) as SwipeMenuRecyclerView
        swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout) as SwipeRefreshLayout
        onPreDrawListener = object : OnPreDrawListener {
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onPreDraw(): Boolean {
                recyclerViewHeight = swipeRefreshLayout.measuredHeight
                swipeRefreshLayout.viewTreeObserver.removeOnPreDrawListener(onPreDrawListener)
                return true
            }

        }
        swipeRefreshLayout.viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
        val build = HorizontalDividerItemDecoration.Builder(this)
        build.size(recyclerview_divider_height)
        build.color(resources.getColor(R.color.item_divider))
        recyclerView.addItemDecoration(build.build())
        linearLayoutManager = object : LinearLayoutManager(this) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                val params = super.generateDefaultLayoutParams()
                params.width = ViewGroup.LayoutParams.MATCH_PARENT

                return params
            }

        }

        recyclerView.layoutManager = linearLayoutManager
        newsAdapter = NewsAdapter(context = this, requestManager = requestManager, loadMoreListener = this)
        recyclerView.adapter = newsAdapter
        defineLoadMoreView = MyDefaultLoadMoreView(this)
        defineLoadMoreView.mLoadMoreListener = this
        recyclerView.setLoadMoreView(defineLoadMoreView)
        recyclerView.addFooterView(defineLoadMoreView)
//        recyclerView.useDefaultLoadMore()
        recyclerView.setLoadMoreListener {
            loadMoreData(true)
        }
        recyclerView.setAutoLoadMore(false)
        swipeRefreshLayout.isRefreshing = true

        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.isEnabled = true
        asyncLoadData(true)
        val listener = object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

        }
        recyclerView.addOnScrollListener(listener)

    }

    fun canScroll(): Boolean {
        if (newsAdapter.list == null) {
            return false
        }

        val size = newsAdapter.list.size
        var length = 0
        for (i in 0..size - 1) {
            val height = dimens.get(newsAdapter.getItemViewType(i))
            length = length + height + recyclerview_divider_height
            if (length >= recyclerViewHeight) {

                return true
            }

        }


        return false

    }

    fun notifyChanged() {
        val canScroll = canScroll()
        if (canScroll) {
            recyclerView.setAutoLoadMore(true)
        } else {
            recyclerView.setAutoLoadMore(false)
        }
        recyclerView.loadMoreFinish(false, true)

        newsAdapter.notifyDataSetChanged()
    }


    fun asyncLoadData(useCache: Boolean) {
        threadpool {
            try {
                val response = NoAdvRequest(NewsListParam(fileAbsName = filePath, useCache = useCache)).performRequest()
                runOnUiThread({
                    Log.d("seekting", "notifyDataSetChanged")
                    newsAdapter.list.clear()
                    newsAdapter.list.addAll(response)
                    swipeRefreshLayout.isRefreshing = false
                    notifyChanged()


                })
            } catch (t: Throwable) {
                runOnUiThread({
                    Log.e("seekting", "fail", t)
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, "超时" + t.javaClass.name, Toast.LENGTH_SHORT).show()


                })
            }


        }
    }

    var page = 1
    fun loadMoreData(useCache: Boolean) {
        var thread = Thread({
            try {
                val param = NewsListParam(fileAbsName = filePath, useCache = false)
                param.app.page = page.toString()
                page++
                val request = NoAdvRequest(param)
                request.performRequest()
                val response = request.readDBCache()
                runOnUiThread({
                    Log.d("seekting", "notifyDataSetChanged")
                    newsAdapter.list.clear()
                    newsAdapter.list.addAll(response)
                    notifyChanged()


                })
            } catch (t: Throwable) {
                runOnUiThread({
                    Log.e("seekting", "load more fail", t)
                    notifyChanged()
                    Toast.makeText(applicationContext, "超时" + t.javaClass.name, Toast.LENGTH_SHORT).show()


                })
            }


        })
        thread.start()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRefresh() {
        asyncLoadData(false)


    }

    override fun onLoadMore() {
        loadMoreData(false)
    }

}

abstract class BaseHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView)


    abstract fun setData(contentList: Contentlist)
}

open class NewsViewHolder : BaseHolder {
    var titleView: TextView
    var onItemClickListener: AdapterView.OnItemClickListener?
    var requestManager: RequestManager?
    val dividerDrawable: Drawable?

    constructor(itemView: View, onItemClickListener: AdapterView.OnItemClickListener?, requestManager: RequestManager?, dividerDrawable: Drawable?) :
            super(itemView) {
        this.requestManager = requestManager
        this.dividerDrawable = dividerDrawable
        itemView.setOnClickListener({ onItemClickListener?.onItemClick(null, itemView, position, 0) })
        titleView = itemView.findViewById(R.id.title) as TextView
        this.onItemClickListener = onItemClickListener
    }

    override fun setData(contentList: Contentlist) {
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
        requestManager?.load(NoUrlEncodeUrl(url))?.placeholder(R.drawable.default_img)?.centerCrop()?.into(imageView)
        println("url=${url}")
//        requestManager?.load(this)

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


class NewsAdapter(val list: ArrayList<Contentlist> = ArrayList<Contentlist>(), val context: Context,
                  val requestManager: RequestManager,
                  var dividerDrawable: Drawable? = null,
                  var loadMoreListener: SwipeMenuRecyclerView.LoadMoreListener)
    : RecyclerView.Adapter<BaseHolder>(), AdapterView.OnItemClickListener {

    override fun getItemCount(): Int {
        return getListSize()
    }

    fun getListSize(): Int {
        return if (list == null) 0 else list!!.size
    }

//    fun getItemHeight(position: Int): Int {
//        val viewType = getItemViewType(position)
//        when (viewType) {
//            LOAD_MORE:
//        }
//    }

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
        val intent = Intent(context, NewsDetailActivity1::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("Contentlist", content)
        context.startActivity(intent)
    }


    override fun onBindViewHolder(holder: BaseHolder?, position: Int) {
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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseHolder {
        val resId = when (viewType) {
            ITEM0 -> R.layout.news_item
            ITEM1 -> R.layout.news_item1
            ITEM2 -> R.layout.news_item2
            ITEM3 -> R.layout.news_item3
            else -> R.layout.news_item3
        }
        var item = LayoutInflater.from(context).inflate(resId, null)
        val result = when (viewType) {
            ITEM0 -> NewsViewHolder(item, this, requestManager, dividerDrawable!!)
            ITEM1 -> NewsViewHolder1(item, this, requestManager, dividerDrawable!!)
            ITEM2 -> NewsViewHolder2(item, this, requestManager, dividerDrawable!!)
            ITEM3 -> NewsViewHolder3(item, this, requestManager, dividerDrawable!!)

            else -> NewsViewHolder3(item, this, requestManager, dividerDrawable!!)
        }
        return result
    }

    override fun getItemViewType(position: Int): Int {
        val item = list?.get(position)
        val size: Int = list?.get(position)?.imageurls?.size!!
        val res = when (size) {
            0 -> ITEM0
            1 -> ITEM1
            2 -> ITEM2
            else -> ITEM3
        }

        return res
    }
}


const val ITEM0 = 1
const val ITEM1 = 2
const val ITEM2 = 3
const val ITEM3 = 4
fun main(args: Array<String>) {

//    val param = NoAdvRequestParam(fileAbsName = "", useCache = false)
//    val request = NoAdvRequest(param)
//    val response = request.performRequest()
//    for (v in response.showapi_res_body.pagebean.contentlist) {
//        println(v.title)
//    }


}

