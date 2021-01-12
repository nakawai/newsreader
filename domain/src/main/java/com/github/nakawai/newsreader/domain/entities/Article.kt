package com.github.nakawai.newsreader.domain.entities

import java.util.*

data class Article(

    val title: String,

    val storyAbstract: String,

    val url: ArticleUrl,

    val multimediaUrlList: List<Multimedia>,

    val publishedDate: Date?,

    val section: Section,
    val updatedDate: Date?
)
