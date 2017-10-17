package com.seekting.noadvnews

import android.text.TextUtils
import com.seekting.libcommon.MD5Utils
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

/**
 * Created by Administrator on 2017/9/27.
 */

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NewsParam(val key: String)

const val useCache = 1
const val useNetWork = 2

data class NewsListParam(
        val host: String = NOADV_HOAST,
        val sys: SysListParam = SysListParam(),
        val app: AppListParam = AppListParam(),
        val useCache: Boolean = false,
        val saveCache: Boolean = true,
        val fileAbsName: String = ""
)

data class SysListParam(
        @NewsParam(key = "showapi_appid")
        val showapi_appid: String = appid,
        open val showapi_sign: String = appkey,
        @NewsParam(key = "showapi_timestamp")
        val showapi_timestamp: String = getTimeStamp(),
        @NewsParam(key = "showapi_sign_method")
        val showapi_sign_method: String = "md5",
        @NewsParam(key = "showapi_res_gzip")
        val showapi_res_gzip: String = "1"

)


data class AppListParam(
        @NewsParam(key = "channelId")
        val channelId: String = "",
        @NewsParam(key = "channelName")
        val channelName: String = "",
        @NewsParam(key = "title")
        val title: String = "",
        @NewsParam(key = "page")
        var page: String = "",
        @NewsParam(key = "needContent")
        val needContent: String = "1",
        @NewsParam(key = "needHtml")
        val needHtml: String = "1",
        @NewsParam(key = "needAllList")
        val needAllList: String = "1",
        @NewsParam(key = "maxResult")
        val maxResult: String = "",
        @NewsParam(key = "id")
        val id: String = ""

)

const val TIME_FORMAT = "yyyyMMddHHmmss"//20170927172916
val mThreadLocal = ThreadLocal<DateFormat>()
fun getTimeStamp(milliseconds: Long = System.currentTimeMillis()): String {
    val dateFormat = mThreadLocal.getOrSet {
        DateFormat(Date(), SimpleDateFormat(TIME_FORMAT))
    }
    return dateFormat.getDate(milliseconds)
}

data class DateFormat(
        val date: Date,
        val formatter: SimpleDateFormat
)


inline fun DateFormat.getDate(time: Long): String {
    date.time = time
    return formatter.format(date)
}

fun NewsListParam.developShowApiSign(): String {
    val treeMap = TreeMap<String, String>()
    fillWithNewsParam(treeMap, sys)
    fillWithNewsParam(treeMap, app)
    val en = treeMap.asIterable()
    val result = StringBuilder()
    result.append(host)
    result.append("?")
    for (entry in en) {
        result.append(entry.key)
        result.append("=")
        result.append(URLEncoder.encode(entry.value, "utf-8"))
        result.append("&")
    }
    result.append("showapi_sign=${sys.showapi_sign}")
    return result.toString()
}

fun NewsListParam.productShowApiSign(): String {
    val treeMap = TreeMap<String, String>()
    fillWithNewsParam(treeMap, sys)
    fillWithNewsParam(treeMap, app)
    val en = treeMap.asIterable()
    val sb = StringBuilder()
    val result = StringBuilder()
    result.append(host)
    result.append("?")
    for (entry in en) {
        sb.append(entry.key)
        sb.append(entry.value)
        result.append(entry.key)
        result.append("=")
        result.append(URLEncoder.encode(entry.value, "utf-8"))
        result.append("&")
    }
    sb.append(sys.showapi_sign)
    val str = sb.toString()
    val showapi_sign = MD5Utils.getMD5(str)
    result.append("showapi_sign=$showapi_sign")
    return result.toString()

}

fun fillWithNewsParam(treeMap: TreeMap<String, String>, t: Any) {
    for (filed in t.javaClass.declaredFields) {
        val annotation = filed.getAnnotation(NewsParam::class.java)
        if (annotation != null) {
            if (filed.type == String::class.java) {
                filed.isAccessible = true
                val value: String = filed.get(t).toString()
                if (!TextUtils.isEmpty(value)) {
                    treeMap.put(annotation.key, value)
                }

            }

        }
    }
}

fun main(args: Array<String>) {
    val param = NewsListParam(fileAbsName = "f")
    var str = param.productShowApiSign()
    println(str)
    str = param.developShowApiSign()
    println(str)

}

