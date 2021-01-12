package com.github.nakawai.newsreader.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.github.nakawai.newsreader.data.db.room.AppDatabase
import com.github.nakawai.newsreader.data.db.room.HistoryRoomEntity
import com.github.nakawai.newsreader.data.db.room.translate
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryLocalDataSourceImpl(private val db: AppDatabase) : HistoryLocalDataSource {
    override suspend fun addHistory(url: ArticleUrl) = withContext(Dispatchers.IO) {
        db.historyDao().insertAll(HistoryRoomEntity(url = url.value))
    }

    override fun observeHistories(): LiveData<List<History>> {
        return db.historyDao().observeHistories().map { roomEntities ->
            roomEntities.map { it.translate() }
        }
    }
}
