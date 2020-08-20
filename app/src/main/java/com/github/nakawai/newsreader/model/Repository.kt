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
import com.github.nakawai.newsreader.NewsReaderApplication
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import com.github.nakawai.newsreader.model.network.NYTimesDataLoader
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import timber.log.Timber
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
    private val realm: Realm
    private val dataLoader: NYTimesDataLoader
    private val apiKey: String
    private val lastNetworkRequest: MutableMap<String, Long> =
        HashMap()
    private val networkLoading =
        BehaviorSubject.createDefault(false)

    /**
     * Keeps track of the current network state.
     *
     * @return `true` if the network is currently being used, `false` otherwise.
     */
    @UiThread
    fun networkInUse(): Observable<Boolean?> {
        return networkLoading.hide()
    }

    /**
     * Loads the news feed as well as all future updates.
     */
    @UiThread
    fun loadNewsFeed(
        sectionKey: String,
        forceReload: Boolean
    ): Flowable<RealmResults<NYTimesStory>> {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || timeSinceLastNetworkRequest(sectionKey) > MINIMUM_NETWORK_WAIT_SEC) {
            dataLoader.loadData(sectionKey, apiKey, realm, networkLoading)
            lastNetworkRequest[sectionKey] = System.currentTimeMillis()
        }

        // Return the data in Realm. The query result will be automatically updated when the network requests
        // save data in Realm
        return realm.where(NYTimesStory::class.java)
            .equalTo(NYTimesStory.Companion.API_SECTION, sectionKey)
            .sort(NYTimesStory.Companion.PUBLISHED_DATE, Sort.DESCENDING)
            .findAllAsync()
            .asFlowable()
    }

    private fun timeSinceLastNetworkRequest(sectionKey: String): Long {
        val lastRequest = lastNetworkRequest[sectionKey]
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
    @UiThread
    fun updateStoryReadState(storyId: String?, read: Boolean) {
        realm.executeTransactionAsync({ realm ->
            val persistedStory =
                realm.where(NYTimesStory::class.java)
                    .equalTo(NYTimesStory.Companion.URL, storyId).findFirst()
            if (persistedStory != null) {
                persistedStory.isRead = read
            } else {
                Timber.e("Trying to update a story that no longer exists: %1\$s", storyId)
            }
        }) { throwable -> Timber.e(throwable, "Failed to save data.") }
    }

    /**
     * Returns story details
     */
    @UiThread
    fun loadStory(storyId: String): Flowable<NYTimesStory?> {
        return realm.where(NYTimesStory::class.java)
            .equalTo(NYTimesStory.URL, storyId)
            .findFirstAsync()
            .asFlowable<NYTimesStory>()
            .filter { story: NYTimesStory -> story.isLoaded }
    }

    /**
     * Closes all underlying resources used by the Repository.
     */
    @UiThread
    override fun close() {
        realm.close()
    }

    companion object {
        private const val MINIMUM_NETWORK_WAIT_SEC: Long =
            120 // Minimum 2 minutes between each network request
    }

    init {
        realm = Realm.getDefaultInstance()
        dataLoader = NYTimesDataLoader()
        apiKey = NewsReaderApplication.context.getString(R.string.nyc_top_stories_api_key)
    }
}