package com.github.nakawai.newsreader.domain.datasource

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.network.response.topstories.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Section

interface ArticleLocalDataSource {
    suspend fun saveTopStoriesBySection(section: Section, responseItems: List<StoryResponseItem>)
    suspend fun readTopStoriesBySection(section: Section): List<Article>
    fun observeArticlesBySection(section: Section): LiveData<List<Article>>
    fun observeArticle(articleUrl: ArticleUrl): LiveData<Article>
    fun updateArticleReadState(articleUrl: ArticleUrl, read: Boolean)
}
