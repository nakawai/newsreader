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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel(
    private val model: Model
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private lateinit var storyId: String

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = MutableLiveData<NYTimesStory>()
    val story: LiveData<NYTimesStory> = _story

    fun onCreate(storyId: String) {
        this.storyId = storyId
        _isLoading.value = true
    }

    fun onResume() {
        // Show story details
        val detailsDisposable = model.getStory(storyId)
            .subscribe { story: NYTimesStory? ->
                _isLoading.value = false
                _story.value = story
            }
        compositeDisposable.add(detailsDisposable)

        // Mark story as read if screen is visible for 2 seconds
        val timerDisposable =
            Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    model.markAsRead(storyId, true)
                }
        compositeDisposable.add(timerDisposable)
    }

    fun onPause() {
        compositeDisposable.clear()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val model: Model) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(model) as T
        }

    }

}
