package com.github.nakawai.newsreader.domain.datasource

import com.github.nakawai.newsreader.domain.entities.Section

interface ConfigLocalDataSource {
    fun canCallApi(section: Section): Boolean
    fun saveLastNetworkRequestTime(section: Section)
}
