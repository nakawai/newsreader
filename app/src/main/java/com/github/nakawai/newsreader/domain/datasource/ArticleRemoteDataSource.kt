package com.github.nakawai.newsreader.domain.datasource

import com.github.nakawai.newsreader.data.network.response.topstories.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.Section

interface ArticleRemoteDataSource {
    suspend fun fetchTopStories(section: Section): List<Article>
    suspend fun searchArticle(query: String): List<Article>
    suspend fun fetchTopStoriesWithCall(section: Section): List<StoryResponseItem>
}
