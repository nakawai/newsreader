package com.github.nakawai.newsreader.domain.repository

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History

interface HistoryLocalDataSource {
    suspend fun addHistory(url: ArticleUrl)
    fun observeHistories(): LiveData<List<History>>
}
