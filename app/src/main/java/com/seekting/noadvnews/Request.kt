package com.seekting.noadvnews

import android.util.Log
import com.seekting.noadvnews.dao.News
import java.net.URL
import java.util.*


const val NOADV_HOAST = "http://route.showapi.com/109-35"
const val appid = "46647"
const val appkey = "f8226efcfa85460a8f3d703eb08a1689"
const val TAG = "NoAdvRequest"


/**
// * Created by Administrator on 2017/9/23.
 */
class NoAdvRequest(val param: NewsListParam) {

    fun performRequest(): List<Contentlist> {
        if (param.useCache) {
            val news = readDB()
            news?.let {
                if (!news.isEmpty()) {
                    Log.d("seekting", "use cache!")
                    return news.changeBean()
                }
            }


        }
        val url = URL(param.productShowApiSign())
        println("begin request:\n$url")
        val str = url.readText()
        println("response:\n $str")
        val response: NoAdvResponse? = str.toResponse()
        response?.let {
            if (param.useCache) {
                saveDB(response)
                Log.d("seekting", "save cache!")

            }
        }
        Log.d("seekting", "use network!")
        return response?.showapi_res_body?.pagebean?.contentlist ?: ArrayList()

    }


    fun saveDB(bean: NoAdvResponse) {
        val daos = bean.showapi_res_body.pagebean.contentlist.changeDao()
        App.app.mDaoSession.newsDao.insertInTx(daos)
    }

    fun readDB(): List<News> {
        val news: List<News> = App.app.mDaoSession.newsDao.loadAll()
        return news
    }


}

//fun readCache(file: File): NoAdvResponse? {
//
//    // Log.d(TAG, "file=${file.absolutePath}")
//    val res = when {
//        file.isDirectory -> {
//            file.delete()
//            ""
//        }
//        file.exists() -> {
//            file.readText()
//        }
//        else -> ""
//    }
//    //  Log.d(TAG, "end readCache")
//    if (res.isEmpty()) {
//        return null
//    }
//    return changeBean(res)
//
//}
//fun writeCache(file: File, str: String) {
//    if (file.exists()) {
//        file.delete()
//    } else {
//        file.parentFile.mkdirs()
//    }
//    file.writeText(str)
//    // Log.d(TAG, "end writeCache")
//
//}
fun main(args: Array<String>) {
    println(getTimeStamp())
}




