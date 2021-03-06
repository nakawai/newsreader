package com.github.nakawai.newsreader.data.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(
    tableName = "histories",
    indices = [Index(value = [HistoryRoomEntity.COLUMN_URL], unique = true)]
)
data class HistoryRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_URL)
    var url: String,

    @ColumnInfo(name = "dateTime")
    var dateTime: OffsetDateTime

) {
    companion object {
        const val COLUMN_URL = "url"
    }
}
