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
import com.github.nakawai.newsreader.model.entity.Section

/**
 * Model class for handling the business rules of the app.
 */
class Model private constructor(val repository: Repository) {
    companion object {

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

    }

    /**
     * Returns the news feed for the currently selected category.
     */
    suspend fun loadNewsFeed(section: Section, force: Boolean): List<Article> {
        return repository.loadNewsFeed(section, force)
    }

    /**
     * Marks a story as being read.
     */
    fun markAsRead(storyId: String, read: Boolean) {
        repository.updateStoryReadState(storyId, read)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        repository.close()
    }

}
