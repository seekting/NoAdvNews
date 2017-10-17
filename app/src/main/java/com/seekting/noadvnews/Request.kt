package com.seekting.noadvnews

import android.util.Log
import com.seekting.noadvnews.dao.News
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashSet


const val NOADV_HOAST = "http://route.showapi.com/109-35"
const val appid = "46647"
const val appkey = "f8226efcfa85460a8f3d703eb08a1689"
const val TAG = "NoAdvRequest"
val allDBNewsId = HashSet<String>()


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
                    return news.changeBean(allDBNewsId)
                }
            }


        }
        val url = URL(param.productShowApiSign())
        Log.d("seekting", "begin request:\n$url")
        val connect: HttpURLConnection = url.openConnection() as HttpURLConnection
        connect.connectTimeout = 3000
        connect.readTimeout = 3000
        val inputStream = connect.getInputStream()
        val b = ByteArrayOutputStream()
        val array = ByteArray(1024)
        while (true) {
            val length = inputStream.read(array)
            if (length <= 0) {
                break
            }
            b.write(array, 0, length)

        }
        val str = String(b.toByteArray())
        inputStream.close()
        Log.d("seekting", "response:\n $str")
        val response: NoAdvResponse? = str.toResponse()
        response?.let {
            if (param.saveCache) {
                Log.d("seekting", "save cache!")
                saveDB(response)

            }
        }
        Log.d("seekting", "use network!")
        return response?.showapi_res_body?.pagebean?.contentlist ?: ArrayList()

    }


    fun saveDB(bean: NoAdvResponse) {
        val daos = bean.showapi_res_body.pagebean.contentlist.changeDao()
        val allNews = App.app.mDaoSession.newsDao.loadAll()
        val iterator = daos.iterator()
        while (iterator.hasNext()) {
            val dao = iterator.next()
            for (news in allNews) {
                if (dao.id == news.id) {
                    iterator.remove()
                    break
                }
            }

        }
        if (daos.size > 0) {
            App.app.mDaoSession.newsDao.insertInTx(daos)
            Log.d("seekting", "saveDB.()" + daos.size)
        } else {
            Log.d("seekting", "saveDB.()" + "没有要存的数据")

        }
    }

    fun readDB(): List<News> {
        val news: List<News> = App.app.mDaoSession.newsDao.loadAll()
        return news
    }

    fun readDBCache(): List<Contentlist> {
        val news = readDB()
        news?.let {
            if (!news.isEmpty()) {
                Log.d("seekting", "use cache!")
                return news.changeBean(null)
            }
        }
        val contentLists = ArrayList<Contentlist>()
        return contentLists
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
//    println(getTimeStamp())
//    val url = "http://t11.baidu.com/it/u=2302383772,614241019&fm=173&s=3EB035897C931AD042B9388C03007006&w=600&h=381&img.JPEG"
//    val a = URL(url)
//    val con = a.openConnection()
//    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0.1; MI 5 Build/MXB48T; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36")
////    con.addRequestProperty()
//
//    val input = con.getInputStream()
//    val bytes = input.readBytes()
//
//    println("bytes=${bytes.size}")
}





