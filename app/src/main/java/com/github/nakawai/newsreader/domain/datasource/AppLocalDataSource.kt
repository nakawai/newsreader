package com.github.nakawai.newsreader.domain.datasource

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl

interface AppLocalDataSource {
    fun canCallApi(section: Section): Boolean
    fun saveLastNetworkRequestTime(section: Section)
}
