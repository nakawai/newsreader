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
package com.github.nakawai.newsreader.ui.main

import androidx.lifecycle.*
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.Article
import kotlinx.coroutines.launch
import java.util.*

/**
 * Presenter class for controlling the Main Activity
 */
class MainViewModel(
    private val model: Model
) : ViewModel() {
    private val _sectionList = MutableLiveData<List<String>>()
    val sectionList: LiveData<List<String>> = _sectionList

    private val _storiesData = MutableLiveData<List<Article>>()
    val storiesData: LiveData<List<Article>> = _storiesData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    fun onCreate() {
        // Sort sections alphabetically, but always have Home at the top
        val sectionList = ArrayList(model.sections.values)
        sectionList.sortWith(Comparator { lhs: String, rhs: String ->
            if (lhs == "Home") return@Comparator -1
            if (rhs == "Home") return@Comparator 1
            lhs.compareTo(rhs, ignoreCase = true)
        })
        _sectionList.value = sectionList

        sectionSelected(model.currentSectionKey)
    }


    fun loadData(force: Boolean) {
        _isLoading.value = true
        _isRefreshing.value = true
        viewModelScope.launch {
            val result = model.loadNewsFeed(force)
            _storiesData.value = result
        }
        _isLoading.value = false
        _isRefreshing.value = false
    }


    fun titleSpinnerSectionSelected(sectionLabel: String) {
        for (key in model.sections.keys) {
            if (model.sections[key] == sectionLabel) {
                sectionSelected(key)
                break
            }
        }
    }

    private fun sectionSelected(sectionKey: String) {
        model.selectSection(sectionKey)
        loadData(force = false)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val model: Model) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(model) as T
        }

    }
}
