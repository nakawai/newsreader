package com.github.nakawai.newsreader.model.network

import com.fasterxml.jackson.annotation.JsonProperty

class NYTimesResponse<T> {
    @JsonProperty("status")
    var status: String? = null

    @JsonProperty("copyright")
    var copyright: String? = null

    @JsonProperty("section")
    var section: String? = null

    @JsonProperty("last_updated")
    var lastUpdated: String? = null

    @JsonProperty("num_results")
    var numResults: Int? = null

    @JsonProperty("results")
    var results: T? = null
}
