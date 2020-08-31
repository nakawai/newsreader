package com.github.nakawai.newsreader.data.network

import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.domain.story.Section

interface NYTimesRemoteDataSource {
    suspend fun fetchData(section: Section): List<StoryResponseItem>
}
