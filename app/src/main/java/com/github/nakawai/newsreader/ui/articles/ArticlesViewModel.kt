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
package com.github.nakawai.newsreader.ui.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.model.Repository
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Presenter class for controlling the Main Activity
 */
class ArticlesViewModel(
    private val repository: Repository
) : ViewModel() {

    lateinit var currentSection: Section

    private val _articles = MediatorLiveData<List<Article>>().apply {
        addSource(repository.observeArticles()) { articles ->
            value = articles.filter { it.section == currentSection }
        }
    }
    val storiesData: LiveData<List<Article>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun start(section: Section) {
        currentSection = section
        loadData(force = false)
    }

    fun loadData(force: Boolean) {
        _isLoading.value = true

        viewModelScope.launch {
            delay(1000)
            val articles = repository.loadNewsFeed(currentSection, force)
            _articles.value = articles
            _isLoading.value = false
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(repository) as T
        }

    }
}
