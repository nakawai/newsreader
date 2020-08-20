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
package com.github.nakawai.newsreader.model.network

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.github.nakawai.newsreader.model.entity.NYTimesMultimedium
import io.realm.RealmList
import java.io.IOException

class RealmListNYTimesMultimediumDeserializer :
    JsonDeserializer<List<NYTimesMultimedium>>() {
    private val objectMapper: ObjectMapper

    @Throws(IOException::class)
    override fun deserialize(
        parser: JsonParser,
        context: DeserializationContext
    ): List<NYTimesMultimedium> {
        val list = RealmList<NYTimesMultimedium>()
        val treeNode =
            parser.codec.readTree<TreeNode>(parser) as? ArrayNode ?: return list
        for (node in treeNode) {
            val nyTimesMultimedium =
                objectMapper.treeToValue(node, NYTimesMultimedium::class.java)
            list.add(nyTimesMultimedium)
        }
        return list
    }

    init {
        objectMapper = ObjectMapper()
    }
}