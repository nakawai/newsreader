package com.github.nakawai.newsreader.domain

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.Repository
import com.github.nakawai.newsreader.domain.entity.Section
import com.github.nakawai.newsreader.domain.entity.Story
import com.github.nakawai.newsreader.domain.entity.StoryUrl

/**
 * Application Service
 */
class NewsReaderAppService(private val repository: Repository) {
    
    fun observeArticle(storyUrl: StoryUrl): LiveData<Story> {
        return repository.observeArticle(storyUrl)
    }

    fun observeArticlesBySection(section: Section): LiveData<List<Story>> {
        return repository.observeArticlesBySection(section)
    }

    /**
     * Returns the news feed for the currently selected category.
     */
    suspend fun loadNewsFeed(section: Section, force: Boolean): List<Story> {
        return repository.loadNewsFeed(section, force)
    }

    /**
     * Marks a story as being read.
     */
    fun markAsRead(storyUrl: StoryUrl) {
        repository.updateStoryReadState(storyUrl, read = true)
    }

}
