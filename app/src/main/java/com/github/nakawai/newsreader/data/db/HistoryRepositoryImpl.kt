package com.github.nakawai.newsreader.data.db

import com.github.nakawai.newsreader.data.db.room.AppDatabase
import com.github.nakawai.newsreader.data.db.room.HistoryRoomEntity
import com.github.nakawai.newsreader.data.db.room.translate
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History
import com.github.nakawai.newsreader.domain.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime

class HistoryRepositoryImpl(private val db: AppDatabase) : HistoryRepository {
    override suspend fun addHistory(url: ArticleUrl) = withContext(Dispatchers.IO) {
        db.historyDao().insertAll(HistoryRoomEntity(url = url.value, OffsetDateTime.now()))
    }

    override fun observeHistories(): Flow<List<History>> {
        return db.historyDao().observeHistories().map { list ->
            list.map { it.translate() }
        }

    }

}
