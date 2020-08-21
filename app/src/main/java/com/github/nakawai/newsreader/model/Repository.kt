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
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Multimedia
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import com.github.nakawai.newsreader.model.network.NYTimesRemoteDataSource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import io.realm.Sort
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.io.Closeable
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Class for handling loading and saving data.
 *
 * A repository is a potentially expensive resource to have in memory, so should be closed when no longer needed/used.
 *
 * @see [Repository pattern](http://martinfowler.com/eaaCatalog/repository.html)
 */
class Repository @UiThread constructor() : Closeable {
    private val realm: Realm = Realm.getDefaultInstance()
    private val remote: NYTimesRemoteDataSource = NYTimesRemoteDataSource()
    private val apiKey: String = NewsReaderApplication.context.getString(R.string.nyc_top_stories_api_key)
    private val lastNetworkRequest: MutableMap<String, Long> = HashMap()
    private val networkLoading = BehaviorSubject.createDefault(false)

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZZZZZ", Locale.US)
    private val outputDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)

    /**
     * Keeps track of the current network state.
     *
     * @return `true` if the network is currently being used, `false` otherwise.
     */
    @UiThread
    fun networkInUse(): Observable<Boolean> {
        return networkLoading.hide()
    }

    /**
     * Loads the news feed as well as all future updates.
     */
    suspend fun loadNewsFeed(
        sectionKey: String,
        forceReload: Boolean
    ): List<Article> {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || timeSinceLastNetworkRequest(sectionKey) > MINIMUM_NETWORK_WAIT_SEC) {
            val data = remote.loadData(sectionKey, apiKey)
            processAndAddData(realm, sectionKey, data)
            lastNetworkRequest[sectionKey] = System.currentTimeMillis()
        }

        // Return the data in Realm. The query result will be automatically updated when the network requests
        // save data in Realm
        return suspendCoroutine { continuation ->
            realm.where(NYTimesStory::class.java)
                .equalTo(NYTimesStory.API_SECTION, sectionKey)
                .sort(NYTimesStory.PUBLISHED_DATE, Sort.DESCENDING)
                .findAllAsync()
                .addChangeListener { results ->
                    val list = mutableListOf<Article>()
                    results.forEach { story ->

                        val mediaArray = mutableListOf<Multimedia>()
                        story.multimedia?.forEach {
                            mediaArray.add(Multimedia(it.url.orEmpty()))
                        }
                        list.add(
                            Article(
                                title = story.title.orEmpty(),
                                url = story.url.orEmpty(),
                                storyAbstract = story.storyAbstract.orEmpty(),
                                multimedia = mediaArray
                            )
                        )
                    }
                    continuation.resume(list)
                }
        }


    }

    // Converts data into a usable format and save it to Realm
    private suspend fun processAndAddData(realm: Realm, sectionKey: String, stories: List<NYTimesStory>) {
        if (stories.isEmpty()) return

        suspendCancellableCoroutine<Unit> { continuation ->
            realm.executeTransactionAsync({ r: Realm ->
                for (story in stories) {
                    val parsedPublishedDate = inputDateFormat.parse(story.publishedDate, ParsePosition(0))
                    story.sortTimeStamp = parsedPublishedDate.time
                    story.publishedDate = outputDateFormat.format(parsedPublishedDate)

                    // Find existing story in Realm (if any)
                    // If it exists, we need to merge the local state with the remote, because the local state
                    // contains more info than is available on the server.
                    val persistedStory =
                        r.where(NYTimesStory::class.java)
                            .equalTo(NYTimesStory.Companion.URL, story.url).findFirst()
                    if (persistedStory != null) {
                        // Only local state is the `read` boolean.
                        story.isRead = persistedStory.isRead
                    }

                    // Only create or update the local story if needed
                    if (persistedStory == null || persistedStory.updatedDate != story.updatedDate) {
                        story.apiSection = sectionKey
                        r.copyToRealmOrUpdate(story)
                    }
                }
                continuation.resume(Unit)
            }) { throwable: Throwable ->
                Timber.e(throwable, "Could not save data")
                continuation.resumeWithException(throwable)
            }
        }

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

}
