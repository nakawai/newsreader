package com.github.nakawai.newsreader.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg histories: HistoryRoomEntity)

    @Query("SELECT * FROM histories")
    fun observeHistories(): Flow<List<HistoryRoomEntity>>
}
