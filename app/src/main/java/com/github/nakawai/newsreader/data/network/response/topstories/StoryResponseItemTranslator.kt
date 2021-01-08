package com.github.nakawai.newsreader.data.network.response.topstories

import com.github.nakawai.newsreader.data.DataTranslator
import com.github.nakawai.newsreader.data.db.realm.MultimediaRealmObject
import com.github.nakawai.newsreader.data.db.realm.StoryRealmObject
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Multimedia
import com.github.nakawai.newsreader.domain.entities.Section
import io.realm.RealmList

object StoryResponseItemTranslator {

    fun translate(responseItem: StoryResponseItem): Article {
        return Article(
            title = responseItem.title.orEmpty(),
            storyAbstract = responseItem.storyAbstract.orEmpty(),
            url = ArticleUrl(responseItem.url.orEmpty()),
            multimediaUrlList = responseItem.multimedia?.map { Multimedia(it.url.orEmpty()) } ?: emptyList(),
            publishedDate = responseItem.publishedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) },
            updatedDate = responseItem.updatedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) },
            // FIXME remove property
            isRead = false,
            section = Section.values().find { DataTranslator.translate(it).value == responseItem.section }
                ?: throw IllegalArgumentException("invalid apiSection:${responseItem.section}"),
        )
    }

    fun translateToLocal(response: StoryResponseItem): StoryRealmObject {
        val realmObject = StoryRealmObject()
        realmObject.section = response.section
        realmObject.subsection = response.subsection
        realmObject.title = response.title
        realmObject.storyAbstract = response.storyAbstract
        realmObject.url = response.url
        realmObject.byline = response.byline
        realmObject.itemType = response.itemType
        realmObject.updatedDate = response.updatedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) }
        realmObject.createdDate = response.createdDate
        realmObject.publishedDate = response.publishedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) }
        realmObject.sortTimeStamp = realmObject.publishedDate?.time ?: 0L

        realmObject.materialTypeFacet = response.materialTypeFacet
        realmObject.kicker = response.kicker

        realmObject.multimedia = RealmList<MultimediaRealmObject>()
        response.multimedia?.forEach { multimedia ->
            realmObject.multimedia?.add(translate(multimedia))
        }

        return realmObject
    }

    private fun translate(responseItem: MultimediaResponseItem): MultimediaRealmObject {
        val multimedia = MultimediaRealmObject()

        multimedia.url = responseItem.url
        multimedia.format = responseItem.format
        multimedia.height = responseItem.height
        multimedia.width = responseItem.width
        multimedia.type = responseItem.type
        multimedia.subtype = responseItem.subtype
        multimedia.caption = responseItem.caption
        multimedia.copyright = responseItem.copyright

        return multimedia
    }
}

fun StoryResponseItem.translate(): Article = StoryResponseItemTranslator.translate(this)
