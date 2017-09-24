package com.seekting.noadvnews

import com.google.gson.Gson
import java.io.File
import java.net.URL


const val NOADV_HOAST = "http://route.showapi.com/109-35"
const val appid = "46647"
const val appkey = "f8226efcfa85460a8f3d703eb08a1689"
const val TAG = "NoAdvRequest"


/**
 * Created by Administrator on 2017/9/23.
 */
class NoAdvRequest(val param: NoAdvRequestParam) {

    fun performRequest(): NoAdvResponse {
        var response: NoAdvResponse? = null
        // Log.d(TAG, "usecache=${param.useCache}")
        if (param.useCache) {
            val response = readCache(File(param.fileAbsName))
            val msg = if (response == null) "null" else "not null"
            // Log.d(TAG, "readCache=$msg")
            if (response != null) {
                return response
            }

        }
        val url = URL("$NOADV_HOAST?showapi_appid=${param.showapi_appid}&showapi_sign=${param.showapi_sign}&needHtml=1&needContent=1&needAllList=1")
        //  Log.d(TAG, "begin request$url")
        val str = url.readText()
        System.out.println(str)
        if (param.useCache) {
            writeCache(File(param.fileAbsName), str)
        }
        return changeBean(str)
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
}

data class NoAdvRequestParam(
        var showapi_appid: String = appid,
        var showapi_sign: String = appkey,
        var fileAbsName: String,
        var useCache: Boolean

)


