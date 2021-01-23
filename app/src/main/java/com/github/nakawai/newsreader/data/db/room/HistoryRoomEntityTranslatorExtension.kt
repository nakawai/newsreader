package com.github.nakawai.newsreader.data.db.room

import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History

fun HistoryRoomEntity.translate(): History {
    return History(ArticleUrl(this.url))
}
