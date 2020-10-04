package com.github.nakawai.newsreader.data

import com.github.nakawai.newsreader.data.db.MultimediaRealmObject
import com.github.nakawai.newsreader.data.db.StoryRealmObject
import com.github.nakawai.newsreader.data.network.response.MultimediaResponseItem
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Multimedia
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl
import io.realm.RealmList

class Translator {


    companion object {


        fun translate(realmObject: StoryRealmObject): Story {
            return Story(
                title = realmObject.title.orEmpty(),
                url = StoryUrl(realmObject.url.orEmpty()),
                storyAbstract = realmObject.storyAbstract.orEmpty(),
                multimedia = realmObject.multimedia?.map { Multimedia(it.url.orEmpty()) } ?: emptyList(),
                publishedDate = realmObject.publishedDate,
                isRead = realmObject.isRead,
                section = Section.values().find { translate(it).value == realmObject.apiSection }
                    ?: throw IllegalArgumentException("invalid apiSection:${realmObject.apiSection}")
            )
        }

        fun translate(response: StoryResponseItem): StoryRealmObject {
            val realmObject = StoryRealmObject()
            realmObject.section = response.section
            realmObject.subsection = response.subsection
            realmObject.title = response.title
            realmObject.storyAbstract = response.storyAbstract
            realmObject.url = response.url
            realmObject.byline = response.byline
            realmObject.itemType = response.itemType
            realmObject.updatedDate = response.updatedDate
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

        fun translate(section: Section): SectionData {
            return when (section) {
                Section.HOME -> SectionData("home")

                Section.WORLD -> SectionData("world")

                Section.NATIONAL -> SectionData("national")

                Section.POLITICS -> SectionData("politics")

                Section.NEW_YORK_REGION -> SectionData("nyregion")

                Section.BUSINESS -> SectionData("business")

                Section.OPINION -> SectionData("opinion")

                Section.TECHNOLOGY -> SectionData("technology")

                Section.SCIENCE -> SectionData("science")

                Section.HEALTH -> SectionData("health")

                Section.SPORTS -> SectionData("sports")

                Section.ARTS -> SectionData("arts")

                Section.FASHION -> SectionData("fashion")

                Section.DINING -> SectionData("dining")

                Section.TRAVEL -> SectionData("travel")

                Section.MAGAZINE -> SectionData("magazine")

                Section.REAL_ESTATE -> SectionData("realestate")
            }
        }
    }
}

data class SectionData(val value: String)

fun StoryRealmObject.translate(): Story {
    return Translator.translate(this)
}

fun Section.toData(): SectionData {
    return Translator.translate(this)
}
