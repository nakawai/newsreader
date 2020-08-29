package com.github.nakawai.newsreader.data

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.db.NYTimesLocalDataSource
import com.github.nakawai.newsreader.data.network.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.domain.story.Section
import com.github.nakawai.newsreader.domain.story.Story
import com.github.nakawai.newsreader.domain.story.StoryUrl
import java.util.*
import java.util.concurrent.TimeUnit

class Repository(private val local: NYTimesLocalDataSource, private val remote: NYTimesRemoteDataSource) {
    private val lastNetworkRequestTimeMillis: MutableMap<Section, Long> = EnumMap(
        Section::class.java
    )

    /**
     * Loads the news feed as well as all future updates.
     */
    suspend fun loadNewsFeed(section: Section, forceReload: Boolean): List<Story> {
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
    fun updateStoryReadState(storyUrl: StoryUrl, read: Boolean) {
        local.updateStoryReadState(storyUrl, read)
    }

    fun observeArticlesBySection(section: Section): LiveData<List<Story>> {
        return local.observeArticles(section)
    }

    fun observeArticle(storyUrl: StoryUrl): LiveData<Story> {
        return local.observeStory(storyUrl)
    }

    companion object {
        private const val MINIMUM_NETWORK_WAIT_SEC: Long = 120 // Minimum 2 minutes between each network request
    }

}
