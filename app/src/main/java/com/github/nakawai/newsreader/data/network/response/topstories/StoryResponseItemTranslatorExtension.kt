package com.github.nakawai.newsreader.data.network.response.topstories

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
    section = Section.fromRawValue(this.section)
)
