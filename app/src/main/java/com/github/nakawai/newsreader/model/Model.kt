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

import com.github.nakawai.newsreader.model.entity.Article
import java.util.*

/**
 * Model class for handling the business rules of the app.
 */
class Model private constructor(val repository: Repository) {
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
    suspend fun loadNewsFeed(force: Boolean): List<Article> {
        return repository.loadNewsFeed(currentSectionKey, force)
    }

    /**
     * Marks a story as being read.
     */
    fun markAsRead(storyId: String, read: Boolean) {
        repository.updateStoryReadState(storyId, read)
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
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        repository.close()
    }

}
