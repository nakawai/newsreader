package com.github.nakawai.newsreader.domain.repository

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.datasource.ArticleLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.ArticleRemoteDataSource
import com.github.nakawai.newsreader.domain.datasource.ConfigLocalDataSource
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Section

/**
 * Model implementation class of MVVM
 */
class ArticleRepositoryImpl(
    private val local: ArticleLocalDataSource,
    private val remote: ArticleRemoteDataSource,
    private val config: ConfigLocalDataSource
) : ArticleRepository {


    /**
     * Loads the news feed as well as all future updates.
     */
    override suspend fun loadTopStoriesBySection(section: Section, forceReload: Boolean): List<Article> {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || config.canCallApi(section)) {
            val response = remote.fetchTopStories(section)

            local.saveTopStories(response)
            config.saveLastNetworkRequestTime(section)
        }

        return local.readTopStoriesBySection(section)
    }

    /**
     * Loads the news feed as well as all future updates.
     */
    override suspend fun loadTopStoriesBySectionIfNeed(section: Section, forceReload: Boolean) {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || config.canCallApi(section)) {
            val response = remote.fetchTopStories(section)

            local.saveTopStories(response)
            config.saveLastNetworkRequestTime(section)
        }
    }


    /**
     * Updates a story.
     *
     * @param articleUrl story to update
     * @param read `true` if the story has been read, `false` otherwise.
     */
    override fun updateStoryReadState(articleUrl: ArticleUrl, read: Boolean) {
        local.updateArticleReadState(articleUrl, read)
    }

    override fun observeArticlesBySection(section: Section): LiveData<List<Article>> {
        return local.observeArticlesBySection(section)
    }

    override fun observeArticle(articleUrl: ArticleUrl): LiveData<Article> {
        return local.observeArticle(articleUrl)
    }

    override fun loadSections(): List<Section> {
        return Section.values().toList()
    }

    override suspend fun searchArticle(query: String): List<Article> {
        return remote.searchArticle(query)
    }


}
