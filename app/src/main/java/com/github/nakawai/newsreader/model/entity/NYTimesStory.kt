/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nakawai.newsreader.model.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.nakawai.newsreader.model.network.RealmListNYTimesMultimediumDeserializer
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

@JsonIgnoreProperties(ignoreUnknown = true)
open class NYTimesStory : RealmObject() {
    var apiSection: String? = null

    @JsonProperty("section")
    var section: String? = null

    @JsonProperty("subsection")
    var subsection: String? = null

    @JsonProperty("title")
    var title: String? = null

    @JsonProperty("abstract")
    var storyAbstract: String? = null

    @PrimaryKey
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

    @set:JsonDeserialize(using = RealmListNYTimesMultimediumDeserializer::class)
    @JsonProperty("multimedia")
    var multimedia: RealmList<NYTimesMultimedium>? = null
    var sortTimeStamp: Long = 0
    var isRead = false

    companion object {
        const val PUBLISHED_DATE = "publishedDate"
        const val URL = "url"
        const val API_SECTION = "apiSection"
    }
}