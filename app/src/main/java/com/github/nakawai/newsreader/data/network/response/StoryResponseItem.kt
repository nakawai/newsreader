package com.github.nakawai.newsreader.data.network.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.text.SimpleDateFormat
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class StoryResponseItem {

    @JsonProperty("section")
    var section: String? = null

    @JsonProperty("subsection")
    var subsection: String? = null

    @JsonProperty("title")
    var title: String? = null

    @JsonProperty("abstract")
    var storyAbstract: String? = null

    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("byline")
    var byline: String? = null

    @JsonProperty("item_type")
    var itemType: String? = null

    /**
     * @see PATTERN
     */
    @JsonProperty("updated_date")
    var updatedDate: String? = null

    /**
     * @see PATTERN
     */
    @JsonProperty("created_date")
    var createdDate: String? = null

    /**
     * @see PATTERN
     */
    @JsonProperty("published_date")
    var publishedDate: String? = null

    @JsonProperty("material_type_facet")
    var materialTypeFacet: String? = null

    @JsonProperty("kicker")
    var kicker: String? = null

    @JsonProperty("multimedia")
    var multimedia: List<MultimediaResponseItem>? = null

    companion object {
        private const val PATTERN = "yyyy-MM-d'T'HH:mm:ssZZZZZ"
        val DATE_FORMAT = SimpleDateFormat(PATTERN, Locale.US)
    }

}
