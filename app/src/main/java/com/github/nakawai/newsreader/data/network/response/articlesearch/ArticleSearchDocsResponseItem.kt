package com.github.nakawai.newsreader.data.network.response.articlesearch

import com.squareup.moshi.Json

class ArticleSearchDocsResponseItem {
    @Json(name = "abstract")
    var articleAbstract: String? = null

    @Json(name = "web_url")
    var webUrl: String? = null

    @Json(name = "section_name")
    var sectionName: String? = null

    @Json(name = "subsection")
    var subsection: String? = null

    @Json(name = "lead_paragraph")
    var title: String? = null

    /**
     * @see PATTERN
     */
    @Json(name = "pub_date")
    var publishedDate: String? = null

    @Json(name = "multimedia")
    var multimedia: List<ArticleSearchMultimediaResponseItem>? = null
}
