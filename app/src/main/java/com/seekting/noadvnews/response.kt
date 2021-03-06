package com.seekting.noadvnews

import java.io.Serializable

data class NoAdvResponse(
        val showapi_res_code: Int, //0
        val showapi_res_error: String,
        val showapi_res_body: ShowapiResBody
)

data class ShowapiResBody(
        val ret_code: Int, //0
        val pagebean: Pagebean
)

data class Pagebean(
        val allPages: Int, //184
        val contentlist: List<Contentlist>,
        val currentPage: Int, //1
        val allNum: Int, //3667
        val maxResult: Int //20
)

class Contentlist(
        val isRead: Boolean,
        val pubDate: String, //2017-09-24 22:25:56
        var pubTime: Long = -1,
        val channelName: String, //国内足球最新
        val desc: String, //腾讯体育9月24日随着富力、权健相继输球，华夏幸福又被长春亚泰逼平，对于山东鲁能而言，这是个向亚冠资格继续发起冲击的绝好机会。但是，就像他们的竞争对手一样，在关键的时刻，鲁能也遭遇了当头一棒。马加特1-2不敌河南建业，不知是建业球迷赛前的“做法”起了效果，还是鲁能自己出了什么难以启齿的问题，总之最近这段
        val channelId: String, //5572a10ab3cdc86cf39001e8
        val link: String, //http://sports.qq.com/a/20170924/049838.htm
        val allList: Array<Any>,
        val content: String, //自动播放开关 自动播放 【战报】建业主场2-1胜鲁能 里卡多胡靖航建功 正在加载... < >腾讯体育9月24日 随着富力、权健相继输球，华夏幸福又被长春亚泰逼平，对于山东鲁能而言，这是个向亚冠资格继续发起冲击的绝好机会。但是，就像他们的竞争对手一样，在关键的时刻，鲁能也遭遇了当头一棒。1-2不敌河南建业，不知是建业球迷赛前的“做法”起了效果，还是鲁能自己出了什么难以启齿的问题，总之最近这段时间，马加特的球队一直霉运不断，状态始终不见起色。就拿本场比赛来说，后防中坚吉尔战前伤退，让鲁能在面对建业这条由巴索戈和里卡多领衔的进攻线时，上半场几乎被打爆。再加上打申花之前主力左后卫郑铮突然骨折，导致鲁能今晚出现在赛场上的这条防线，基本等于临时拼凑，最终吞下失利的苦果，也就不足为奇了。吉尔和郑铮先后受伤，塔尔德利也不在，这场对阵建业的失败，或许可以用“意外”来形容。但这无法解释他们在之前三场比赛中同样难以获胜、同样不能在亚冠资格的竞争中哪怕往前挪动一点点的位置。马加特曾经指出，他希望球员们能够拿出关键时刻获得成功的决心，但现在来看，鲁能不仅仅是缺少决心而已。自从马加特执教鲁能以来，五轮不胜还是从没有出现过的现象。即使是上赛季鲁能保级最艰难的时刻，马加特也没有经历过这样的局面。一些此前被掩盖的弱点，如今随着球队战绩的低迷被一股脑暴露出来。无论不胜期间，鲁能只打进三个进球，今晚对阵建业，他们唯一的攻击手段只有起高球找佩莱。如此简单的套路，或许在赛季初球队气势正盛、对手立足未稳时上去冲一冲，还有效果，但随着赛季的深入，马加特依然继续固守老套路，只能让鲁能的进攻效率越来越低下。此外，鲁能还面临两个很难解决的问题。其一就是不善对付速度型前锋，以今晚为例，上半场宋龙和吴兴涵被巴索戈突了个人仰马翻，鲁能只能通过不断染黄来阻止这位喀麦隆风之子，而相似的一幕，在此前鲁能对付拥有斯蒂夫的延边时，就已经出现。其二，鲁能很诡异地在占据控球优势的比赛中，反而战绩一塌糊涂。今晚他们的控球率高达6成，但场面上非但没有优势可言，还被建业的快速进攻搞得焦头烂额。空有控球优势，只能说明鲁能的进攻效率实在太低，缺少具备灵感的创造型球员，即便鲁能再能控球，结局也只能是拿到一把型号不匹配的“钥匙”，而无法做到“破门而入”。如果鲁能的赛季目标只不过是前六或者前八，那么现在他们似乎已经可以准备庆功宴了。但如果鲁能还有更高的目标、还没有放弃冲击更高层次的野心，那么现在的局面，已经是马加特登陆中超以来最困难的一次，需要引起所有人的警觉。下一轮，鲁能将直接面对亚冠拦路虎天津权健，该怎么办，需要马加特、更需要球队所有人做出姿态。
        val id: String, //9066ee57fce3b62fb5e05c4788f785ef
        val nid: String, //
        val havePic: Boolean, //true
        val title: String, //5轮不胜！马加特遇最危机时刻 再这样恐帅位难保
        val imageurls: Array<Imageurl>,
        val source: String, //国内足球新闻
        val html: String //<p>自动播放开关 自动播放 【战报】建业主场2-1胜鲁能 里卡多胡靖航建功 正在加载... < ></p><p>腾讯体育9月24日 随着富力、权健相继输球，华夏幸福又被长春亚泰逼平，对于山东鲁能而言，这是个向亚冠资格继续发起冲击的绝好机会。但是，就像他们的竞争对手一样，在关键的时刻，鲁能也遭遇了当头一棒。</p><p><img src='http://inews.gtimg.com/newsapp_bt/0/2089839144/641' /></p><p>1-2不敌河南建业，不知是建业球迷赛前的“做法”起了效果，还是鲁能自己出了什么难以启齿的问题，总之最近这段时间，马加特的球队一直霉运不断，状态始终不见起色。就拿本场比赛来说，后防中坚吉尔战前伤退，让鲁能在面对建业这条由巴索戈和里卡多领衔的进攻线时，上半场几乎被打爆。再加上打申花之前主力左后卫郑铮突然骨折，导致鲁能今晚出现在赛场上的这条防线，基本等于临时拼凑，最终吞下失利的苦果，也就不足为奇了。</p><p>吉尔和郑铮先后受伤，塔尔德利也不在，这场对阵建业的失败，或许可以用“意外”来形容。但这无法解释他们在之前三场比赛中同样难以获胜、同样不能在亚冠资格的竞争中哪怕往前挪动一点点的位置。马加特曾经指出，他希望球员们能够拿出关键时刻获得成功的决心，但现在来看，鲁能不仅仅是缺少决心而已。</p><p>自从马加特执教鲁能以来，五轮不胜还是从没有出现过的现象。即使是上赛季鲁能保级最艰难的时刻，马加特也没有经历过这样的局面。一些此前被掩盖的弱点，如今随着球队战绩的低迷被一股脑暴露出来。无论不胜期间，鲁能只打进三个进球，今晚对阵建业，他们唯一的攻击手段只有起高球找佩莱。如此简单的套路，或许在赛季初球队气势正盛、对手立足未稳时上去冲一冲，还有效果，但随着赛季的深入，马加特依然继续固守老套路，只能让鲁能的进攻效率越来越低下。</p><p>此外，鲁能还面临两个很难解决的问题。其一就是不善对付速度型前锋，以今晚为例，上半场宋龙和吴兴涵被巴索戈突了个人仰马翻，鲁能只能通过不断染黄来阻止这位喀麦隆风之子，而相似的一幕，在此前鲁能对付拥有斯蒂夫的延边时，就已经出现。其二，鲁能很诡异地在占据控球优势的比赛中，反而战绩一塌糊涂。今晚他们的控球率高达6成，但场面上非但没有优势可言，还被建业的快速进攻搞得焦头烂额。空有控球优势，只能说明鲁能的进攻效率实在太低，缺少具备灵感的创造型球员，即便鲁能再能控球，结局也只能是拿到一把型号不匹配的“钥匙”，而无法做到“破门而入”。</p><p>如果鲁能的赛季目标只不过是前六或者前八，那么现在他们似乎已经可以准备庆功宴了。但如果鲁能还有更高的目标、还没有放弃冲击更高层次的野心，那么现在的局面，已经是马加特登陆中超以来最困难的一次，需要引起所有人的警觉。下一轮，鲁能将直接面对亚冠拦路虎天津权健，该怎么办，需要马加特、更需要球队所有人做出姿态。</p>

) : Serializable {
//    @SerializedName("pubDate")
//    var pubDate: String = ""
//        set(pubDate) {
//            field = pubDate
//            pubTime = date_format.parse(pubDate).time
//            Log.d("seekting", "data=${pubDate}:$pubTime")
//
//        }

}

data class Imageurl(
        val height: Int, //0
        val width: Int, //0
        val url: String //http://inews.gtimg.com/newsapp_bt/0/2089839144/641
) : Serializable

inline fun Contentlist.markReaded() {
    val news = this.changeDao()
    App.app.mDaoSession.newsDao.update(news)


}


