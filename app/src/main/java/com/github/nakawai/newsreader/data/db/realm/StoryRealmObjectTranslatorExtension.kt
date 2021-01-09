package com.github.nakawai.newsreader.data.db.realm

import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.Multimedia
import io.realm.RealmList

fun Article.translate(): StoryRealmObject {

    return StoryRealmObject().also { realmObject ->
        realmObject.section = this.section.toData().value
        //realmObject.subsection = response.subsection
        realmObject.title = this.title
        realmObject.storyAbstract = this.storyAbstract
        realmObject.url = this.url.value
        //realmObject.byline = response.byline
        //realmObject.itemType = response.itemType
        realmObject.updatedDate = this.updatedDate
        //realmObject.createdDate = response.createdDate
        realmObject.publishedDate = this.publishedDate
        realmObject.sortTimeStamp = realmObject.publishedDate?.time ?: 0L

        //realmObject.materialTypeFacet = response.materialTypeFacet
        //realmObject.kicker = response.kicker

        realmObject.multimedia = RealmList()
        this.multimediaUrlList.forEach { multimedia ->
            realmObject.multimedia?.add(translate(multimedia))
        }
    }
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
