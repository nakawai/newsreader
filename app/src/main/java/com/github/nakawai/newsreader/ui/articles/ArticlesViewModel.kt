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
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section
import kotlinx.coroutines.launch

/**
 * Presenter class for controlling the Main Activity
 */
class ArticlesViewModel(
    private val model: Model
) : ViewModel() {

    var currentSection: Section = Section.HOME

    private val _storiesData = MediatorLiveData<List<Article>>().apply {
        addSource(model.repository.observeArticles()) { articles ->
            value = articles.filter { it.apiSection == currentSection.key }
        }
    }
    val storiesData: LiveData<List<Article>> = _storiesData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    fun loadData(force: Boolean) {
        _isLoading.value = true
        _isRefreshing.value = true

        viewModelScope.launch {
            _storiesData.value = model.loadNewsFeed(currentSection, force)
        }
        _isLoading.value = false
        _isRefreshing.value = false
    }


    fun sectionSelected(section: Section) {
        currentSection = section
        loadData(force = false)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val model: Model) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(model) as T
        }

    }
}
