package com.github.nakawai.newsreader.presentation.articles

import android.text.format.DateUtils
import com.github.nakawai.newsreader.domain.entities.Story

class ArticleUiModel(
    val relativeDate: String
) {
    constructor(story: Story, nowTimeMillis: Long) : this(
        relativeDate = DateUtils.getRelativeTimeSpanString(story.publishedDate!!.time, nowTimeMillis, DateUtils.MINUTE_IN_MILLIS).toString()
    )
}
