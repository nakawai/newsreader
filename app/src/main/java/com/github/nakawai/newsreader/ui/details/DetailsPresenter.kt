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

import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import com.github.nakawai.newsreader.ui.Presenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsPresenter(
    private val view: DetailsActivity,
    private val model: Model,
    private val storyId: String
) : Presenter {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        view.showLoader()
    }

    override fun onResume() {
        // Show story details
        val detailsDisposable = model.getStory(storyId)
            .subscribe { story: NYTimesStory? ->
                view.hideLoader()
                story?.let {
                    view.showStory(it)
                    view.setRead(it.isRead)
                }
            }
        compositeDisposable.add(detailsDisposable)

        // Mark story as read if screen is visible for 2 seconds
        val timberDisposable =
            Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    model.markAsRead(storyId, true)
                }
        compositeDisposable.add(timberDisposable)
    }

    override fun onPause() {
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        // Do nothing
    }

}