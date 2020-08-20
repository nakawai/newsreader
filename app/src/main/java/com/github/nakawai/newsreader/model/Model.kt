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

import android.text.TextUtils
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import io.reactivex.Flowable
import io.reactivex.Observable
import io.realm.RealmResults
import java.util.*

/**
 * Model class for handling the business rules of the app.
 */
class Model private constructor(private val repository: Repository) {
    companion object {
        /**
         * Map between section titles and their NYTimes API keys
         */
        private var _sections: MutableMap<String, String> = HashMap()

        // This could be replaced by Dependency Injection for easier testing
        var instance: Model? = null
            get() {
                if (field == null) {
                    val repository =
                        Repository()
                    field =
                        Model(repository)
                }
                return field
            }
            private set

        init {
            _sections["home"] = "Home"
            _sections["world"] = "World"
            _sections["national"] = "National"
            _sections["politics"] = "Politics"
            _sections["nyregion"] = "NY Region"
            _sections["business"] = "Business"
            _sections["opinion"] = "Opinion"
            _sections["technology"] = "Technology"
            _sections["science"] = "Science"
            _sections["health"] = "Health"
            _sections["sports"] = "Sports"
            _sections["arts"] = "Arts"
            _sections["fashion"] = "Fashion"
            _sections["dining"] = "Dining"
            _sections["travel"] = "Travel"
            _sections["magazine"] = "Magazine"
            _sections["realestate"] = "Real Estate"
        }
    }

    var currentSectionKey = "home"
        private set

    /**
     * Returns the news feed for the currently selected category.
     */
    val selectedNewsFeed: Flowable<RealmResults<NYTimesStory>>
        get() = repository.loadNewsFeed(currentSectionKey, false)

    /**
     * Forces a reload of the news feed
     */
    fun reloadNewsFeed() {
        repository.loadNewsFeed(currentSectionKey, true)
    }

    /**
     * Returns the current state of network usage.
     */
    val isNetworkUsed: Observable<Boolean?>
        get() = repository.networkInUse().distinctUntilChanged()

    /**
     * Marks a story as being read.
     */
    fun markAsRead(storyId: String, read: Boolean) {
        repository.updateStoryReadState(storyId, read)
    }

    /**
     * Returns the story with the given Id
     */
    fun getStory(storyId: String): Flowable<NYTimesStory?> {
        // Repository is only responsible for loading the data
        // Any validation is done by the model
        // See http://blog.danlew.net/2015/12/08/error-handling-in-rxjava/
        require(!TextUtils.isEmpty(storyId)) { "Invalid storyId: $storyId" }
        return repository.loadStory(storyId)
            .filter { story: NYTimesStory? -> story!!.isValid }
    }

    /**
     * Returns all sections available.
     *
     * @return A map of <key></key>, title> pairs for all available sections.
     */
    val sections: Map<String, String>
        get() = _sections

    fun selectSection(key: String) {
        currentSectionKey = key
        repository.loadNewsFeed(currentSectionKey, false)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        repository.close()
    }

}