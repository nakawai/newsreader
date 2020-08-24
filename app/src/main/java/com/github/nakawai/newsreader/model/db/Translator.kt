package com.github.nakawai.newsreader.model.db

import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Multimedia
import com.github.nakawai.newsreader.model.entity.Section
import com.github.nakawai.newsreader.model.network.response.NYTimesMultimediaResponseItem
import com.github.nakawai.newsreader.model.network.response.NYTimesStoryResponseItem
import io.realm.RealmList
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class Translator {


    companion object {

        private val inputDateFormat = SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZZZZZ", Locale.US)

        fun translate(story: StoryRealmObject): Article {
            val mediaArray = mutableListOf<Multimedia>()
            story.multimedia?.forEach {
                mediaArray.add(Multimedia(it.url.orEmpty()))
            }
            return Article(
                title = story.title.orEmpty(),
                url = story.url.orEmpty(),
                storyAbstract = story.storyAbstract.orEmpty(),
                multimedia = mediaArray,
                publishedDate = story.publishedDate,
                isRead = story.isRead,
                section = Section.valueOfApiSection(story.apiSection.orEmpty())
            )
        }

        fun translate(item: NYTimesStoryResponseItem): StoryRealmObject {
            val story = StoryRealmObject()
            story.section = item.section
            story.subsection = item.subsection
            story.title = item.title
            story.storyAbstract = item.storyAbstract
            story.url = item.url
            story.byline = item.byline
            story.itemType = item.itemType
            story.updatedDate = item.updatedDate
            story.createdDate = item.createdDate

            val parsedPublishedDate = item.publishedDate?.let { inputDateFormat.parse(it, ParsePosition(0)) }
            story.publishedDate = item.publishedDate?.let { inputDateFormat.parse(it, ParsePosition(0)) }
            story.sortTimeStamp = parsedPublishedDate?.time ?: 0L

            story.materialTypeFacet = item.materialTypeFacet
            story.kicker = item.kicker
            
            story.multimedia = RealmList<MultimediaRealmObject>()
            item.multimedia?.forEach { multimedia ->
                story.multimedia?.add(translate(multimedia))
            }

            return story
        }

        private fun translate(responseItem: NYTimesMultimediaResponseItem): MultimediaRealmObject {
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
}

fun StoryRealmObject.translate(): Article {
    return Translator.translate(this)
}
