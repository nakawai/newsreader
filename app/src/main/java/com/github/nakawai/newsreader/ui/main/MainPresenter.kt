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

import android.content.Intent
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import com.github.nakawai.newsreader.ui.Presenter
import com.github.nakawai.newsreader.ui.details.DetailsActivity
import io.reactivex.disposables.Disposable
import io.realm.RealmResults
import java.util.*

/**
 * Presenter class for controlling the Main Activity
 */
class MainPresenter(
    private val view: MainActivity,
    private val model: Model,
    private var sections: Map<String, String> = model.sections

) : Presenter {
    private var storiesData: List<NYTimesStory>? = null
    private var loaderDisposable: Disposable? = null
    private var listDataDisposable: Disposable? = null
    override fun onCreate() {
        // Sort sections alphabetically, but always have Home at the top
        val sectionList = ArrayList(sections.values)
        sectionList.sortWith(Comparator { lhs: String, rhs: String ->
            if (lhs == "Home") return@Comparator -1
            if (rhs == "Home") return@Comparator 1
            lhs.compareTo(rhs, ignoreCase = true)
        })
        view.configureToolbar(sectionList)
    }

    override fun onResume() {
        loaderDisposable = model.isNetworkUsed
            .subscribe { networkInUse: Boolean? ->
                view.showNetworkLoading(networkInUse)
            }
        sectionSelected(model.currentSectionKey)
    }

    override fun onPause() {
        loaderDisposable!!.dispose()
        listDataDisposable!!.dispose()
    }

    override fun onDestroy() {
        // Do nothing
    }

    fun refreshList() {
        model.reloadNewsFeed()
        view.hideRefreshing()
    }

    fun listItemSelected(position: Int) {
        val intent: Intent = DetailsActivity.getIntent(view, storiesData!![position])
        view.startActivity(intent)
    }

    fun titleSpinnerSectionSelected(sectionLabel: String) {
        for (key in sections.keys) {
            if (sections[key] == sectionLabel) {
                sectionSelected(key)
                break
            }
        }
    }

    private fun sectionSelected(sectionKey: String) {
        model.selectSection(sectionKey)
        if (listDataDisposable != null) {
            listDataDisposable!!.dispose()
        }
        listDataDisposable = model.selectedNewsFeed
            .subscribe { stories: RealmResults<NYTimesStory> ->
                storiesData = stories
                view.showList(stories)
            }
    }

}