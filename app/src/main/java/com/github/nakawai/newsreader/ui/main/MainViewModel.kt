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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import io.reactivex.disposables.Disposable
import io.realm.RealmResults
import java.util.*

/**
 * Presenter class for controlling the Main Activity
 */
class MainViewModel(
    private val model: Model
) : ViewModel() {
    private val _sectionList = MutableLiveData<List<String>>()
    val sectionList: LiveData<List<String>> = _sectionList

    private val _storiesData = MutableLiveData<List<NYTimesStory>>()
    val storiesData: LiveData<List<NYTimesStory>> = _storiesData

    private val _isNetworkInUse = MutableLiveData<Boolean>()
    val isNetworkInUse: LiveData<Boolean> = _isNetworkInUse

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private var loaderDisposable: Disposable? = null
    private var listDataDisposable: Disposable? = null
    fun onCreate() {
        // Sort sections alphabetically, but always have Home at the top
        val sectionList = ArrayList(model.sections.values)
        sectionList.sortWith(Comparator { lhs: String, rhs: String ->
            if (lhs == "Home") return@Comparator -1
            if (rhs == "Home") return@Comparator 1
            lhs.compareTo(rhs, ignoreCase = true)
        })
        _sectionList.value = sectionList
    }

    fun onResume() {
        loaderDisposable = model.isNetworkUsed
            .subscribe { networkInUse: Boolean ->
                _isNetworkInUse.value = networkInUse
            }
        sectionSelected(model.currentSectionKey)
    }

    fun onPause() {
        loaderDisposable!!.dispose()
        listDataDisposable!!.dispose()
    }


    fun refreshList() {
        model.reloadNewsFeed()
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
        listDataDisposable?.dispose()

        listDataDisposable = model.selectedNewsFeed
            .subscribe { stories: RealmResults<NYTimesStory> ->
                _storiesData.value = stories
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val model: Model) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(model) as T
        }

    }
}
