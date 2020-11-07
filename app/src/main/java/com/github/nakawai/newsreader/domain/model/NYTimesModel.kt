package com.github.nakawai.newsreader.domain.model

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl

interface NYTimesModel {
    suspend fun loadNewsFeed(section: Section, forceReload: Boolean): List<Story>
    fun updateStoryReadState(storyUrl: StoryUrl, read: Boolean)
    fun observeArticlesBySection(section: Section): LiveData<List<Story>>
    fun observeArticle(storyUrl: StoryUrl): LiveData<Story>
    fun loadSections(): List<Section>
    suspend fun searchArticle(query: String): List<Story>
}
