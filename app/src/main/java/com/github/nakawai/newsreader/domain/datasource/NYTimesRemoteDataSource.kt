package com.github.nakawai.newsreader.domain.datasource

import com.github.nakawai.newsreader.data.network.response.topstories.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story

interface NYTimesRemoteDataSource {
    suspend fun fetchTopStories(section: Section): List<StoryResponseItem>
    suspend fun searchArticle(query: String): List<Story>
    suspend fun fetchTopStoriesWithCall(section: Section): List<StoryResponseItem>
}
