package com.github.nakawai.newsreader.data.network.response.topstories

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

class StoryResponseItem {

    @Json(name = "section")
    var section: String? = null

    @Json(name = "subsection")
    var subsection: String? = null

    @Json(name = "title")
    var title: String? = null

    @Json(name = "abstract")
    var storyAbstract: String? = null

    @Json(name = "url")
    var url: String? = null

    @Json(name = "byline")
    var byline: String? = null

    @Json(name = "item_type")
    var itemType: String? = null

    /**
     * @see PATTERN
     */
    @Json(name = "updated_date")
    var updatedDate: String? = null

    /**
     * @see PATTERN
     */
    @Json(name = "created_date")
    var createdDate: String? = null

    /**
     * @see PATTERN
     */
    @Json(name = "published_date")
    var publishedDate: String? = null

    @Json(name = "material_type_facet")
    var materialTypeFacet: String? = null

    @Json(name = "kicker")
    var kicker: String? = null

    @Json(name = "multimedia")
    var multimedia: List<MultimediaResponseItem>? = null

    companion object {
        private const val PATTERN = "yyyy-MM-d'T'HH:mm:ssZZZZZ"
        val DATE_FORMAT = SimpleDateFormat(PATTERN, Locale.US)
    }

}
