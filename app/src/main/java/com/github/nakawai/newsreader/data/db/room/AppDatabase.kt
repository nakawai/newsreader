package com.github.nakawai.newsreader.data.db.room

import androidx.room.Database

@Database(
    entities = [HistoryRoomEntity::class],
    version = 1
)
abstract class AppDatabase {
    abstract fun historyDao(): HistoryDao
}
