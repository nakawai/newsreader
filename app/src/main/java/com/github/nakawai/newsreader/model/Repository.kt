/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nakawai.newsreader.model

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.model.db.NYTimesLocalDataSource
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section
import com.github.nakawai.newsreader.model.network.NYTimesRemoteDataSource
import java.io.Closeable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Class for handling loading and saving data.
 *
 * A repository is a potentially expensive resource to have in memory, so should be closed when no longer needed/used.
 *
 * @see [Repository pattern](http://martinfowler.com/eaaCatalog/repository.html)
 */
class Repository @UiThread constructor() : Closeable {
    private val remote = NYTimesRemoteDataSource()
    private val local = NYTimesLocalDataSource()
    private val lastNetworkRequest: MutableMap<Section, Long> = EnumMap(Section::class.java)


    /**
     * Loads the news feed as well as all future updates.
     */
    suspend fun loadNewsFeed(
        section: Section,
        forceReload: Boolean
    ): List<Article> {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || timeSinceLastNetworkRequest(section) > MINIMUM_NETWORK_WAIT_SEC) {
            val data = remote.loadData(section)

            local.processAndAddData(section.key, data)
            lastNetworkRequest[section] = System.currentTimeMillis()
        }

        return local.readData(section.key)


    }


    private fun timeSinceLastNetworkRequest(section: Section): Long {
        val lastRequest = lastNetworkRequest[section]
        return if (lastRequest != null) {
            TimeUnit.SECONDS.convert(
                System.currentTimeMillis() - lastRequest,
                TimeUnit.MILLISECONDS
            )
        } else {
            Long.MAX_VALUE
        }
    }

    /**
     * Updates a story.
     *
     * @param storyId story to update
     * @param read `true` if the story has been read, `false` otherwise.
     */
    fun updateStoryReadState(storyId: String, read: Boolean) {
        local.updateStoryReadState(storyId, read)
    }

    fun observeArticles(): LiveData<List<Article>> {
        return local.observeArticles()
    }

    fun observeArticle(storyId: String): LiveData<Article> {
        return local.observeStory(storyId)
    }

    /**
     * Closes all underlying resources used by the Repository.
     */
    @UiThread
    override fun close() {
        local.close()
    }

    companion object {
        private const val MINIMUM_NETWORK_WAIT_SEC: Long =
            120 // Minimum 2 minutes between each network request
    }

}
