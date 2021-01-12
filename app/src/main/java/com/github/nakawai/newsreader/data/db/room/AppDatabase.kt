package com.github.nakawai.newsreader.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HistoryRoomEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
