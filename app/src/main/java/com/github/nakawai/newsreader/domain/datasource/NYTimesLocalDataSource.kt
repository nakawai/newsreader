package com.github.nakawai.newsreader.domain.datasource

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl

interface NYTimesLocalDataSource {
    suspend fun saveData(section: Section, responseItems: List<StoryResponseItem>)
    suspend fun readData(section: Section): List<Story>
    fun observeArticles(section: Section): LiveData<List<Story>>
    fun observeStory(storyUrl: StoryUrl): LiveData<Story>
    fun updateStoryReadState(storyUrl: StoryUrl, read: Boolean)
}
