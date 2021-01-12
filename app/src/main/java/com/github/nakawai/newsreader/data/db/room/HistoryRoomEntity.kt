package com.github.nakawai.newsreader.data.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import io.realm.annotations.PrimaryKey

@Entity(
    tableName = "histories",
    indices = [Index(value = [HistoryRoomEntity.COLUMN_URL], unique = true)]
)
data class HistoryRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_URL)
    var url: String? = null
) {
    companion object {
        const val COLUMN_URL = "url"
    }
}
