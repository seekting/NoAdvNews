package com.seekting.noadvnews

import com.google.gson.Gson
import com.seekting.noadvnews.dao.News
import java.io.File
import java.net.URL


const val NOADV_HOAST = "http://route.showapi.com/109-35"
const val appid = "46647"
const val appkey = "f8226efcfa85460a8f3d703eb08a1689"
const val TAG = "NoAdvRequest"


/**
 * Created by Administrator on 2017/9/23.
 */
class NoAdvRequest(val param: NewsListParam) {

    fun performRequest(): NoAdvResponse {
        var response: NoAdvResponse? = null
        // Log.d(TAG, "usecache=${param.useCache}")
        if (param.useCache) {
            val response = readCache(File(param.fileAbsName))
            val msg = if (response == null) "null" else "not null"
            // Log.d(TAG, "readCache=$msg")
//            if (response != null) {
//                return response
//            }

        }
        val news = readDB()
        val rrr = changeBean(news)
        if (response != null) {
            return response
        }
//        "$NOADV_HOAST?showapi_appid=${param.sys.showapi_appid}&showapi_sign=${param.sys.showapi_sign}&needHtml=1&needContent=1&needAllList=1"
        val url = URL(param.productShowApiSign())
        println("begin request$url")
        val str = url.readText()
        println(str)
        if (param.useCache) {
            writeCache(File(param.fileAbsName), str)

        }

        val bean = changeBean(str)

        saveDB(bean)
        return bean
    }

    fun readCache(file: File): NoAdvResponse? {

        // Log.d(TAG, "file=${file.absolutePath}")
        val res = when {
            file.isDirectory -> {
                file.delete()
                ""
            }
            file.exists() -> {
                file.readText()
            }
            else -> ""
        }
        //  Log.d(TAG, "end readCache")
        if (res.isEmpty()) {
            return null
        }
        return changeBean(res)

    }

    fun saveDB(bean: NoAdvResponse) {
        val daos = changeDao(bean)
        App.app.mDaoSession.newsDao.insertInTx(daos)
    }

    fun readDB(): List<News> {
        val news: List<News> = App.app.mDaoSession.newsDao.loadAll()
        return news
    }

    fun writeCache(file: File, str: String) {
        if (file.exists()) {
            file.delete()
        } else {
            file.parentFile.mkdirs()
        }
        file.writeText(str)
        // Log.d(TAG, "end writeCache")

    }

    fun changeBean(str: String): NoAdvResponse {
        val gson = Gson()
        val response = gson.fromJson<NoAdvResponse>(str, NoAdvResponse::class.java)
        return response
    }

    fun changeBean(list: List<News>): ArrayList<Contentlist> {
        val contentLists = ArrayList<Contentlist>()
        val gson = Gson()
        for (news in list) {
            val contentList = Contentlist(
                    pubDate = news.pubDate,
                    channelName = news.channelName,
                    desc = news.desc,
                    channelId = news.channelId,
                    link = news.link,
                    allList = gson.fromJson<Array<Any>>(news.allList, Array<Any>::class.java),
                    content = news.content,
                    id = news.id,
                    nid = news.nid,
                    havePic = news.havePic,
                    title = news.title,
                    imageurls = gson.fromJson<List<Imageurl>>(news.imageurls, Array<Imageurl>::class.java),
                    source = news.source,
                    html = news.html
            )

            contentLists.add(contentList)
        }
        return contentLists
    }

    fun changeDao(response: NoAdvResponse): List<News> {
        val list = ArrayList<News>()
        for (contentList in response.showapi_res_body.pagebean.contentlist) {
            val news = News()
            news.pubDate = contentList.pubDate
            news.channelName = contentList.channelName
            news.desc = contentList.desc
            news.channelId = contentList.channelId
            news.link = contentList.link
            news.allList = contentList.allList.toString()
            news.content = contentList.content
            news.id = contentList.id
            news.nid = contentList.nid
            news.havePic = contentList.havePic
            news.title = contentList.title
            news.imageurls = contentList.imageurls.toString()
            news.source = contentList.source
            news.html = contentList.html
            list.add(news)

        }
        return list
    }
}


fun main(args: Array<String>) {
    println(getTimeStamp())
}




