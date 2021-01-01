package com.github.nakawai.newsreader.domain.entities

import java.util.*

data class Article(

    val title: String,

    val storyAbstract: String,

    val url: ArticleUrl,

    val multimediaUrl: List<MultimediaUrl>,

    val publishedDate: Date?,

    val isRead: Boolean,

    val section: Section
)
