package com.github.nakawai.newsreader.domain.model

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.datasource.AppLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.NYTimesLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.NYTimesRemoteDataSource
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl

/**
 * Model implementation class of MVVM
 */
class NYTimesModelImpl(private val local: NYTimesLocalDataSource, private val remote: NYTimesRemoteDataSource, private val appLocal: AppLocalDataSource) : NYTimesModel {


    /**
     * Loads the news feed as well as all future updates.
     */
    override suspend fun loadNewsFeed(section: Section, forceReload: Boolean): List<Story> {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || appLocal.canCallApi(section)) {
            val response = remote.fetchTopStories(section)

            local.saveData(section, response)
            appLocal.saveLastNetworkRequestTime(section)
        }

        return local.readData(section)
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

    override suspend fun searchArticle(query: String): List<Story> {
        return remote.searchArticle(query)
    }


}
