package com.github.nakawai.newsreader.data.network.response.topstories

import com.squareup.moshi.Json


class MultimediaResponseItem {
    @Json(name = "url")
    var url: String? =
        null // https://static01.nyt.com/images/2020/10/03/world/03reunification-farright4/merlin_172580730_47b5e877-068c-40fc-b5a5-d489ab11c7d8-superJumbo.jpg

    @Json(name = "format")
    var format: String? = null // superJumbo

    @Json(name = "height")
    var height = 0 // 1365

    @Json(name = "width")
    var width = 0 // 2048

    @Json(name = "type")
    var type: String? = null // image

    @Json(name = "subtype")
    var subtype: String? = null // photo

    @Json(name = "caption")
    var caption: String? =
        null // A demonstration in Berlin in May, organized by members of the AfD, to protest the government’s coronavirus policies.

    @Json(name = "copyright")
    var copyright: String? = null // Emile Ducke for The New York Times

}
