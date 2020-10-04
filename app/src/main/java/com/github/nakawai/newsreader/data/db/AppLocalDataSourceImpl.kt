package com.github.nakawai.newsreader.data.db

import com.github.nakawai.newsreader.domain.datasource.AppLocalDataSource
import com.github.nakawai.newsreader.domain.entities.Section
import java.util.*
import java.util.concurrent.TimeUnit

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
        private const val MINIMUM_NETWORK_WAIT_SEC: Long = 120 // Minimum 2 minutes between each network request
    }

}
