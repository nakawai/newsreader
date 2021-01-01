package com.github.nakawai.newsreader.presentation.articles

import android.text.format.DateUtils
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.MultimediaUrl
import com.github.nakawai.newsreader.domain.entities.Section

/**
 * Ui Model class for Article
 */
data class ArticleUiModel(
    val title: String,
    val storyAbstract: String,
    val url: ArticleUrl,
    val multimediaUrl: List<MultimediaUrl>,
    val isRead: Boolean,
    val section: Section,
    val relativeTimeSpanText: String
) {
    constructor(article: Article, nowTimeMillis: Long) : this(
        title = article.title,
        storyAbstract = article.storyAbstract,
        url = article.url,
        multimediaUrl = article.multimediaUrl,
        isRead = article.isRead,
        section = article.section,
        relativeTimeSpanText = DateUtils.getRelativeTimeSpanString(
            article.publishedDate!!.time, nowTimeMillis, DateUtils.MINUTE_IN_MILLIS
        ).toString()
    )
}
