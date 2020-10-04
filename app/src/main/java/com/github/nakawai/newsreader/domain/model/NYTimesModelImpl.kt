package com.github.nakawai.newsreader.domain.model

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.datasource.NYTimesLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl
import java.util.*
import java.util.concurrent.TimeUnit

class NYTimesModelImpl(private val local: NYTimesLocalDataSource, private val remote: NYTimesRemoteDataSource) : NYTimesModel {
    private val lastNetworkRequestTimeMillis: MutableMap<Section, Long> = EnumMap(
        Section::class.java
    )

    /**
     * Loads the news feed as well as all future updates.
     */
    override suspend fun loadNewsFeed(section: Section, forceReload: Boolean): List<Story> {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || timeSinceLastNetworkRequest(section) > MINIMUM_NETWORK_WAIT_SEC) {
            val response = remote.fetchData(section)

            local.saveData(section, response)
            lastNetworkRequestTimeMillis[section] = System.currentTimeMillis()
        }

        return local.readData(section)
    }

    private fun timeSinceLastNetworkRequest(section: Section): Long {
        val lastRequestTimeMillis = lastNetworkRequestTimeMillis[section]
        return if (lastRequestTimeMillis != null) {
            TimeUnit.SECONDS.convert(System.currentTimeMillis() - lastRequestTimeMillis, TimeUnit.MILLISECONDS)
        } else {
            Long.MAX_VALUE
        }
    }

    /**
     * Updates a story.
     *
     * @param storyUrl story to update
     * @param read `true` if the story has been read, `false` otherwise.
     */
    override fun updateStoryReadState(storyUrl: StoryUrl, read: Boolean) {
        local.updateStoryReadState(storyUrl, read)
    }

    override fun observeArticlesBySection(section: Section): LiveData<List<Story>> {
        return local.observeArticles(section)
    }

    override fun observeArticle(storyUrl: StoryUrl): LiveData<Story> {
        return local.observeStory(storyUrl)
    }

    override fun loadSections(): List<Section> {
        return Section.values().toList()
    }

    companion object {
        private const val MINIMUM_NETWORK_WAIT_SEC: Long = 120 // Minimum 2 minutes between each network request
    }

}