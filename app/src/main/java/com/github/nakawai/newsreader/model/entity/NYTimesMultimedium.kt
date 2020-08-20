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

import com.fasterxml.jackson.annotation.JsonProperty
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NYTimesMultimedium : RealmObject() {
    @PrimaryKey
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