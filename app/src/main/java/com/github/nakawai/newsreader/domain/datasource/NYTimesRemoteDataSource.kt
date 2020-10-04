package com.github.nakawai.newsreader.domain.datasource

import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Section

interface NYTimesRemoteDataSource {
    suspend fun fetchTopStories(section: Section): List<StoryResponseItem>
}
