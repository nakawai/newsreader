package com.github.nakawai.newsreader.model.network.response

import com.fasterxml.jackson.annotation.JsonProperty

class NYTimesMultimediaResponseItem {
    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("format")
    var format: String? = null

    @JsonProperty("height")
    var height = 0

    @JsonProperty("width")
    var width = 0

    @JsonProperty("type")
    var type: String? = null

    @JsonProperty("subtype")
    var subtype: String? = null

    @JsonProperty("caption")
    var caption: String? = null

    @JsonProperty("copyright")
    var copyright: String? = null

}
