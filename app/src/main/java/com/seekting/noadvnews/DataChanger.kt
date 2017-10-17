package com.seekting.noadvnews

import com.google.gson.Gson
import com.seekting.noadvnews.dao.News

/**
 * Created by Administrator on 2017/10/13.
 */
inline fun String.toResponse(): NoAdvResponse {
    val gson = Gson()
    val response = gson.fromJson<NoAdvResponse>(this, NoAdvResponse::class.java)
    return response
}

inline fun List<News>.changeBean(set: HashSet<String>?): ArrayList<Contentlist> {
    val contentLists = ArrayList<Contentlist>()
    val gson = Gson()
    for (news in this) {
        val contentList = Contentlist(
                pubTime = news.pubTime,
                channelName = news.channelName,
                desc = news.desc,
                channelId = news.channelId,
                link = news.link,
                allList = gson.fromJson<Array<Any>>(news.allList, Array<Any>::class.java),
                content = news.content,
                id = news.id,
                nid = news.nid ?: "",
                havePic = news.havePic,
                title = news.title,
                imageurls = gson.fromJson<Array<Imageurl>>(news.imageurls, Array<Imageurl>::class.java),
                source = news.source,
                html = news.html
        )
        contentList.pubDate = news.pubDate

        contentLists.add(contentList)
        set?.add(news.id)
    }
    return contentLists
}

inline fun List<Contentlist>.changeDao(): MutableList<News> {
    val list = ArrayList<News>()
    for (contentList in this) {
        val news = News()
        val gson = Gson()
        news.pubDate = contentList.pubDate
        news.pubTime = contentList.pubTime
        news.channelName = contentList.channelName
        news.desc = contentList.desc
        news.channelId = contentList.channelId
        news.link = contentList.link
        news.allList = gson.toJson(contentList.allList)
        news.content = contentList.content
        news.id = contentList.id
        news.nid = contentList.nid
        news.havePic = contentList.havePic
        news.title = contentList.title
        news.imageurls = gson.toJson(contentList.imageurls)
        news.source = contentList.source
        news.html = contentList.html
        list.add(news)

    }
    return list
}