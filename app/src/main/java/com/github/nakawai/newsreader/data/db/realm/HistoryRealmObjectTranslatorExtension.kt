package com.github.nakawai.newsreader.data.db.realm

import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.History


fun HistoryRealmObject.translate(): History {
    return History(ArticleUrl(this.url ?: throw IllegalStateException()))
}
