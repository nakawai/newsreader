package com.github.nakawai.newsreader.data.network.response.articlesearch

import com.github.nakawai.newsreader.data.network.response.topstories.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Multimedia
import com.github.nakawai.newsreader.domain.entities.Section

fun ArticleSearchDocsResponseItem.translate(): Article = Article(
    title = this.title.orEmpty(),
    storyAbstract = this.articleAbstract.orEmpty(),
    url = ArticleUrl(this.webUrl.orEmpty()),
    multimediaUrlList = this.multimedia?.map { Multimedia(it.url.orEmpty()) } ?: emptyList(),
    publishedDate = this.publishedDate?.let { StoryResponseItem.DATE_FORMAT.parse(it) },
    isRead = false,
    section = Section.fromRawValue(this.sectionName),
    // TODO
    updatedDate = null
)
