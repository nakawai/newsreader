package com.github.nakawai.newsreader.data.db

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.DataTranslator
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.data.translate
import com.github.nakawai.newsreader.domain.datasource.AppLocalDataSource
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl
import com.github.nakawai.newsreader.domain.datasource.NYTimesLocalDataSource
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AppLocalDataSourceImpl : AppLocalDataSource {
    private val lastNetworkRequestTimeMillis: MutableMap<Section, Long> = EnumMap(
        Section::class.java
    )

    override fun canCallApi(section: Section): Boolean {
        val lastRequestTimeMillis = lastNetworkRequestTimeMillis[section] ?: return true
        val deltaTimeMillis = System.currentTimeMillis() - lastRequestTimeMillis
        return deltaTimeMillis > TimeUnit.SECONDS.convert(MINIMUM_NETWORK_WAIT_SEC, TimeUnit.MILLISECONDS)


    }

    override fun saveLastNetworkRequestTime(section: Section) {
        lastNetworkRequestTimeMillis[section] = System.currentTimeMillis()
    }

    companion object {
        private const val MINIMUM_NETWORK_WAIT_SEC: Long = 2 // Minimum 2 minutes between each network request
    }

}
