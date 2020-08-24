package com.github.nakawai.newsreader.model.entity

import java.util.*

data class Article(

    val title: String,

    val storyAbstract: String,

    val url: String,

    val multimedia: List<Multimedia>,

    val publishedDate: Date?,

    val isRead: Boolean,

    var section: Section
)
