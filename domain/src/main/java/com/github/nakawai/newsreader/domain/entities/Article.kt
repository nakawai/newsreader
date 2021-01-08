package com.github.nakawai.newsreader.domain.entities

import java.util.*

data class Article(

    val title: String,

    val storyAbstract: String,

    val url: ArticleUrl,

    val multimediaUrlList: List<Multimedia>,

    val publishedDate: Date?,

    @Deprecated("")
    val isRead: Boolean,

    val section: Section,
    val updatedDate: Date?
)
