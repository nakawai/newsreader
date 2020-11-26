package com.github.nakawai.newsreader.data.network.response.articlesearch

import com.squareup.moshi.Json

class ArticleSearchResponseBody {
    @Json(name = "docs")
    var docs: List<ArticleSearchDocsResponseItem>? = null
}
