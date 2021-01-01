package com.github.nakawai.newsreader.presentation.search.list

import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl

/**
 * Ui Model class for Article
 */
data class SearchResultUiModel(
    val title: String,
    val storyAbstract: String,
    val url: ArticleUrl
) {

    constructor(article: Article) : this(
        title = article.title,
        storyAbstract = article.storyAbstract,
        url = article.url

    )
}
