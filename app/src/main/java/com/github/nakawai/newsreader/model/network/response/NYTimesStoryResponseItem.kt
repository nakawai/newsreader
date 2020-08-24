package com.github.nakawai.newsreader.model.network.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class NYTimesStoryResponseItem {

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

    @JsonProperty("updated_date")
    var updatedDate: String? = null

    @JsonProperty("created_date")
    var createdDate: String? = null

    @JsonProperty("published_date")
    var publishedDate: String? = null

    @JsonProperty("material_type_facet")
    var materialTypeFacet: String? = null

    @JsonProperty("kicker")
    var kicker: String? = null

    @JsonProperty("multimedia")
    var multimedia: List<NYTimesMultimediaResponseItem>? = null

}
