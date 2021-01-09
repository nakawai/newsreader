package com.github.nakawai.newsreader.data.network.response.topstories

import com.github.nakawai.newsreader.data.SectionData
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Multimedia
import com.github.nakawai.newsreader.domain.entities.Section


fun StoryResponseItem.translate(): Article = Article(
    title = this.title.orEmpty(),
    storyAbstract = this.storyAbstract.orEmpty(),
    url = ArticleUrl(this.url.orEmpty()),
    multimediaUrlList = this.multimedia?.map { Multimedia(it.url.orEmpty()) } ?: emptyList(),
    publishedDate = this.publishedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) },
    updatedDate = this.updatedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) },
    // FIXME remove property
    isRead = false,
    section = Section.values().find {
        when (it) {
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
        }.value == this.section
    }
        ?: throw IllegalArgumentException("invalid apiSection:${this.section}"),
)
