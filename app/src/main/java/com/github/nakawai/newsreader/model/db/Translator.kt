package com.github.nakawai.newsreader.model.db

import com.github.nakawai.newsreader.model.entity.*
import com.github.nakawai.newsreader.model.network.NYTimesMultimediumResponseItem
import com.github.nakawai.newsreader.model.network.NYTimesStoryResponseItem
import io.realm.RealmList
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class Translator {


    companion object {

        private val inputDateFormat = SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZZZZZ", Locale.US)

        fun translate(story: NYTimesStory): Article {
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

        fun translate(item: NYTimesStoryResponseItem): NYTimesStory {
            val story = NYTimesStory()
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


            story.multimedia = RealmList<NYTimesMultimedium>()
            item.multimedia?.forEach { multimedia ->
                story.multimedia?.add(translate(multimedia))
            }

            return story
        }

        private fun translate(responseItem: NYTimesMultimediumResponseItem): NYTimesMultimedium {
            val multimedia = NYTimesMultimedium()
            multimedia.url = responseItem.url

            return multimedia
        }
    }
}

fun NYTimesStory.translate(): Article {
    return Translator.translate(this)
}
