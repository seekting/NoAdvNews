package com.seekting.noadvnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.webkit.WebView
import java.util.regex.Pattern


val PATTERN = Pattern.compile("(?<=charset=)[(a-zA-Z0-9-)]+")
const val TAG1 = "NewsDetailActivity"

/**
 * Created by Administrator on 2017/9/24.
 */
class NewsDetailActivity : AppCompatActivity() {


    var webView: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        val webSettings = webView!!.settings
        webSettings.savePassword = false
        webSettings.saveFormData = false
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(false)
        val contentList = intent.getSerializableExtra("Contentlist") as Contentlist

        println(contentList.html)
        webView!!.loadData(contentList.html, "text/html", null)
//        val ok = OkHttpClient()
//        val builder = Request.Builder()
//        builder.header("Accept-Charset", "utf-8")
//        builder.url(contentList.link)
//        Log.d(TAG1, "link=${contentList.link}")
//        val call = ok.newCall(builder.build())
//        webView!!.settings.setDefaultTextEncodingName("gb2312");
//        Thread {
//            try {
//                val response = call.execute()
//                val str = response.body().toString()
//                val matcher = PATTERN.matcher(str)
//                if (matcher.find()) {
//                    val charset = matcher.group()
//                    Log.d(TAG1, "charsest=$charset")
//
//                    runOnUiThread({
//                        Log.d(TAG1, str)
//                        webView!!.loadData(str, "text/html", null)
//                    })
//                } else {
//
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                Log.e(TAG1, "exception!!", e)
//            }
//
//
//        }.start()

        setContentView(webView)

    }


    override fun onDestroy() {
        super.onDestroy()
        if (webView != null) {
            var parent = webView!!.parent
            if (parent is ViewGroup) {
                parent.removeView(webView)
            }
            webView!!.clearHistory()
            webView!!.clearCache(true)
            webView!!.loadUrl("about:blank")// clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webView!!.freeMemory()
            webView!!.pauseTimers()
            webView = null

        }
    }
}