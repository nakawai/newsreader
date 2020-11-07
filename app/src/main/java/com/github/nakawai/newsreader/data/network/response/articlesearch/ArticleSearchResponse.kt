package com.github.nakawai.newsreader.data.network.response.articlesearch

import com.squareup.moshi.Json

class ArticleSearchResponse {
    @Json(name = "status")
    var status: String? = null

    @Json(name = "copyright")
    var copyright: String? = null

    @Json(name = "response")
    var response: ArticleSearchResponseBody? = null
}
