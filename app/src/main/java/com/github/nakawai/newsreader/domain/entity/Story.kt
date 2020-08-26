package com.github.nakawai.newsreader.domain.entity

import java.util.*

data class Story(

    val title: String,

    val storyAbstract: String,

    val url: StoryUrl,

    val multimedia: List<Multimedia>,

    val publishedDate: Date?,

    val isRead: Boolean,

    val section: Section
)
