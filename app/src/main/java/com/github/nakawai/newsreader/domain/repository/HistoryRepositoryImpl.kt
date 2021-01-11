package com.github.nakawai.newsreader.domain.repository

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History

class HistoryRepositoryImpl : HistoryRepository {
    override suspend fun addHistory(url: ArticleUrl) {
        TODO("Not yet implemented")
    }

    override fun observeHistories(): LiveData<List<History>> {
        TODO("Not yet implemented")
    }
}
