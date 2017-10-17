package com.seekting.noadvnews

import android.content.Context
import com.bumptech.glide.Priority
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GenericLoaderFactory
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Administrator on 2017/10/17.
 */
class NoUrlEncodeUrl(val url: String) {
    fun getCacheKey(): String {
        return url
    }
}

class NoUrlEncodeLoader : ModelLoader<NoUrlEncodeUrl, InputStream> {


    override fun getResourceFetcher(model: NoUrlEncodeUrl?, width: Int, height: Int): DataFetcher<InputStream> {
        return NoUrlEncodeStreamFetcher(model)
    }


}

class NoUrlEncodeLoaderFactory() : ModelLoaderFactory<NoUrlEncodeUrl, InputStream> {
    override fun build(context: Context?, factories: GenericLoaderFactory?): ModelLoader<NoUrlEncodeUrl, InputStream> {
        return NoUrlEncodeLoader()
    }

    override fun teardown() {
    }
}


class NoUrlEncodeStreamFetcher(val model: NoUrlEncodeUrl?) : DataFetcher<InputStream> {
    lateinit var mStream: InputStream
    lateinit var urlConnection: HttpURLConnection
    var cancel: Boolean = false

    override fun getId(): String {
        return model!!.getCacheKey()
    }

    override fun cancel() {
        cancel = true
    }

    override fun loadData(priority: Priority?): InputStream? {
        val url = URL(model!!.url.replace("&amp;","&"))
        urlConnection = url.openConnection() as HttpURLConnection

        urlConnection.setConnectTimeout(2500)
        urlConnection.setReadTimeout(2500)
        urlConnection.setUseCaches(false)
        urlConnection.setDoInput(true)

        urlConnection.connect()
        if (cancel) {
            return null
        }
        mStream = urlConnection.getInputStream()

        return mStream
    }

    override fun cleanup() {
        if (mStream != null) {
            try {
                mStream.close()
            } catch (e: IOException) {
                // Ignore
            }

        }
        if (urlConnection != null) {
            urlConnection.disconnect()
        }
    }


}