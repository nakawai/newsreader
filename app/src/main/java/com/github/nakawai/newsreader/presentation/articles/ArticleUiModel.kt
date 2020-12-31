package com.github.nakawai.newsreader.presentation.articles

import android.text.format.DateUtils
import com.github.nakawai.newsreader.domain.entities.MultimediaUrl
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl

/**
 * Ui Model class for Article
 */
data class ArticleUiModel(
    val title: String,
    val storyAbstract: String,
    val url: StoryUrl,
    val multimediaUrl: List<MultimediaUrl>,
    val isRead: Boolean,
    val section: Section,
    val relativeTimeSpanText: String
) {
    constructor(story: Story, nowTimeMillis: Long) : this(
        title = story.title,
        storyAbstract = story.storyAbstract,
        url = story.url,
        multimediaUrl = story.multimediaUrl,
        isRead = story.isRead,
        section = story.section,
        relativeTimeSpanText = DateUtils.getRelativeTimeSpanString(
            story.publishedDate!!.time, nowTimeMillis, DateUtils.MINUTE_IN_MILLIS
        ).toString()
    )
}
