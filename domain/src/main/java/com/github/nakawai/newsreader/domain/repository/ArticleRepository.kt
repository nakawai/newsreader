package com.github.nakawai.newsreader.domain.repository

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Section

interface ArticleRepository {
    suspend fun loadTopStoriesBySection(section: Section, forceReload: Boolean = false): List<Article>

    @Deprecated("deprecated", ReplaceWith("HistoryRepository#addHistory"))
    fun updateStoryReadState(articleUrl: ArticleUrl, read: Boolean)
    fun observeArticlesBySection(section: Section): LiveData<List<Article>>
    fun observeArticle(articleUrl: ArticleUrl): LiveData<Article>
    fun loadSections(): List<Section>
    suspend fun searchArticle(query: String): List<Article>
    suspend fun loadTopStoriesBySectionIfNeed(section: Section, forceReload: Boolean)
}
