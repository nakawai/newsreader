package com.github.nakawai.newsreader.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [HistoryRoomEntity::class],
    version = 1
)

@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
