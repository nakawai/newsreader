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
