package com.github.nakawai.newsreader.domain.repository

import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun addHistory(url: ArticleUrl)
    fun observeHistories(): Flow<List<History>>
}
