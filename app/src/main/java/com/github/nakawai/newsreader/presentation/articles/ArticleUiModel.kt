package com.github.nakawai.newsreader.presentation.articles

import android.text.format.DateUtils
import com.github.nakawai.newsreader.domain.entities.Multimedia
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl

data class ArticleUiModel(
    val title: String,
    val storyAbstract: String,
    val url: StoryUrl,
    val multimedia: List<Multimedia>,
    val isRead: Boolean,
    val section: Section,
    val relativeTimeSpanText: String
) {
    constructor(story: Story, nowTimeMillis: Long) : this(
        title = story.title,
        storyAbstract = story.storyAbstract,
        url = story.url,
        multimedia = story.multimedia,
        isRead = story.isRead,
        section = story.section,
        relativeTimeSpanText = DateUtils.getRelativeTimeSpanString(
            story.publishedDate!!.time, nowTimeMillis, DateUtils.MINUTE_IN_MILLIS
        ).toString()
    )
}
