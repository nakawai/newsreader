package com.github.nakawai.newsreader.data.network.response.topstories

import com.squareup.moshi.Json

class TopStoriesResponse {
    @Json(name = "status")
    var status: String? = null

    @Json(name = "copyright")
    var copyright: String? = null

    @Json(name = "section")
    var section: String? = null

    @Json(name = "last_updated")
    var lastUpdated: String? = null

    @Json(name = "num_results")
    var numResults: Int? = null

    @Json(name = "results")
    var results: List<StoryResponseItem>? = null
}
