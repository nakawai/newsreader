package com.github.nakawai.newsreader.data.network.response.topstories

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

class StoryResponseItem {
    @Json(name = "section")
    var section: String? = null // world

    @Json(name = "subsection")
    var subsection: String? = null // europe

    @Json(name = "title")
    var title: String? = null // Germany’s Far Right Reunified, Too, Making It Much Stronger

    @Json(name = "abstract")
    var storyAbstract: String? =
        null //Thirty years after Germany came back together, the former East has become the stronghold of a once-marginalized movement that now sits in Parliament.

    @Json(name = "url")
    var url: String? = null // https://www.nytimes.com/2020/10/03/world/europe/germany-reunification-far-right.html

    @Json(name = "byline")
    var byline: String? = null // By Katrin Bennhold

    @Json(name = "item_type")
    var itemType: String? = null // Article

    /**
     * @see PATTERN
     */
    @Json(name = "updated_date")
    var updatedDate: String? = null // 2020-10-03T17:09:29-04:00

    /**
     * @see PATTERN
     */
    @Json(name = "created_date")
    var createdDate: String? = null // 2020-10-03T00:10:10-04:00

    /**
     * @see PATTERN
     */
    @Json(name = "published_date")
    var publishedDate: String? = null // 2020-10-03T00:10:10-04:00

    @Json(name = "material_type_facet")
    var materialTypeFacet: String? = null // ""

    @Json(name = "kicker")
    var kicker: String? = null // ""

    @Json(name = "multimedia")
    var multimedia: List<MultimediaResponseItem>? = null

    companion object {
        private const val PATTERN = "yyyy-MM-d'T'HH:mm:ssZZZZZ"
        val DATE_FORMAT = SimpleDateFormat(PATTERN, Locale.US)
    }

}
