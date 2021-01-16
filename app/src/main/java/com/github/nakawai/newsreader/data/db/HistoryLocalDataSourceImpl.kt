package com.github.nakawai.newsreader.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.github.nakawai.newsreader.data.db.room.AppDatabase
import com.github.nakawai.newsreader.data.db.room.HistoryRoomEntity
import com.github.nakawai.newsreader.data.db.room.translate
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.OffsetDateTime

class HistoryLocalDataSourceImpl(private val db: AppDatabase) : HistoryLocalDataSource {
    override suspend fun addHistory(url: ArticleUrl) = withContext(Dispatchers.IO) {
        db.historyDao().insertAll(HistoryRoomEntity(url = url.value, OffsetDateTime.now()))
    }

    override fun observeHistories(): LiveData<List<History>> {
        return Transformations.switchMap(db.historyDao().observeHistories()) { roomEntities ->
            Timber.d("onChange map histories")
            liveData {
                emit(roomEntities.map { it.translate() })
            }

        }
    }

    override fun observeHistoryEntities(): LiveData<List<HistoryRoomEntity>> {
        return db.historyDao().observeHistories()
    }
}
