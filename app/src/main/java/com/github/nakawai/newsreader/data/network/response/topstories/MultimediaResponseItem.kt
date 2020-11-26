package com.github.nakawai.newsreader.data.network.response.topstories

import com.squareup.moshi.Json


class MultimediaResponseItem {
    @Json(name = "url")
    var url: String? = null

    @Json(name = "format")
    var format: String? = null

    @Json(name = "height")
    var height = 0

    @Json(name = "width")
    var width = 0

    @Json(name = "type")
    var type: String? = null

    @Json(name = "subtype")
    var subtype: String? = null

    @Json(name = "caption")
    var caption: String? = null

    @Json(name = "copyright")
    var copyright: String? = null

}
