package com.github.nakawai.newsreader.data.db.realm

import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Multimedia
import com.github.nakawai.newsreader.domain.entities.Section
import io.realm.RealmList

fun Article.translate(): StoryRealmObject {

    return StoryRealmObject().also { realmObject ->
        realmObject.section = this.section.value
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


fun StoryRealmObject.translate(): Article {
    return Article(
        title = this.title.orEmpty(),
        storyAbstract = this.storyAbstract.orEmpty(),
        url = ArticleUrl(this.url.orEmpty()),
        multimediaUrlList = this.multimedia?.map { Multimedia(it.url.orEmpty()) } ?: emptyList(),
        publishedDate = this.publishedDate,
        isRead = this.isRead,
        section = Section.fromRawValue(this.apiSection),
        updatedDate = this.updatedDate
    )
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
