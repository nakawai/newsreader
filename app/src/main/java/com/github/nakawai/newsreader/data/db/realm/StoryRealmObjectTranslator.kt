package com.github.nakawai.newsreader.data.db.realm

import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.Multimedia
import io.realm.RealmList

object StoryRealmObjectTranslator {

    fun translateToLocal(response: Article): StoryRealmObject {
        val realmObject = StoryRealmObject()
        realmObject.section = response.section.toData().value
        //realmObject.subsection = response.subsection
        realmObject.title = response.title
        realmObject.storyAbstract = response.storyAbstract
        realmObject.url = response.url.value
        //realmObject.byline = response.byline
        //realmObject.itemType = response.itemType
        realmObject.updatedDate = response.updatedDate
        //realmObject.createdDate = response.createdDate
        realmObject.publishedDate = response.publishedDate
        realmObject.sortTimeStamp = realmObject.publishedDate?.time ?: 0L

        //realmObject.materialTypeFacet = response.materialTypeFacet
        //realmObject.kicker = response.kicker

        realmObject.multimedia = RealmList<MultimediaRealmObject>()
        response.multimediaUrlList.forEach { multimedia ->
            realmObject.multimedia?.add(translate(multimedia))
        }

        return realmObject
    }

    private fun translate(responseItem: Multimedia): MultimediaRealmObject {
        val multimedia = MultimediaRealmObject()

        multimedia.url = responseItem.url
//        multimedia.format = responseItem.format
//        multimedia.height = responseItem.height
//        multimedia.width = responseItem.width
//        multimedia.type = responseItem.type
//        multimedia.subtype = responseItem.subtype
//        multimedia.caption = responseItem.caption
//        multimedia.copyright = responseItem.copyright

        return multimedia
    }
}

fun Article.translate(): StoryRealmObject = StoryRealmObjectTranslator.translateToLocal(this)
