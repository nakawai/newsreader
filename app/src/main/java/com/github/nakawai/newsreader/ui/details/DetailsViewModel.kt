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
package com.github.nakawai.newsreader.ui.details

import androidx.lifecycle.*
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.Article
import kotlinx.coroutines.*

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel(
    private val model: Model
) : ViewModel() {
    private var job: Job? = null

    private lateinit var storyId: String

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = MutableLiveData<Article>()
    val story: LiveData<Article> = _story

    fun onCreate(storyId: String) {
        this.storyId = storyId
    }

    fun onResume() {
        // Show story details
        viewModelScope.launch {
            _isLoading.value = true
            _story.value = model.getStory(storyId)
            _isLoading.value = false
        }

        // Mark story as read if screen is visible for 2 seconds
        job = GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            model.markAsRead(storyId, true)
        }

    }

    fun onPause() {
        job?.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val model: Model) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(model) as T
        }

    }

}
