package com.github.nakawai.newsreader.model.entity

data class Article(

    val title: String,

    val storyAbstract: String,

    val url: String,

    val multimedia: List<Multimedia>,

    val publishedDate: String?,

    val isRead: Boolean,

    var section: Section
)
