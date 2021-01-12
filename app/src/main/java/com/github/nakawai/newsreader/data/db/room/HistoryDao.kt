package com.github.nakawai.newsreader.data.db.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    fun insertAll(vararg histories: HistoryRoomEntity)

    @Query("SELECT * FROM histories")
    fun observeHistories(): LiveData<List<HistoryRoomEntity>>
}
