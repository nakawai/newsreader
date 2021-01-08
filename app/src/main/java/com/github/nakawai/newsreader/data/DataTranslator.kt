package com.github.nakawai.newsreader.data

import com.github.nakawai.newsreader.data.db.realm.StoryRealmObject
import com.github.nakawai.newsreader.data.network.response.articlesearch.ArticleSearchDocsResponseItem
import com.github.nakawai.newsreader.data.network.response.topstories.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.MultimediaUrl
import com.github.nakawai.newsreader.domain.entities.Section

class DataTranslator {


    companion object {


        fun translate(realmObject: StoryRealmObject): Article {
            return Article(
                title = realmObject.title.orEmpty(),
                url = ArticleUrl(realmObject.url.orEmpty()),
                storyAbstract = realmObject.storyAbstract.orEmpty(),
                multimediaUrl = realmObject.multimedia?.map { MultimediaUrl(it.url.orEmpty()) } ?: emptyList(),
                publishedDate = realmObject.publishedDate,
                isRead = realmObject.isRead,
                section = Section.values().find { translate(it).value == realmObject.apiSection }
                    ?: throw IllegalArgumentException("invalid apiSection:${realmObject.apiSection}")
            )
        }

        fun translate(responseItem: ArticleSearchDocsResponseItem): Article {
            return Article(
                title = responseItem.title.orEmpty(),
                url = ArticleUrl(responseItem.webUrl.orEmpty()),
                storyAbstract = responseItem.articleAbstract.orEmpty(),
                multimediaUrl = responseItem.multimedia?.map { MultimediaUrl(it.url.orEmpty()) } ?: emptyList(),
                publishedDate = responseItem.publishedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) },
                isRead = false,
                section = Section.values().find { translate(it).value == responseItem.sectionName }
                    ?: Section.HOME
            )
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

fun StoryRealmObject.translate(): Article {
    return DataTranslator.translate(this)
}

fun Section.toData(): SectionData {
    return DataTranslator.translate(this)
}
