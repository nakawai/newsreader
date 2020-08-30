package com.github.nakawai.newsreader.data

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.story.Section
import com.github.nakawai.newsreader.domain.story.Story
import com.github.nakawai.newsreader.domain.story.StoryUrl

interface Repository {
    suspend fun loadNewsFeed(section: Section, forceReload: Boolean): List<Story>
    fun updateStoryReadState(storyUrl: StoryUrl, read: Boolean)
    fun observeArticlesBySection(section: Section): LiveData<List<Story>>
    fun observeArticle(storyUrl: StoryUrl): LiveData<Story>
    fun loadSections(): List<Section>
}
