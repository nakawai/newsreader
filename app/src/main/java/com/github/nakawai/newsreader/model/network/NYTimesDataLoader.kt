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
package com.github.nakawai.newsreader.model.network

import android.annotation.SuppressLint
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class that handles network requests for the New York Times API
 */
class NYTimesDataLoader {
    private val nyTimesService: NYTimesService
    private val inputDateFormat =
        SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZZZZZ", Locale.US)
    private val outputDateFormat =
        SimpleDateFormat("MM-dd-yyyy", Locale.US)
    private lateinit var apiKey: String
    private var realm: Realm? = null
    private var networkInUse: BehaviorSubject<Boolean?>? = null
    fun loadData(
        sectionKey: String,
        apiKey: String,
        realm: Realm?,
        networkLoading: BehaviorSubject<Boolean?>?
    ) {
        this.apiKey = apiKey
        this.realm = realm
        networkInUse = networkLoading
        loadNextSection(sectionKey)
    }

    // Load all sections one by one.
    @SuppressLint("CheckResult")
    private fun loadNextSection(sectionKey: String) {
        networkInUse!!.onNext(true)
        nyTimesService.topStories(sectionKey, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: NYTimesResponse<List<NYTimesStory>> ->
                    Timber.d("Success - Data received: %s", sectionKey)
                    // response.section is different from sectionKey
                    processAndAddData(realm, sectionKey, response.results!!)
                    networkInUse!!.onNext(false)
                }
            ) { throwable: Throwable ->
                networkInUse!!.onNext(false)
                Timber.d("Failure: Data not loaded: %s - %s", sectionKey, throwable.toString())
            }
    }

    // Converts data into a usable format and save it to Realm
    private fun processAndAddData(
        realm: Realm?,
        sectionKey: String,
        stories: List<NYTimesStory>
    ) {
        if (stories.isEmpty()) return
        realm!!.executeTransactionAsync(
            { r: Realm ->
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
            }
        ) { throwable: Throwable? ->
            Timber.e(
                throwable,
                "Could not save data"
            )
        }
    }

    init {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl("https://api.nytimes.com/")
            .build()
        nyTimesService = retrofit.create(NYTimesService::class.java)
    }
}